package io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import airportVisualizer.Airport;
import airportVisualizer.Flight;
import exceptions.MyIOException;
import io.format.Formatter;

public interface IO {
	
	// Load flights and airports from file
	void loadFile(String filename, ArrayList<Flight> flights, ArrayList<Airport> airports) throws MyIOException;
	
	// Save flights and airports to file
	void saveFile(String filename, ArrayList<Flight> flights, ArrayList<Airport> airports) throws MyIOException;
	
	// Save a list of type T to file named filename
	<T>void save(ArrayList<T> list, String filename, Formatter<T> f, BufferedWriter bw) throws MyIOException, IOException;
	
	// Load a list of type T from a file named filename
	<T>ArrayList<T> load(ArrayList<String> lines, Formatter<T> f) throws MyIOException;

	// Accepted formats
	static String[] formats = {"json", "csv"};
	
	// Get IO class based on filename extension
	static IO getIO (String filename) throws MyIOException {
		String[] f = filename.split("\\.");
		if (f.length != 2) throw new MyIOException("Incorrect filename format.");
		
		String extension = f[1].toLowerCase();
		
		switch (extension) {
		case "json": 	return new JSON();
		case "csv":		return new CSV();
		default:		throw new MyIOException("Unsupported file format: " + extension);
		}
	}
}
