package io.format;

import airportVisualizer.Flight;
import exceptions.CSVException;
import exceptions.FlightCreationException;
import exceptions.MyIOException;

public class FlightCSVFormatter implements CSVFormatter<Flight>{

	@Override
	public Flight fromLine(String line) throws CSVException {
		String[] data = line.split(",");
		if (data.length != 4) throw new CSVException("Incorrect number of values in line:\n" + line);
		Flight f = null;
		try {
			f = new Flight(data[0], data[1], data[2], Integer.parseInt(data[3]));
		} catch (NumberFormatException | FlightCreationException e) {
			throw new CSVException("Incorectly formated line:\n" + line);
		}
		return f;
	}

	@Override
	public String toLine(Flight t) throws MyIOException {
		return t.getSrcAirport() + "," + t.getDstAirport() + "," + t.getDepartureTime() + "," + t.getDuration() + "\n";
	}

	@Override
	public String getHeader() {
		return "FROM,TO,DEPARTURE,DURATION\n";
	}

	@Override
	public boolean correctHeader(String line) {
		return line.split(",").length == 4 && line.trim().equals(getHeader().trim());
	}

	@Override
	public String getTitle() {
		return "# FLIGHTS";
	}

}
