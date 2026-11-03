package io.format;

public interface CSVFormatter<T> extends Formatter<T>{
	
	// Get CSV header for given T
	String getHeader();

	// Check if CSV header is correct
	boolean correctHeader(String line);
}
