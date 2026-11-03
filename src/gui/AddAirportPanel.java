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
import airportVisualizer.Storage;
import exceptions.AirportCreationException;
import exceptions.AirportVisualizerException;
import exceptions.MyIOException;

public class AddAirportPanel extends JPanel{
	private JTextField tfCode, tfName, tfX, tfY;
	private JButton addBtn;
	private MainWindow parent;
	
	public AddAirportPanel(MainWindow parent) {
		this.parent = parent;
		setLayout(new BorderLayout(10,10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    addFields();
	    addButton();	
	}
	
	private void addButton() {
		JPanel btnPanel = new JPanel();
	    addBtn = new JButton("Add airport");
	    addBtn.addActionListener(e -> {
	    	try {
				addAirport();
			} catch (AirportVisualizerException exception) {
				ExceptionPane.showException(this, exception);
			} 
	    });
	    
	    btnPanel.add(addBtn);
	    add(btnPanel, BorderLayout.SOUTH);
	}
	
	private void addFields() {
	    JPanel inputsPanel = new JPanel(new GridLayout(2,2,5,5));
	    
	    tfCode = new JTextField(10);
	    tfName = new JTextField(10);
	    tfX = new JTextField(10);
	    tfY = new JTextField(10);
	    
	    inputsPanel.add(row("\tCode: ", tfCode));
	    inputsPanel.add(row("\tX: ", tfX));
	    inputsPanel.add(row("\tName: ", tfName));
	    inputsPanel.add(row("\tY: ", tfY));
	    
	    add(inputsPanel, BorderLayout.CENTER);
	}
	
	private JPanel row(String name, JTextField tf) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel label = new JLabel(name);
		label.setPreferredSize(new Dimension(40, 20));
		row.add(label);
		row.add(tf);
		return row;
	}
	
	private void addAirport() throws MyIOException,  AirportCreationException{
		String code = tfCode.getText().trim();
		String name = tfName.getText().trim();
		String x = tfX.getText().trim();
		String y = tfY.getText().trim();
		
		if (code.isEmpty() || name.isEmpty() || x.isEmpty() || y.isEmpty())
			throw new AirportCreationException("All fields are required.");
		if (Storage.getAirportFromCode(code) != null)
			throw new AirportCreationException("Airport with code " + code + " already exists.");
		try {
			Airport a = new Airport(code, name, Integer.parseInt(x), Integer.parseInt(y));
			Storage.addAirport(a);
		}
		catch (NumberFormatException e) {
			throw new AirportCreationException("Expected numbers for X and Y.");
		}
		
		parent.refreshSideLists();
		
		tfCode.setText("");
		tfName.setText("");
		tfX.setText("");
		tfY.setText("");
		
	}
}
