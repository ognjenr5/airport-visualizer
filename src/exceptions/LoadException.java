package exceptions;

public class LoadException extends AirportVisualizerException{
	public LoadException(String msg) {
		super("LoadException: " + msg);
	}
	
	public String getName() { 
		return "LoadException";
	}
}
