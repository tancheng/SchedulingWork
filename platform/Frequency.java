package platform;

public class Frequency {

	private double frequency;
	private double activePower;
	private double idlePower;
	private boolean voltageChange;
	
	public Frequency(double f, boolean upperVoltage){
		frequency = f;
		activePower = f*f*f;
		idlePower = f*f;
		voltageChange = upperVoltage; 
	}
	
	
	public Frequency(double _frequency, double _activePower, double _idlePower, boolean upperVoltage){
		frequency = _frequency;
		activePower = _activePower;
		idlePower = _idlePower;
		voltageChange = upperVoltage; 
	}

	public boolean isBottleneck(){
		return voltageChange;
	}
	
	public double getFrequency(){
		return frequency;
	}
	
	public double getActivePower(){
		return activePower;
	}
	
	public double getIdlePower(){
		return idlePower;
	}
	
}
