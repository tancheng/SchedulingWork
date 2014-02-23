package system;

import java.util.ArrayList;

import common.Common;

import platform.*;

public class DVFSManager {
	
	ArrayList<Cluster> clusters_list;
	
	public DVFSManager(ArrayList<Cluster> _clusters_list){
		clusters_list = _clusters_list;
		for(Cluster clu: clusters_list){
			clu.initFrequencyTimeSlot();
		}
	}
	
	public void initMaxFrequencyOnAllClusters(){
		Frequency fre;
		for(Cluster clu: clusters_list){
			fre = clu.getMaxFrequency();
			for(int timing = 0;timing<Common.getTotalPeriod();++timing){
				clu.setFrequency(timing, fre);
			}
			
		}
	}
	
}
