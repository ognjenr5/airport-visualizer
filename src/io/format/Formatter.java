package io.format;

import exceptions.MyIOException;

public interface Formatter<T> {
	
	// Get object T from line
	T fromLine(String line) throws MyIOException;
	
	// Parse object T to a line
	String toLine(T t) throws MyIOException;
	
	// Get title of data
	String getTitle();
}
