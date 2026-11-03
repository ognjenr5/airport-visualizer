package exceptions;

public class FlightCreationException extends AirportVisualizerException{

	public FlightCreationException(String msg) {
		super("Flight creation exception: " + msg);
	}

	@Override
	public String getName() {
		return "FlightCreationException";
	}

}
