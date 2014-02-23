package system;

import java.util.ArrayList;

import common.Common;

import benchmarks.Job;
import benchmarks.Version;
import platform.Core;

public class LocalJobScheduler {
	private Core myCore;
	private Job currentJob = null;
	private ArrayList<Job> jobs_list = new ArrayList<Job>();
	private int timing = 0;
	
	public LocalJobScheduler(Core c){
		myCore = c;
	}
	
	public void clear(){
		currentJob = null;
		jobs_list = new ArrayList<Job>();
		timing = 0;
	}
	
	public void addJob(Job j){
		jobs_list.add(j);
		j.allocateCore(myCore);
		
		//set worst version for this job
		j.setVersion(j.getWorstVersion());
		
		currentJob = getJobWithMinDeadline(jobs_list);
		
		//proceed();
	}
	
	public boolean addJob_allocatedVersion(Job j){
		jobs_list.add(j);
		j.allocateCore(myCore);

		//set worst version for this job
		if(j.getChozenVersion().getCluster()!=myCore.getCluster()){
			//System.err.println("Cluster is not matched, cannot schedule this combination through EDF.");
			System.out.println("TMk%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%TM");
			return false;
		}
		if(jobs_list.size()!=0){
			currentJob = getJobWithMinDeadline(jobs_list);
		}
		//proceed();
		return true;
	}
	
	public Core getMatchedCore(){
		return myCore;
	}

	public int getRank(){
		return myCore.getRank();
	}
	
	public Job getCurrentJob(){

		return currentJob;
	}
	
	public void proceed(){
		currentJob = getJobWithMinDeadline(jobs_list);
		
		if(currentJob!=null){
			currentJob.setJobTimeSlot(timing);
		}	
		
	}
	
	private Job getJobWithMinDeadline(ArrayList<Job> _comingJobs){
		
		ArrayList<Job> temp_list = new ArrayList<Job>();
		for(Job job: _comingJobs){
			temp_list.add(job);
		}
		
/*		for(Job j: _comingJobs){
			if(j.isCompleted()){
				temp_list.remove(j);
			}
		}*/
		int minDeadline;
		Job job = null;
		if(temp_list.size()!=0){
			minDeadline = temp_list.get(0).getDeadline();
			job = temp_list.get(0);
			for(Job j: temp_list){
				if(minDeadline>j.getDeadline()){
					minDeadline = j.getDeadline();
					job = j;
				}
			}
		}
		return job;
	}
	
	public boolean waitForNextCycleToProceed(){
/*		if(currentJob!=null){
			if(currentJob.getJobID()==0||currentJob.getJobID()==1||currentJob.getJobID()==2||currentJob.getJobID()==6||currentJob.getJobID()==5||currentJob.getJobID()==9||currentJob.getJobID()==11){
				
					System.out.println("cannot be scheduled using EDF! let's see why~?! JobID: "+currentJob.getJobID()+" timing: "+timing+" job Run Time: "+currentJob.getRunTime()+";version: "+currentJob.getChozenVersion().getVersionID()+";core: "+currentJob.getCore().getCoreID());
			}
		}*/
		ArrayList<Job> temp_list = new ArrayList<Job>();
		for(Job t: jobs_list){
			temp_list.add(t);
		}
		for(Job j: temp_list){
		
			if(!j.stillMeetDeadline(timing)){
				return false;
			}
			if(j.isCompleted()){
				if(j==currentJob){
					currentJob = null;
				}
				jobs_list.remove(j);
			}
		}
	
		//currentJob = null;
		
	
		++timing;
		
		return true;
	}
}
