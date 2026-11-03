package gui;

import java.awt.BorderLayout;
import java.awt.CheckboxGroup;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;

import airportVisualizer.Airport;
import airportVisualizer.Flight;
import airportVisualizer.Storage;
import exceptions.FlightCreationException;
import simulation.FlightThread;
import simulation.Simulation;

public class MainWindow extends JFrame {
	
	private JPanel mainPanel;
	private SimulationPanel simulationPanel;
	private FooterPanel footerPanel;
	private SidePanel sidePanel;
	private InactivityThread inactivityThread;

	public MainWindow() {
		super("Airport Visualizer");
		setSize(1000, 600);
		this.setMinimumSize(new Dimension(1000, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addPanels();
		
		setVisible(true);
		
		Simulation.setMainWindow(this);
		
		inactivityThread = new InactivityThread(this);
		inactivityThread.start();
		
		Storage.setInactivityThread(inactivityThread);
		ExceptionPane.setInactivityThread(inactivityThread);
	}
	
	private void addPanels() {
		mainPanel = new JPanel(new BorderLayout());
		simulationPanel = new SimulationPanel(this);
		footerPanel = new FooterPanel(this);
		sidePanel = new SidePanel(this);
		
		mainPanel.add(footerPanel, BorderLayout.SOUTH);
		mainPanel.add(simulationPanel, BorderLayout.CENTER);
		
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(sidePanel, BorderLayout.EAST);
		
	}

	// Show card in footer panel
	public void showCard(String cardName) {
		footerPanel.showCard(cardName);
	}
	
	// Refresh display of airports and flights on side panel
	public void refreshSideLists() {
		sidePanel.addAirports();
		sidePanel.addFlights();
		refreshAirports();
		sidePanel.repaint();
	}
	
	
	// Refresh airport display
	public void refreshAirports() {
		simulationPanel.repaintAirports();
	}
	
	// Refresh flight simulation display
	public void refreshFlightSimulation() {
		simulationPanel.repaintFlights();
	}
	
	public FooterPanel getFooterPanel() {
		return footerPanel;
	}

	public SimulationPanel getSimulationPanel() {
		return simulationPanel;
	}

	public SidePanel getSidePanel() {
		return sidePanel;
	}
	
	public void pauseInactivity() {
		this.inactivityThread.pause();
	}
	
	public void unpauseInactivity() {
		this.inactivityThread.unpause();
	}
	
	public void disableSidePanelButtons() {
		sidePanel.disableBtns();
	}
	
	public void enableSidePanelButtons() {
		sidePanel.enableBtns();
	}

	public InactivityThread getInactivityThread() {
		return inactivityThread;
	}
	
	public void setInactivityThread(InactivityThread inactivityThread) {
		this.inactivityThread = inactivityThread;
	}

}
