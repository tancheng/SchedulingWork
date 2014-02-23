package system;

import java.util.ArrayList;

import common.Common;

import benchmarks.Job;


public class GlobalTaskScheduler {
	ArrayList<LocalJobScheduler> ljs_list = new ArrayList<LocalJobScheduler>();
	
	public void addJobScheduler(LocalJobScheduler ljs){
		ljs_list.add(ljs);
	}
	
	public ArrayList<LocalJobScheduler> getLocalJobSchedulers(){
		return ljs_list;
	}
	
	/**
	 * Allocating job. Allocating job onto appropriate core.
	 * @param j
	 */
	public boolean allocateJob(ArrayList<Job> jobs_list){
		ArrayList<Job> comingJobs = new ArrayList<Job>();
		clear();
		for(int currentTime=0;currentTime<Common.getTotalPeriod();++currentTime){

			
			for(Job j: jobs_list){
				
				if(j.getIssueTime()==currentTime){
					//System.out.println("jobID: "+j.getJobID()+" ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
					comingJobs.add(j);
				}
			}
			Job tempJob = null;
			int size = comingJobs.size();
			
			for(int i = 0;i<size;++i){
				tempJob = getJobWithMinDeadline(comingJobs);
				
				if(!chooseProperLocalJobScheduler(tempJob)){
					//System.out.println("timing: ******************"+currentTime+"let us see!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!: "+tempJob.getJobID()+"allocatedCore: "+tempJob.getCore().getCoreID());
					System.out.println("^^^^^^^^^^^^^^^^^^^^chachahchahchahca^^^^^^^^^^^^^^^^^^^^");
					System.exit(0);
					//System.err.println("sorry");
					return false;
				}
				//System.out.println("timing: "+currentTime+"let us see!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!: "+tempJob.getJobID()+"allocatedCore: "+tempJob.getCore().getCoreID());
				comingJobs.remove(tempJob);
			}
			for(LocalJobScheduler ljs: ljs_list){
				ljs.proceed();
				if(!ljs.waitForNextCycleToProceed()){
					//System.out.println("timing: ******************"+currentTime+"let us see!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!: "+ljs.getCurrentJob().getJobID()+"allocatedCore: "+ljs.getCurrentJob().getCore().getCoreID());
					return false;
				}
			}
		}
		for(LocalJobScheduler ljs: ljs_list){
			if(!ljs.waitForNextCycleToProceed()){
				System.out.println("!@#%^&^&**%&&#%^#");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Allocating job. Allocating job onto appropriate core.
	 * @param j
	 */
	public boolean allocateJob_allocatedVersion(ArrayList<Job> jobs_list){
		
		ArrayList<Job> comingJobs = new ArrayList<Job>();
		clear();
		for(int currentTime=0;currentTime<Common.getTotalPeriod();++currentTime){
			for(Job j: jobs_list){		
				
				if(j.getIssueTime()==currentTime){
					comingJobs.add(j);
					
				}
			}
			Job tempJob = null;
			int size = comingJobs.size();
			for(int i = 0;i<size;++i){
				tempJob = getJobWithMinDeadline(comingJobs);
				
				
				if(!chooseProperLocalJobScheduler_allocatedVersion(tempJob)){
					return false;
				}
				comingJobs.remove(tempJob);
			}
			for(LocalJobScheduler ljs: ljs_list){
				ljs.proceed();
				if(!ljs.waitForNextCycleToProceed()){

					return false;
				}
			}
		}
		return true;
	}
	
	private void clear(){
		//System.out.println("clear");
		for(LocalJobScheduler ljs: ljs_list){
			ljs.clear();
		}
	}
	
	private Job getJobWithMinDeadline(ArrayList<Job> _comingJobs){
		int minDeadline = _comingJobs.get(0).getDeadline();
		Job job = _comingJobs.get(0);
		for(Job j: _comingJobs){
			if(minDeadline>j.getDeadline()){
				minDeadline = j.getDeadline();
				job = j;
			}
		}
		return job;
	}
	
	private int getHighestRank(){
		int maxRank = 0;
		for(LocalJobScheduler ljs: ljs_list){
			if(ljs.getRank()>maxRank){
				maxRank = ljs.getRank();
			}
		}
		return maxRank;
	}
	
	private boolean chooseProperLocalJobScheduler(Job job){
		int maxRank = getHighestRank();
		ArrayList<LocalJobScheduler> maxRank_ljs_list;
		for(int i=maxRank;i>=0;--i){
			maxRank_ljs_list = new ArrayList<LocalJobScheduler>();
			for(LocalJobScheduler ljs: ljs_list){
				if(ljs.getRank()==i){
					maxRank_ljs_list.add(ljs);
				}
			}
			//there is a core has no jobs need to be executed
			for(LocalJobScheduler ljs: maxRank_ljs_list){
				if(ljs.getCurrentJob()==null){
					ljs.addJob(job);
					return true;
				}
			}		
			//there is a core has a job with farther deadline than this job
			LocalJobScheduler ljs = getLocalJobSchedulerWithFarthestDeadline(maxRank_ljs_list);
			if(i == 0){
				ljs.addJob(job);
				return true;
			}
			if(ljs.getCurrentJob().getDeadline()>job.getDeadline()){
				ljs.addJob(job);
				return true;
			}	
			//no proper cores in this rank, consider the cores with lower rank			
		}
		return false;
	}
	
	private boolean chooseProperLocalJobScheduler_allocatedVersion(Job job){
		int maxRank = getHighestRank();
		ArrayList<LocalJobScheduler> maxRank_ljs_list;
		for(int i=maxRank;i>=0;--i){
			maxRank_ljs_list = new ArrayList<LocalJobScheduler>();
			for(LocalJobScheduler ljs: ljs_list){
				if(ljs.getRank()==i){
					maxRank_ljs_list.add(ljs);
				}
			}
			//there is a core has no jobs need to be executed
			for(LocalJobScheduler ljs: maxRank_ljs_list){
				if(ljs.getCurrentJob()==null){
					if(!ljs.addJob_allocatedVersion(job)){
						return false;
						
					}
					return true;
				}
			}		
			//there is a core has a job with farther deadline than this job
			LocalJobScheduler ljs = getLocalJobSchedulerWithFarthestDeadline(maxRank_ljs_list);
			if(i == 0){
				if(!ljs.addJob_allocatedVersion(job)){
					return false;
				}
				return true;
			}
			if(ljs.getCurrentJob().getDeadline()>job.getDeadline()){
				if(!ljs.addJob_allocatedVersion(job)){
					return false;
				}
				return true;
			}	
			//no proper cores in this rank, consider the cores with lower rank			
		}
		return false;
	}
	
	private LocalJobScheduler getLocalJobSchedulerWithFarthestDeadline(ArrayList<LocalJobScheduler> ljs_certainRank_list){
		int farthestDeadline = 0;
		LocalJobScheduler ljs_FarthestDeadline = null;
		int comparedDeadline = 0;
		for(LocalJobScheduler ljs: ljs_certainRank_list){
			comparedDeadline = ljs.getCurrentJob().getDeadline();
			if(comparedDeadline > farthestDeadline){
				farthestDeadline = comparedDeadline;
				ljs_FarthestDeadline = ljs;
			}		
		}
		return ljs_FarthestDeadline; 
	}
	
}
