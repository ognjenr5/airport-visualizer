package io.format;

import airportVisualizer.Airport;
import exceptions.CSVException;
import exceptions.AirportCreationException;

public class AirportCSVFormatter implements CSVFormatter<Airport>{	
	@Override
	public Airport fromLine(String line) throws CSVException {
		String[] data = line.split(",");
		if (data.length != 4) throw new CSVException("Incorrect number of values in line:\n" + line);
		Airport a = null;
		try {
			a = new Airport(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
		} catch (NumberFormatException | AirportCreationException e) {
			throw new CSVException("Incorectly formated line:\n" + line);
		}
		return a;
	}

	@Override
	public String toLine(Airport t){
		return t.getCode() + "," + t.getName() + "," + t.getX() + "," + t.getY() + "\n";
	}

	@Override
	public String getHeader() {
		return "CODE,NAME,X,Y\n";
	}

	@Override
	public boolean correctHeader(String line) {
		return line.split(",").length == 4 && line.trim().equals(getHeader().trim());
	}

	@Override
	public String getTitle() {
		return "# AIRPORTS";
	}

}
