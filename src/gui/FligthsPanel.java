package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import simulation.FlightThread;
import simulation.Simulation;

public class FligthsPanel extends JPanel{
	private MainWindow parent;
	private Font font;

	// Panel on which flight's are painted and repainted on period
	public FligthsPanel(MainWindow parent) {
		setOpaque(false);
		this.parent = parent;
		this.font = new Font("Sans serif", Font.BOLD, 32);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawFlights(g);
		drawTime(g);
	}

	private void drawTime(Graphics g) {
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString(Simulation.getCurrentSimulationTime(), 0, 32);
	}
	
	private void drawFlights(Graphics g) {
		ArrayList<FlightThread> threads = Simulation.getInstance().getActiveThreadList();
		for (FlightThread ft : threads)
			drawFlight(g, ft);
	}
	
	private void drawFlight(Graphics g, FlightThread ft) {
		int x = SimulationPanel.realX(ft.getX(), getWidth(), SimulationPanel.AIRPLANE_SIZE);
		int y = SimulationPanel.realY(ft.getY(), getHeight(), SimulationPanel.AIRPLANE_SIZE);
		
		g.setColor(Color.BLUE);
		g.fillOval(x, y, SimulationPanel.AIRPLANE_SIZE, SimulationPanel.AIRPLANE_SIZE);
		g.setColor(Color.CYAN);
		g.drawOval(x, y, SimulationPanel.AIRPLANE_SIZE, SimulationPanel.AIRPLANE_SIZE);
	}
	
}
