package exceptions;

public class MyIOException extends AirportVisualizerException {

	public MyIOException(String msg) {
		super("IOException " + msg);
	}

	public String getName() {
		return "IOException";
	}
}

