package io.format;

import java.util.ArrayList;
import exceptions.JSONException;

public interface JSONFormatter<T> extends Formatter<T>  {
	
	// Check if columns in a JSON are correct
	static boolean correctColumns(String[] data, String[] columns) {
		if (data.length != columns.length) return false;
		
		for (int i = 0; i < data.length; i++) {
			if (!columns[i].equals(data[i])) return false;
		}
			
		return true;
	}
	
	// Extract data and column names from one JSON object
	static void extractColumnsData(String line, String[] columns, String[] data) throws JSONException {

        line = line.trim();

        if (!line.startsWith("{") || !line.endsWith("}"))
            throw new JSONException("Incorrect JSON Object format.");

        String[] lineSplit = line.substring(1, line.length() - 1).split(",");

        if (lineSplit.length != columns.length)
            throw new JSONException("Incorrect number of JSON Object fields.");

		for (int i = 0; i < lineSplit.length; i++) {
			String[] tmp = lineSplit[i].split(":", 2);

			if (tmp.length != 2) throw new JSONException("Incorrect format of JSON file.");

			columns[i] = tmp[0].trim().replaceAll("^\"|\"$", "");
			data[i] = tmp[1].trim().replaceAll("^\"|\"$", "");
		}
	}
	
	// Create a whole JSON object list from lines
	static String formObjectString(ArrayList<String> lines, JSONFormatter<?> format) {
		StringBuilder sb = new StringBuilder(format.getTitle() + ":[");
		for (int i = 0; i < lines.size(); i++) 
			sb.append(lines.get(i) + (i == lines.size() - 1 ? "" : ","));
		sb.append("]");
		return sb.toString();
	}


}
