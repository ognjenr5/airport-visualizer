package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import airportVisualizer.Airport;
import airportVisualizer.Flight;
import airportVisualizer.Storage;
import exceptions.AirportVisualizerException;
import exceptions.FlightCreationException;
import exceptions.MyIOException;

public class AddFlightPanel extends JPanel {
	private JTextField tfAirport1, tfAirport2, tfDeparture, tfDuration;
	private JButton addBtn;
	private MainWindow parent;

	public AddFlightPanel(MainWindow parent) {
		this.parent = parent;
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		addFields();
		addButton();
	}

	private void addButton() {
		JPanel btnPanel = new JPanel();
		addBtn = new JButton("Add flight");
		addBtn.addActionListener(e -> {
			try {
				addFlight();
			} catch (AirportVisualizerException exception) {
				ExceptionPane.showException(this, exception);
			}
		});

		btnPanel.add(addBtn);
		add(btnPanel, BorderLayout.SOUTH);
	}

	private void addFields() {
		JPanel inputsPanel = new JPanel(new GridLayout(2, 2, 5, 5));

		tfAirport1 = new JTextField(10);
		tfAirport2 = new JTextField(10);
		tfDeparture = new JTextField(10);
		tfDuration = new JTextField(10);

		inputsPanel.add(row("\tFrom: ", tfAirport1));
		inputsPanel.add(row("\tTo: ", tfAirport2));
		inputsPanel.add(row("\tDeparture: ", tfDeparture));
		inputsPanel.add(row("\tDuration: ", tfDuration));

		add(inputsPanel, BorderLayout.CENTER);
	}

	private JPanel row(String name, JTextField tf) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel label = new JLabel(name);
		label.setPreferredSize(new Dimension(80, 20));
		row.add(label);
		row.add(tf);
		return row;
	}

	private void addFlight() throws MyIOException, FlightCreationException {
		String from = tfAirport1.getText().trim();
		String to = tfAirport2.getText().trim();
		String departure = tfDeparture.getText().trim();
		String duration = tfDuration.getText().trim();

		if (from.isEmpty() || to.isEmpty() || departure.isEmpty() || duration.isEmpty())
			throw new FlightCreationException("All fields are required.");

		Airport aFrom = Storage.getAirportFromCode(from);
		Airport aTo = Storage.getAirportFromCode(to);

		if (aTo == null || aFrom == null)
			throw new FlightCreationException("Given airports don't exist.");
		try {
			Flight f = new Flight(aFrom, aTo, departure, Integer.parseInt(duration));
			Storage.addFlight(f);
		} catch (NumberFormatException e) {
			throw new FlightCreationException("Expected number for duration.");
		}

		parent.refreshSideLists();

		tfAirport1.setText("");
		tfAirport2.setText("");
		tfDeparture.setText("");
		tfDuration.setText("");

	}
}
