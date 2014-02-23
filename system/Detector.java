package system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import common.Common;
import platform.Cluster;
import platform.Core;
import platform.Frequency;
import solution.Scheduling;
import benchmarks.Job;

public class Detector {
	
	private Job[][] scheduleTable;
	private Frequency[][] frequencyTable;
	private double[] powerTable;
	private int currentCheckPoint;
	double energy;
	double QoS;
	double MIN_INSERT_UNIT = 1.5;
	
	int threadID;
	private int possibleSchedulingNum = 0;
	
	public Detector(Job[][] _scheduleTable, Frequency[][] _frequencyTable){
		scheduleTable = _scheduleTable.clone();
		frequencyTable = _frequencyTable.clone();	
		powerTable = new double[_scheduleTable[0].length];
	}
	
	public void setThreadID(int id){
		threadID = id;
	}
	
	public boolean reScheduleUnderConstraint(){	
		init_PowerOffClustersWithoutJobs();
		currentCheckPoint = 0;
		int nextCheckPoint = getNextCheckPoint();
		while(currentCheckPoint<Common.getTotalPeriod()){
			nextCheckPoint = getNextCheckPoint();
			if(getPower(currentCheckPoint)>Common.getTDPConstraint()){
				//Power on this point exceeds TDP
				if(!decreaseDVFS(currentCheckPoint,nextCheckPoint)){
					return false;
				}
			}
			powerTable[currentCheckPoint] = getPower(currentCheckPoint);
			currentCheckPoint = nextCheckPoint+1;
			
		}
		for(int i=0;i<powerTable.length;++i){
			if(powerTable[i]==0&&!isEmpty(i)){
				powerTable[i] = powerTable[i-1]; 
			}
		}
		freshPowerTable();
		Common.addIntoSchedulingSet(/*frequencyTable, scheduleTable, powerTable, */energy, QoS);
		QoS = 0;
		energy = 0;
		return true;
	}
	
	private void changeDVFS(int beginTime, Table table, HashMap<Cluster, Frequency> hm){
		
		//In order to decline the time cost of execution.
		if(possibleSchedulingNum>=10){
			return;
		}
	//	System.out.print('.');
		
		Table myTable = new Table();
		myTable.copy(table);
		//showTables(table.getSchedulingTable(),table.getFrequencyTable());
		if(beginTime<Common.getTotalPeriod()){
			//not reach LCM:
			int nextPoint = getNextCheckPoint_exhaustive(beginTime, myTable);
			Table _table = relax_exhaustive(beginTime, nextPoint, hm, myTable);
			if(_table != null){
				if(nextPoint+1>=Common.getTotalPeriod()){
					changeDVFS(nextPoint+1, _table, null);
					return;
				}
				//If can relax, then try the next point:
				ArrayList<HashMap<Cluster, Frequency>> hm_list = getAvailableFrequencyLevelsUnderConstraint_exhaustive(nextPoint+1, getFrequencies_exhaustive(nextPoint+1, _table), _table);
				if(hm_list==null||hm_list.size()==0){
					changeDVFS(nextPoint+1, _table, null);
				}else{
					for(HashMap<Cluster, Frequency> _hm: hm_list){			
						changeDVFS(nextPoint+1, _table, _hm);
					}
				}	
			
			}else{
				//cannot relax: do nothing
				//System.out.println("cannot relax  T.T");
				//_table.display();
				//System.out.println("-----------------------");
			}
		}else{
			//System.out.println("My thread ID is: "+threadID);
			//System.out.println("successfully relax  ^.^");
			//myTable.display();
			//System.out.println("-----------------------");
			//reach LCM (add into the scheduling list):
			//System.out.println("##########################################################################");
			Common.addIntoSchedulingSet(myTable.getSchedulingTable(), myTable.getFrequencyTable(), myTable.getEnergy(), QoS);
			++possibleSchedulingNum;
			//System.out.println("Current Size: "+Common.getSchedulingCounts());
			//Scanner scan = new Scanner(System.in);
			//scan.next();
			
			
		}
		
	}
	
	
	/**
	 * For this exhaustive algorithm, try different frequency level during different period and get the best one to add into the final pool.
	 * @return
	 */
	public boolean reScheduleUnderConstraint_exhaustive(double quality){	
		init_PowerOffClustersWithoutJobs();
		int currentPoint = 0;
		Table table = new Table(scheduleTable, frequencyTable);
		QoS = quality;

		
		//table.display();
		
		ArrayList<HashMap<Cluster, Frequency>> hm_list = getAvailableFrequencyLevelsUnderConstraint_exhaustive(currentPoint, getFrequencies_exhaustive(currentPoint, table), table);
		
		if(hm_list==null||hm_list.size() ==0){
			changeDVFS(currentPoint, table, null);
		}else{
			for(HashMap<Cluster, Frequency> hm: hm_list){
				changeDVFS(currentPoint, table, hm);
			}
		}
		
		
		QoS = 0;
		return true;
	}
	
	public boolean reScheduleUnderConstraint(Scheduling s){
		
		init_PowerOffClustersWithoutJobs();
		currentCheckPoint = 0;
		int nextCheckPoint = getNextCheckPoint();
/*		System.out.println("***********************************Before relax, let us see the matrix.***********************************");
		showFrequencyTable();
		showScheduleTable();
		showPowerTable();
		System.out.println("**********************************************************************************************************");*/
		while(currentCheckPoint<Common.getTotalPeriod()){
			nextCheckPoint = getNextCheckPoint();
			if(getPower(currentCheckPoint)>Common.getTDPConstraint()){
				//Power on this point exceeds TDP
				if(!decreaseDVFS(currentCheckPoint,nextCheckPoint)){
					System.out.println("Cannot meet TDP constraint.");
					return false;
				}
			}
			powerTable[currentCheckPoint] = getPower(currentCheckPoint);
//			System.out.println("-----------------------------Every time relax:------------------------------");
//			this.showFrequencyTable();
//			this.showScheduleTable();
			
			currentCheckPoint = nextCheckPoint+1;
		}
		
		for(int i=0;i<powerTable.length;++i){
			if(powerTable[i]==0&&!isEmpty(i)){
				powerTable[i] = powerTable[i-1]; 
			}
		}
		
		freshPowerTable();
		System.out.println("***********************************After relax, let us see the matrix.***********************************");
		//showFrequencyTable();
		//showScheduleTable();
		//showPowerTable();
		System.out.println("**********************************************************************************************************");
		s.fresh(scheduleTable, frequencyTable, /*powerTable, */energy, QoS);
		QoS = 0;
		energy = 0;
		return true;
	}
	
	
	private boolean isEmpty(int index){
		for(int c=0;c<scheduleTable.length;++c){
			if(scheduleTable[c][index]!=null){
				return false;
			}
		}
		return true;
	}
	
	public void mapTablesToQoSManager(QoSManager manager_QoS){
		manager_QoS.initVersionsQueue(scheduleTable, frequencyTable, powerTable);
	}

	private boolean decreaseDVFS(int timing_begin, int timing_end){
		HashMap<Cluster, Frequency> needToBeChangedDVFS_map;
		needToBeChangedDVFS_map = getMaxFrequencyUnderConstraint(timing_begin, getFrequencies(timing_begin));
		if(needToBeChangedDVFS_map==null){
			return false;
		}
		if(relax(timing_begin, timing_end, needToBeChangedDVFS_map)){
			return true;
		}
		return false;
	}

	private boolean decreaseDVFS_Heuristic(int timing_begin, int timing_end){
		HashMap<Cluster, Frequency> needToBeChangedDVFS_map;
		needToBeChangedDVFS_map = getMaxFrequencyUnderConstraint_withoutBottleneckFrequency(timing_begin, getFrequencies(timing_begin));
		if(needToBeChangedDVFS_map==null){
			return false;
		}
		if(relax(timing_begin, timing_end, needToBeChangedDVFS_map)){
			return true;
		}
		return false;
	}
	
	/**
	 * Choose appropriate frequency one more time in order to avoid the most significant voltage which leads to a quite high power consumption.
	 */
	public void changeFrequency_avoidSignificantVoltage(Scheduling s){
		scheduleTable = s.getJobTable();
		frequencyTable = s.getFrequencyTable();
		System.out.println("=========================Next we change the frequency to avoid the key voltage!=================================");
		currentCheckPoint = 0;
		int nextCheckPoint;
		boolean bottleneck = false;
		Job[][] scheduleTable_temp = new Job[scheduleTable.length][scheduleTable[0].length];
		Frequency[][] frequencyTable_temp = new Frequency[frequencyTable.length][frequencyTable[0].length];
		double[] powerTable_temp = new double[powerTable.length];
		
		while(currentCheckPoint<Common.getTotalPeriod()){
			nextCheckPoint = getNextCheckPoint_avoidReversalFrequency();
			if(nextCheckPoint==28){
				System.out.println("haha");
			}
			bottleneck = false;
			for(Cluster clu: Common.getClusters()){
				if(frequencyTable[clu.getCores().get(0).getCoreID()][currentCheckPoint]!=null&&frequencyTable[clu.getCores().get(0).getCoreID()][currentCheckPoint].isBottleneck()){//use interval!
					//Then decrease this frequency:
					bottleneck = true;
				}
			}
			if(bottleneck){
				//some new tables~!
				
				loadTables(frequencyTable_temp, scheduleTable_temp, powerTable_temp);
				
				if(!decreaseDVFS_Heuristic(currentCheckPoint,nextCheckPoint)){
//					System.out.println("------------------------------------------Failed in trying to decrease frequency due to deadline, then roll back................................");

					for(int i=0;i<powerTable.length;++i){
						powerTable[i] = getPower(i);
					}
					rollBackTables(frequencyTable_temp, scheduleTable_temp, powerTable_temp);
					
				}else{
					for(int i=0;i<powerTable.length;++i){
						if(powerTable[i]==0&&!isEmpty(i)){
							powerTable[i] = powerTable[i-1]; 
						}
					}
//					System.out.println("------------------------------------------Succeeded in trying to decrease frequency................................");
/*					showFrequencyTable();
					showScheduleTable();
					showPowerTable();*/
					for(int i=0;i<powerTable.length;++i){
						powerTable[i] = getPower(i);
					}
				}
				
			}
			
			currentCheckPoint = nextCheckPoint+1;
			
		}
		
		showFrequencyTable();
		showScheduleTable();
		showPowerTable();
		
		System.out.println("energy: "+getEnergy());
		
		
	}
	
	
	private void loadTables(Frequency[][] o_f, Job[][] o_j, double[] o_p){
		for(int i=0;i<frequencyTable.length;++i){
			for(int j=0;j<frequencyTable[0].length;++j){
				o_f[i][j] = frequencyTable[i][j];
			}
		}
		for(int i=0;i<scheduleTable.length;++i){
			for(int j=0;j<scheduleTable[0].length;++j){
				o_j[i][j] = scheduleTable[i][j];
			}
		}
		for(int i=0;i<powerTable.length;++i){
			o_p[i] = powerTable[i];		
		}
	}

	private void rollBackTables(Frequency[][] o_f, Job[][] o_j, double[] o_p){
		for(int i=0;i<frequencyTable.length;++i){
			for(int j=0;j<frequencyTable[0].length;++j){
				frequencyTable[i][j] = o_f[i][j];
			}
		}
		for(int i=0;i<scheduleTable.length;++i){
			for(int j=0;j<scheduleTable[0].length;++j){
				scheduleTable[i][j] = o_j[i][j];
			}
		}
		for(int i=0;i<powerTable.length;++i){
			powerTable[i] = o_p[i];		
		}
	}
	
	/**
	 * Return all combinations of clusters with frequencies.
	 * @param timing
	 * @return
	 */
	private ArrayList<HashMap<Cluster, Frequency>> getFrequencies(int timing){
		ArrayList<HashMap<Cluster, Frequency>> map_list = new ArrayList<HashMap<Cluster, Frequency>>();
		HashMap<Cluster, Frequency> map; 
		if(Common.getClusters().size()==2){
			
			Cluster cluster_0 = Common.getCluster(0);
			Cluster cluster_1 = Common.getCluster(1);
			
			Frequency fre_clu_0 = frequencyTable[cluster_0.getCores().get(0).getCoreID()][timing];
			Frequency fre_clu_1 = frequencyTable[cluster_1.getCores().get(1).getCoreID()][timing];
			//Cluster 0 is power off.
			if(fre_clu_0==null){
				fre_clu_1 = cluster_1.getLowerFrequency(fre_clu_1);
				while(fre_clu_1!=null){
					map = new HashMap<Cluster, Frequency>();
					map.put(cluster_1, fre_clu_1);
					map_list.add(map);
					fre_clu_1 = cluster_1.getLowerFrequency(fre_clu_1);
				}	
				return map_list;
			}
			//Cluster 1 is power off.
			if(fre_clu_1==null){
				fre_clu_0 = cluster_0.getLowerFrequency(fre_clu_0);
				while(fre_clu_0!=null){
					map = new HashMap<Cluster, Frequency>();
					map.put(cluster_0, fre_clu_0);
					map_list.add(map);
					fre_clu_0 = cluster_0.getLowerFrequency(fre_clu_0);
				}	
				return map_list;
			}
			//Neither cluster 0 nor cluster 1 is power off.
			boolean firstTime = true;
			while(fre_clu_0!=null){	
				fre_clu_1 = frequencyTable[cluster_1.getCores().get(1).getCoreID()][timing];
				while(fre_clu_1!=null){
					if(firstTime){
						firstTime = false;
						break;
					}
					map = new HashMap<Cluster, Frequency>();
					map.put(cluster_0, fre_clu_0);
					map.put(cluster_1, fre_clu_1);
					map_list.add(map);	
					fre_clu_1 = cluster_1.getLowerFrequency(fre_clu_1);
				}	
				fre_clu_0 = cluster_0.getLowerFrequency(fre_clu_0);
			}
	
			return map_list;
		}else if(Common.getClusters().size()==4){
			
			Cluster cluster_0 = Common.getCluster(0);
			Cluster cluster_1 = Common.getCluster(1);
			Cluster cluster_2 = Common.getCluster(2);
			Cluster cluster_3 = Common.getCluster(3);
			
			Frequency fre_clu_0 = frequencyTable[cluster_0.getCores().get(0).getCoreID()][timing];
			Frequency fre_clu_1 = frequencyTable[cluster_1.getCores().get(1).getCoreID()][timing];
			Frequency fre_clu_2 = frequencyTable[cluster_2.getCores().get(0).getCoreID()][timing];
			Frequency fre_clu_3 = frequencyTable[cluster_3.getCores().get(1).getCoreID()][timing];
			//Cluster 0 is power off.
			if(fre_clu_0==null){
				fre_clu_1 = cluster_1.getLowerFrequency(fre_clu_1);
				while(fre_clu_1!=null){
					map = new HashMap<Cluster, Frequency>();
					map.put(cluster_1, fre_clu_1);
					map_list.add(map);
					fre_clu_1 = cluster_1.getLowerFrequency(fre_clu_1);
				}	
				return map_list;
			}
			//Cluster 1 is power off.
			if(fre_clu_1==null){
				fre_clu_0 = cluster_0.getLowerFrequency(fre_clu_0);
				while(fre_clu_0!=null){
					map = new HashMap<Cluster, Frequency>();
					map.put(cluster_0, fre_clu_0);
					map_list.add(map);
					fre_clu_0 = cluster_0.getLowerFrequency(fre_clu_0);
				}	
				return map_list;
			}
			//Neither cluster 0 nor cluster 1 is power off.
			boolean firstTime = true;
			while(fre_clu_0!=null){	
				fre_clu_1 = frequencyTable[cluster_1.getCores().get(1).getCoreID()][timing];
				while(fre_clu_1!=null){
					if(firstTime){
						firstTime = false;
						break;
					}
					map = new HashMap<Cluster, Frequency>();
					map.put(cluster_0, fre_clu_0);
					map.put(cluster_1, fre_clu_1);
					map_list.add(map);	
					fre_clu_1 = cluster_1.getLowerFrequency(fre_clu_1);
				}	
				fre_clu_0 = cluster_0.getLowerFrequency(fre_clu_0);
			}
	
			return map_list;
			
			
		}else{
			System.out.println("The number of clusters has changed instead of 2 or 4, modify the codes!");
			return null;
		}
	}
	
	/**
	 * Return all combinations of clusters with frequencies.
	 * @param timing
	 * @return
	 */
	private ArrayList<HashMap<Cluster, Frequency>> getFrequencies_exhaustive(int timing, Table table){
		ArrayList<HashMap<Cluster, Frequency>> map_list = new ArrayList<HashMap<Cluster, Frequency>>();
		HashMap<Cluster, Frequency> map; 
		if(Common.getClusters().size()==2){
			
			Cluster cluster_0 = Common.getCluster(0);
			Cluster cluster_1 = Common.getCluster(1);
			
			Frequency fre_clu_0 = table.getFrequencyTable()[cluster_0.getCores().get(0).getCoreID()][timing];
			Frequency fre_clu_1 = table.getFrequencyTable()[cluster_1.getCores().get(1).getCoreID()][timing];
			
			if(fre_clu_0==null&&fre_clu_1==null){
				return null;
			}
			
			//Cluster 0 is power off.
			if(fre_clu_0==null){
				
				//showTables(table.getSchedulingTable(),table.getFrequencyTable());
	
				fre_clu_1 = cluster_1.getLowerFrequency(fre_clu_1);
				while(fre_clu_1!=null){
					map = new HashMap<Cluster, Frequency>();
					map.put(cluster_1, fre_clu_1);
					map_list.add(map);
					fre_clu_1 = cluster_1.getLowerFrequency(fre_clu_1);
				}	
				return map_list;
			}
			//Cluster 1 is power off.
			if(fre_clu_1==null){
				fre_clu_0 = cluster_0.getLowerFrequency(fre_clu_0);
				while(fre_clu_0!=null){
					map = new HashMap<Cluster, Frequency>();
					map.put(cluster_0, fre_clu_0);
					map_list.add(map);
					fre_clu_0 = cluster_0.getLowerFrequency(fre_clu_0);
				}	
				return map_list;
			}
			//Neither cluster 0 nor cluster 1 is power off.
			boolean firstTime = true;
			while(fre_clu_0!=null){	
				fre_clu_1 = table.getFrequencyTable()[cluster_1.getCores().get(1).getCoreID()][timing];
				while(fre_clu_1!=null){
					if(firstTime){
						firstTime = false;
						break;
					}
					map = new HashMap<Cluster, Frequency>();
					map.put(cluster_0, fre_clu_0);
					map.put(cluster_1, fre_clu_1);
					map_list.add(map);	
					fre_clu_1 = cluster_1.getLowerFrequency(fre_clu_1);
				}	
				fre_clu_0 = cluster_0.getLowerFrequency(fre_clu_0);
			}
	
			return map_list;
		}else if(Common.getClusters().size()==4){
			
			Cluster cluster_0 = Common.getCluster(0);
			Cluster cluster_1 = Common.getCluster(1);
			Cluster cluster_2 = Common.getCluster(2);
			Cluster cluster_3 = Common.getCluster(3);
			
			Frequency fre_clu_0 = table.getFrequencyTable()[cluster_0.getCores().get(0).getCoreID()][timing];
			Frequency fre_clu_1 = table.getFrequencyTable()[cluster_1.getCores().get(1).getCoreID()][timing];
			Frequency fre_clu_2 = table.getFrequencyTable()[cluster_2.getCores().get(0).getCoreID()][timing];
			Frequency fre_clu_3 = table.getFrequencyTable()[cluster_3.getCores().get(1).getCoreID()][timing];
			//Cluster 0 is power off.
			if(fre_clu_0==null){
				fre_clu_1 = cluster_1.getLowerFrequency(fre_clu_1);
				while(fre_clu_1!=null){
					map = new HashMap<Cluster, Frequency>();
					map.put(cluster_1, fre_clu_1);
					map_list.add(map);
					fre_clu_1 = cluster_1.getLowerFrequency(fre_clu_1);
				}	
				return map_list;
			}
			//Cluster 1 is power off.
			if(fre_clu_1==null){
				fre_clu_0 = cluster_0.getLowerFrequency(fre_clu_0);
				while(fre_clu_0!=null){
					map = new HashMap<Cluster, Frequency>();
					map.put(cluster_0, fre_clu_0);
					map_list.add(map);
					fre_clu_0 = cluster_0.getLowerFrequency(fre_clu_0);
				}	
				return map_list;
			}
			//Neither cluster 0 nor cluster 1 is power off.
			boolean firstTime = true;
			while(fre_clu_0!=null){	
				fre_clu_1 = table.getFrequencyTable()[cluster_1.getCores().get(1).getCoreID()][timing];
				while(fre_clu_1!=null){
					if(firstTime){
						firstTime = false;
						break;
					}
					map = new HashMap<Cluster, Frequency>();
					map.put(cluster_0, fre_clu_0);
					map.put(cluster_1, fre_clu_1);
					map_list.add(map);	
					fre_clu_1 = cluster_1.getLowerFrequency(fre_clu_1);
				}	
				fre_clu_0 = cluster_0.getLowerFrequency(fre_clu_0);
			}
	
			return map_list;
			
			
		}else{
			System.out.println("The number of clusters has changed instead of 2 or 4, modify the codes!");
			return null;
		}
	}
	
	/**
	 * Return the most appropriate DVFS level for each cluster. Choose proper clusters to decrease DVFS level.
	 * @param map_list
	 * @return
	 */
	private HashMap<Cluster, Frequency> getMaxFrequencyUnderConstraint(int timing, ArrayList<HashMap<Cluster, Frequency>> map_list){
		//Get the combination with minimum slowing down under TDP.
		double minExtend = 99999;
		HashMap<Cluster, Frequency> finalMap = null;
		for(HashMap<Cluster, Frequency> map: map_list){
			if(getChangedPower(timing, map)<=Common.getTDPConstraint()){
				
				if(minExtend>getChangedExtendedPeriod(timing, map)){
					minExtend = getChangedExtendedPeriod(timing, map);
					finalMap = map;
				}
			}
		}
		return finalMap;	
	}
	
	/**
	 * Return available frequency levels.
	 * @param timing
	 * @param map_list
	 * @return
	 */
	private ArrayList<HashMap<Cluster, Frequency>> getAvailableFrequencyLevelsUnderConstraint_exhaustive(int timing, ArrayList<HashMap<Cluster, Frequency>> map_list, Table table){
		//Get the combination with minimum slowing down under TDP.
		//double minExtend = 99999;
		if(map_list==null)return null;
		ArrayList<HashMap<Cluster, Frequency>> finalMap_list = new ArrayList<HashMap<Cluster, Frequency>>();
		for(HashMap<Cluster, Frequency> map: map_list){
			if(getChangedPower_exhaustive(timing, map, table)<=Common.getTDPConstraint()){
				finalMap_list.add(map);
			}
		}
		return finalMap_list;	
	}
	
	/**
	 * Return the most appropriate DVFS level for each cluster. Choose proper clusters to decrease DVFS level.
	 * @param map_list
	 * @return
	 */
	private HashMap<Cluster, Frequency> getMaxFrequencyUnderConstraint_withoutBottleneckFrequency(int timing, ArrayList<HashMap<Cluster, Frequency>> map_list){
		//Get the combination with minimum slowing down under TDP.
		double minExtend = 99999;
		HashMap<Cluster, Frequency> finalMap = null;
		boolean bottleneck = false;
		for(HashMap<Cluster, Frequency> map: map_list){
			bottleneck = false;
			for(Cluster clu: Common.getClusters()){
				if(map.get(clu)!=null&&map.get(clu).isBottleneck()){
					bottleneck = true;
				}
			}
			if(bottleneck){
				continue;
			}
			if(getChangedPower(timing, map)<=Common.getTDPConstraint()){
				
				if(minExtend>getChangedExtendedPeriod(timing, map)){
					minExtend = getChangedExtendedPeriod(timing, map);
					finalMap = map;
				}
			}
		}
		return finalMap;	
	}
	
	/**
	 * Relax all relative jobs.
	 * @param timing_begin
	 * @param timing_end
	 * @param mapToBeChanged
	 * @return
	 */
	private boolean relax(int timing_begin, int timing_end, HashMap<Cluster,Frequency> mapToBeChanged){	
		//the most hard part in our algorithm!
		if(mapToBeChanged==null){
			return false;
		}
		for(Cluster clu: mapToBeChanged.keySet()){	
			if(!relaxOnCluster(timing_begin, timing_end, clu, mapToBeChanged.get(clu))){
				return false;
			}
		}
		return true;
	}
	
	private boolean relaxOnCluster(int timing_begin, int timing_end, Cluster cluster, Frequency newFrequency){
		//double temp = (timing_end - timing_begin + 1)*;
		//int interval = (int)(((timing_end - timing_begin + 1)*frequencyTable[cluster.getCores().get(0).getCoreID()][timing_begin].getFrequency()-temp)/frequencyTable[cluster.getCores().get(0).getCoreID()][timing_begin].getFrequency()+0.5);
		double interval = (timing_end - timing_begin + 1)*(frequencyTable[cluster.getCores().get(0).getCoreID()][timing_begin].getFrequency()-newFrequency.getFrequency());
		//Relax the latter jobs on this cluster

		Frequency oldFrequency = null;
		//Change frequencyTable here
		for(Core c: cluster.getCores()){
			for(int i=timing_begin;i<=timing_end;++i){
				if(i==timing_begin){
					oldFrequency = frequencyTable[c.getCoreID()][i];
				}	
				frequencyTable[c.getCoreID()][i] = newFrequency;
			}
		}	
		for(Core c: cluster.getCores()){
			if(scheduleTable[c.getCoreID()][timing_begin]!=null){//When it is empty at the beginning, it is still empty at last.
				//scheduleTable[c.getCoreID()][timing_begin].extendFillTime(interval);
				if(!insertInterval(scheduleTable[c.getCoreID()][timing_begin],oldFrequency,interval)){
					//System.out.println("Cannot relax the latter jobs.");
					return false;
				}
			}	
		}
		return true;
	}
	
	
	/**
	 * Relax all relative jobs. For the exhaustive algorithm.
	 * @param timing_begin
	 * @param timing_end
	 * @param mapToBeChanged
	 * @return
	 */
	private Table relax_exhaustive(int timing_begin, int timing_end, HashMap<Cluster,Frequency> mapToBeChanged, Table table){	
		//the most hard part in our algorithm!
		if(mapToBeChanged==null){
			return table;
		}
		Table _table = table;
		for(Cluster clu: mapToBeChanged.keySet()){	
			_table = relaxOnCluster_exhaustive(timing_begin, timing_end, clu, mapToBeChanged.get(clu), _table);
			if(_table==null){
				//System.out.println("The table is null~!!!!!!!!!!!!!!");
				return null;
			}
		}
		return _table;
	}
	
	/**
	 * For the exhaustive algorithm.
	 * @param timing_begin
	 * @param timing_end
	 * @param cluster
	 * @param newFrequency
	 * @return
	 */
	private Table relaxOnCluster_exhaustive(int timing_begin, int timing_end, Cluster cluster, Frequency newFrequency, Table table){
		Frequency[][] fTable = table.getFrequencyTable();
		Job[][] sTable = table.getSchedulingTable();
		//double temp = (timing_end - timing_begin + 1)*;
		//int interval = (int)(((timing_end - timing_begin + 1)*frequencyTable[cluster.getCores().get(0).getCoreID()][timing_begin].getFrequency()-temp)/frequencyTable[cluster.getCores().get(0).getCoreID()][timing_begin].getFrequency()+0.5);
		double interval = (timing_end - timing_begin + 1)*(fTable[cluster.getCores().get(0).getCoreID()][timing_begin].getFrequency()-newFrequency.getFrequency());
		//Relax the latter jobs on this cluster

		Frequency oldFrequency = null;
		//Change frequencyTable here
		for(Core c: cluster.getCores()){
			for(int i=timing_begin;i<=timing_end;++i){
				if(i==timing_begin){
					oldFrequency = fTable[c.getCoreID()][i];
				}	
				fTable[c.getCoreID()][i] = newFrequency;
			}
		}	
		
		Table _table = new Table(sTable, fTable);
		for(Core c: cluster.getCores()){
			if(sTable[c.getCoreID()][timing_begin]!=null){//When it is empty at the beginning, it is still empty at last.
				//sTable[c.getCoreID()][timing_begin].extendFillTime(interval);
				_table = insertInterval_exhaustive(sTable[c.getCoreID()][timing_begin],oldFrequency,_table,interval);
				if(_table==null){
					//System.out.println("Cannot relax the latter jobs.");
					return null;
				}
			}	
		}
		return _table;
	}
	
	
	/**
	 * Insert interval into jobs before deadline which decrease slack as a result. For the exhaustive algorithm.
	 */
	private Table insertInterval_exhaustive(Job job, Frequency fre, Table table, double interval){
		return insertIntervalOneByOne_exhaustive(job,fre,table,interval);
	}
	
	
	/**
	 * Recursion method to insert interval into jobs one time slot by one time slot. For the exhaustive algorithm.
	 */
	private Table insertIntervalOneByOne_exhaustive(Job job, Frequency oldfre, Table table, double interval){	
		/*Frequency[][] fTable = table.getFrequencyTable();
		Job[][] jTable = table.getSchedulingTable();*/
		Table _table = table;
		while(true){
			//if(interval/oldfre.getFrequency()<0.6){//interval <= maxFrequencyminFrequency -> break;
			if(interval<1.2){//interval <= maxFrequencyminFrequency -> break;
			//if(job.getFillTime()<oldfre.getFrequency()){//interval <= maxFrequencyminFrequency -> break;
				break;
			}
			int position = getNextAvailablePoint_exhaustive(job,_table);
			//Deadline constraint need to be checked at each time.
			if(position==-1){//Note that this 'deadline' is one more than the true deadline due to beginning with 0.
				//System.out.println("job: "+job.getJobID()+"; interval: "+interval);
				return null;
			}
			Job toBeOccupiedJob = null;
			Frequency newFrequency = _table.getFrequencyTableValue(job.getCore().getCoreID(), position);
			if(_table.getSchedulingTableValue(job.getCore().getCoreID(),position) != null){
				toBeOccupiedJob = _table.getSchedulingTableValue(job.getCore().getCoreID(),position);
				//job.shortenFillTime(newFrequency.getFrequency());
				//toBeOccupiedJob.extendFillTime(newFrequency.getFrequency());
				interval = interval - newFrequency.getFrequency();
				_table = insertIntervalOneByOne_exhaustive(toBeOccupiedJob, newFrequency, _table, newFrequency.getFrequency());
				if(_table==null){	
					
					return null;
				}
				//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!for test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				//showTables(_table.getSchedulingTable(),_table.getFrequencyTable());
				_table.setSchedulingTableValue(job.getCore().getCoreID(),position,job);
				_table.setFrequencyTableValue(job.getCore().getCoreID(),position,newFrequency);
			}else{
				
				//consider different scenario??????????????????????????????????????????????????
				//job.shortenFillTime(oldfre.getFrequency());
				
				if(newFrequency!=null){
					interval = interval - newFrequency.getFrequency();
					//job.shortenFillTime(newFrequency.getFrequency());
					_table.setSchedulingTableValue(job.getCore().getCoreID(),position,job);
					//scheduleTable[job.getCore().getCoreID()][position] = job;
				}else{
					interval = interval - oldfre.getFrequency();
					//job.shortenFillTime(oldfre.getFrequency());
					_table.setSchedulingTableValue(job.getCore().getCoreID(),position,job);
					//scheduleTable[job.getCore().getCoreID()][position] = job;
					for(Core c: job.getCore().getCluster().getCores()){
						_table.setFrequencyTableValue(c.getCoreID(),position,oldfre);
						//frequencyTable[c.getCoreID()][position] = oldfre;
					}
				}
				
				
				//interval = interval - fre.getFrequency();
				/*_table.setSchedulingTableValue(job.getCore().getCoreID(),position,job);
				for(Core c: job.getCore().getCluster().getCores()){
					_table.setFrequencyTableValue(c.getCoreID(),position,fre);
					//System.out.println("ftable: ["+c.getCoreID()+"]["+position+"]"+table.getFrequencyTableValue(c.getCoreID(),position).getFrequency());
				}*/
			}
		}
		return _table;	
	}
	
	/**
	 * Insert interval into jobs before deadline which decrease slack as a result.
	 */
	private boolean insertInterval(Job job, Frequency fre, double interval){
		if(!insertIntervalOneByOne(job,fre,interval)){
			return false;
		}
		return true;
	}
	
	
	/**
	 * Recursion method to insert interval into jobs one time slot by one time slot.
	 */
	private boolean insertIntervalOneByOne(Job job, Frequency oldfre, double interval){	
		Frequency newFrequency = null;
		while(true){/*
			showScheduleTable();
			showFrequencyTable();*/
			int position = getNextAvailablePoint(job);
			//if(interval/oldfre.getFrequency()<0.6){//interval <= maxFrequencyminFrequency -> break;
			if(interval<2.1){//interval <= maxFrequencyminFrequency -> break;
				break;
			}
			//Deadline constraint need to be checked at each time.
			if(position==-1){//Note that this 'deadline' is one more than the true deadline due to beginning with 0.
				//System.out.println("job: "+job.getJobID()+"; interval: "+interval);
				//System.out.println("fail??????????????????????????????????");
				return false;
			}
			newFrequency = frequencyTable[job.getCore().getCoreID()][position];
			Job toBeOccupiedJob = null;
			
			if(scheduleTable[job.getCore().getCoreID()][position] != null){
				//If there is task, do not change the original frequency
				toBeOccupiedJob = scheduleTable[job.getCore().getCoreID()][position];
				
				interval = interval - newFrequency.getFrequency();
				//job.shortenFillTime(newFrequency.getFrequency());
				//interval = interval - newFrequency.getFrequency();
				//if(!insertIntervalUnit(toBeOccupiedJob, frequencyTable[job.getCore().getCoreID()][position])){
				//if(!insertIntervalUnit(toBeOccupiedJob, frequencyTable[job.getCore().getCoreID()][position])){
				//toBeOccupiedJob.extendFillTime(newFrequency.getFrequency());
				if(!insertIntervalOneByOne(toBeOccupiedJob, newFrequency, newFrequency.getFrequency())){	
					return false;
				}
				scheduleTable[job.getCore().getCoreID()][position] = job;
				
				//frequencyTable[job.getCore().getCoreID()][position] = fre;
				//frequencyTable[job.getCore().getCoreID()][position] = newFrequency;
			}else{
				if(newFrequency!=null){
					interval = interval - newFrequency.getFrequency();
					scheduleTable[job.getCore().getCoreID()][position] = job;
				}else{
					interval = interval - oldfre.getFrequency();
					scheduleTable[job.getCore().getCoreID()][position] = job;
					for(Core c: job.getCore().getCluster().getCores()){
						frequencyTable[c.getCoreID()][position] = oldfre;
					}
				}
			}
		}
		return true;	
	}
	
	/*private boolean insertIntervalUnit(Job job, Frequency oldfre){	

		int position = getNextAvailablePoint(job);
		//Deadline constraint need to be checked at each time.
		if(position==-1){//Note that this 'deadline' is one more than the true deadline due to beginning with 0.
			return false;
		}
		Job toBeOccupiedJob = null;
		if(scheduleTable[job.getCore().getCoreID()][position] != null){
			//If there is a task, do not change frequency, keep the previous frequency.
			toBeOccupiedJob = scheduleTable[job.getCore().getCoreID()][position];
			if(!insertIntervalUnit(toBeOccupiedJob, frequencyTable[job.getCore().getCoreID()][position])){					
				return false;
			}
			scheduleTable[job.getCore().getCoreID()][position] = job;
			for(Core c: job.getCore().getCluster().getCores()){
				frequencyTable[c.getCoreID()][position] = fre;
			}
		}else{
			scheduleTable[job.getCore().getCoreID()][position] = job;
			for(Core c: job.getCore().getCluster().getCores()){
				frequencyTable[c.getCoreID()][position] = oldfre;
			}
		}
		
		return true;	
	}*/
	
	/*private Table insertIntervalUnit_exhaustive(Job job, Frequency fre, Table table, double interval){	
		Job[][] sTable = table.getSchedulingTable();
		Frequency[][] fTable = table.getFrequencyTable();
		Table _table = table;
		int position = getNextAvailablePoint_exhaustive(job,_table);
		//Deadline constraint need to be checked at each time.
		if(position==-1){//Note that this 'deadline' is one more than the true deadline due to beginning with 0.
			return null;
		}
		Job toBeOccupiedJob = null;
		if(_table.getSchedulingTableValue(job.getCore().getCoreID(),position) != null){
			toBeOccupiedJob = _table.getSchedulingTableValue(job.getCore().getCoreID(),position);
			_table = insertIntervalUnit_exhaustive(toBeOccupiedJob, _table.getFrequencyTableValue(job.getCore().getCoreID(), position), _table, );
			if(_table==null){					
				return null;
			}
			_table.setSchedulingTableValue(job.getCore().getCoreID(),position,job);
			for(Core c: job.getCore().getCluster().getCores()){
				_table.setFrequencyTableValue(c.getCoreID(),position,fre);
			}
			
		}else{
			_table.setSchedulingTableValue(job.getCore().getCoreID(),position,job);
			for(Core c: job.getCore().getCluster().getCores()){
				_table.setFrequencyTableValue(c.getCoreID(),position,fre);
			}
		}
		
		return _table;	
	}
	*/
	/**
	 * For exhaustive algorithm.
	 * @param job
	 * @return
	 */
	private int getNextAvailablePoint_exhaustive(Job job, Table table){
		
		for(int i=job.getDeadline()-1;i>job.getIssueTime();--i){
			if(table.getSchedulingTableValue(job.getCore().getCoreID(),i)!=null&&table.getSchedulingTableValue(job.getCore().getCoreID(),i)==job){
				if(i==job.getDeadline()-1){
					return -1;
				}else{
					for(int j=i+1;j<job.getDeadline();++j){
						if(table.getSchedulingTableValue(job.getCore().getCoreID(),j)==null){
							return j;
						}
						if(job.getDeadline()<=table.getSchedulingTableValue(job.getCore().getCoreID(),j).getDeadline()){
							return j;
						}
						//If 'nextPosition' has a higher priority (earlier deadline), do not choose! (quite deliberate!)
					}
				}
				
			}
		}
		return -1;
	}
	
	private int getNextAvailablePoint(Job job){
		for(int i=job.getDeadline()-1;i>=job.getIssueTime();--i){
			if(scheduleTable[job.getCore().getCoreID()][i]==job){
				if(i==job.getDeadline()-1){
					return -1;
				}else{
					for(int j=i+1;j<job.getDeadline();++j){
						if(scheduleTable[job.getCore().getCoreID()][j]==null){
							return j;
						}
						if(job.getDeadline()<=scheduleTable[job.getCore().getCoreID()][j].getDeadline()){
							return j;
						}
						//If 'nextPosition' has a higher priority (earlier deadline), do not choose! (quite deliberate!)
					}
				}
				
			}
		}
		return -1;
	}
	
	/**
	 * Find next check point in terms of checking TDP constraint using specific point & table.
	 */
	private int getNextCheckPoint_exhaustive(int currentPoint, Table table){
		Job[][]sTable = table.getSchedulingTable();
		for(int timing=currentPoint+1;timing<Common.getTotalPeriod();++timing){
			for(int core=0;core<sTable.length;++core){
				if(sTable[core][timing]!=sTable[core][currentPoint]){
					return timing-1;
				}
			}
		}
		return Common.getTotalPeriod()-1;
	}
	
	/**
	 * Find next check point in terms of checking TDP constraint.
	 */
	private int getNextCheckPoint(){
		for(int timing=currentCheckPoint+1;timing<Common.getTotalPeriod();++timing){
			for(int core=0;core<scheduleTable.length;++core){
				if(scheduleTable[core][timing]!=scheduleTable[core][currentCheckPoint]){
					return timing-1;
				}
			}
		}
		return Common.getTotalPeriod()-1;
	}
	
	/**
	 * Find next check point in terms of checking reversal frequency.
	 */
	private int getNextCheckPoint_avoidReversalFrequency(){
		for(int timing=currentCheckPoint+1;timing<Common.getTotalPeriod();++timing){
			for(int core=0;core<scheduleTable.length;++core){
				if(scheduleTable[core][timing]!=scheduleTable[core][currentCheckPoint]||frequencyTable[core][timing]!=frequencyTable[core][currentCheckPoint]){
					return timing-1;
				}
			}
		}
		return Common.getTotalPeriod()-1;
	}
	
	/**
	 * Before checking TDP, we need initialize clusters in order to power off clusters without executing jobs.
	 */
	private void init_PowerOffClustersWithoutJobs(){
		boolean powerOff = true;
		for(int timing=0;timing<Common.getTotalPeriod();++timing){

			for(Cluster clu: Common.getClusters()){	
				powerOff = true;
				for(Core c: clu.getCores()){
					if(scheduleTable[c.getCoreID()][timing]!=null){
						powerOff = false;
						break;
					}
				}	
				if(powerOff){
					for(Core c: clu.getCores()){			
							frequencyTable[c.getCoreID()][timing] = null;							
					}
				}
			}
		}	
	}
	
	/**
	 * For exhaustive algorithm.
	 * @param timing
	 * @return
	 */
	private double getPower_exhaustive(int timing, Table table){
		Job[][] jTable = table.getSchedulingTable();
		Frequency[][] fTable = table.getFrequencyTable();
		freFrequencyTable(timing);
		double power = 0;
		for(int i=0;i<jTable.length;++i){
			if(jTable[i][timing]!=null){
				power += fTable[i][timing].getActivePower();
			}else{//no job is executing now
				if(fTable[i][timing]==null){//cluster is closed
					power += 0;
				}else{//cluster is in idle status
					power += fTable[i][timing].getIdlePower();
				}
			}
		}
		return power;
	}
	
	private double getPower(int timing){
		freFrequencyTable(timing);
		double power = 0;
		for(int i=0;i<scheduleTable.length;++i){
			if(scheduleTable[i][timing]!=null){
				power += frequencyTable[i][timing].getActivePower();
			}else{//no job is executing now
				if(frequencyTable[i][timing]==null){//cluster is closed
					power += 0;
				}else{//cluster is in idle status
					power += frequencyTable[i][timing].getIdlePower();
				}
			}
		}
		return power;
	}
	


	
	private double getChangedPower(int timing, HashMap<Cluster,Frequency> mapToBeChanged){
		double changedPower = getPower(timing);
		for(Cluster clu: Common.getClusters()){
			if(mapToBeChanged.containsKey(clu)){
				for(Core c: clu.getCores()){
					if(scheduleTable[c.getCoreID()][timing]!=null){
						changedPower = changedPower-frequencyTable[c.getCoreID()][timing].getActivePower()+mapToBeChanged.get(clu).getActivePower();				
					}else{
						changedPower = changedPower-frequencyTable[c.getCoreID()][timing].getIdlePower()+mapToBeChanged.get(clu).getIdlePower();
					}
				}
			}
		}
		return changedPower;
	}
	
	private double getChangedPower_exhaustive(int timing, HashMap<Cluster,Frequency> mapToBeChanged, Table table){
		Job[][] sTable = table.getSchedulingTable();
		Frequency[][] fTable = table.getFrequencyTable();
		double changedPower = getPower_exhaustive(timing,table);
		for(Cluster clu: Common.getClusters()){
			if(mapToBeChanged.containsKey(clu)){
				for(Core c: clu.getCores()){
					if(sTable[c.getCoreID()][timing]!=null){
						changedPower = changedPower-fTable[c.getCoreID()][timing].getActivePower()+mapToBeChanged.get(clu).getActivePower();				
					}else{
						changedPower = changedPower-fTable[c.getCoreID()][timing].getIdlePower()+mapToBeChanged.get(clu).getIdlePower();
					}
				}
			}
		}
		return changedPower;
	}
	
	
	/**
	 * For exhaustive algorithm.
	 * @param timing
	 * @param mapToBeChanged
	 * @return
	 */
	private double getChangedExtendedPeriod_exhaustive(int timing, HashMap<Cluster,Frequency> mapToBeChanged, Frequency[][] fTable){
		//'mapToBeChanged' has one or two elements.
		if(mapToBeChanged.size()==1){
			Frequency fre = null;
			Cluster cluster = null;
			for(Cluster clu: Common.getClusters()){
				if(mapToBeChanged.get(clu)!=null){
					fre = mapToBeChanged.get(clu);
					cluster = clu;
					break;
				}
			}
			double oldSpeed = fTable[cluster.getCores().get(0).getCoreID()][timing].getFrequency();
			double newSpeed = fre.getFrequency();
			return oldSpeed/newSpeed;
		}
		if(mapToBeChanged.size()==2){	
			Cluster cluster_0 = Common.getClusters().get(0);
			Frequency fre_0 = mapToBeChanged.get(cluster_0);
			Cluster cluster_1 = Common.getClusters().get(1);
			Frequency fre_1 = mapToBeChanged.get(cluster_1);
			
		
			double oldSpeed_0 = fTable[cluster_0.getCores().get(0).getCoreID()][timing].getFrequency();
			double newSpeed_0 = fre_0.getFrequency();
			double r_0 = oldSpeed_0/newSpeed_0;
			
			double oldSpeed_1 = fTable[cluster_1.getCores().get(0).getCoreID()][timing].getFrequency();
			double newSpeed_1 = fre_1.getFrequency();
			double r_1 = oldSpeed_1/newSpeed_1;
			if(r_0>r_1)
				return r_0;
			else
				return r_1;
		}
		else{
			System.out.println("Why there are more than two decreasing frequency solutions?");
			return 0;
		}
		
	}
	
	private double getChangedExtendedPeriod(int timing, HashMap<Cluster,Frequency> mapToBeChanged){
		//'mapToBeChanged' has one or two elements.
		if(mapToBeChanged.size()==1){
			Frequency fre = null;
			Cluster cluster = null;
			for(Cluster clu: Common.getClusters()){
				if(mapToBeChanged.get(clu)!=null){
					fre = mapToBeChanged.get(clu);
					cluster = clu;
					break;
				}
			}
			double oldSpeed = frequencyTable[cluster.getCores().get(0).getCoreID()][timing].getFrequency();
			double newSpeed = fre.getFrequency();
			return oldSpeed/newSpeed;
		}
		if(mapToBeChanged.size()==2){	
			Cluster cluster_0 = Common.getClusters().get(0);
			Frequency fre_0 = mapToBeChanged.get(cluster_0);
			Cluster cluster_1 = Common.getClusters().get(1);
			Frequency fre_1 = mapToBeChanged.get(cluster_1);
			
		
			double oldSpeed_0 = frequencyTable[cluster_0.getCores().get(0).getCoreID()][timing].getFrequency();
			double newSpeed_0 = fre_0.getFrequency();
			double r_0 = oldSpeed_0/newSpeed_0;
			
			double oldSpeed_1 = frequencyTable[cluster_1.getCores().get(0).getCoreID()][timing].getFrequency();
			double newSpeed_1 = fre_1.getFrequency();
			double r_1 = oldSpeed_1/newSpeed_1;
			if(r_0>r_1)
				return r_0;
			else
				return r_1;
		}
		else{
			System.out.println("Why there are more than two decreasing frequency solutions?");
			return 0;
		}
		
	}
	
	private void showTables(Job[][]sTable, Frequency[][]fTable){
		System.out.println("-----------------------Schedule Table(in showTables()):-------------------------");
		for(int i=0;i<sTable.length;++i){
			for(int j=0;j<sTable[0].length;++j){
				if(sTable[i][j]==null){
					System.out.print("N\t");
				}else{
					System.out.print(sTable[i][j].getJobID()+"\t");
				}
			}
			System.out.println();
		}
		
		System.out.println("-----------------------Frequency Table(in showTables()):-------------------------");
		for(int i=0;i<fTable.length;++i){
			for(int j=0;j<fTable[0].length;++j){
				if(fTable[i][j]==null){
					System.out.print("N\t");
				}else{
					System.out.print((fTable[i][j].getFrequency())+"\t");
				}
			}
			System.out.println();
		}
	}
	
	public void showFrequencyTable(){			
		System.out.println("-----------------------Frequency Table(in Detector):-------------------------");
		for(int i=0;i<frequencyTable.length;++i){
			for(int j=0;j<frequencyTable[0].length;++j){
				if(frequencyTable[i][j]==null){
					System.out.print("N\t");
				}else{

					if(scheduleTable[i][j]==null){
						System.out.print((frequencyTable[i][j].getFrequency())+"("+frequencyTable[i][j].getIdlePower()+")\t");
					}else{
						System.out.print((frequencyTable[i][j].getFrequency())+"("+frequencyTable[i][j].getActivePower()+")\t");
					}

					//System.out.print((frequencyTable[i][j].getFrequency())+"\t");
				}
			}
			System.out.println();
		}
	}

	public void showScheduleTable(){			
		System.out.println("-----------------------Schedule Table(in Detector):-------------------------");
		for(int i=0;i<scheduleTable.length;++i){
			for(int j=0;j<scheduleTable[0].length;++j){
				if(scheduleTable[i][j]==null){
					System.out.print("N\t");
				}else{
					System.out.print(scheduleTable[i][j].getJobID()+"\t");
				}
			}
			System.out.println();
		}
	}
	
	private double getEnergy(){
		energy = 0;
		for(double d: powerTable){
			energy += d;
		}
		return energy;
	}
	
	public void freshPowerTable(){
		energy = 0;
		
		for(int i=0;i<powerTable.length;++i){
			powerTable[i] = getPower(i);
		}
		
		for(double d: powerTable){
			energy += d;
		}
		QoS = 0;
		for(Job job: Common.getJobs()){
			QoS += job.getChozenVersion().getQuality();
		}
		QoS = QoS/(Common.getJobs().size());
		
	}
	
	public void showPowerTable(){
		for(int i=0;i<powerTable.length;++i){
			powerTable[i] = getPower(i);
		}
		System.out.println("----------------------Power Table(in Detector):-------------------------------");
		for(double d: powerTable){
			System.out.print(d+"\t");
		}
		System.out.println();
		System.out.println("------------------------------- "+getEnergy()+" ---------------------------------------");
	}
	
	public void freFrequencyTable(int timing){
		for(Cluster clu: Common.getClusters()){
			for(Core c: clu.getCores()){
				if(frequencyTable[c.getCoreID()][timing]!=null){
					for(Core cc: clu.getCores()){
						frequencyTable[cc.getCoreID()][timing] = frequencyTable[c.getCoreID()][timing];
					}
				}
			}
		}
	}
}

class Table{
	
	private Job[][]schedulingTable;
	private Frequency[][]frequencyTable;
	
	public Table(Job[][]jTable, Frequency[][]fTable){
		schedulingTable = jTable.clone();
		frequencyTable = fTable.clone();
	}
	
	public Table(){
		
	}
	
	public void copy(Table table){
		schedulingTable = new Job[table.getSchedulingTable().length][table.getSchedulingTable()[0].length];
		frequencyTable = new Frequency[table.getFrequencyTable().length][table.getFrequencyTable()[0].length];
		ArrayList<Job> job_list = new ArrayList<Job>();
		Job t_j;
		
		for(int i=0;i<schedulingTable.length;++i){
			for(int j=0;j<schedulingTable[0].length;++j){
				if(table.getSchedulingTable()[i][j]!=null){				
					schedulingTable[i][j] = table.getSchedulingTable()[i][j];				
				}
				if(table.getFrequencyTable()[i][j]!=null){
					frequencyTable[i][j] = table.getFrequencyTable()[i][j];
				}
			}
		}
		
	}
	
	public Job getSchedulingTableValue(int row, int col){
		return schedulingTable[row][col];
	}
	
	public Frequency getFrequencyTableValue(int row, int col){
		return frequencyTable[row][col];
	}
	
	public void setSchedulingTableValue(int row, int col, Job j){
		schedulingTable[row][col] = j;
	}
	
	public void setFrequencyTableValue(int row, int col, Frequency f){
		frequencyTable[row][col] = f;
	}
	
	public Job[][] getSchedulingTable(){
		return schedulingTable;
	}
	
	public Frequency[][] getFrequencyTable(){
		return frequencyTable;
	}
	
	public double getEnergy(){
		double energy = 0;	
		for(int i=0;i<schedulingTable[0].length;++i){
			energy += getPower(i);
		}
		return energy;
	}
	
	private double getPower(int timing){
		double power = 0;
		for(int i=0;i<schedulingTable.length;++i){
			if(schedulingTable[i][timing]!=null){
				power += frequencyTable[i][timing].getActivePower();
			}else{//no job is executing now
				if(frequencyTable[i][timing]==null){//cluster is closed
					power += 0;
				}else{//cluster is in idle status
					power += frequencyTable[i][timing].getIdlePower();
				}
			}
		}
		return power;
	}
	
	public void display(){
		System.out.println("Scheduling: ");
		for(int i=0;i<schedulingTable.length;++i){
			for(int j=0;j<schedulingTable[0].length;++j){
				if(schedulingTable[i][j]!=null){
					System.out.print(schedulingTable[i][j].getJobID()+"\t");
				}else{
					System.out.print("N\t");
				}
			}
			System.out.println();
		}
		System.out.println("Frequency: ");
		for(int i=0;i<frequencyTable.length;++i){
			for(int j=0;j<frequencyTable[0].length;++j){
				if(frequencyTable[i][j]!=null){
					System.out.print(frequencyTable[i][j].getFrequency()+"\t");
				}else{
					System.out.print("N\t");
				}
				
			}
			System.out.println();
		}
		System.out.println("=========================");
	}
}

