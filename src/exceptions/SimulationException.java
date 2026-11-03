package exceptions;

public class SimulationException extends AirportVisualizerException{

	public SimulationException(String msg) {
		super("Simulation exception: " + msg);
	}

	@Override
	public String getName() {
		return "Simulation exception";
	}

}
