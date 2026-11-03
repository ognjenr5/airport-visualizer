package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import airportVisualizer.Airport;
import airportVisualizer.Flight;
import exceptions.CSVException;
import exceptions.MyIOException;
import io.format.AirportCSVFormatter;
import io.format.CSVFormatter;
import io.format.FlightCSVFormatter;
import io.format.Formatter;

public class CSV implements IO {

	private AirportCSVFormatter airportFormatter;
	private FlightCSVFormatter flightFormatter;
	
	public CSV() {
		airportFormatter = new AirportCSVFormatter();
		flightFormatter = new FlightCSVFormatter();
	}
	
	@Override
	public void saveFile(String filename, ArrayList<Flight> flights, ArrayList<Airport> airports) throws MyIOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
			this.save(airports, filename, airportFormatter, bw);
			this.save(flights, filename, flightFormatter, bw);
		} catch (IOException e) {
			throw new CSVException(e.getMessage());
		}
		
	}
	
	@Override
	public <T> void save(ArrayList<T> list, String filename, Formatter<T> f, BufferedWriter bw) throws MyIOException, IOException {
		if (!(f instanceof CSVFormatter))
			throw new CSVException("Incorrect formatter passed.");
		CSVFormatter<T> format = (CSVFormatter<T>) f;
		
		bw.write(format.getTitle() + "\n");
		bw.write(format.getHeader());
		for (T a : list)
			bw.write(format.toLine(a));
		bw.write("\n");
	}
	
	@Override
	public <T> ArrayList<T> load(ArrayList<String> lines, Formatter<T> f) throws MyIOException {
		if (!(f instanceof CSVFormatter))
			throw new CSVException("Incorrect formatter passed.");
		CSVFormatter<T> format = (CSVFormatter<T>) f;

		ArrayList<T> list = new ArrayList<>();

		if (lines.isEmpty()) 
			throw new MyIOException("Missing section header.");
		
		String header = lines.remove(0);
		if (!format.correctHeader(header))
			throw new CSVException("Incorrect header: " + header + ".");

		for (String line : lines)
			list.add(format.fromLine(line));

		return list;
	}

	@Override
	public void loadFile(String filename, ArrayList<Flight> flights, ArrayList<Airport> airports) throws CSVException {
		ArrayList<String> linesFlights = new ArrayList<>();
		ArrayList<String> linesAirports = new ArrayList<>();
		ArrayList<String> curr = null;
		boolean isSeparator = false;

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			
			while ((line = br.readLine()) != null) {
				line = line.trim();
				
				if (line.isEmpty() && curr == null)
					throw new CSVException("Empty lines at file begining.");
				
				if (line.isEmpty()) {
					isSeparator = true;
					continue;
				}
				
				if (line.startsWith("#") && !line.equals(airportFormatter.getTitle()) && !line.equals(flightFormatter.getTitle()))
					throw new CSVException("Wrong title given: " + line);
				else if (line.equals(airportFormatter.getTitle()))
					curr = linesAirports;
				else if (line.equals(flightFormatter.getTitle()))
					curr = linesFlights;
				else if (isSeparator && !curr.isEmpty())
					throw new CSVException("Empty lines in the middle of data.");
				else if (curr == null)
					throw new CSVException("Lines of data before title.");
				else
					curr.add(line);
				
				isSeparator = false;
			}

		} catch (FileNotFoundException e) {
			throw new CSVException("Couldn't open file: " + filename);
		} catch (IOException e) {
			throw new CSVException(e.getMessage());
		}

		try {
			airports.clear();
			airports.addAll(load(linesAirports, airportFormatter));
			flights.clear();
			flights.addAll(load(linesFlights, flightFormatter));
		} catch (MyIOException e) {
			throw new CSVException(e.getMessage());
		}
	}

}
