package exceptions;

public abstract class AirportVisualizerException extends Exception{
	
	// Root of program exception hierarchy 
	public AirportVisualizerException(String msg) {
		super(msg);
	}
	
	public abstract String getName();
}
