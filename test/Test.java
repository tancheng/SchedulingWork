package test;

import java.util.Scanner;

import benchmarks.Task;
import benchmarks.Version;
import platform.*;
import system.GlobalTaskScheduler;
import system.LocalJobScheduler;
import common.Common;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub 
		//initClustersAndScheduler_small();
		initClustersAndScheduler_true();
		initTasks();
		
		long begin = System.currentTimeMillis();
		
		

		Common.scheduleTasks_heuristic();
		//Common.scheduleTasks_exhaustive();
		
		
		long end = System.currentTimeMillis();
		System.out.println("ExeTime_exaustive: "+(end-begin));
		
	}


	/**
	 * For changing the demo tasks.
	 */
	static void initTasks(){
		
		//demo_assumption_1t_1j_smallPlatform();
		//demo_SPEC_small();
		//demo_x264_bodytrack_true();
		//demo_bodytrack_true();
		
     		//demo_x264_bodytrack_4t_55j_true();

     		demo_x264_bodytrack_12t_112j_true();

	}
	
	
	
	static void demo_assumption_1t_1j_smallPlatform(){
		Cluster clu0 = Common.getCluster(0);
		Cluster clu1 = Common.getCluster(1);
		Task t;		

		t = new Task(0,10);
		t.setRunTimeOnCluster(clu0, 20);
		t.setRunTimeOnCluster(clu1, 20);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		Common.loadJobs();
	}
	
	static void demo_assumption_2t_7j_smallPlatform(){
		Cluster clu0 = Common.getCluster(0);
		Cluster clu1 = Common.getCluster(1);
		Task t;		
		t = new Task(0,4);
		t.setRunTimeOnCluster(clu0, 8);
		t.setRunTimeOnCluster(clu1, 8);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.998,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.998,2,Common.getCluster(1)));
		Common.loadTask(t);

		t = new Task(1,10);
		t.setRunTimeOnCluster(clu0, 20);
		t.setRunTimeOnCluster(clu1, 20);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		Common.loadJobs();
	}

     	static void demo_x264_true(){
                Cluster clu0 = Common.getCluster(0);
                Cluster clu1 = Common.getCluster(1);
                Task t;
                t = new Task(0,100);
                t.setRunTimeOnCluster(clu0, 110);
                t.setRunTimeOnCluster(clu1, 93.74);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.999,1.84,Common.getCluster(0)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(0)));
                t.addVersions(new Version(3,0.9898,3.1,Common.getCluster(0)));
                t.addVersions(new Version(4,0.9901,4.22,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9971,4.45,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.999,2,Common.getCluster(1)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(1)));
                t.addVersions(new Version(3,0.9898,4.1,Common.getCluster(1)));
                t.addVersions(new Version(4,0.9901,5.22,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9971,5.45,Common.getCluster(1)));
		Common.loadTask(t);
               
                Common.loadJobs();
        }


     	static void demo_bodytrack_true(){
                Cluster clu0 = Common.getCluster(0);
                Cluster clu1 = Common.getCluster(1);
                Task t;

                t = new Task(0,20);
                t.setRunTimeOnCluster(clu0, 18);
                t.setRunTimeOnCluster(clu1, 13.49);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.9983,1.0001,Common.getCluster(0)));
                t.addVersions(new Version(2,0.99634,2.589,Common.getCluster(0)));
                t.addVersions(new Version(3,0.98693,3.735,Common.getCluster(0)));
                t.addVersions(new Version(4,0.98556,4.253,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9847,4.422,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.9983,1.0007,Common.getCluster(1)));
                t.addVersions(new Version(2,0.99634,2.789,Common.getCluster(1)));
                t.addVersions(new Version(3,0.98693,3.935,Common.getCluster(1)));
                t.addVersions(new Version(4,0.98556,5.253,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9847,5.422,Common.getCluster(1)));

		Common.loadTask(t);
       
		Common.loadJobs();
	}	


     	static void demo_x264_bodytrack_2t_6j_true(){
                Cluster clu0 = Common.getCluster(0);
                Cluster clu1 = Common.getCluster(1);
                Task t;
                t = new Task(0,100);
                t.setRunTimeOnCluster(clu0, 110);
                t.setRunTimeOnCluster(clu1, 93.74);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.999,1.84,Common.getCluster(0)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(0)));
                t.addVersions(new Version(3,0.9898,3.1,Common.getCluster(0)));
                t.addVersions(new Version(4,0.9901,4.22,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9971,4.45,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.999,2,Common.getCluster(1)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(1)));
                t.addVersions(new Version(3,0.9898,4.1,Common.getCluster(1)));
                t.addVersions(new Version(4,0.9901,5.22,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9971,5.45,Common.getCluster(1)));

		Common.loadTask(t);

        

                t = new Task(1,20);
                t.setRunTimeOnCluster(clu0, 18);
                t.setRunTimeOnCluster(clu1, 13.49);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.9983,1.0001,Common.getCluster(0)));
                t.addVersions(new Version(2,0.99634,2.589,Common.getCluster(0)));
                t.addVersions(new Version(3,0.98693,3.735,Common.getCluster(0)));
                t.addVersions(new Version(4,0.98556,4.253,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9847,4.422,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.9983,1.0007,Common.getCluster(1)));
                t.addVersions(new Version(2,0.99634,2.789,Common.getCluster(1)));
                t.addVersions(new Version(3,0.98693,3.935,Common.getCluster(1)));
                t.addVersions(new Version(4,0.98556,5.253,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9847,5.422,Common.getCluster(1)));

		Common.loadTask(t);
       
		Common.loadJobs();
	}	


     	static void demo_x264_bodytrack_4t_55j_true(){
                Cluster clu0 = Common.getCluster(0);
                Cluster clu1 = Common.getCluster(1);
                Task t;
                t = new Task(0,100);
                t.setRunTimeOnCluster(clu0, 110);
                t.setRunTimeOnCluster(clu1, 93.74);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.999,1.84,Common.getCluster(0)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(0)));
                t.addVersions(new Version(3,0.9898,3.1,Common.getCluster(0)));
                t.addVersions(new Version(4,0.9901,4.22,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9971,4.45,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.999,2,Common.getCluster(1)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(1)));
                t.addVersions(new Version(3,0.9898,4.1,Common.getCluster(1)));
                t.addVersions(new Version(4,0.9901,5.22,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9971,5.45,Common.getCluster(1)));

		Common.loadTask(t);

        

                t = new Task(1,20);
                t.setRunTimeOnCluster(clu0, 18);
                t.setRunTimeOnCluster(clu1, 13.49);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.9983,1.0001,Common.getCluster(0)));
                t.addVersions(new Version(2,0.99634,2.589,Common.getCluster(0)));
                t.addVersions(new Version(3,0.98693,3.735,Common.getCluster(0)));
                t.addVersions(new Version(4,0.98556,4.253,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9847,4.422,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.9983,1.0007,Common.getCluster(1)));
                t.addVersions(new Version(2,0.99634,2.789,Common.getCluster(1)));
                t.addVersions(new Version(3,0.98693,3.935,Common.getCluster(1)));
                t.addVersions(new Version(4,0.98556,5.253,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9847,5.422,Common.getCluster(1)));

		Common.loadTask(t);
      

                t = new Task(2,150);
                t.setRunTimeOnCluster(clu0, 110);
                t.setRunTimeOnCluster(clu1, 93.74);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.999,1.84,Common.getCluster(0)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(0)));
                t.addVersions(new Version(3,0.9898,3.1,Common.getCluster(0)));
                t.addVersions(new Version(4,0.9901,4.22,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9971,4.45,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.999,2,Common.getCluster(1)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(1)));
                t.addVersions(new Version(3,0.9898,4.1,Common.getCluster(1)));
                t.addVersions(new Version(4,0.9901,5.22,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9971,5.45,Common.getCluster(1)));

		Common.loadTask(t);
        
                t = new Task(3,40);
                t.setRunTimeOnCluster(clu0, 18);
                t.setRunTimeOnCluster(clu1, 13.49);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.9983,1.0001,Common.getCluster(0)));
                t.addVersions(new Version(2,0.99634,2.589,Common.getCluster(0)));
                t.addVersions(new Version(3,0.98693,3.735,Common.getCluster(0)));
                t.addVersions(new Version(4,0.98556,4.253,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9847,4.422,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.9983,1.0007,Common.getCluster(1)));
                t.addVersions(new Version(2,0.99634,2.789,Common.getCluster(1)));
                t.addVersions(new Version(3,0.98693,3.935,Common.getCluster(1)));
                t.addVersions(new Version(4,0.98556,5.253,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9847,5.422,Common.getCluster(1)));

		Common.loadTask(t);

		Common.loadJobs();
	}	


     	static void demo_x264_bodytrack_12t_112j_true(){
                Cluster clu0 = Common.getCluster(0);
                Cluster clu1 = Common.getCluster(1);
                Task t;
                t = new Task(0,1000);
                t.setRunTimeOnCluster(clu0, 110);
                t.setRunTimeOnCluster(clu1, 93.74);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.999,1.84,Common.getCluster(0)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(0)));
                t.addVersions(new Version(3,0.9898,3.1,Common.getCluster(0)));
                t.addVersions(new Version(4,0.9901,4.22,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9971,4.45,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.999,2,Common.getCluster(1)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(1)));
                t.addVersions(new Version(3,0.9898,4.1,Common.getCluster(1)));
                t.addVersions(new Version(4,0.9901,5.22,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9971,5.45,Common.getCluster(1)));

		Common.loadTask(t);

        

                t = new Task(1,200);
                t.setRunTimeOnCluster(clu0, 18);
                t.setRunTimeOnCluster(clu1, 13.49);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.9983,1.0001,Common.getCluster(0)));
                t.addVersions(new Version(2,0.99634,2.589,Common.getCluster(0)));
                t.addVersions(new Version(3,0.98693,3.735,Common.getCluster(0)));
                t.addVersions(new Version(4,0.98556,4.253,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9847,4.422,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.9983,1.0007,Common.getCluster(1)));
                t.addVersions(new Version(2,0.99634,2.789,Common.getCluster(1)));
                t.addVersions(new Version(3,0.98693,3.935,Common.getCluster(1)));
                t.addVersions(new Version(4,0.98556,5.253,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9847,5.422,Common.getCluster(1)));

		Common.loadTask(t);
      

                t = new Task(2,1500);
                t.setRunTimeOnCluster(clu0, 110);
                t.setRunTimeOnCluster(clu1, 93.74);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.999,1.84,Common.getCluster(0)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(0)));
                t.addVersions(new Version(3,0.9898,3.1,Common.getCluster(0)));
                t.addVersions(new Version(4,0.9901,4.22,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9971,4.45,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.999,2,Common.getCluster(1)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(1)));
                t.addVersions(new Version(3,0.9898,4.1,Common.getCluster(1)));
                t.addVersions(new Version(4,0.9901,5.22,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9971,5.45,Common.getCluster(1)));

		Common.loadTask(t);
        
                t = new Task(3,400);
                t.setRunTimeOnCluster(clu0, 18);
                t.setRunTimeOnCluster(clu1, 13.49);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.9983,1.0001,Common.getCluster(0)));
                t.addVersions(new Version(2,0.99634,2.589,Common.getCluster(0)));
                t.addVersions(new Version(3,0.98693,3.735,Common.getCluster(0)));
                t.addVersions(new Version(4,0.98556,4.253,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9847,4.422,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.9983,1.0007,Common.getCluster(1)));
                t.addVersions(new Version(2,0.99634,2.789,Common.getCluster(1)));
                t.addVersions(new Version(3,0.98693,3.935,Common.getCluster(1)));
                t.addVersions(new Version(4,0.98556,5.253,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9847,5.422,Common.getCluster(1)));

		Common.loadTask(t);

                t = new Task(4,1000);
                t.setRunTimeOnCluster(clu0, 110);
                t.setRunTimeOnCluster(clu1, 93.74);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.999,1.84,Common.getCluster(0)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(0)));
                t.addVersions(new Version(3,0.9898,3.1,Common.getCluster(0)));
                t.addVersions(new Version(4,0.9901,4.22,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9971,4.45,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.999,2,Common.getCluster(1)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(1)));
                t.addVersions(new Version(3,0.9898,4.1,Common.getCluster(1)));
                t.addVersions(new Version(4,0.9901,5.22,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9971,5.45,Common.getCluster(1)));

		Common.loadTask(t);

        

                t = new Task(5,300);
                t.setRunTimeOnCluster(clu0, 18);
                t.setRunTimeOnCluster(clu1, 13.49);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.9983,1.0001,Common.getCluster(0)));
                t.addVersions(new Version(2,0.99634,2.589,Common.getCluster(0)));
                t.addVersions(new Version(3,0.98693,3.735,Common.getCluster(0)));
                t.addVersions(new Version(4,0.98556,4.253,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9847,4.422,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.9983,1.0007,Common.getCluster(1)));
                t.addVersions(new Version(2,0.99634,2.789,Common.getCluster(1)));
                t.addVersions(new Version(3,0.98693,3.935,Common.getCluster(1)));
                t.addVersions(new Version(4,0.98556,5.253,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9847,5.422,Common.getCluster(1)));

		Common.loadTask(t);
      

                t = new Task(6,2000);
                t.setRunTimeOnCluster(clu0, 110);
                t.setRunTimeOnCluster(clu1, 93.74);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.999,1.84,Common.getCluster(0)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(0)));
                t.addVersions(new Version(3,0.9898,3.1,Common.getCluster(0)));
                t.addVersions(new Version(4,0.9901,4.22,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9971,4.45,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.999,2,Common.getCluster(1)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(1)));
                t.addVersions(new Version(3,0.9898,4.1,Common.getCluster(1)));
                t.addVersions(new Version(4,0.9901,5.22,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9971,5.45,Common.getCluster(1)));

		Common.loadTask(t);
        
                t = new Task(7,500);
                t.setRunTimeOnCluster(clu0, 18);
                t.setRunTimeOnCluster(clu1, 13.49);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.9983,1.0001,Common.getCluster(0)));
                t.addVersions(new Version(2,0.99634,2.589,Common.getCluster(0)));
                t.addVersions(new Version(3,0.98693,3.735,Common.getCluster(0)));
                t.addVersions(new Version(4,0.98556,4.253,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9847,4.422,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.9983,1.0007,Common.getCluster(1)));
                t.addVersions(new Version(2,0.99634,2.789,Common.getCluster(1)));
                t.addVersions(new Version(3,0.98693,3.935,Common.getCluster(1)));
                t.addVersions(new Version(4,0.98556,5.253,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9847,5.422,Common.getCluster(1)));

		Common.loadTask(t);



                t = new Task(8,2000);
                t.setRunTimeOnCluster(clu0, 110);
                t.setRunTimeOnCluster(clu1, 93.74);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.999,1.84,Common.getCluster(0)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(0)));
                t.addVersions(new Version(3,0.9898,3.1,Common.getCluster(0)));
                t.addVersions(new Version(4,0.9901,4.22,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9971,4.45,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.999,2,Common.getCluster(1)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(1)));
                t.addVersions(new Version(3,0.9898,4.1,Common.getCluster(1)));
                t.addVersions(new Version(4,0.9901,5.22,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9971,5.45,Common.getCluster(1)));

		Common.loadTask(t);

        

                t = new Task(9,3000);
                t.setRunTimeOnCluster(clu0, 18);
                t.setRunTimeOnCluster(clu1, 13.49);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.9983,1.0001,Common.getCluster(0)));
                t.addVersions(new Version(2,0.99634,2.589,Common.getCluster(0)));
                t.addVersions(new Version(3,0.98693,3.735,Common.getCluster(0)));
                t.addVersions(new Version(4,0.98556,4.253,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9847,4.422,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.9983,1.0007,Common.getCluster(1)));
                t.addVersions(new Version(2,0.99634,2.789,Common.getCluster(1)));
                t.addVersions(new Version(3,0.98693,3.935,Common.getCluster(1)));
                t.addVersions(new Version(4,0.98556,5.253,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9847,5.422,Common.getCluster(1)));

		Common.loadTask(t);
      

                t = new Task(10,6000);
                t.setRunTimeOnCluster(clu0, 110);
                t.setRunTimeOnCluster(clu1, 93.74);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.999,1.84,Common.getCluster(0)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(0)));
                t.addVersions(new Version(3,0.9898,3.1,Common.getCluster(0)));
                t.addVersions(new Version(4,0.9901,4.22,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9971,4.45,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.999,2,Common.getCluster(1)));
                t.addVersions(new Version(2,0.9919,2.45,Common.getCluster(1)));
                t.addVersions(new Version(3,0.9898,4.1,Common.getCluster(1)));
                t.addVersions(new Version(4,0.9901,5.22,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9971,5.45,Common.getCluster(1)));

		Common.loadTask(t);
        
                t = new Task(11,600);
                t.setRunTimeOnCluster(clu0, 18);
                t.setRunTimeOnCluster(clu1, 13.49);
                t.addVersions(new Version(0,1,1,Common.getCluster(0)));
                t.addVersions(new Version(1,0.9983,1.0001,Common.getCluster(0)));
                t.addVersions(new Version(2,0.99634,2.589,Common.getCluster(0)));
                t.addVersions(new Version(3,0.98693,3.735,Common.getCluster(0)));
                t.addVersions(new Version(4,0.98556,4.253,Common.getCluster(0)));
                t.addVersions(new Version(5,0.9847,4.422,Common.getCluster(0)));

		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
                t.addVersions(new Version(1,0.9983,1.0007,Common.getCluster(1)));
                t.addVersions(new Version(2,0.99634,2.789,Common.getCluster(1)));
                t.addVersions(new Version(3,0.98693,3.935,Common.getCluster(1)));
                t.addVersions(new Version(4,0.98556,5.253,Common.getCluster(1)));
                t.addVersions(new Version(5,0.9847,5.422,Common.getCluster(1)));

		Common.loadTask(t);



		Common.loadJobs();
	}	

	static void demo_2_small(){
		Cluster clu0 = Common.getCluster(0);
		Cluster clu1 = Common.getCluster(1);
		Task t;		
		t = new Task(0,40);
		t.setRunTimeOnCluster(clu0, 80);
		t.setRunTimeOnCluster(clu1, 80);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);

		t = new Task(1,100);
		t.setRunTimeOnCluster(clu0, 200);
		t.setRunTimeOnCluster(clu1, 200);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		Common.loadJobs();
	}
	
	/**
	 * demo_1_huge:
	 */
	static void demo_1_huge(){
		Cluster clu0 = Common.getCluster(0);
		Cluster clu1 = Common.getCluster(1);
		Cluster clu2 = Common.getCluster(2);
		Cluster clu3 = Common.getCluster(3);
		
		
		Task t;		
		t = new Task(0,4);
		t.setRunTimeOnCluster(clu0, 8);
		t.setRunTimeOnCluster(clu1, 8);
		t.setRunTimeOnCluster(clu2, 6);
		t.setRunTimeOnCluster(clu3, 5);
		
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.8,2.1,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.8,2.2,Common.getCluster(3)));
		Common.loadTask(t);

		t = new Task(1,10);
		t.setRunTimeOnCluster(clu0, 20);
		t.setRunTimeOnCluster(clu1, 20);
		t.setRunTimeOnCluster(clu2, 18);
		t.setRunTimeOnCluster(clu3, 16);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,2.2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2.3,Common.getCluster(3)));
		Common.loadTask(t);
		
		Common.loadJobs();
	}
	
	/**
	 * Benchmark: SPEC2006
	 */
	static void demo_SPEC_small(){
		Cluster clu0 = Common.getCluster(0);
		Cluster clu1 = Common.getCluster(1);
		Task t;		
		//task 0:
		t = new Task(0,20);
		t.setRunTimeOnCluster(clu0, 54);
		t.setRunTimeOnCluster(clu1, 41);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 1:
		t = new Task(1,600);
		t.setRunTimeOnCluster(clu0, 210);
		t.setRunTimeOnCluster(clu1, 83);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 2:
		t = new Task(2,300);
		t.setRunTimeOnCluster(clu0, 374);
		t.setRunTimeOnCluster(clu1, 236);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 3:
		t = new Task(3,300);
		t.setRunTimeOnCluster(clu0, 315);
		t.setRunTimeOnCluster(clu1, 134);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);

		
		//task 4:
		t = new Task(4,120);
		t.setRunTimeOnCluster(clu0, 12);
		t.setRunTimeOnCluster(clu1, 6);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 5:
		t = new Task(5,10);
		t.setRunTimeOnCluster(clu0, 5);
		t.setRunTimeOnCluster(clu1, 2);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 6:
		t = new Task(6,600);
		t.setRunTimeOnCluster(clu0, 656);
		t.setRunTimeOnCluster(clu1, 309);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		Common.loadJobs();
	}
	
	/**
	 * Benchmark: 
	 */
	static void demo_SPEC_ALL_media(){
		Cluster clu0 = Common.getCluster(0);
		Cluster clu1 = Common.getCluster(1);
		Task t;		
		//task 0:
		t = new Task(0,20);
		t.setRunTimeOnCluster(clu0, 54);
		t.setRunTimeOnCluster(clu1, 41);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 1:
		t = new Task(1,600);
		t.setRunTimeOnCluster(clu0, 210);
		t.setRunTimeOnCluster(clu1, 83);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 2:
		t = new Task(2,300);
		t.setRunTimeOnCluster(clu0, 374);
		t.setRunTimeOnCluster(clu1, 236);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 3:
		t = new Task(3,300);
		t.setRunTimeOnCluster(clu0, 315);
		t.setRunTimeOnCluster(clu1, 134);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);

		
		//task 4:
		t = new Task(4,120);
		t.setRunTimeOnCluster(clu0, 12);
		t.setRunTimeOnCluster(clu1, 6);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 5:
		t = new Task(5,10);
		t.setRunTimeOnCluster(clu0, 5);
		t.setRunTimeOnCluster(clu1, 2);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 6:
		t = new Task(6,600);
		t.setRunTimeOnCluster(clu0, 656);
		t.setRunTimeOnCluster(clu1, 309);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 7:
		t = new Task(7,300);
		t.setRunTimeOnCluster(clu0, 97);
		t.setRunTimeOnCluster(clu1, 54);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		//task 8:
		t = new Task(8,100);
		t.setRunTimeOnCluster(clu0, 29);
		t.setRunTimeOnCluster(clu1, 21);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 9:
		t = new Task(9,50);
		t.setRunTimeOnCluster(clu0, 15);
		t.setRunTimeOnCluster(clu1, 7);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 10:
		t = new Task(10,300);
		t.setRunTimeOnCluster(clu0, 246);
		t.setRunTimeOnCluster(clu1, 149);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 11:
		t = new Task(11,100);
		t.setRunTimeOnCluster(clu0, 27);
		t.setRunTimeOnCluster(clu1, 16);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		//task 12:
		t = new Task(12,100);
		t.setRunTimeOnCluster(clu0, 30);
		t.setRunTimeOnCluster(clu1, 16);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 13:
		t = new Task(13,200);
		t.setRunTimeOnCluster(clu0, 126);
		t.setRunTimeOnCluster(clu1, 64);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 14:
		t = new Task(14,400);
		t.setRunTimeOnCluster(clu0, 107);
		t.setRunTimeOnCluster(clu1, 56);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 15:
		t = new Task(15,40);
		t.setRunTimeOnCluster(clu0, 5);
		t.setRunTimeOnCluster(clu1, 4);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		
		//task 16:
		t = new Task(16,40);
		t.setRunTimeOnCluster(clu0, 8);
		t.setRunTimeOnCluster(clu1, 5);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 17:
		t = new Task(17,300);
		t.setRunTimeOnCluster(clu0, 187);
		t.setRunTimeOnCluster(clu1, 80);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		//task 18:
		t = new Task(18,400);
		t.setRunTimeOnCluster(clu0, 54);
		t.setRunTimeOnCluster(clu1, 41);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 19:
		t = new Task(19,800);
		t.setRunTimeOnCluster(clu0, 210);
		t.setRunTimeOnCluster(clu1, 83);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 20:
		t = new Task(20,1600);
		t.setRunTimeOnCluster(clu0, 374);
		t.setRunTimeOnCluster(clu1, 236);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 21:
		t = new Task(21,1600);
		t.setRunTimeOnCluster(clu0, 315);
		t.setRunTimeOnCluster(clu1, 134);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);

		
		//task 22:
		t = new Task(22,160);
		t.setRunTimeOnCluster(clu0, 12);
		t.setRunTimeOnCluster(clu1, 6);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 23:
		t = new Task(23,160);
		t.setRunTimeOnCluster(clu0, 5);
		t.setRunTimeOnCluster(clu1, 2);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 24:
		t = new Task(24,1600);
		t.setRunTimeOnCluster(clu0, 656);
		t.setRunTimeOnCluster(clu1, 309);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 25:
		t = new Task(25,400);
		t.setRunTimeOnCluster(clu0, 97);
		t.setRunTimeOnCluster(clu1, 54);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		//task 26:
		t = new Task(26,400);
		t.setRunTimeOnCluster(clu0, 29);
		t.setRunTimeOnCluster(clu1, 21);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 27:
		t = new Task(27,400);
		t.setRunTimeOnCluster(clu0, 15);
		t.setRunTimeOnCluster(clu1, 7);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 28:
		t = new Task(28,1600);
		t.setRunTimeOnCluster(clu0, 246);
		t.setRunTimeOnCluster(clu1, 149);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 29:
		t = new Task(29,400);
		t.setRunTimeOnCluster(clu0, 27);
		t.setRunTimeOnCluster(clu1, 16);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		//task 30:
		t = new Task(30,160);
		t.setRunTimeOnCluster(clu0, 30);
		t.setRunTimeOnCluster(clu1, 16);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 31:
		t = new Task(31,400);
		t.setRunTimeOnCluster(clu0, 126);
		t.setRunTimeOnCluster(clu1, 64);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 32:
		t = new Task(32,800);
		t.setRunTimeOnCluster(clu0, 107);
		t.setRunTimeOnCluster(clu1, 56);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 33:
		t = new Task(33,160);
		t.setRunTimeOnCluster(clu0, 5);
		t.setRunTimeOnCluster(clu1, 4);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		
		//task 34:
		t = new Task(34,160);
		t.setRunTimeOnCluster(clu0, 8);
		t.setRunTimeOnCluster(clu1, 5);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		//task 35:
		t = new Task(35,1600);
		t.setRunTimeOnCluster(clu0, 187);
		t.setRunTimeOnCluster(clu1, 80);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		Common.loadTask(t);
		Common.loadJobs();
	}
	
	/**
	 * Change the clusters: 
	 */
	static void demo_SPEC_ALL_huge(){
		Cluster clu0 = Common.getCluster(0);
		Cluster clu1 = Common.getCluster(1);
		Cluster clu2 = Common.getCluster(2);
		Cluster clu3 = Common.getCluster(3);
		
		
		Task t;		
		//task 0:
		t = new Task(0,20);
		t.setRunTimeOnCluster(clu0, 54);
		t.setRunTimeOnCluster(clu1, 41);
		t.setRunTimeOnCluster(clu2, 40);
		t.setRunTimeOnCluster(clu3, 38);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.8,1.9,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.95,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 1:
		t = new Task(1,600);
		t.setRunTimeOnCluster(clu0, 210);
		t.setRunTimeOnCluster(clu1, 83);
		t.setRunTimeOnCluster(clu2, 90);
		t.setRunTimeOnCluster(clu3, 73);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2.1,Common.getCluster(3)));
		t.addVersions(new Version(2,0.85,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 2:
		t = new Task(2,300);
		t.setRunTimeOnCluster(clu0, 374);
		t.setRunTimeOnCluster(clu1, 236);
		t.setRunTimeOnCluster(clu2, 224);
		t.setRunTimeOnCluster(clu3, 199);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2.1,Common.getCluster(3)));
		t.addVersions(new Version(2,0.9,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 3:
		t = new Task(3,300);
		t.setRunTimeOnCluster(clu0, 315);
		t.setRunTimeOnCluster(clu1, 134);
		t.setRunTimeOnCluster(clu2, 115);
		t.setRunTimeOnCluster(clu3, 94);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.95,1.5,Common.getCluster(2)));
		t.addVersions(new Version(2,0.75,1.8,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.99,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.1,Common.getCluster(3)));
		Common.loadTask(t);

		
		//task 4:
		t = new Task(4,120);
		t.setRunTimeOnCluster(clu0, 12);
		t.setRunTimeOnCluster(clu1, 6);
		t.setRunTimeOnCluster(clu2, 5);
		t.setRunTimeOnCluster(clu3, 4);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.6,Common.getCluster(2)));
		t.addVersions(new Version(2,0.8,1.8,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.95,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.1,Common.getCluster(3)));
		Common.loadTask(t);
		//task 5:
		t = new Task(5,10);
		t.setRunTimeOnCluster(clu0, 5);
		t.setRunTimeOnCluster(clu1, 2);
		t.setRunTimeOnCluster(clu2, 2);
		t.setRunTimeOnCluster(clu3, 2);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2.1,Common.getCluster(3)));
		t.addVersions(new Version(2,0.85,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 6:
		t = new Task(6,600);
		t.setRunTimeOnCluster(clu0, 656);
		t.setRunTimeOnCluster(clu1, 309);
		t.setRunTimeOnCluster(clu2, 300);
		t.setRunTimeOnCluster(clu3, 279);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2.1,Common.getCluster(3)));
		t.addVersions(new Version(2,0.9,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 7:
		t = new Task(7,300);
		t.setRunTimeOnCluster(clu0, 97);
		t.setRunTimeOnCluster(clu1, 54);
		t.setRunTimeOnCluster(clu2, 47);
		t.setRunTimeOnCluster(clu3, 44);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.95,1.5,Common.getCluster(2)));
		t.addVersions(new Version(2,0.75,1.7,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.99,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		
		//task 8:
		t = new Task(8,100);
		t.setRunTimeOnCluster(clu0, 29);
		t.setRunTimeOnCluster(clu1, 21);
		t.setRunTimeOnCluster(clu2, 20);
		t.setRunTimeOnCluster(clu3, 18);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.7,Common.getCluster(2)));
		t.addVersions(new Version(2,0.8,1.8,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.95,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 9:
		t = new Task(9,50);
		t.setRunTimeOnCluster(clu0, 15);
		t.setRunTimeOnCluster(clu1, 7);
		t.setRunTimeOnCluster(clu2, 8);
		t.setRunTimeOnCluster(clu3, 7);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2.1,Common.getCluster(3)));
		t.addVersions(new Version(2,0.85,2.3,Common.getCluster(3)));
		Common.loadTask(t);
		//task 10:
		t = new Task(10,300);
		t.setRunTimeOnCluster(clu0, 246);
		t.setRunTimeOnCluster(clu1, 149);
		t.setRunTimeOnCluster(clu2, 146);
		t.setRunTimeOnCluster(clu3, 139);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2.1,Common.getCluster(3)));
		t.addVersions(new Version(2,0.9,2.3,Common.getCluster(3)));
		Common.loadTask(t);
		//task 11:
		t = new Task(11,100);
		t.setRunTimeOnCluster(clu0, 27);
		t.setRunTimeOnCluster(clu1, 16);
		t.setRunTimeOnCluster(clu2, 17);
		t.setRunTimeOnCluster(clu3, 12);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.95,1.5,Common.getCluster(2)));
		t.addVersions(new Version(2,0.75,1.8,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.99,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		
		//task 12:
		t = new Task(12,100);
		t.setRunTimeOnCluster(clu0, 30);
		t.setRunTimeOnCluster(clu1, 16);
		t.setRunTimeOnCluster(clu2, 15);
		t.setRunTimeOnCluster(clu3, 14);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.6,Common.getCluster(2)));
		t.addVersions(new Version(2,0.8,1.8,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.95,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 13:
		t = new Task(13,200);
		t.setRunTimeOnCluster(clu0, 126);
		t.setRunTimeOnCluster(clu1, 64);
		t.setRunTimeOnCluster(clu2, 66);
		t.setRunTimeOnCluster(clu3, 54);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2.1,Common.getCluster(3)));
		t.addVersions(new Version(2,0.85,2.3,Common.getCluster(3)));
		Common.loadTask(t);
		//task 14:
		t = new Task(14,400);
		t.setRunTimeOnCluster(clu0, 107);
		t.setRunTimeOnCluster(clu1, 56);
		t.setRunTimeOnCluster(clu2, 57);
		t.setRunTimeOnCluster(clu3, 49);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.9,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 15:
		t = new Task(15,40);
		t.setRunTimeOnCluster(clu0, 5);
		t.setRunTimeOnCluster(clu1, 4);
		t.setRunTimeOnCluster(clu2, 4);
		t.setRunTimeOnCluster(clu3, 4);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.95,1.5,Common.getCluster(2)));
		t.addVersions(new Version(2,0.75,1.7,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.99,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		
		
		//task 16:
		t = new Task(16,40);
		t.setRunTimeOnCluster(clu0, 8);
		t.setRunTimeOnCluster(clu1, 5);
		t.setRunTimeOnCluster(clu2, 5);
		t.setRunTimeOnCluster(clu3, 4);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.7,Common.getCluster(2)));
		t.addVersions(new Version(2,0.8,1.9,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.95,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.1,Common.getCluster(3)));
		Common.loadTask(t);
		//task 17:
		t = new Task(17,300);
		t.setRunTimeOnCluster(clu0, 187);
		t.setRunTimeOnCluster(clu1, 80);
		t.setRunTimeOnCluster(clu2, 77);
		t.setRunTimeOnCluster(clu3, 60);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2.1,Common.getCluster(3)));
		t.addVersions(new Version(2,0.85,2.3,Common.getCluster(3)));
		Common.loadTask(t);
		
		//task 18:
		t = new Task(18,400);
		t.setRunTimeOnCluster(clu0, 54);
		t.setRunTimeOnCluster(clu1, 41);
		t.setRunTimeOnCluster(clu2, 40);
		t.setRunTimeOnCluster(clu3, 38);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.7,Common.getCluster(2)));
		t.addVersions(new Version(2,0.8,1.9,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.95,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.1,Common.getCluster(3)));
		Common.loadTask(t);
		//task 19:
		t = new Task(19,800);
		t.setRunTimeOnCluster(clu0, 210);
		t.setRunTimeOnCluster(clu1, 83);
		t.setRunTimeOnCluster(clu2, 80);
		t.setRunTimeOnCluster(clu3, 78);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2.1,Common.getCluster(3)));
		t.addVersions(new Version(2,0.85,2.3,Common.getCluster(3)));
		Common.loadTask(t);
		//task 20:
		t = new Task(20,1600);
		t.setRunTimeOnCluster(clu0, 374);
		t.setRunTimeOnCluster(clu1, 236);
		t.setRunTimeOnCluster(clu2, 234);
		t.setRunTimeOnCluster(clu3, 226);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2.1,Common.getCluster(3)));
		t.addVersions(new Version(2,0.9,2.3,Common.getCluster(3)));
		Common.loadTask(t);
		//task 21:
		t = new Task(21,1600);
		t.setRunTimeOnCluster(clu0, 315);
		t.setRunTimeOnCluster(clu1, 134);
		t.setRunTimeOnCluster(clu2, 135);
		t.setRunTimeOnCluster(clu3, 124);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.95,1.5,Common.getCluster(2)));
		t.addVersions(new Version(2,0.75,1.8,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.99,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.1,Common.getCluster(3)));
		Common.loadTask(t);

		
		//task 22:
		t = new Task(22,160);
		t.setRunTimeOnCluster(clu0, 12);
		t.setRunTimeOnCluster(clu1, 6);
		t.setRunTimeOnCluster(clu2, 7);
		t.setRunTimeOnCluster(clu3, 5);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.7,Common.getCluster(2)));
		t.addVersions(new Version(2,0.8,1.9,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.95,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 23:
		t = new Task(23,160);
		t.setRunTimeOnCluster(clu0, 5);
		t.setRunTimeOnCluster(clu1, 2);
		t.setRunTimeOnCluster(clu2, 4);
		t.setRunTimeOnCluster(clu3, 2);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.85,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 24:
		t = new Task(24,1600);
		t.setRunTimeOnCluster(clu0, 656);
		t.setRunTimeOnCluster(clu1, 309);
		t.setRunTimeOnCluster(clu2, 306);
		t.setRunTimeOnCluster(clu3, 299);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.7,Common.getCluster(2)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.9,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 25:
		t = new Task(25,400);
		t.setRunTimeOnCluster(clu0, 97);
		t.setRunTimeOnCluster(clu1, 54);
		t.setRunTimeOnCluster(clu2, 51);
		t.setRunTimeOnCluster(clu3, 49);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.95,1.5,Common.getCluster(2)));
		t.addVersions(new Version(2,0.75,1.7,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.99,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		
		//task 26:
		t = new Task(26,400);
		t.setRunTimeOnCluster(clu0, 29);
		t.setRunTimeOnCluster(clu1, 21);
		t.setRunTimeOnCluster(clu2, 19);
		t.setRunTimeOnCluster(clu3, 17);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.7,Common.getCluster(2)));
		t.addVersions(new Version(2,0.8,1.9,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.95,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.1,Common.getCluster(3)));
		Common.loadTask(t);
		//task 27:
		t = new Task(27,400);
		t.setRunTimeOnCluster(clu0, 15);
		t.setRunTimeOnCluster(clu1, 7);
		t.setRunTimeOnCluster(clu2, 7);
		t.setRunTimeOnCluster(clu3, 5);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.85,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 28:
		t = new Task(28,1600);
		t.setRunTimeOnCluster(clu0, 246);
		t.setRunTimeOnCluster(clu1, 149);
		t.setRunTimeOnCluster(clu2, 146);
		t.setRunTimeOnCluster(clu3, 139);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.7,Common.getCluster(2)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.9,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 29:
		t = new Task(29,400);
		t.setRunTimeOnCluster(clu0, 27);
		t.setRunTimeOnCluster(clu1, 16);
		t.setRunTimeOnCluster(clu2, 17);
		t.setRunTimeOnCluster(clu3, 14);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.95,1.5,Common.getCluster(2)));
		t.addVersions(new Version(2,0.75,1.7,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.99,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		
		//task 30:
		t = new Task(30,160);
		t.setRunTimeOnCluster(clu0, 30);
		t.setRunTimeOnCluster(clu1, 16);
		t.setRunTimeOnCluster(clu2, 15);
		t.setRunTimeOnCluster(clu3, 14);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.6,Common.getCluster(2)));
		t.addVersions(new Version(2,0.8,1.8,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.95,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 31:
		t = new Task(31,400);
		t.setRunTimeOnCluster(clu0, 126);
		t.setRunTimeOnCluster(clu1, 64);
		t.setRunTimeOnCluster(clu2, 56);
		t.setRunTimeOnCluster(clu3, 54);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.7,Common.getCluster(2)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.85,2.1,Common.getCluster(3)));
		Common.loadTask(t);
		//task 32:
		t = new Task(32,800);
		t.setRunTimeOnCluster(clu0, 107);
		t.setRunTimeOnCluster(clu1, 56);
		t.setRunTimeOnCluster(clu2, 57);
		t.setRunTimeOnCluster(clu3, 51);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.6,Common.getCluster(2)));
		t.addVersions(new Version(2,0.9,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.9,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 33:
		t = new Task(33,160);
		t.setRunTimeOnCluster(clu0, 5);
		t.setRunTimeOnCluster(clu1, 4);
		t.setRunTimeOnCluster(clu2, 5);
		t.setRunTimeOnCluster(clu3, 4);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(2,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.95,1.5,Common.getCluster(2)));
		t.addVersions(new Version(2,0.75,1.8,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.99,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		
		
		//task 34:
		t = new Task(34,160);
		t.setRunTimeOnCluster(clu0, 8);
		t.setRunTimeOnCluster(clu1, 5);
		t.setRunTimeOnCluster(clu2, 5);
		t.setRunTimeOnCluster(clu3, 4);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(2,0.8,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.6,Common.getCluster(2)));
		t.addVersions(new Version(2,0.8,1.8,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.95,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.8,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		//task 35:
		t = new Task(35,1600);
		t.setRunTimeOnCluster(clu0, 187);
		t.setRunTimeOnCluster(clu1, 80);
		t.setRunTimeOnCluster(clu2, 77);
		t.setRunTimeOnCluster(clu3, 70);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(2,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(1)));
		t.addVersions(new Version(0,1,1,Common.getCluster(2)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(2)));
		t.addVersions(new Version(2,0.85,2,Common.getCluster(2)));
		t.addVersions(new Version(0,1,1,Common.getCluster(3)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(3)));
		t.addVersions(new Version(2,0.85,2.2,Common.getCluster(3)));
		Common.loadTask(t);
		Common.loadJobs();
	}
	
	/**
	 * This huge architecture is useless recently.
	 */
	static void initClustersAndScheduler_huge(){
		
		Cluster clu;
		Core c;
		LocalJobScheduler ljs;
		
		clu = new Cluster(0);
		clu.addFrequencyLevel(new Frequency(1,false));
		clu.addFrequencyLevel(new Frequency(1.2,false));
		clu.addFrequencyLevel(new Frequency(1.4,false));
		clu.addFrequencyLevel(new Frequency(1.5,false));
		clu.addFrequencyLevel(new Frequency(1.6,false));
		clu.addFrequencyLevel(new Frequency(1.7,true));
		clu.addFrequencyLevel(new Frequency(1.8,true));
		clu.addFrequencyLevel(new Frequency(2,true));
		
		c = new Core(0,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(1,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(2,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(3,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(4,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(5,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(6,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(7,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(8,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(9,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		Common.loadCluster(clu);
		
		
		
		clu = new Cluster(1);
		clu.addFrequencyLevel(new Frequency(1.5,false));
		clu.addFrequencyLevel(new Frequency(1.7,false));
		clu.addFrequencyLevel(new Frequency(1.8,false));
		clu.addFrequencyLevel(new Frequency(2,false));
		clu.addFrequencyLevel(new Frequency(2.3,false));
		clu.addFrequencyLevel(new Frequency(2.5,true));
		clu.addFrequencyLevel(new Frequency(2.8,true));
		clu.addFrequencyLevel(new Frequency(3,true));
		
		
		c = new Core(10,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(11,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(12,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(13,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(14,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(15,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(16,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(17,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(18,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(19,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		Common.loadCluster(clu);
		
		
		clu = new Cluster(2);
		clu.addFrequencyLevel(new Frequency(2.5,false));
		clu.addFrequencyLevel(new Frequency(2.7,false));
		clu.addFrequencyLevel(new Frequency(2.8,false));
		clu.addFrequencyLevel(new Frequency(3,false));
		clu.addFrequencyLevel(new Frequency(3.3,false));
		clu.addFrequencyLevel(new Frequency(3.5,true));
		clu.addFrequencyLevel(new Frequency(3.8,true));
		clu.addFrequencyLevel(new Frequency(4,true));
		
		
		c = new Core(20,2);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(21,2);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(22,2);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(23,2);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(24,2);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(25,2);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(26,2);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(27,2);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(28,2);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(29,2);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		Common.loadCluster(clu);
		
		
		
		
		clu = new Cluster(3);
		clu.addFrequencyLevel(new Frequency(3.5,false));
		clu.addFrequencyLevel(new Frequency(3.7,false));
		clu.addFrequencyLevel(new Frequency(3.8,false));
		clu.addFrequencyLevel(new Frequency(4,false));
		clu.addFrequencyLevel(new Frequency(4.3,false));
		clu.addFrequencyLevel(new Frequency(4.5,true));
		clu.addFrequencyLevel(new Frequency(4.8,true));
		clu.addFrequencyLevel(new Frequency(5,true));
		
		
		c = new Core(30,3);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(31,3);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(32,3);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(33,3);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(34,3);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(35,3);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(36,3);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(37,3);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(38,3);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(39,3);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		
		
		Common.loadCluster(clu);
		
		
		Common.setTDPConstraint(1540);
	}
	
	static void initClustersAndScheduler_media(){
		
		Cluster clu;
		Core c;
		LocalJobScheduler ljs;
		
		clu = new Cluster(0);
		clu.addFrequencyLevel(new Frequency(1,false));
		clu.addFrequencyLevel(new Frequency(1.2,false));
		clu.addFrequencyLevel(new Frequency(1.4,false));
		clu.addFrequencyLevel(new Frequency(1.5,false));
		clu.addFrequencyLevel(new Frequency(1.6,false));
		clu.addFrequencyLevel(new Frequency(1.7,true));
		clu.addFrequencyLevel(new Frequency(1.8,true));
		clu.addFrequencyLevel(new Frequency(2,true));
		
		c = new Core(0,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(1,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(2,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(3,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(4,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(5,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(6,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(7,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(8,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(9,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		Common.loadCluster(clu);
		
		
		
		clu = new Cluster(1);
		clu.addFrequencyLevel(new Frequency(1.5,false));
		clu.addFrequencyLevel(new Frequency(1.7,false));
		clu.addFrequencyLevel(new Frequency(1.8,false));
		clu.addFrequencyLevel(new Frequency(2,false));
		clu.addFrequencyLevel(new Frequency(2.3,false));
		clu.addFrequencyLevel(new Frequency(2.5,true));
		clu.addFrequencyLevel(new Frequency(2.8,true));
		clu.addFrequencyLevel(new Frequency(3,true));
		
		
		c = new Core(10,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(11,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(12,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(13,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(14,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(15,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(16,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(17,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(18,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(19,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		
		
		Common.loadCluster(clu);
		
		Common.setTDPConstraint(280);
	}
	
	static void initClustersAndScheduler_small(){
		
		Cluster clu;
		Core c;
		LocalJobScheduler ljs;
		
		clu = new Cluster(0);
		clu.addFrequencyLevel(new Frequency(1,false));
		clu.addFrequencyLevel(new Frequency(1.5,false));
		clu.addFrequencyLevel(new Frequency(2,true));
		
		c = new Core(0,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(1,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(2,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		Common.loadCluster(clu);
		
		
		
		clu = new Cluster(1);
		clu.addFrequencyLevel(new Frequency(1.5,false));
		clu.addFrequencyLevel(new Frequency(2,false));
		clu.addFrequencyLevel(new Frequency(3,true));
		
		
		c = new Core(3,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(4,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		
		Common.loadCluster(clu);
		
		Common.setTDPConstraint(42);
	}


	static void initClustersAndScheduler_true(){
		
		Cluster clu;
		Core c;
		LocalJobScheduler ljs;
		
		clu = new Cluster(0);
		clu.addFrequencyLevel(new Frequency(1,0.814,0.404,true));
		clu.addFrequencyLevel(new Frequency(1.19,0.818,0.404,false));
		clu.addFrequencyLevel(new Frequency(1.4,0.86,0.423,false));
		clu.addFrequencyLevel(new Frequency(1.59,0.91,0.439,false));
		clu.addFrequencyLevel(new Frequency(1.78,0.95,0.451,false));
		clu.addFrequencyLevel(new Frequency(1.98,0.96,0.453,false));
		clu.addFrequencyLevel(new Frequency(2.17,1.73,0.711,true));
		clu.addFrequencyLevel(new Frequency(2.36,1.76,1.256,true));

	
	
		c = new Core(0,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(1,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(2,0);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		Common.loadCluster(clu);
		
		
		
		clu = new Cluster(1);
		clu.addFrequencyLevel(new Frequency(1.2,1.814,0.604,true));
		clu.addFrequencyLevel(new Frequency(1.39,1.818,0.606,false));
		clu.addFrequencyLevel(new Frequency(1.6,1.86,0.623,false));
		clu.addFrequencyLevel(new Frequency(1.79,1.91,0.639,false));
		clu.addFrequencyLevel(new Frequency(1.98,1.95,0.651,false));
		clu.addFrequencyLevel(new Frequency(2.18,1.96,0.653,false));
		clu.addFrequencyLevel(new Frequency(2.37,2.73,0.911,true));
		clu.addFrequencyLevel(new Frequency(2.56,3.76,1.256,true));
		
		c = new Core(3,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		c = new Core(4,1);
		ljs = new LocalJobScheduler(c);
		Common.loadScheduler(ljs);
		clu.addCore(c);
		
		
		Common.loadCluster(clu);
		
		Common.setTDPConstraint(7);
	}
	
	/**
	 * PPT demo.
	 */
	static void demo_PPT_small(){
		Cluster clu0 = Common.getCluster(0);
		Cluster clu1 = Common.getCluster(1);
		Task t;		
		t = new Task(0,5);
		t.setRunTimeOnCluster(clu0, 13);
		t.setRunTimeOnCluster(clu1, 12);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		//t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(1,0.8,1.6,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		//t.addVersions(new Version(1,0.95,1.8,Common.getCluster(1)));
		t.addVersions(new Version(1,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		t = new Task(1,6);
		t.setRunTimeOnCluster(clu0, 14);
		t.setRunTimeOnCluster(clu1, 12);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		//t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(1,0.85,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		//t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(1,0.85,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		t = new Task(2,10);
		t.setRunTimeOnCluster(clu0, 22);
		t.setRunTimeOnCluster(clu1, 20);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		//t.addVersions(new Version(1,0.9,1.5,Common.getCluster(0)));
		t.addVersions(new Version(1,0.9,1.8,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		//t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		t.addVersions(new Version(1,0.9,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		t = new Task(3,15);
		t.setRunTimeOnCluster(clu0, 34);
		t.setRunTimeOnCluster(clu1, 30);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		//t.addVersions(new Version(1,0.95,1.3,Common.getCluster(0)));
		t.addVersions(new Version(1,0.75,1.5,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		//t.addVersions(new Version(1,0.99,1.8,Common.getCluster(1)));
		t.addVersions(new Version(1,0.8,2,Common.getCluster(1)));
		Common.loadTask(t);
		
		t = new Task(4,30);
		t.setRunTimeOnCluster(clu0, 60);
		t.setRunTimeOnCluster(clu1, 50);
		t.addVersions(new Version(0,1,1,Common.getCluster(0)));
		//t.addVersions(new Version(1,0.99,1.2,Common.getCluster(0)));
		t.addVersions(new Version(1,0.4,2,Common.getCluster(0)));
		t.addVersions(new Version(0,1,1,Common.getCluster(1)));
		//t.addVersions(new Version(1,0.99,2.3,Common.getCluster(1)));
		t.addVersions(new Version(1,0.8,2.5,Common.getCluster(1)));
		Common.loadTask(t);
		
		Common.loadJobs();
	}
}
