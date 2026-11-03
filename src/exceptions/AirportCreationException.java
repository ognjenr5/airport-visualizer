package exceptions;

public class AirportCreationException extends AirportVisualizerException {

	public AirportCreationException(String msg) {
		super("AirportCreationException: " + msg);
	}

	@Override
	public String getName() {
		return "AirportCreationException";
	}
	
}
