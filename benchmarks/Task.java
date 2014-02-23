package benchmarks;

import java.util.ArrayList;
import java.util.HashMap;

import platform.Cluster;

public class Task {
	int taskID;
	ArrayList<Version> versions_list;
	ArrayList<Job> jobs_list;
	
	HashMap<Cluster,Double> relativeRunTime;
	//int relativeRunTime;
	int deadline;
	
	public Task(int id, int _deadline){
		this.taskID = id;
		//relativeRunTime1 = _runTime;
		relativeRunTime = new HashMap<Cluster,Double>();
		deadline = _deadline;
		versions_list =  new ArrayList<Version>();
		jobs_list = new ArrayList<Job>();
	}
	
	public void setRunTimeOnCluster(Cluster clu, double runtime){
		relativeRunTime.put(clu, runtime);
	}
	
	public void addVersions(Version v){
		versions_list.add(v);
	}
	
	public void addJobs(Job j){
		jobs_list.add(j);
	}
	
	
	
	public HashMap<Cluster, Double> getRunTimeSetOnClusters(){
		return relativeRunTime;
	}
	
	public int getDeadline(){
		return deadline;
	}
	
	public int getTaskID(){
		return taskID;
	}
	
	
	public ArrayList<Version> getVersionsOnSpecificCluster(Cluster c){
		ArrayList<Version> temp_versions_list = new ArrayList<Version>();
		for(Version v: versions_list){
			if(c.equals(v.getCluster()))
				temp_versions_list.add(v);
		}
		return temp_versions_list;
	}
	
	public Version getWorstVersionOnSpecificCluster(Cluster clu){
		ArrayList<Version> list = getVersionsOnSpecificCluster(clu);
		double maxSpeed = 0;
		Version version = null;
		for(Version v: list){
			if(v.getSpeedup()>maxSpeed){
				maxSpeed = v.getSpeedup();
				version = v;
			}
		}
		return version;
	}
}
