package platform;

import java.util.ArrayList;

import common.Common;

public class Cluster {
	
	int clusterID;
	
	ArrayList<Core> cores_list = new ArrayList<Core>();
	ArrayList<Frequency> frequencies_list = new ArrayList<Frequency>();
	Frequency[] frequencyTimeSlot;
	
	public Cluster(int id){
		clusterID = id;
		
	}
	
	public void setFrequency(int timing, Frequency fre){
		frequencyTimeSlot[timing] = fre;
	}
	
	public void initFrequencyTimeSlot(){
		frequencyTimeSlot = new Frequency[Common.getTotalPeriod()];
	}
	
	public Frequency[] getFrequencyTimeSlot(){
		return frequencyTimeSlot;
	}
	
	public Frequency getMaxFrequency(){
		double maxFre = 0;
		Frequency fre_maxFrequency = null;
		for(Frequency fre: frequencies_list){
			if(fre.getFrequency()>maxFre){
				maxFre = fre.getFrequency();
				fre_maxFrequency = fre;
			}
		}
		return fre_maxFrequency;
	}
	
	public boolean isLowestFrequency(Frequency current_Frequency){
		for(Frequency fre: frequencies_list){
			if(fre.getFrequency()<current_Frequency.getFrequency()){
				return false;
			}
		}
		return true;
	}
	
	public Frequency getLowerFrequency(Frequency current_Frequency){
		ArrayList<Frequency> temp_fre_list = new ArrayList<Frequency>();
		for(Frequency fre: frequencies_list){
			if(fre.getFrequency()<current_Frequency.getFrequency()){
				temp_fre_list.add(fre);
			}
		}
		double maxFre = 0;
		Frequency fre_maxFrequency = null;
		for(Frequency fre: temp_fre_list){
			if(fre.getFrequency()>maxFre){
				maxFre = fre.getFrequency();
				fre_maxFrequency = fre;
			}
		}
		return fre_maxFrequency;
	}
	
	public void addFrequencyLevel(Frequency f){
		this.frequencies_list.add(f);
	}
	
	public ArrayList<Frequency> getFrequencies(){
		return frequencies_list;
	}
	
	public void addCore(Core c){
		this.cores_list.add(c);
		c.setCluster(this);
	}
	
	public ArrayList<Core> getCores(){
		return cores_list;
	}
	
	public int getNumOfCores(){
		return cores_list.size();
	}
	
	public int getClusterID(){
		return clusterID;
	}
	
}
