package gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.*;

public class SimulationPanel extends JPanel{
	static final int MAX_X = 180;
	static final int MIN_X = -180;
	static final int MAX_Y = 90;
	static final int MIN_Y = -90;

	static final int AIRPORT_SIZE = 20;
	static final int AIRPLANE_SIZE = 15;
	
	AirportsPanel airportLayer;
	FligthsPanel flightLayer;
	MainWindow parent;
	JLayeredPane layers;
	
	// Simulation panel with layered background airport and foreground flight panels
	public SimulationPanel(MainWindow parent) {
		this.parent = parent;
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		airportLayer = new AirportsPanel(parent);
		flightLayer = new FligthsPanel(parent);
		
		layers = new JLayeredPane();
		layers.setLayout(new OverlayLayout(layers));
		layers.add(airportLayer, Integer.valueOf(0));
		layers.add(flightLayer, Integer.valueOf(1));
		
		add(layers);
	}
	
	public void repaintAirports() {
		airportLayer.repaint();
	}
	
	public void repaintFlights() {
		flightLayer.repaint();
	}
	
	static int realX (double x, int width, int elemSize) {
		double relX = (double) (x - MIN_X) / (MAX_X - MIN_X);
		return  (int) Math.round((relX * width) - elemSize / 2);
	}
	
	static int realY (double y, int height, int elemSize) {
		double relY = 1 - (double) (y - MIN_Y) / (MAX_Y - MIN_Y);
		return (int) Math.round((relY * height) - elemSize / 2);
	}
}
