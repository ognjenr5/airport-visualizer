package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import airportVisualizer.Airport;
import airportVisualizer.Flight;
import airportVisualizer.Storage;

public class SidePanel extends JPanel {

	private MainWindow parent;
	private JButton btnList[];

	private JPanel airportListPanel;
	private ArrayList<JCheckBox> checkboxList;
	private JScrollPane airportListScrollPane;

	private JList<Flight> flightList;
	private JScrollPane flightListScrollPane;

	// Side panel with airport and flight information
	public SidePanel(MainWindow parent) {
		super(new BorderLayout(0, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.parent = parent;

		add(configureButtonPanel(), BorderLayout.NORTH);

		JPanel lists = new JPanel(new GridLayout(2, 1, 0, 10));

		lists.add(configureAirportList());
		lists.add(configureFlightList());

		add(lists, BorderLayout.CENTER);
	}

	private JPanel configureButtonPanel() {
		JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 0, 10));
		btnList = new JButton[5];

		btnList[0] = new JButton("Load flights and airports");
		btnList[0].addActionListener(e -> parent.showCard("LOAD"));
		buttonPanel.add(btnList[0]);

		btnList[1] = new JButton("Save flights and airports");
		btnList[1].addActionListener(e -> parent.showCard("SAVE"));
		buttonPanel.add(btnList[1]);

		btnList[2] = new JButton("Add a new flight");
		btnList[2].addActionListener(e -> parent.showCard("ADD_FLIGHT"));
		buttonPanel.add(btnList[2]);

		btnList[3] = new JButton("Add a new airport");
		btnList[3].addActionListener(e -> parent.showCard("ADD_AIRPORT"));
		buttonPanel.add(btnList[3]);

		btnList[4] = new JButton("Simulation control");
		btnList[4].addActionListener(e -> parent.showCard("SIMULATION"));
		buttonPanel.add(btnList[4]);

		return buttonPanel;
	}

	private JScrollPane configureAirportList() {
		airportListPanel = new JPanel();
		airportListPanel.setLayout(new BoxLayout(airportListPanel, BoxLayout.Y_AXIS));
		airportListPanel.setBackground(Color.WHITE);
		checkboxList = new ArrayList<>();
		addAirports();

		airportListScrollPane = new JScrollPane(airportListPanel);
		airportListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		return airportListScrollPane;
	}

	// Add airports to side panel list
	public void addAirports() {
		airportListPanel.removeAll();

		ArrayList<Airport> airports = Storage.getAirports();

		if (airports.isEmpty())
			return;

		for (Airport a : airports) {
			JCheckBox checkbox = new JCheckBox(a.toString(), a.isVisible());
			checkbox.setBackground(Color.WHITE);
			checkboxList.add(checkbox);
			checkbox.addActionListener(e -> {
				a.toggle();
				if (!a.isVisible()) {
					Storage.clearSelected();
					this.parent.getInactivityThread().unpause();
				}
					
				this.parent.refreshSideLists();
			});
			airportListPanel.add(checkbox);
		}

		airportListPanel.revalidate();
		airportListPanel.repaint();
		airportListScrollPane.repaint();
	}

	private JScrollPane configureFlightList() {
		flightList = new JList<>();
		flightListScrollPane = new JScrollPane(flightList);
		flightListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		flightListScrollPane.setOpaque(false);

		addFlights();


		return flightListScrollPane;
	}

	// Add flights to side panel list
	public void addFlights() {

		List<Flight> flights = Storage.getFlights().stream()
				.filter(f -> f.getFrom() != null && f.getFrom().isVisible() && f.getTo() != null && f.getTo().isVisible())
				.collect(Collectors.toList());

		flightList.setListData(flights.toArray(new Flight[0]));
		
		flightList.revalidate();
		flightList.repaint();
	}

	public void disableBtns() {
		for (JButton btn : btnList)
			btn.setEnabled(false);
		for (JCheckBox chkbox : checkboxList)
			chkbox.setEnabled(false);
	}

	public void enableBtns() {
		for (JButton btn : btnList)
			btn.setEnabled(true);
		for (JCheckBox chkbox : checkboxList)
			chkbox.setEnabled(true);
	}
}
