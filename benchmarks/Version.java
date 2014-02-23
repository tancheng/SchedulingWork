package benchmarks;

import platform.Cluster;

public class Version {
	
	private int versionID;
	private double quality;
	private double speedup;
	private Cluster onCluster;
	
	public Version(int id, double q, double s, Cluster clu){
		versionID = id;
		quality = q;
		speedup = s;
		onCluster = clu;
	}
	
	public int getVersionID(){
		return versionID;
	}
	
	public double getQuality(){
		return quality;
	}
	
	public double getSpeedup(){
		return speedup;
	}
	
	public Cluster getCluster(){
		return onCluster;
	}
	
}
