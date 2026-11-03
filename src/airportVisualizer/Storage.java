package airportVisualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import exceptions.AirportCreationException;
import exceptions.FlightCreationException;
import exceptions.LoadException;
import exceptions.MyIOException;
import gui.InactivityThread;
import io.IO;

public class Storage {
	private static ArrayList<Airport> airports;
	private static ArrayList<Flight> flights;
	
	// HashMap for searching airports by code
	private static HashMap<String, Airport> airportMap;
	
	// Current selected airport
	private static Airport selected;
	
	// Eligible flights for simulation
	private static int simulationFlightsCount;
	
	// Reference to inactivity thread
	private static InactivityThread inactivityThread;

	static {
		airports = new ArrayList<>();
		flights = new ArrayList<>();
		airportMap = new HashMap<String, Airport>();
		selected = null;
		simulationFlightsCount = 0;
	}

	// Load storage from filename using given format, handle errors
	public static void loadStorage(IO io, String filename)
			throws AirportCreationException, FlightCreationException, MyIOException, LoadException {
		clearAll();
		io.loadFile(filename, flights, airports);

		handleDuplicateAirports();
		handleMissingAirports();

		airports.sort(null);
		flights.sort(null);

		airportMap.clear();
		for (Airport a : airports)
			airportMap.put(a.getCode(), a);

		connectFlights();
	}

	private static void handleDuplicateAirports() throws LoadException {
		HashSet<String> airportCodeSet = new HashSet<>();
		HashSet<String> duplicateCodes = new HashSet<>();
		for (Airport a : airports) {
			if (airportCodeSet.contains(a.getCode()))
				duplicateCodes.add(a.getCode());
			else
				airportCodeSet.add(a.getCode());
		}

		if (duplicateCodes.size() > 0)
			handleLoadError("File contains following duplicate airports:\n", duplicateCodes.toArray(new String[0]));

	}

	private static void handleMissingAirports() throws LoadException {
		String[] missingCodes = getMissingAirportCodes();
		if (missingCodes.length > 0)
			handleLoadError("Following airports are missing:\n", missingCodes);
	}

	// Helper function for handling load errors
	private static void handleLoadError(String msg, String[] arr) throws LoadException {
		StringBuilder sb = new StringBuilder(msg);
		for (String s : arr)
			sb.append(s + "\n");
		clearAll();
		throw new LoadException(sb.toString());
	}

	// Clear lists and un-pause inactivity if there was a selected
	private static void clearAll() {
		if (selected != null) {
			inactivityThread.unpause();
			clearSelected();
		}
			
		
		flights.clear();
		airports.clear();
	}
	
	// Check for missing airports that are referenced in flights
	private static String[] getMissingAirportCodes() {
		HashSet<String> missingCodes = new HashSet<>();
		HashSet<String> knownCodes = new HashSet<>();

		for (Airport a : airports)
			knownCodes.add(a.getCode());

		for (Flight f : flights) {
			if (!knownCodes.contains(f.getSrcAirport()))
				missingCodes.add(f.getSrcAirport());
			if (!knownCodes.contains(f.getDstAirport()))
				missingCodes.add(f.getDstAirport());
		}

		return missingCodes.toArray(new String[0]);
	}

	// Connect flights to airport and filter out flights that contain hidden airports
	public static void connectFlights() {
		for (Airport a : airports)
			a.clearFlights();

		simulationFlightsCount = 0;

		for (Flight f : flights) {
			Airport aFrom = airportMap.get(f.getSrcAirport());
			Airport aTo = airportMap.get(f.getDstAirport());

			if (!aFrom.isVisible() || !aTo.isVisible())
				continue;

			f.setFrom(aFrom);
			f.setTo(aTo);

			aFrom.addFlight(f);
			simulationFlightsCount++;
		}
	}

	// Save flights and airports to given file
	public static void saveStorage(IO io, String filename) throws MyIOException {
		io.saveFile(filename, flights, airports);
	}

	public static void addAirport(Airport a) {
		airports.add(a);
		airports.sort(null);
		airportMap.put(a.getCode(), a);
	}

	public static void addFlight(Flight f) {
		flights.add(f);
		flights.sort(null);
	}

	public static ArrayList<Airport> getAirports() {
		return airports;
	}

	public static ArrayList<Flight> getFlights() {
		return flights;
	}

	public static Airport getAirportFromCode(String code) {
		return airportMap.get(code);
	}

	public static boolean hasSimulationFlights() {
		return simulationFlightsCount != 0;
	}
	
	public static Airport getSelected() {
		return selected;
	}

	public static void setSelected(Airport a) {
		selected = a;
	}

	public static void clearSelected() {
		selected = null;
	}
	
	public static void setInactivityThread(InactivityThread it) {
		inactivityThread = it;
	}
}
