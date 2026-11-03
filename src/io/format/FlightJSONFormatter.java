package io.format;

import airportVisualizer.Flight;
import exceptions.FlightCreationException;
import exceptions.JSONException;
import exceptions.MyIOException;

public class FlightJSONFormatter implements JSONFormatter<Flight> {
	
	static String[] columns = { "from", "to", "departure", "duration" };

	@Override
	public Flight fromLine(String line) throws MyIOException {
		String[] data = new String[4];
		String[] columns = new String[4];
		JSONFormatter.extractColumnsData(line, columns, data);

		if (!JSONFormatter.correctColumns(columns, FlightJSONFormatter.columns))
			throw new JSONException("Invalid columns in JSON Object given in line:\n" + line);

		Flight f = null;
		try {
			f = new Flight(data[0], data[1], data[2], Integer.parseInt(data[3]));
		} catch (NumberFormatException | FlightCreationException e) {
			throw new JSONException("Incorectly formated line:\n" + line);
		}
		return f;
	}

	@Override
	public String toLine(Flight f) throws MyIOException {
		return String.format("{\"from\":\"%s\",\"to\":\"%s\",\"departure\":\"%s\",\"duration\":%d}", f.getSrcAirport(),
				f.getDstAirport(), f.getDepartureTime(), f.getDuration());
	}

	@Override
	public String getTitle() {
		return "\"flights\"";
	}
}
