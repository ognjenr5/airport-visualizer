package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import airportVisualizer.Airport;
import airportVisualizer.Storage;

public class AirportsPanel extends JPanel {
	private Font font;
	private Timer timer;
	private MainWindow parent;
	private boolean blink;

	// Panel in the background that shows airports
	public AirportsPanel(MainWindow parent) {
		setBackground(Color.WHITE);
		this.parent = parent;
		this.font = new Font("Sans serif", Font.BOLD, 12);
		
		// Blink timer that's started and stopped on airport selection
		this.timer = new Timer(500, e -> {
			blink = !blink;
			this.repaint();
		});
		configureListener();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawAirports(g);
	}

	private void drawAirports(Graphics g) {
		for (Airport a : Storage.getAirports())
			if (a.isVisible())
				drawAirport(a, g);
	}

	private void drawAirport(Airport a, Graphics g) {
		int x = SimulationPanel.realX(a.getX(), this.getWidth(), SimulationPanel.AIRPORT_SIZE);
		int y = SimulationPanel.realY(a.getY(), this.getHeight(), SimulationPanel.AIRPORT_SIZE);

		Color airportColor = a == Storage.getSelected() && !blink ? Color.RED : Color.GRAY;

		g.setColor(airportColor);
		g.fillRect(x, y, SimulationPanel.AIRPORT_SIZE, SimulationPanel.AIRPORT_SIZE);

		g.setColor(Color.BLACK);
		g.setFont(font);
		g.drawString(a.getCode(), x, y);
	}

	// Configure blinking selected airport
	private void configureListener() {
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int relX = (int) (SimulationPanel.MIN_X
						+ (double) e.getX() / getWidth() * (SimulationPanel.MAX_X - SimulationPanel.MIN_X));
				int relY = (int) (SimulationPanel.MIN_Y
						+ (1 - (double) e.getY() / getHeight()) * (SimulationPanel.MAX_Y - SimulationPanel.MIN_Y));

				Airport at = airportAt(relX, relY);

				if (at == null)
					return;

				if (at == Storage.getSelected()) {
					Storage.clearSelected();
					parent.unpauseInactivity();
					timer.stop();

				} else if (at != null) {
					if (Storage.getSelected() == null)
						parent.pauseInactivity();
					Storage.setSelected(at);
					timer.start();
				}

				repaint();
			}
		});
	}

	// Find if there's an airport at click location
	private Airport airportAt(int relX, int relY) {
		double minDist = Double.MAX_VALUE;
		Airport at = null;
		for (Airport a : Storage.getAirports()) {
			int x = Math.abs(relX - a.getX());
			int y = Math.abs(relY - a.getY());
			if (a.isVisible() && x < SimulationPanel.AIRPORT_SIZE / 4 && y < SimulationPanel.AIRPORT_SIZE / 4)
				if (Math.sqrt(x*x + y*y) < minDist) {
					minDist = Math.sqrt(x*x + y*y);
					at = a;
				}
		}

		return at;
	}

}
