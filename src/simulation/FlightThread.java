package simulation;

import airportVisualizer.Flight;

public class FlightThread extends Thread{
	private Flight flight;
	private double x, y;
	
	// Flight thread for each currently active flight
	public FlightThread (Flight flight) {
		this.flight = flight;
	}
	
	@Override
	public void run() {
		Simulation.getInstance().addThread(this);
		long periodCount = flight.getDuration() / Simulation.MINUTES_PER_PERIOD;
		
		// Calculate difference in every period
		double dx = ((double)flight.getTo().getX() - flight.getFrom().getX()) / (periodCount);
		double dy = ((double)flight.getTo().getY() - flight.getFrom().getY()) / (periodCount);
		
		for (int i = 0; i < periodCount; i++) {
			// Calculate current position
			x = flight.getFrom().getX() * 1.0 + dx * i;
			y = flight.getFrom().getY() * 1.0 + dy * i;
			
			try {
				synchronized(Simulation.getInstance()) {
					while (Simulation.getInstance().isPaused()) Simulation.getInstance().wait();
				}
				// Sleep for a period of time
				Thread.sleep(Simulation.PERIOD_MS);
			} catch (InterruptedException e) {
				Simulation.getInstance().removeThread(this);
				return;
			}
		}
		Simulation.getInstance().removeThread(this);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public String getDeparture() {
		return flight.getDepartureTime();
	}
	
	

}
