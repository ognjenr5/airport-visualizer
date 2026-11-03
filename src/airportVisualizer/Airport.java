package airportVisualizer;

import exceptions.AirportCreationException;

import java.util.PriorityQueue;
import java.util.regex.*;

public class Airport implements Comparable<Airport>{
	private String code, name;
	private int x, y;
	
	// Is airport visible on the map
	private boolean visible; 
	
	// Is airport selected on the map
	private boolean selected;
	
	// Flights starting from the given airport
	private PriorityQueue<Flight> flights;
	
	// Last flight to have taken of given airport
	private Flight latestFlight;
	
	public Airport(String code, String name, int x, int y) throws AirportCreationException{
		if (x < -180 || x > 180) 
			throw new AirportCreationException("x coordinate out of bounds [-180,180].");
		if (y < -90 || y > 90) 
			throw new AirportCreationException("y coordinate out of bounds [-90,90].");
		if (!Pattern.matches("[A-Z]{3}", code))
			throw new AirportCreationException("name doesn't match format.");
		this.x = x;
		this.y = y;
		this.code = code;
		this.name = name;
		this.visible = true;
		
		this.flights = new PriorityQueue<Flight>();
		this.latestFlight = null;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return this.getShortName() + " (" + this.x + ", " + this.y + ")";
	}
	
	public String getShortName() {
		return this.code + " - " + this.name;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isVisible() {
		return visible;
	}

	public void hide() {
		this.visible = false;
	}
	
	public void show() {
		this.visible = true;
	}
	
	public void toggle() {
		this.visible = !this.visible;
	}

	@Override
	public int compareTo(Airport a) {
		return this.code.compareTo(a.code);
	}
	
	public void addFlight(Flight f) {
		this.flights.add(f);
	}
	
	public void clearFlights() {
		this.flights.clear();
	}
	
	public Flight getFlight() {
		if (this.flights.isEmpty()) return null;
		Flight f = this.flights.remove();
		this.latestFlight = f;
		return f;
	}
	
	public Flight peekFirstFlight() {
		if (this.flights.isEmpty()) return null;
		return this.flights.peek();
	}
	
	public Flight getLatestFlight() {
		return this.latestFlight;
	}
	
	public void printFlightsOrdered() {
		PriorityQueue<Flight> printFlights = new PriorityQueue<>(this.flights);
		while (!printFlights.isEmpty())
			System.out.println(printFlights.remove());
	}
}
