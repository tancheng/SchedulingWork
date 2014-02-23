package platform;

public class Core {
	int coreID;
	Cluster myCluster;
	int rank;
	
	
	public Core(int id, int _rank){
		coreID = id;
		rank = _rank;
	}
	
	public void setCluster(Cluster clu){
		this.myCluster = clu;
	}
	
	public Cluster getCluster(){
		return myCluster;
	}
	
	public int getRank(){
		return rank;
	}
	
	public int getCoreID(){
		return coreID;
	}
}
