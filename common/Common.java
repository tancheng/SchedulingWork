package common;


import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import benchmarks.*;
import platform.*;
import system.*;
import solution.*;

public class Common {

	private static int totalLCMPeriod;
	private static Job[][]jobTimeSlot;
	private static Frequency[][]frequencyTimeSlot;
	
	private static int num_cores = 0;
	private static ArrayList<Cluster> clusters_list = new ArrayList<Cluster>();
	private static ArrayList<Task> tasks_list = new ArrayList<Task>();
	private static ArrayList<Job> jobs_list = new ArrayList<Job>();
	private static GlobalTaskScheduler gts = new GlobalTaskScheduler();
	private static DVFSManager manager_DVFS;
	private static QoSManager manager_QoS = new QoSManager();;
	private static double TDP;
	private static Detector detector;
	
	private static Object synchObj = new Object();
	private static ArrayList<MultiThread> thread_list = new ArrayList<MultiThread>();
	/**
	 * This scheduling_list need to be synchronized...
	 * 
	 * And this 'if(schedulings_list.size()>=10000)' need to be executed in synchronized...
	 * (There are 2 places...) If change above method, then the 'scheduling_list'
	 * need not to be synchronized any more...
	 */
	private static ArrayList<Scheduling> schedulings_list = new ArrayList<Scheduling>();
	
	
	private static final int POPULATION_GA = 10;
	private static int GENERATIONS_GA = 0;
	private static final double PROBABILITY_MUTATE_GA = 1;
	
	private static Scheduling scheduling_Heuristic = null;
	private static int threadCounts = 0;
	
	public static ArrayList<Cluster> getClusters(){
		return clusters_list;
	}
	
	public static void scheduleTasks_extendedEDF(){
		if(!gts.allocateJob(jobs_list)){
			System.err.println("Failed in EDF scheduling");
			System.exit(0);
		}
	}
	
	public static void scheduleTasks_exhaustiveEDF(){
		//Get all the combination of jobs with version; 2: And for each combination schedule them using EDF. 3: Finally, consider TDP constraint and relax them.
		int maxJobID = jobs_list.size()-1;
		tryCombination(maxJobID);
		
		rankAllSolutions();
		showSchedulings(getSchedulingsForRank(schedulings_list,getMaxRank(schedulings_list)));
/*		System.out.println("Min Rank:");
		showSchedulings(getSchedulingsForRank(getMinRank()));
		System.out.println("****************************************************");
		System.out.println("****************************************************");
		for(Scheduling s: schedulings_list){
			s.show_withTables();
		}*/
	}
	
	public static void scheduleTasks_exhaustive(){
		//Get all the combination of jobs with version; 2: And for each combination schedule them using EDF. 3: Finally, consider TDP constraint and relax them.
		int maxJobID = jobs_list.size()-1;
		
		
		schedulings_list.clear();
		tryCombination_exhaustive(maxJobID);
		for(MultiThread mt: thread_list){
			try {
				mt.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("=========================================================================================");
		System.out.println("===================================Finish exhaustive~!===================================");
		System.out.println("=========================================================================================");
		rankAllSolutions();
		showSchedulings(getSchedulingsForRank(schedulings_list,getMaxRank(schedulings_list)));

	}
	
	
	private static void tryCombination(int i){
		Job job;
		job = jobs_list.get(i);
		for(int i_version=0;i_version<job.getFatherTask().getVersionsOnSpecificCluster(clusters_list.get(0)).size();++i_version){
				job.setReadyVersion(i_version);
				if(i==0){			
					for(Job j: jobs_list){
						j.clear();
					}
					for(LocalJobScheduler ljs: gts.getLocalJobSchedulers()){
						ljs.clear();
					}
					for(Cluster clu: clusters_list){
						clu.initFrequencyTimeSlot();
					}
					manager_DVFS.initMaxFrequencyOnAllClusters();					
					if(!gts.allocateJob_allocatedVersion(jobs_list)){
						System.out.println("EDF failed :-(");
						continue;
					}
					freshTables_afterEDF();
					
					
					//2. Consider TDP constraint and relax.
					if(checkAndRelax()){
						System.out.println("Succeed for this version combination.             :-) ");
						
						
						
					}else{
						System.out.println("Fail for this version combination.                :-( ");
					}
				}else{
					tryCombination(i-1);
				}	
		}
	}
	
	private static void displayVersionCombination(){
		for(Job j: jobs_list){
			System.out.print(j.getReadyVersion()+"\t");
		}
		System.out.println();
	}
	
	private static void tryCombination_exhaustive(int i){
		Job job;
		job = jobs_list.get(i);
		for(int i_version=job.getFatherTask().getVersionsOnSpecificCluster(clusters_list.get(0)).size()-1;i_version>=0;--i_version){
				job.setReadyVersion(i_version);
				if(i==0){		
					
					/**
					 * Here, we need modify the codes to multi-thread...
					 * And note that the memory is shared~!!!!!!!!!!!!!!!!
					 */
					displayVersionCombination();
					
					
					for(Job j: jobs_list){
						j.clear();
					}
					for(LocalJobScheduler ljs: gts.getLocalJobSchedulers()){
						ljs.clear();
					}
					
					for(Cluster clu: clusters_list){
						clu.initFrequencyTimeSlot();
					}
					
					manager_DVFS.initMaxFrequencyOnAllClusters();					
					if(!gts.allocateJob_allocatedVersion(jobs_list)){
						System.out.println("EDF failed :-(");
						continue;
					}
					
					freshTables_afterEDF();
					//showTables_afterEDF();
					//2. Consider TDP constraint and relax.
					checkAndRelax_exhaustive();
				
					
				}else{
					tryCombination_exhaustive(i-1);
				}	
		}
	}
	
	public static ArrayList<Job> getJobs(){
		return jobs_list;
	}
	
	public static void loadCluster(Cluster clu){
		clusters_list.add(clu);
		num_cores += clu.getNumOfCores();
		manager_DVFS = new DVFSManager(clusters_list);

	}
	
	public static Cluster getCluster(int clusterID){
		Cluster clu = null;
		for(Cluster temp: clusters_list){
			if(temp.getClusterID()==clusterID){
				clu = temp;
			}
		}
		return clu;
	}
	
	public static void setTDPConstraint(double _TDP){
		TDP = _TDP;
	}
	
	public static double getTDPConstraint(){
		return TDP;
	}
	
	public static void loadScheduler(LocalJobScheduler ljs){
		gts.addJobScheduler(ljs);
	}
	
	public static void loadTask(Task t){
		tasks_list.add(t);
		totalLCMPeriod = getLCM();
		jobTimeSlot = new Job[num_cores][totalLCMPeriod];
		frequencyTimeSlot = new Frequency[clusters_list.size()][totalLCMPeriod];
	
	}
	
	public static int getNumOfCores(){
		return num_cores;
	}
	
	public static int getTotalPeriod(){
		return totalLCMPeriod;
	}
	
	/**
	 * Generating the jobs from the periodic tasks
	 * @param j
	 */
	public static void loadJobs(){
		int n_job = 0;
		int currentJobsSize;
		Job j;
		for(Task t: tasks_list){
			n_job = totalLCMPeriod/t.getDeadline();
			for(int i=0; i<n_job; ++i){
				currentJobsSize = jobs_list.size();
				j = new Job(currentJobsSize,i*t.getDeadline(),t.getRunTimeSetOnClusters(),(i+1)*t.getDeadline());
				j.setFatherTask(t);
				jobs_list.add(j);
			}
		}
		for(Cluster clu: clusters_list){
			clu.initFrequencyTimeSlot();
		}
		manager_DVFS.initMaxFrequencyOnAllClusters();

	}
	
	
	public static void showJobs(){
		for(Job j: jobs_list){
			System.out.println("jobID: "+j.getJobID()+" fatherTaskID: "+j.getFatherTask().getTaskID()+" issueTime: "+j.getIssueTime()+" deadline: "+j.getDeadline()+" relativeRunTime: "+j.getRunTime()+" allocatedCore: "+j.getCore().getCoreID()+".");
		}
	}
	
	public static void showSchedulers(){
		for(LocalJobScheduler ljs: gts.getLocalJobSchedulers()){
			System.out.println("CoreID: "+ljs.getMatchedCore().getCoreID()+" scheduler.");
		}
	}
	
	public static void freshTables_afterEDF(){
		
		for(int i=0;i<jobTimeSlot.length;++i){
			for(int j=0;j<jobTimeSlot[0].length;++j){
				jobTimeSlot[i][j]=null;
			}
		}
		
		for(Job job: jobs_list){
			for(int timing=0;timing<totalLCMPeriod;++timing){
				if(job.getJobTimeSlot()[timing]!=0){
					jobTimeSlot[job.getCore().getCoreID()][timing] = job;//(char)(job.getJobTimeSlot()[timing]*(job.getJobID()+65));
				}
				
			}
			
		}	
		
		for(int i=0;i<frequencyTimeSlot.length;++i){
			for(int j=0;j<frequencyTimeSlot[0].length;++j){
				frequencyTimeSlot[i][j]=null;
			}
		}
		boolean empty;
		for(Cluster clu: clusters_list){
			for(int timing=0;timing<totalLCMPeriod;++timing){
				
				empty = true;
				for(Core c: clu.getCores()){
					if(jobTimeSlot[c.getCoreID()][timing]!=null){
						empty=false;
						break;
					}
				}
				if(!empty){
					frequencyTimeSlot[clu.getClusterID()][timing] = clu.getMaxFrequency();//clu.getFrequencyTimeSlot()[timing];
				}else{
					frequencyTimeSlot[clu.getClusterID()][timing] = null;
				}
				
				//frequencyTimeSlot[clu.getClusterID()][timing] = clu.getFrequencyTimeSlot()[timing];
			}
		}
		
	}
	
	
	public static Job[][] freshJobTables_afterEDF(){

		Job[][] jobTimeSlot_temp = new Job[num_cores][totalLCMPeriod];
		
		for(int i=0;i<jobTimeSlot_temp.length;++i){
			for(int j=0;j<jobTimeSlot_temp[0].length;++j){
				jobTimeSlot_temp[i][j]=null;
			}
		}
		
		for(Job job: jobs_list){
			for(int timing=0;timing<totalLCMPeriod;++timing){
				if(job.getJobTimeSlot()[timing]!=0){
					jobTimeSlot_temp[job.getCore().getCoreID()][timing] = job;//(char)(job.getJobTimeSlot()[timing]*(job.getJobID()+65));
				}
				
			}
			
		}	
		return jobTimeSlot_temp;
	}
	
	public static Frequency[][] freshFrequencyTables_afterEDF(Job[][]jobTimeSlot_temp){		
		
		Frequency[][]frequencyTimeSlot_temp = new Frequency[clusters_list.size()][totalLCMPeriod];;
		
		for(int i=0;i<frequencyTimeSlot_temp.length;++i){
			for(int j=0;j<frequencyTimeSlot_temp[0].length;++j){
				frequencyTimeSlot_temp[i][j]=null;
			}
		}
		boolean empty;
		for(Cluster clu: clusters_list){
			for(int timing=0;timing<totalLCMPeriod;++timing){
				
				empty = true;
				for(Core c: clu.getCores()){
					if(jobTimeSlot_temp[c.getCoreID()][timing]!=null){
						empty=false;
						break;
					}
				}
				if(!empty){
					frequencyTimeSlot_temp[clu.getClusterID()][timing] = clu.getMaxFrequency();//clu.getFrequencyTimeSlot()[timing];
				}else{
					frequencyTimeSlot_temp[clu.getClusterID()][timing] = null;
				}
				
				//frequencyTimeSlot[clu.getClusterID()][timing] = clu.getFrequencyTimeSlot()[timing];
			}
		}
		return frequencyTimeSlot_temp;
	}
	
	public static void showTables_afterEDF(){
		
		for(int i=0;i<jobTimeSlot.length;++i){
			for(int j=0;j<jobTimeSlot[0].length;++j){
				jobTimeSlot[i][j]=null;
			}
		}
		
		for(Job job: jobs_list){
			for(int timing=0;timing<totalLCMPeriod;++timing){
				if(job.getJobTimeSlot()[timing]!=0){
					jobTimeSlot[job.getCore().getCoreID()][timing] = job;//(char)(job.getJobTimeSlot()[timing]*(job.getJobID()+65));
				}
			}
		}
		
		System.out.println("Schedule Table: (after EDF)");
		for(int i=0;i<jobTimeSlot.length;++i){
			for(int j=0;j<jobTimeSlot[0].length;++j){
				if(jobTimeSlot[i][j]==null){
					System.out.print("N\t");
				}else{
					System.out.print(jobTimeSlot[i][j].getJobID()+"\t");
				}
			}
			System.out.println();
		}
		
		
		for(int i=0;i<frequencyTimeSlot.length;++i){
			for(int j=0;j<frequencyTimeSlot[0].length;++j){
				frequencyTimeSlot[i][j]=null;
			}
		}
		
		boolean empty;
		for(Cluster clu: clusters_list){
			for(int timing=0;timing<totalLCMPeriod;++timing){
				
				empty = true;
				for(Core c: clu.getCores()){
					if(jobTimeSlot[c.getCoreID()][timing]!=null){
						empty=false;
						break;
					}
				}
				if(!empty){
					frequencyTimeSlot[clu.getClusterID()][timing] = clu.getMaxFrequency();//clu.getFrequencyTimeSlot()[timing];
				}else{
					frequencyTimeSlot[clu.getClusterID()][timing] = null;
				}
				
			}
		}
		
		System.out.println("Frequency Table: (after EDF)");
		for(int i=0;i<frequencyTimeSlot.length;++i){
			for(int j=0;j<frequencyTimeSlot[0].length;++j){
				if(frequencyTimeSlot[i][j]==null){
					System.out.print("N\t");
				}else{
					System.out.print(frequencyTimeSlot[i][j].getFrequency()+"\t");
				}
			}
			System.out.println();
		}
		
	}
	
	public static void showTables_afterRelax(){
		detector.showFrequencyTable();
		detector.showScheduleTable();
	}
	
	public static void showTables_afterMaxAndMin(){
		manager_QoS.showFrequencyTable();
		manager_QoS.showScheduleTable();
		manager_QoS.showVersionTable();
		manager_QoS.showPowerTable();
	}
	
	public static void addIntoSchedulingSet(/*Frequency[][] _frequencyTable, Job[][] _jobTable, double[] _powerTable, */double _energy, double _QoS){	
		synchronized(synchObj){
			schedulings_list.add(new Scheduling(/*_frequencyTable, _jobTable, _powerTable, */_energy, _QoS));
			if(schedulings_list.size()>=10000){
				rankAllSolutions();
				ArrayList<Scheduling> temp = new ArrayList<Scheduling>();
				int maxRank = getMaxRank(schedulings_list);
				for(Scheduling s: schedulings_list){
					if(s.getRank()==maxRank){
						temp.add(s);
					}
				}
				schedulings_list = temp;
			}
		}
	}
	
	public static int getSchedulingCounts(){
		return schedulings_list.size();
	}
	
	public static void addIntoSchedulingSet(Job[][] _jobTable, Frequency[][] _frequencyTable, double _energy, double _QoS){	
		synchronized(synchObj){
			Scheduling sch = new Scheduling(_jobTable, _frequencyTable, _energy, _QoS);
			//sch.fresh(_jobTable, _frequencyTable, _energy, _QoS);
			schedulings_list.add(sch);
		
			if(schedulings_list.size()>=10000){
				rankAllSolutions();
				ArrayList<Scheduling> temp = new ArrayList<Scheduling>();
				int maxRank = getMaxRank(schedulings_list);
				for(Scheduling s: schedulings_list){
					if(s.getRank()==maxRank){
						temp.add(s);
					}
				}
				schedulings_list = temp;
			}
		}
	}
	
	protected static void showSchedulingSetSize(){
		System.out.println("Set size is: "+schedulings_list.size());
	}
	
	/**
	 * Rank the schedulings_list for filtering.
	 */
	private static void rankAllSolutions(){
		for(Scheduling s: schedulings_list){
			s.clearRank();
		}
		for(Scheduling s: schedulings_list){	
			for(Scheduling t: schedulings_list){	
				if(s.equals(t)){
					continue;
				}
				s.upRank();
				if(s.getEnergy()==t.getEnergy()&&s.getQoS()==t.getQoS()){
					continue;
				}
				if(s.getEnergy()>=t.getEnergy()&&s.getQoS()<=t.getQoS()){
					s.downRank();
				}
			}
		}
	}
	
	private static int getMaxRank(ArrayList<Scheduling> s_l){
		int maxRank = 0;
		for(Scheduling s: s_l){
			if(s.getRank()>maxRank){
				maxRank = s.getRank();
			}
		}
		return maxRank;
	}
	
/*	private static int getMinRank(ArrayList<Scheduling> s_l){
		if(s_l.size()==0)return 0;
		int minRank = s_l.get(0).getRank();
		for(Scheduling s: s_l){
			if(s.getRank()<minRank){
				minRank = s.getRank();
			}
		}
		return minRank;
	}*/
	
	private static ArrayList<Scheduling> getSchedulingsForRank(ArrayList<Scheduling> s_l, int rank){
		ArrayList<Scheduling> s_list = new ArrayList<Scheduling>();
		for(Scheduling s: s_l){
			if(s.getRank()==rank){
				s_list.add(s);
			}
		}
		return s_list;
	}


	private static void showSchedulings(ArrayList<Scheduling> s_list){
		System.out.println("********************Let's see all the scheduling solutions: ********************");
		for(Scheduling s: s_list){
			s.show_withTables();
		}
	}
	
	public static boolean checkAndRelax(){
		Frequency[][]frequencyTable_forCores = new Frequency[num_cores][totalLCMPeriod];
		for(int timing=0;timing<totalLCMPeriod;++timing){
			for(Cluster clu: clusters_list){
				for(Core c: clu.getCores()){
					frequencyTable_forCores[c.getCoreID()][timing] = frequencyTimeSlot[clu.getClusterID()][timing];
				}
			}
		}
		
		detector = new Detector(jobTimeSlot, frequencyTable_forCores);
		if(detector.reScheduleUnderConstraint()){
			detector.showPowerTable();
			detector.mapTablesToQoSManager(manager_QoS);
			return true;
		}else{
			return false;
		}	
	}
	
	public static void checkAndRelax_exhaustive(){
		Job[][]jobTable_forCores = new Job[num_cores][totalLCMPeriod];
		Frequency[][]frequencyTable_forCores = new Frequency[num_cores][totalLCMPeriod];
		for(int timing=0;timing<totalLCMPeriod;++timing){
			for(Cluster clu: clusters_list){
				for(Core c: clu.getCores()){
					frequencyTable_forCores[c.getCoreID()][timing] = frequencyTimeSlot[clu.getClusterID()][timing];
					if(jobTimeSlot[c.getCoreID()][timing]!=null){
						jobTable_forCores[c.getCoreID()][timing] = new Job(); 
						jobTable_forCores[c.getCoreID()][timing].copy(jobTimeSlot[c.getCoreID()][timing]);
					}else{
						jobTable_forCores[c.getCoreID()][timing] = null;
					}
					
				}
			}
		}
		Detector detector_temp = new Detector(jobTable_forCores, frequencyTable_forCores);  
		
		MultiThread mt = new MultiThread();
		thread_list.add(mt);
		double qos = 0;
		for(Job job: getJobs()){
			qos += job.getChozenVersion().getQuality();
		}
		qos = qos/(Common.getJobs().size());
		mt.setDetector(detector_temp, threadCounts++, qos);
		mt.start();//contains: detector_temp.reScheduleUnderConstraint_exhaustive();
		
	}
	
	public static boolean checkAndRelax_GA(Scheduling s){
		
		Frequency[][]frequencyTable_forCores = new Frequency[num_cores][totalLCMPeriod];
		for(int timing=0;timing<totalLCMPeriod;++timing){
			for(Cluster clu: clusters_list){	
				for(Core c: clu.getCores()){
					frequencyTable_forCores[c.getCoreID()][timing] = frequencyTimeSlot[clu.getClusterID()][timing];	
				}
			}
		}
		
		detector = new Detector(jobTimeSlot, frequencyTable_forCores);
		if(detector.reScheduleUnderConstraint(s)){
			//detector.mapTablesToQoSManager(manager_QoS);
			
			return true;
		}else{
			return false;
		}	
	}
	
	
	public static void maximizeQoS_minimizeEnergy(){
		manager_QoS.pursueObjectives();
	}
	
	public static void scheduleTasks_GA(){
		ArrayList<Scheduling> pool = initialize_GA();
		while(true){
			
			evaluate_GA(pool);
			rank_GA(pool);
			//report_GA(pool);
			//scan.next();
			System.out.println("=========================GENERATIONS: "+GENERATIONS_GA+"=======================================");
			if(shouldHalt_GA(pool)){
			
				break;
			}
			
			pool = reproduce_GA(pool);
			
		}
		report_GA(pool);
	}
	
	//private:
	private static int getLCM(){
		int temp = 1;
		if(tasks_list.size()==0)temp = 0;
		for(Task t: tasks_list){
			temp = getLcm_2(temp,t.getDeadline());
		}
		return temp;
	}
	
	private static int getLcm_2(int x,int y){
		for(int i = 1;i<x*y;i++){
			if(i%x==0&&i%y==0){
				return i;
			}
		}
		return x*y;	
		
	}
	
	public static void scheduleTasks_heuristic(){
		ArrayList<VersionForJob> list = initVersionsQueueAndScheduling_Heuristic();
		VersionForJob vfj = null;
		Scheduling scheduling_temp = new Scheduling();
		//Scheduling scheduling_final = new Scheduling();
		
		if(!tryVersionCombination(vfj)){
			System.out.println("Never scheduled due to this worst versions cannot!");
			return;
		}
		
		scheduling_Heuristic.show_withTables();
		System.out.println("++++++++++++++++++++++++++++++++++Have scheduled the worst versions++++++++++++++++++++++++++++++++++++++++++++++++++");
		
		while(!list.isEmpty()){
			 vfj = getBestJobVersion(list);
			 //This tryVersionCombination method need more attention!!!!!!!!!!!!!!!!!!!!!!!!!
			 
			 if(tryVersionCombination(vfj)){
				 list = eliminateJobVersion_working(list,vfj);
				 System.out.println("Succeed in this combination~!");
				 scheduling_Heuristic.show_withTables();
				 scheduling_temp.copy(scheduling_Heuristic);
				// scheduling_final.copy(scheduling_Heuristic);
				 
			 }else{
				 scheduling_Heuristic.copy(scheduling_temp);
				 list = eliminateJobVersion_noWorking(list,vfj);
				 //System.out.println("Fail in this combination~! And now my DNA is: ");
				 //scheduling_Heuristic.show();
			 }
		}
		scheduling_temp.show_withTables();
		//scheduling_Heuristic.copy(scheduling_final);
		System.out.println("======================Have tried all the combinations, ready to turn to the 'Improve' stage....and see the tables: ======================");
		
		//Change this improve stage to the frequency choose stage when we consider the TDP constraint.
		improveThroughDVFS(scheduling_temp);
	}
	
	
	
	
	/**
	 * Try to change the DVFS in order to get lower energy consumption with current fixed QoS.
	 */
	private static void improveThroughDVFS(Scheduling s){
		
		//Find some point to decrease the frequency. Then we would see whether energy will decrease.
		//If energy decreases, good; And at this time, there is no way to increase energy, 'cuz we choose the maximum frequency just under the TDP.
		//So the most appropriate way is to avoid the bottleneck frequency. (So before this, we need get the bottleneck frequency for all jobs!)

		detector.changeFrequency_avoidSignificantVoltage(s);
		
	}
	
	
	private static VersionForJob getBestJobVersion(ArrayList<VersionForJob> list){
		VersionForJob jv_temp = list.get(0);
		double maxMatrix = list.get(0).getMatrix();
		for(VersionForJob jv: list){
			if(jv.getMatrix()>maxMatrix){
				maxMatrix = jv.getMatrix();
				jv_temp = jv;
			}
		}
		return jv_temp;
	}
	
	
	private static VersionForJob getNextJobVersion_exhaustive(ArrayList<VersionForJob> list){
		return list.get(0);
	}
	
	private static ArrayList<VersionForJob> eliminateJobVersion_working(ArrayList<VersionForJob> list, VersionForJob vfj){
		ArrayList<VersionForJob> temp_list = new ArrayList<VersionForJob>();
		for(VersionForJob e: list){
			temp_list.add(e);
		}
		for(VersionForJob e: list){
			if(e.getJobID()==vfj.getJobID()){
				temp_list.remove(e);
			}
		}
		return temp_list;
	}
	
	private static ArrayList<VersionForJob> eliminateJobVersion_noWorking(ArrayList<VersionForJob> list, VersionForJob vfj){
		ArrayList<VersionForJob> temp_list = new ArrayList<VersionForJob>();
		for(VersionForJob e: list){
			temp_list.add(e);
		}
		for(VersionForJob e: list){
			if(e.getJobID()==vfj.getJobID()&&e.getVersionID()==vfj.getVersionID()){
				temp_list.remove(e);
			}
		}
		return temp_list;
	}
	
	private static ArrayList<VersionForJob> initVersionsQueueAndScheduling_Heuristic(){
		/**
		 * Here need more modification.
		 */
		scheduling_Heuristic = new Scheduling();
		
		ArrayList<VersionForJob> list = new ArrayList<VersionForJob>();
		VersionForJob vfj;
		for(Job j: jobs_list){
			for(Version v: j.getFatherTask().getVersionsOnSpecificCluster(getClusters().get(1))){
				vfj = new VersionForJob(j.getJobID(), v.getVersionID());
				vfj.setMatrix(v);
				list.add(vfj);
			}
		}
		return list;
	}
	
	private static boolean tryVersionCombination(VersionForJob vfj){
		
		if(vfj!=null){
			if(!scheduling_Heuristic.noChange(vfj)){
				scheduling_Heuristic.specificInit(scheduling_Heuristic.getVersion());
				scheduling_Heuristic.referInit(vfj);
			}else{
				return true;
			}
		}else{
			scheduling_Heuristic.fastestVersionInit();
		}
		
		Job job = null;
		
		for(int i_job=0;i_job<jobs_list.size();++i_job){
			job = jobs_list.get(i_job);
			job.setReadyVersion(scheduling_Heuristic.getVersionOfJob(i_job));
			job.clear();
		}
		for(LocalJobScheduler ljs: gts.getLocalJobSchedulers()){
			ljs.clear();
		}
		for(Cluster clu: clusters_list){
			clu.initFrequencyTimeSlot();
		}
			
			
		manager_DVFS.initMaxFrequencyOnAllClusters();
		if(!gts.allocateJob_allocatedVersion(jobs_list)){
		//	System.out.println("EDF failed :-(");
			return false;
		}
		
		freshTables_afterEDF();
		//System.out.println("succeed in EDF, let us see detector for TDP~!");	
		//2. Consider TDP constraint and relax.
		if(checkAndRelax_GA(scheduling_Heuristic)){//In the method 'checkAndRelax_GA()', we call the initialize method instead of the construct method. 
		//	System.out.println("Succeed for this version combination when decreasing frequency in terms of TDP.             :-) ");
			
//			displayVersionCombination();
//			
//			Scanner scan = new Scanner(System.in);
//			scan.next();
			return true;
		}else{
		//	System.out.println("Fail for this version combination when decreasing frequency in terms of TDP.                :-( ");
			return false;
		}
		
		
		
		
	}
	
	//for GA():
	private static ArrayList<Scheduling> initialize_GA(){
		Scheduling s = null;
		ArrayList<Scheduling> pool = new ArrayList<Scheduling>();
		for(int x=0;x<POPULATION_GA-1;++x){
			s = new Scheduling();
			s.randomInit();
			pool.add(s);
		}
		Scheduling t = new Scheduling();
		t.fastestVersionInit();
		pool.add(t);
		return pool;
	}
	
	private static void evaluate_GA(ArrayList<Scheduling> pool){
		Job job;
		for(Scheduling s: pool){
			if(s.isNewBorn()){//Have not been scheduled before. Should schedule it.	
				for(int i_job=0;i_job<jobs_list.size();++i_job){
					job = jobs_list.get(i_job);
					job.setReadyVersion(s.getVersionOfJob(i_job));
					job.clear();
				}
				for(LocalJobScheduler ljs: gts.getLocalJobSchedulers()){
					ljs.clear();
				}
				for(Cluster clu: clusters_list){
					clu.initFrequencyTimeSlot();
				}
				manager_DVFS.initMaxFrequencyOnAllClusters();
				if(!gts.allocateJob_allocatedVersion(jobs_list)){
					System.out.println("EDF failed :-(");
					continue;
				}
				
				freshTables_afterEDF();
				System.out.println("succeed in EDF, let us see detector~!");	
				//2. Consider TDP constraint and relax.
				if(checkAndRelax_GA(s)){//In the method 'checkAndRelax_GA()', we call the initialize method instead of the construct method. 
					System.out.println("Succeed for this version combination.             :-) ");
					
				}else{
					System.out.println("Fail for this version combination.                :-( ");
				}
			}
		}
	}
	
	private static void rank_GA(ArrayList<Scheduling> pool){
		for(Scheduling s: pool){
			s.clearRank();
		}
		for(Scheduling i: pool){
			if(!i.isAvailable())
				continue;		
			for(Scheduling j: pool){
				if(j.equals(i))continue;
				i.upRank();
				if(!j.isAvailable())
					continue;
				if(i.getEnergy()==j.getEnergy()&&i.getQoS()==j.getQoS()){
					continue;
				}
				if((i.getEnergy()>=j.getEnergy())&&(i.getQoS()<=j.getQoS())){
					i.downRank();
				}
			}
		}
	}
	
	private static void report_GA(ArrayList<Scheduling> pool){
		rankAllSolutions();
		showSchedulings(getSchedulingsForRank(pool,getMaxRank(pool)));
	}
	
	private static boolean shouldHalt_GA(ArrayList<Scheduling> pool){
		int i = 0;
		ArrayList<Scheduling> temp = new ArrayList<Scheduling>();
		for(Scheduling s: pool){
			if(s.isAvailable()&&!contain(s,temp)){
				++i;
				temp.add(s);
			}
		}
		if(GENERATIONS_GA>=10&&i>pool.size()*0.3){
			return true;
		}
		++GENERATIONS_GA;
		return false;
	}
	
	private static boolean contain(Scheduling s, ArrayList<Scheduling> pool){
		int[] s_v = s.getVersion();
		int[] s_p;
		
		for(Scheduling p: pool){
			boolean contain = true;
			s_p = p.getVersion();
			for(int i=0;i<s_v.length;++i){
				if(s_v[i]!=s_p[i]){
					contain = false;
					break;
				}
			}
			if(contain)
				return true;
		}
		
		return false;
	}
	
	private static ArrayList<Scheduling> reproduce_GA(ArrayList<Scheduling> pool){
		ArrayList<Scheduling> newPool = new ArrayList<Scheduling>();
		int maxRank = Common.getMaxRank(pool);
		if(maxRank!=0){
			for(Scheduling s: pool){
				if(s.getRank()>=pool.size()/4&&newPool.size()<=pool.size()/2){
					newPool.add(s);
				}
			}	
		}/*else{
			return initialize_GA();
		}*/
		return crossover(newPool, pool, (pool.size()-newPool.size())/2);
	}
	
	private static ArrayList<Scheduling> crossover(ArrayList<Scheduling> newPool, ArrayList<Scheduling> pool, int num){

		Scheduling scheduling_mom = null;
		Scheduling scheduling_papa = null;
		int position;
		int[] tempVersionSet_1 = new int[jobs_list.size()];
		int[] tempVersionSet_2 = new int[jobs_list.size()];
		//ArrayList<Scheduling> list_list = prepareForChoose(pool);

		for(int i=0;i<num;++i){
			scheduling_mom = getPossibleScheduling(pool);
			scheduling_papa = getPossibleScheduling(pool);
			position = (int)(1+Math.random()*(Common.getJobs().size()-1));
			for(int index=0;index<position;++index){
				tempVersionSet_1[index] = scheduling_mom.getVersion()[index];
				tempVersionSet_2[index] = scheduling_papa.getVersion()[index];
			}
			for(int index=position;index<Common.getJobs().size();++index){
				tempVersionSet_1[index] = scheduling_papa.getVersion()[index];
				tempVersionSet_2[index] = scheduling_mom.getVersion()[index];
			}
			Scheduling s_1 = new Scheduling();
			Scheduling s_2 = new Scheduling();
			s_1.specificInit(tempVersionSet_1);
			s_2.specificInit(tempVersionSet_2);

			mutate(s_1);
			mutate(s_2);

			newPool.add(s_1);
			newPool.add(s_2);
		}
		
		return newPool;
	}
	
/*	private static ArrayList<Scheduling> prepareForChoose(ArrayList<Scheduling> pool){
		
		ArrayList<Scheduling> temp_pool = new ArrayList<Scheduling>();
		int maxRank = getMaxRank(pool);
		for(Scheduling s: pool){
			if(maxRank == s.getRank()){
				temp_pool.add(s);
			}	
		}
		
		return temp_pool;
	}*/
	
	private static Scheduling getPossibleScheduling(ArrayList<Scheduling> list){
		ArrayList<Scheduling> choices_pool = new ArrayList<Scheduling>();
		int maxRank = getMaxRank(list);
		if(Math.random()>0.5){
			for(Scheduling s: list){
				if(s.getRank()==maxRank){
					return s;
				}
			}
		}
		return list.get((int)(Math.random()*(choices_pool.size())));
	}
	
	
	public static void mutate(Scheduling s){
		//int position = (int)(1+Math.random()*(Common.getJobs().size()-1));
		if(Math.random()<=PROBABILITY_MUTATE_GA){
			for(int i=0;i<1+getJobs().size()/100;++i){
				s.mutate((int)(1+Math.random()*(getJobs().size()-1)));
			}
			
		}
	}
	
	
	
}


class MultiThread extends Thread{
	
	private Detector detector;
	private int ID;
	private double QoS;
	public void setDetector(Detector de, int id, double quality){
		detector = de;
		ID = id;
		detector.setThreadID(ID);
		QoS = quality;
	}
	
	public void run(){
		//if(detector.reScheduleUnderConstraint_exhaustive()){
		//System.out.println("Exhaustive: Succeed for this version combination.    :-)  And now the size is: ");
		detector.reScheduleUnderConstraint_exhaustive(QoS);
		//System.out.println("Thread "+ID+" start working...");
		
		
		//}else{
			//System.out.println("Exhaustive: Fail for this version combination.                :-( ");
		//}
	}
	
}

