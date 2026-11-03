package exceptions;

public class SaveException extends AirportVisualizerException{
	public SaveException(String msg) {
		super("SaveException: " + msg);
	}
	
	public String getName() { 
		return "SaveException";
	}
}
