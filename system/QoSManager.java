package system;

import java.util.ArrayList;
import platform.Core;
import platform.Frequency;

import common.Common;

import benchmarks.Job;
import benchmarks.Version;

public class QoSManager {
	
	ArrayList<JobVersion> jobs_versions_list = null;
	JobVersion lastJobVersion;
	Job[][]jobsTable;
	Frequency[][]frequenciesTable;
	double[] powerTable;
	double energy;
	double QoS;

	/**
	 * Before using all below methods, this initialization method should be used first.
	 */
	public void initVersionsQueue(Job[][] _jobsTable, Frequency[][] _frequenciesTable, double[] _powerTable){
		//System.out.println("I am here!!!!!!!!!!!!!!!!!");
		jobs_versions_list = new ArrayList<JobVersion>();
		jobsTable = _jobsTable;
		frequenciesTable = _frequenciesTable;
		powerTable = _powerTable;
		
		for(int i=0;i<powerTable.length;++i){
			if(powerTable[i]==0&&!isEmpty(i)){
				powerTable[i] = powerTable[i-1];
			}
		}
		
		
		for(Job job: Common.getJobs()){
			for(Version version: job.getFatherTask().getVersionsOnSpecificCluster(job.getCore().getCluster())){
				jobs_versions_list.add(new JobVersion(job, version));
			}
			
		}
	}
	
	private boolean isEmpty(int index){
		for(int c=0;c<jobsTable.length;++c){
			if(jobsTable[c][index]!=null){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Get next good job with version and configure.
	 * @return
	 */
	public void pursueObjectives(){
		double oleRunTime_WithoutDVFS = 0;
		double newRunTime_withoutDVFS = 0;
		printPriorityQueue();
		setBestJobVersion();
		while(lastJobVersion!=null){
			oleRunTime_WithoutDVFS = lastJobVersion.getJob().getOldRunTimeWithoutDVFS(); 
			newRunTime_withoutDVFS = lastJobVersion.getJob().getNewRunTimeWithoutDVFS(lastJobVersion.getVersion());
			//Note that if itself is the best version, then interval==0 and succeed.
//System.out.println("lastJobVersion.getJob(),newRunTime_withoutDVFS-oleRunTimeWithoutDVFS: "+lastJobVersion.getJob().getJobID()+" interval: "+(newRunTime_withoutDVFS-oleRunTimeWithoutDVFS));
			if(relax(lastJobVersion.getJob(),newRunTime_withoutDVFS-oleRunTime_WithoutDVFS)){//If succeed, next good JobVersion. And delete all versions of this job in the JobVersion queue:
				lastJobVersion.getJob().setVersion(lastJobVersion.getVersion());
				fillTables(lastJobVersion.getJob(),newRunTime_withoutDVFS-oleRunTime_WithoutDVFS);
				versionWorks(true);
			}else{//If fail, delete this item in the JobVersion queue and turn to next item.
				versionWorks(false);
			}
			//showFrequencyTable();
			//showScheduleTable();
			//showVersionTable();
			//showPowerTable();
			
			setBestJobVersion();
			
		}	
		Common.addIntoSchedulingSet(/*frequenciesTable, jobsTable, powerTable, */energy, QoS);
	}
	
	private void printPriorityQueue(){
		
		ArrayList<JobVersion> temp_list = new ArrayList<JobVersion>();
		
		double max = 0;
		JobVersion currentMaxJobVersion = null;
		int size = jobs_versions_list.size();
		for(int i=0;i<size;++i){
			for(JobVersion jv: jobs_versions_list){
				if(jv.getVersion().getSpeedup()*jv.getVersion().getQuality()>max&&!temp_list.contains(jv)){
					max = jv.getVersion().getSpeedup()*jv.getVersion().getQuality();
					currentMaxJobVersion = jv;
				}
			}
			max = 0;
			temp_list.add(currentMaxJobVersion);
			//System.out.println("jobID: "+currentMaxJobVersion.getJob().getJobID()+" version->quality: "+currentMaxJobVersion.getVersion().getQuality()+" speedup: "+currentMaxJobVersion.getVersion().getSpeedup());
		}
	}
	
	/**
	 * Need slots (meet deadline), DVFS level (under TDP), and interval. And two conditions for the no jobs slots: one is no frequency; and the other is having frequency.
	 * @param job
	 * @param interval
	 * @return
	 */
	private boolean relax(Job job, double originalInterval_withoutDVFS){
		if(matchSlotsWithInterval(job.getCore(),getAvailableSlotsList(job),originalInterval_withoutDVFS)){
			return true;
		}
		return false;
	}
	
	/**
	 * Through the jobsTable, get the available slots list.
	 * @param job
	 * @return
	 */
	private ArrayList<Integer> getAvailableSlotsList(Job job){
		ArrayList<Integer> slotsList = new ArrayList<Integer>();
		for(int i=job.getIssueTime();i<job.getDeadline();++i){
			if(jobsTable[job.getCore().getCoreID()][i]==null){
				slotsList.add(i);
			}
		}
		return slotsList;
	}
	
	private boolean matchSlotsWithInterval(Core core, ArrayList<Integer> sl, double interval){
		Frequency tempFrequency = null;
		for(int index: sl){
			if(frequenciesTable[core.getCoreID()][index]!=null){
				if(powerTable[index]-frequenciesTable[core.getCoreID()][index].getIdlePower()+frequenciesTable[core.getCoreID()][index].getActivePower()<=Common.getTDPConstraint()){
					interval -= frequenciesTable[core.getCoreID()][index].getFrequency()*1;
				}	
				if(interval<0.5){
					return true;
				}
			}else{			
				tempFrequency = getProperFrequency(core, index);		
				if(tempFrequency!=null){
					interval -= tempFrequency.getFrequency()*1;
				}
				if(interval<0.5){
					return true;
				}
			}		
		}
		return false;
	}
	
	private boolean realMatchSlotsWithInterval(Job job, Core core, ArrayList<Integer> sl, double interval){
		Frequency tempFrequency = null;
		for(int index: sl){
			if(frequenciesTable[core.getCoreID()][index]!=null){
				if(powerTable[index]-frequenciesTable[core.getCoreID()][index].getIdlePower()+frequenciesTable[core.getCoreID()][index].getActivePower()<=Common.getTDPConstraint()){
					interval -= frequenciesTable[core.getCoreID()][index].getFrequency()*1;
					jobsTable[core.getCoreID()][index] = job;
					powerTable[index] = powerTable[index]-frequenciesTable[core.getCoreID()][index].getIdlePower()+frequenciesTable[core.getCoreID()][index].getActivePower();
				}	
				if(interval<0.5){
					return true;
				}
			}else{			
				tempFrequency = getProperFrequency(core, index);		
				if(tempFrequency!=null){
					interval -= tempFrequency.getFrequency()*1;
					jobsTable[core.getCoreID()][index] = job;
					for(Core c: core.getCluster().getCores()){
						frequenciesTable[c.getCoreID()][index] = tempFrequency;
					}
					powerTable[index] = powerTable[index]+tempFrequency.getActivePower()+(core.getCluster().getCores().size()-1)*tempFrequency.getIdlePower();
				}
				if(interval<0.5){
					return true;
				}
			}		
		}
		return false;
	}
	
	
	/**
	 * When this method is called, there must be no frequency on that cluster. In other words, the cluster is shut down.
	 * @param core
	 * @param index
	 * @return
	 */
	private Frequency getProperFrequency(Core core, int index){
		Frequency fre = core.getCluster().getMaxFrequency();
		while(fre!=null){
			if(powerTable[index]+fre.getActivePower()+(core.getCluster().getCores().size()-1)*fre.getIdlePower()<=Common.getTDPConstraint()){
				return fre;
			}		
			fre = core.getCluster().getLowerFrequency(fre);
		}
		return fre;
	}
	
	private void fillTables(Job job, double originalInterval_withoutDVFS){	
		realMatchSlotsWithInterval(job, job.getCore(),getAvailableSlotsList(job),originalInterval_withoutDVFS);
	}
	
	private void setBestJobVersion(){
		double max = 0;
		lastJobVersion = null;
		for(JobVersion jv: jobs_versions_list){
			if(jv.getVersion().getSpeedup()*jv.getVersion().getQuality()>max){
				max = jv.getVersion().getSpeedup()*jv.getVersion().getQuality();
				lastJobVersion = jv;
			}
		}
	}
	
	/**
	 * If last choice works, delete all versions for this job; If not, delete this choice.
	 * @param works
	 */
	private void versionWorks(boolean works){
		ArrayList<JobVersion> temp_list = new ArrayList<JobVersion>();
		for(JobVersion jv: jobs_versions_list){
			temp_list.add(jv);
		}
		if(works){
			for(JobVersion jv: temp_list){
				if(jv.getJob().equals(lastJobVersion.getJob())){
					jobs_versions_list.remove(jv);
				}
			}
		}else{
			for(JobVersion jv: temp_list){
				if(jv.equals(lastJobVersion)){
					jobs_versions_list.remove(jv);
					break;
				}
			}
		}
	}
	
	public void showFrequencyTable(){			
		System.out.println("-----------------------Frequency Table(in QoSManager):-------------------------");
		for(int i=0;i<frequenciesTable.length;++i){
			for(int j=0;j<frequenciesTable[0].length;++j){
				if(frequenciesTable[i][j]==null){
					System.out.print("N\t");
				}else{
					System.out.print((frequenciesTable[i][j].getFrequency())+"\t");
				}
			}
			System.out.println();
		}
	}

	public void showScheduleTable(){			
		System.out.println("-----------------------Schedule Table(in QoSManager):-------------------------");
		for(int i=0;i<jobsTable.length;++i){
			for(int j=0;j<jobsTable[0].length;++j){
				if(jobsTable[i][j]==null){
					System.out.print("N\t");
				}else{
					System.out.print(jobsTable[i][j].getJobID()+"\t");
				}
			}
			System.out.println();
		}
	}
	
	public void showVersionTable(){		
		QoS = 0;
		System.out.println("-----------------------Version Table(in QoSManager):-------------------------");
		for(int i=0;i<jobsTable.length;++i){
			for(int j=0;j<jobsTable[0].length;++j){
				if(jobsTable[i][j]==null){
					System.out.print("N\t");
				}else{
					System.out.print(jobsTable[i][j].getChozenVersion().getVersionID()+" ");
				}
			}
			System.out.println();
		}
		
		for(Job job: Common.getJobs()){
			QoS += job.getChozenVersion().getQuality();
		}
		for(Job job: Common.getJobs()){
			System.out.print(job.getChozenVersion().getVersionID()+'\t');
		}
		System.out.println("\n--------QoS: "+QoS/Common.getJobs().size()+"--------");
		
	}
	
	public void showPowerTable(){
		energy = 0;
		System.out.println("-----------------------Power Table(in QoSManager): -----------------------");
		for(double i: powerTable){
			System.out.print(i+"\t");
			energy += i;
		}
		System.out.println("\n--------energy: "+energy+"--------");
		
		
		
	}
	
}

class JobVersion{
	Job job;
	Version version;
	int rank;
	JobVersion(Job _job, Version _version){
		job = _job;
		version = _version;
	}
	
	Job getJob(){
		return job;
	}
	
	Version getVersion(){
		return version;
	}
	
	
	
}
