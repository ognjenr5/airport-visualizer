package gui;

import java.awt.CardLayout;
import java.awt.LayoutManager;

import javax.swing.*;

public class FooterPanel extends JPanel{
	private static CardLayout layout = new CardLayout();
	private MainWindow parent;
	
	// Footer panel where different user requested actions are done
	public FooterPanel(MainWindow parent) {
		super(layout);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.parent = parent;
		
		add(new LoadPanel(parent), "LOAD");
		add(new SavePanel(), "SAVE");
		add(new AddFlightPanel(parent), "ADD_FLIGHT");
		add(new AddAirportPanel(parent), "ADD_AIRPORT");
		add(new SimulationControlPanel(parent), "SIMULATION");
	}
	
	public CardLayout getLayout() {
		return layout;
	}
	
	public void showCard(String cardName) {
		layout.show(this, cardName);
	}
}
