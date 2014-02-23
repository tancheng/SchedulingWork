package benchmarks;

import java.util.HashMap;

import common.Common;

import platform.Cluster;
import platform.Core;
import platform.Frequency;

public class Job {
	int jobID;
	Task myTask;
	Version chozenVersion;
	Core allocatedCore;
	int issueTime;
	int deadline;
	HashMap<Cluster,Double>originalRunTime;
	
	public int currentExecutedTime = 0;
	int[] jobTimeSlot;
	Frequency[] frequencyTimeSlot;
	int readyVersion = 0;
	boolean setReadyVersion = false;
	
	//double intervalStillNeedToBeExtend = 0;
	
	public Job(int id,int _issueTime, HashMap<Cluster,Double> _runTime, int _deadline){
		jobID = id;
		issueTime = _issueTime;
		//originalRunTime = _runTime;
		originalRunTime = _runTime;
		deadline = _deadline;
		jobTimeSlot = new int[Common.getTotalPeriod()];
		frequencyTimeSlot = new Frequency[Common.getTotalPeriod()];

	}

	public Job(){
		
	}
	
	public void copy(Job job_copy){
//		jobID = job.getJobID();
//		issueTime = job.getIssueTime();
//		//originalRunTime = _runTime;
//		originalRunTime = job.getAllRunTime();
//		deadline = job.getDeadline();
//		jobTimeSlot = job.getJobTimeSlot();
//		frequencyTimeSlot = job.getFrequencyTimeSlot();
//		intervalStillNeedToBeExtend = job.getFillTime();
//		
//		myTask = job.getFatherTask();
// 		chozenVersion = job.getChozenVersion();
//		allocatedCore = job.getCore();
//		readyVersion = job.getReadyVersion();
//		setReadyVersion = job.getSetReadyVersion();
//		currentExecutedTime = job.getCurrentExecutedTime();
		
		

		jobID = job_copy.getJobID();
		myTask = job_copy.getFatherTask();
		chozenVersion = job_copy.getChozenVersion();
		allocatedCore = job_copy.getCore();
		issueTime = job_copy.getIssueTime();
		deadline = job_copy.getDeadline();
		originalRunTime = job_copy.originalRunTime;
		
		currentExecutedTime = job_copy.getCurrentExecutedTime();

		jobTimeSlot = new int[Common.getTotalPeriod()];
		frequencyTimeSlot = new Frequency[Common.getTotalPeriod()];
		for(int i=0;i<jobTimeSlot.length;++i){
			jobTimeSlot[i] = job_copy.jobTimeSlot[i];
		}
		for(int i=0;i<jobTimeSlot.length;++i){
			frequencyTimeSlot[i] = job_copy.frequencyTimeSlot[i];
		}
		
		readyVersion = job_copy.getReadyVersion();
		setReadyVersion = job_copy.getSetReadyVersion();
		
	}
	
	public int getCurrentExecutedTime(){
		return currentExecutedTime;
	}
	
	public int getReadyVersion(){
		return readyVersion;
	}
	
	public boolean getSetReadyVersion(){
		return setReadyVersion;
	}
	
	public HashMap<Cluster,Double> getAllRunTime(){
		return originalRunTime;
	}
	
/*	public void extendFillTime(double t){
		intervalStillNeedToBeExtend += t;
	}
	
	public void shortenFillTime(double t){
		intervalStillNeedToBeExtend -= t;
	}
	
	public double getFillTime(){
		return intervalStillNeedToBeExtend;
	}
	*/
	public void clear(){
		jobTimeSlot = new int[Common.getTotalPeriod()];
		frequencyTimeSlot = new Frequency[Common.getTotalPeriod()];
		currentExecutedTime = 0;
	//	intervalStillNeedToBeExtend = 0;
	}

	public Version getWorstVersion(){
		double minQuality = 1;
		Version worstVersion = null;
		for(Version v: myTask.getVersionsOnSpecificCluster(allocatedCore.getCluster())){
			if(v.getQuality()<=minQuality){
				minQuality = v.getQuality();
				worstVersion = v;
			}
		}
		return worstVersion;
	}
	
	public Version getChozenVersion(){
		return chozenVersion;
	}
	
	public boolean isWorstVersion(){
		double minQuality = 1;
		Version ver = null;
		for(Version v: myTask.getVersionsOnSpecificCluster(allocatedCore.getCluster())){
			if(v.getQuality()<minQuality){
				minQuality = v.getQuality();
				ver = v;
			}
		}
		return ver.equals(chozenVersion);
	}
	
	public void setVersion(Version v){
		//System.out.println("allocatedCore: "+allocatedCore.getCoreID());
/*		if(!v.getCluster().equals(allocatedCore.getCluster())){
			System.err.println("Warning: Version on core cannot be matched! ");
		}*/
		//relativeRunTime = (int)(originalRunTime/v.getSpeedup()/v.getCluster().getMaxFrequency().getFrequency()+0.5);
		chozenVersion = v;
	}
	
	public void setReadyVersion(int index_version){

		readyVersion = index_version;
		setReadyVersion = true;
		
	}
	
	
	public double getNewRunTimeWithoutDVFS(Version v){
		/*if(!v.getCluster().equals(allocatedCore.getCluster())){
			System.err.println("Warning: Version on core cannot be matched! ");
		}*/
		return (originalRunTime.get(allocatedCore.getCluster())/v.getSpeedup());
	}
	
	public double getOldRunTimeWithoutDVFS(){
		
		return (originalRunTime.get(allocatedCore.getCluster())/chozenVersion.getSpeedup());
	}
	
	public void allocateCore(Core _allocatedCore){
		allocatedCore = _allocatedCore;
		if(setReadyVersion){
			Version v = myTask.getVersionsOnSpecificCluster(_allocatedCore.getCluster()).get(readyVersion);
			//relativeRunTime = (int)(originalRunTime/v.getSpeedup()/_allocatedCore.getCluster().getMaxFrequency().getFrequency()+0.5);
			chozenVersion = v;
		}
	}
	
	public int[] getJobTimeSlot(){
		
		return jobTimeSlot;
	}
	
	public void setJobTimeSlot(int timeSlot){
		jobTimeSlot[timeSlot] = 1;
		++currentExecutedTime;
	}
	
	public Core getCore(){
		return allocatedCore;
	}
	
	public Frequency[] getFrequencyTimeSlot(){
		
		return frequencyTimeSlot;
	}
	
	public int getIssueTime(){
		return issueTime;
	}

	public int getDeadline(){
		return deadline;
	}
	
	public double getRunTime(){
		return originalRunTime.get(allocatedCore.getCluster())/chozenVersion.getSpeedup()/allocatedCore.getCluster().getMaxFrequency().getFrequency();
		//return relativeRunTime;
	}
	
	public void setFatherTask(Task t){
		myTask = t;
	}
	
	public Task getFatherTask(){
		return myTask;
	}
	
	public int getJobID(){
		return jobID;
	}
	
	public int getCompleteTime(){
		int completeTime = 0;
		for(int i=0;i<jobTimeSlot.length;++i){
			if(jobTimeSlot[i]!=0&&completeTime<i)
				completeTime = i;
		}
		return completeTime;
	}
	

	
	public boolean stillMeetDeadline(int timing){
		if(timing<deadline){
			return true;
		}
		return false;
	}
	
	public boolean isCompleted(){
		if(currentExecutedTime+0.5>(int)(originalRunTime.get(allocatedCore.getCluster())/chozenVersion.getSpeedup()/allocatedCore.getCluster().getMaxFrequency().getFrequency())){
			
			return true;
		}
		return false;
	}
	
}
