package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import airportVisualizer.Airport;
import airportVisualizer.Flight;
import exceptions.JSONException;
import exceptions.MyIOException;
import io.format.AirportJSONFormatter;
import io.format.FlightJSONFormatter;
import io.format.Formatter;
import io.format.JSONFormatter;

public class JSON implements IO {
	
	private AirportJSONFormatter airportFormatter;
	private FlightJSONFormatter flightFormatter;
	
	public JSON () {
		airportFormatter = new AirportJSONFormatter();
		flightFormatter = new FlightJSONFormatter();
	}

	@Override
	public void loadFile(String filename, ArrayList<Flight> flights, ArrayList<Airport> airports) throws MyIOException {
		StringBuilder sb = new StringBuilder();
		
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) sb.append(line.replace("\t", ""));
		} catch (FileNotFoundException e) {
			throw new JSONException("Couldn't open file: " + filename);
		} catch (IOException e) {
			throw new JSONException(e.getMessage());
		}
		
		int airportsStart = sb.indexOf("[");
		int airportsEnd = sb.indexOf("]");
		int flightsStart = sb.lastIndexOf("[");
		int flightsEnd = sb.lastIndexOf("]");
		
		if (airportsStart == -1 || airportsEnd == -1 || flightsStart == -1 || flightsEnd == -1) {
			throw new MyIOException("Incorrect file structure");
		}
		
		addToList(airports, sb.substring(airportsStart+1, airportsEnd).trim(), airportFormatter);
		addToList(flights, sb.substring(flightsStart+1, flightsEnd).trim(), flightFormatter);
	}

	// Helper function to add list of T with given format
	private <T> void addToList(ArrayList<T> list, String content, Formatter<T> format) throws MyIOException {
		list.clear();
		
		content = content.trim();
		if (content.isEmpty()) return;

        String[] objects = content.split("(?<=}),\\s*(?=\\{)");

		list.addAll(load(new ArrayList<>(Arrays.asList(objects)), format));
	}
	
	@Override
	public void saveFile(String filename, ArrayList<Flight> flights, ArrayList<Airport> airports) throws MyIOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
			bw.write("{");
			this.save(airports, filename, airportFormatter, bw);
			bw.write(",");
			this.save(flights, filename, flightFormatter, bw);
			bw.write("}");
		} catch (IOException e) {
			throw new JSONException(e.getMessage());
		}
		
	}

	@Override
	public <T> void save(ArrayList<T> list, String filename, Formatter<T> f, BufferedWriter bw) throws MyIOException, IOException {
		if (!(f instanceof JSONFormatter))
			throw new JSONException("Incorrect formatter passed.");
		JSONFormatter<T> format = (JSONFormatter<T>) f;
		
		ArrayList<String> lines = new ArrayList<>();
		for (T t : list) lines.add(format.toLine(t));
		
		String jsonObj = JSONFormatter.formObjectString(lines, format);

		bw.write(jsonObj);
		
	}

	@Override
	public <T> ArrayList<T> load(ArrayList<String> lines, Formatter<T> f) throws MyIOException {
		if (!(f instanceof JSONFormatter))
			throw new JSONException("Incorrect formatter passed.");
		JSONFormatter<T> format = (JSONFormatter<T>) f;
		ArrayList<T> list = new ArrayList<>();
		for (String line : lines)
			list.add(format.fromLine(line.trim()));
		return list;
	}

}
