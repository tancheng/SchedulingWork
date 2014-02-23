package solution;

import benchmarks.Version;


public class VersionForJob {
	int jobID;
	int versionID;
	double QoS_Energy_Matrix;
	
	public VersionForJob(int _jobID, int _versionID){
		jobID = _jobID;
		versionID = _versionID;
	}
	

	
	public int getJobID(){
		return jobID;
	}
	
	public int getVersionID(){
		return versionID;
	}
	
	public double getMatrix(){
		return QoS_Energy_Matrix;
	}
	
	/**
	 * This method need some complex calculation.
	 */
	public void setMatrix(){
		
	}
	/**
	 * So we need to re-modify this method later.
	 * @param v
	 */
	public void setMatrix(Version v){
		QoS_Energy_Matrix = v.getQuality()*v.getSpeedup();
	}
}
