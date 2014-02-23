package solution;

import platform.Frequency;
import benchmarks.Job;

import common.Common;


public class Scheduling{
	
	private int[] versionCode;
	private boolean available = false;
	private boolean newBorn = true;
	private Job[][] jT = null;
	private Frequency[][] fT = null;
	private double[][] frequencyTable;
	private int[][] jobTable;
	private char[][] versionTable;
	/*private double[] powerTable;	*/
	private double energy;
	private double QoS;
	private int rank = 0;
	
	public Scheduling(){
		versionCode = new int[Common.getJobs().size()];
		newBorn = true;
		available = false;
	}
	
	public Scheduling(double _energy, double _QoS){
		energy = _energy;
		if(_QoS>1){
			QoS = _QoS/Common.getJobs().size();
		}else{
			QoS = _QoS;
		}
		newBorn = false;
		available = true;
	}
	
	public Scheduling(Job[][] _jobTable, Frequency[][] _frequencyTable, double _energy, double _QoS){
/*		jT = _jobTable.clone();
		fT = _frequencyTable.clone();*/
		jobTable = new int[_jobTable.length][_jobTable[0].length];
		frequencyTable = new double[_frequencyTable.length][_frequencyTable[0].length];
		versionTable = new char[_jobTable.length][_jobTable[0].length];
		for(int i=0;i<_frequencyTable.length;++i){
			for(int j=0;j<_frequencyTable[0].length;++j){
				if(_frequencyTable[i][j]!=null){
					frequencyTable[i][j] = _frequencyTable[i][j].getFrequency();
				}
				if(_frequencyTable[i][j]==null){
					frequencyTable[i][j] = 0;
				}
				if(_jobTable[i][j]!=null){
					jobTable[i][j] = _jobTable[i][j].getJobID();
					versionTable[i][j] = (char) ('0'+_jobTable[i][j].getChozenVersion().getVersionID());
				}
				if(_jobTable[i][j]==null){
					jobTable[i][j] = -1;
					versionTable[i][j] = 'N';
				}
			}
		}
		energy = _energy;
		if(_QoS>1){
			QoS = _QoS/Common.getJobs().size();
		}else{
			QoS = _QoS;
		}

		
		newBorn = false;
		available = true;
	}
	
	public void copy(Scheduling s){
		fresh(s.getJobTable(), s.getFrequencyTable(), s.getEnergy(), s.getQoS());
	}
	
	public Job[][] getJobTable(){
		return jT;
	}
	
	public Frequency[][] getFrequencyTable(){
		return fT;
	}
	
	public int getVersionOfJob(int jobID){
		return versionCode[jobID];
	}
	
	public int[] getVersion(){
		return versionCode;
	}
	
	public boolean isNewBorn(){
		return newBorn;
	}
	
	public void referInit(VersionForJob jv){	
		versionCode[jv.getJobID()] = jv.getVersionID();
	}
	
	public void randomInit(){
		for(int i=0;i<Common.getJobs().size();++i){
			versionCode[i] = (int)(Math.random()*Common.getJobs().get(0).getFatherTask().getVersionsOnSpecificCluster(Common.getClusters().get(0)).size());
		}
	}
	
	public void specificInit(int[] specificVersion){
		for(int i=0;i<Common.getJobs().size();++i){
			versionCode[i] = specificVersion[i];
		}
	}
	
	public boolean isAvailable(){
		return available;
	}
	
	public synchronized void upRank(){
		++rank;
	}
	
	public synchronized void downRank(){
		--rank;
	}
	
	public synchronized void clearRank(){
		rank = 0;
	}
	
	public int getRank(){
		return rank;
	}
	
	public double getQoS(){
		return QoS;
	}
	
	public double getEnergy(){
		return energy;
	}
	
	public boolean noChange(VersionForJob vfj){
		if(versionCode[vfj.getJobID()]==vfj.getVersionID()){
			//System.out.println("do not need change~!");
			return true;
		}
		return false;
	}
	
	public void show(){	
		System.out.println("Scheduling solution (energy): "+energy);
		System.out.println("Scheduling solution (QoS): "+QoS);
		System.out.println("Scheduling solution (Rank): "+rank);
		System.out.println("---------------------------------------------------");
	}
	
	public void show_withTables(){
		/*System.out.println("---------------------------------------------------");
		System.out.println("Scheduling solution (Job): ");
		for(int i=0;i<jobTable.length;++i){
			for(int j=0;j<jobTable[i].length;++j){
				if(jobTable[i][j]==-1){
					System.out.print("N\t");
				}else{
					System.out.print(jobTable[i][j]+"\t");
				}
				
			}
			System.out.println();
		}
		System.out.println("Scheduling solution (Frequency): ");
		for(int i=0;i<frequencyTable.length;++i){
			for(int j=0;j<frequencyTable[i].length;++j){
				if(frequencyTable[i][j]==0){
					System.out.print("N\t");
				}else{
					System.out.print(frequencyTable[i][j]+"\t");
				}
				
			}
			System.out.println();
		}
		System.out.println("Scheduling solution (Version): ");
		for(int i=0;i<versionTable.length;++i){
			for(int j=0;j<versionTable[i].length;++j){
				System.out.print(versionTable[i][j]+"\t");
			}
			System.out.println();
		}
		*/	
		System.out.print(QoS+"\t");
		System.out.println(energy);
	}
	
	public void fresh(Job[][] _jobTable, Frequency[][] _frequencyTable, double _energy, double _QoS){
		jT = new Job[_jobTable.length][_jobTable[0].length];
		fT = new Frequency[_frequencyTable.length][_frequencyTable[0].length];
		jobTable = new int[_jobTable.length][_jobTable[0].length];
		frequencyTable = new double[_frequencyTable.length][_frequencyTable[0].length];
		versionTable = new char[_jobTable.length][_jobTable[0].length];
		for(int i=0;i<_frequencyTable.length;++i){
			for(int j=0;j<_frequencyTable[0].length;++j){
				if(_frequencyTable[i][j]!=null){
					fT[i][j] = _frequencyTable[i][j];
					frequencyTable[i][j] = _frequencyTable[i][j].getFrequency();
				}
				if(_frequencyTable[i][j]==null){
					fT[i][j] = null;
					frequencyTable[i][j] = 0;
				}
				if(_jobTable[i][j]!=null){
					jT[i][j] = _jobTable[i][j];
					jobTable[i][j] = _jobTable[i][j].getJobID();
					versionTable[i][j] = (char) ('0'+_jobTable[i][j].getChozenVersion().getVersionID());
				}
				if(_jobTable[i][j]==null){
					jT[i][j] = null;
					jobTable[i][j] = -1;
					versionTable[i][j] = 'N';
				}
			}
		}
		
		energy = _energy;
		if(_QoS>1){
			QoS = _QoS/Common.getJobs().size();
		}else{
			QoS = _QoS;
		}
		
		newBorn = false;
		available = true;
	}
	
	public void fresh(double _energy, double _QoS){
		energy = _energy;
		if(_QoS>1){
			QoS = _QoS/Common.getJobs().size();
		}else{
			QoS = _QoS;
		}
		
		newBorn = false;
		available = true;
	}
	
	public void mutate(int index){
	
		if(versionCode[index]==2){
			versionCode[index]=1;
		}
		if(versionCode[index]==1){
			versionCode[index]=0;
		}
	}
	
	public void fastestVersionInit(){
		for(int i=0;i<Common.getJobs().size();++i){
			versionCode[i] = Common.getJobs().get(0).getFatherTask().getVersionsOnSpecificCluster(Common.getClusters().get(0)).size()-1;
		}
		
	}
}
