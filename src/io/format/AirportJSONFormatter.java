package io.format;

import airportVisualizer.Airport;
import exceptions.AirportCreationException;
import exceptions.JSONException;
import exceptions.MyIOException;

public class AirportJSONFormatter implements JSONFormatter<Airport>{
	
	static String[] columns = {"code", "name", "x", "y"};

	@Override
	public Airport fromLine(String line) throws MyIOException {
		String[] data = new String[4];
		String[] columns = new String[4];
		JSONFormatter.extractColumnsData(line, columns, data);
		
		if (!JSONFormatter.correctColumns(columns, AirportJSONFormatter.columns)) 
			throw new JSONException("Invalid columns in JSON Object given in line:\n" + line);
		
		Airport a = null;
		try {
			a = new Airport(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
		} catch (NumberFormatException | AirportCreationException e) {
			throw new JSONException("Incorectly formated line:\n" + line);
		}
		return a;
	}

	@Override
	public String toLine(Airport t) throws MyIOException {
		return String.format("{\"code\":\"%s\",\"name\":\"%s\",\"x\":%d,\"y\":%d}", t.getCode(), t.getName(), t.getX(), t.getY());
	}

	@Override
	public String getTitle() {
		return "\"airports\"";
	}

}
