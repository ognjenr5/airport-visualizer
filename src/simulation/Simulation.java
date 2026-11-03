package simulation;

import java.util.ArrayList;

import airportVisualizer.Airport;
import airportVisualizer.Flight;
import airportVisualizer.Storage;
import exceptions.SimulationException;
import gui.MainWindow;

public class Simulation extends Thread {

	// Simulation constants
	
	static final int PERIOD_MS = 200;
	static final int DEPARTURE_PERIOD_MS = 1000;
	static final int MINUTES_PER_SECOND = 10;

	static final int MINUTES_PER_PERIOD = MINUTES_PER_SECOND * PERIOD_MS / DEPARTURE_PERIOD_MS;
	static final int PERIOD_PER_DEPARTURE = DEPARTURE_PERIOD_MS / PERIOD_MS;
	
	// Singleton instance of simulation
	private static Simulation instance;
	
	// Active flight threads
	private ArrayList<FlightThread> activeThreadList;
	
	// Status flags of simulation
	private volatile boolean paused, started;
	
	// MainWindow reference
	private static MainWindow mainWindow;
	
	// Elapsed simulation time
	private volatile int currentTime;

	public static synchronized Simulation getInstance() {
		if (instance == null)
			instance = new Simulation();
		return instance;
	}

	private Simulation() {
		activeThreadList = new ArrayList<>();
		paused = false;
		started = false;
		currentTime = 0;
	}

	public static void setMainWindow(MainWindow mainWindow) {
		Simulation.mainWindow = mainWindow;
	}

	public synchronized void addThread(FlightThread thr) {
		activeThreadList.add(thr);
	}

	public synchronized boolean hasActiveFlights() {
		return !activeThreadList.isEmpty();
	}
	
	public synchronized void removeThread(FlightThread thr) {
		activeThreadList.remove(thr);
	}

	public synchronized ArrayList<FlightThread> getActiveThreadList() {
		return new ArrayList<>(activeThreadList);
	}

	public static void startSimulation() throws SimulationException {
		if (getInstance().started)
			return;

		Storage.connectFlights();

		if (!Storage.hasSimulationFlights())
			throw new SimulationException("No flights in the simulation.");

		mainWindow.pauseInactivity();
		mainWindow.disableSidePanelButtons();
		getInstance().started = true;
		getInstance().currentTime = 0;
		getInstance().start();
	}

	public static void stopSimulation() {
		if (instance == null || !instance.started)
			return;
		try {
			getInstance().interrupt();
			getInstance().join();
		} catch (InterruptedException e) {
		}
		getInstance().endOfSimulation();
	}

	public void endOfSimulation() {
		try {
			for (FlightThread thr : getActiveThreadList()) {
				thr.interrupt();
				thr.join();
			}
		} catch (InterruptedException e) {
		}
		mainWindow.unpauseInactivity();
		mainWindow.refreshFlightSimulation();
		mainWindow.enableSidePanelButtons();
		this.started = false;
		instance = null;
	}

	public static void restartSimulation() throws SimulationException {
		if (!getInstance().started)
			return;
		stopSimulation();
		startSimulation();
	}

	public synchronized void pauseSimulation() {
		paused = true;
	}

	public synchronized void unpauseSimulation() {
		paused = false;
		notifyAll();
	}

	public synchronized boolean togglePause() {
		if (instance == null || !instance.started)
			return false;
		if (paused)
			unpauseSimulation();
		else
			pauseSimulation();
		return true;
	}

	public synchronized boolean isPaused() {
		return paused;
	}

	@Override
	public void run() {
		int periods = 0;
		currentTime = 0;
		
		// Simulation runs until the end of the day, or until there are no flights left
		while (currentTime <= 24 * 60 || hasActiveFlights()) {
			
			// Refresh flight simulation
			Simulation.mainWindow.refreshFlightSimulation();
			
			// Every PERIOD_PER_DEPARTURE, if the the day hasn't ended, poll airports for flights
			if (currentTime <= 24 * 60 && periods % PERIOD_PER_DEPARTURE == 0) {
				for (Airport a : Storage.getAirports()) {
					
					//  Take one flight from the queue
					Flight f = a.peekFirstFlight();
					
					// If it's on schedule, create a flight thread
					if (f != null && f.getDepartureTimeInMinutes() <= currentTime) {
						new FlightThread(a.getFlight()).start();
					}
				}
			}

			// Update time and periods
			currentTime += MINUTES_PER_PERIOD;
			periods++;

			try {
				
				// Check if the simulation was paused
				synchronized (this) {
					while (paused)
						this.wait();
				}

				// Sleep for a period of time
				Thread.sleep(PERIOD_MS);
			} catch (InterruptedException e) {
				return;
			}

		}

		endOfSimulation();
	}

	private static String formatTime(int minutes) {
		return String.format("%02d:%02d", (minutes / 60) % 24, minutes % 60);
	}

	public static String getCurrentSimulationTime() {
		return formatTime(Simulation.getInstance().currentTime);
	}

}
