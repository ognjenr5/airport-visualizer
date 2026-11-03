package airportVisualizer;

import exceptions.FlightCreationException;

import java.util.regex.*;

public class Flight implements Comparable<Flight> {

	// Airports in String and Airport format, needed for delaying flight airport interconnection
	private String srcAirport;
	private String dstAirport;
	private Airport aFrom;
	private Airport aTo;

	// Flight departure time in formated String HH:MM
	private String departureTime;
	
	// Calculated value based on formatted input
	private long departureTimeInMinutes;
	
	// Flight duration
	private long duration;

	public Flight(String srcAirport, String dstAirport, String departureTime, long duration)
			throws FlightCreationException {
		if (srcAirport.equals(dstAirport))
			throw new FlightCreationException("Starting and ending airport cannot be the same.");
		if (!Pattern.matches("([01]\\d|2[0-3]):[0-5]\\d", departureTime))
			throw new FlightCreationException("Departure time isn't in valid format.");
		if (duration <= 0)
			throw new FlightCreationException("Flight duration has to be greater than zero.");

		// Calculate departure time in minutes
		String[] tmpTime = departureTime.split(":");
		this.departureTimeInMinutes = Long.parseLong(tmpTime[0]) * 60 + Long.parseLong(tmpTime[1]);

		this.srcAirport = srcAirport;
		this.dstAirport = dstAirport;
		this.departureTime = departureTime;
		this.duration = duration;
	}

	public Flight(Airport aFrom, Airport aTo, String departureTime, int duration) throws FlightCreationException {
		this(aFrom.getCode(), aTo.getCode(), departureTime, duration);
		this.aFrom = aFrom;
		this.aTo = aTo;
	}

	@Override
	public String toString() {
		return this.departureTime + "       " + this.srcAirport + " -> " + this.dstAirport + " (" + this.duration
				+ " min)";
	}

	public String getSrcAirport() {
		return srcAirport;
	}

	public String getDstAirport() {
		return dstAirport;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public long getDuration() {
		return duration;
	}

	public long getDurationInMs() {
		return duration * 60 * 1000;
	}

	public long getDepartureTimeInMinutes() {
		return departureTimeInMinutes;
	}

	public long getDepartureTimeInMs() {
		return departureTimeInMinutes * 60 * 1000;
	}

	public Airport getFrom() {
		return aFrom;
	}

	public Airport getTo() {
		return aTo;
	}

	@Override
	public int compareTo(Flight f) {
		if (this.departureTimeInMinutes != f.departureTimeInMinutes)
			return ((int) (this.departureTimeInMinutes - f.departureTimeInMinutes));
		else if (!this.srcAirport.equals(f.srcAirport))
			return this.srcAirport.compareTo(f.srcAirport);
		return this.dstAirport.compareTo(f.dstAirport);
	}

	public void setFrom(Airport aFrom) {
		this.aFrom = aFrom;
	}

	public void setTo(Airport aTo) {
		this.aTo = aTo;
	}
}
