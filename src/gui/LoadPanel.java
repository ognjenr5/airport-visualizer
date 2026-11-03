package gui;

import javax.swing.*;

import airportVisualizer.Storage;
import exceptions.AirportCreationException;
import exceptions.AirportVisualizerException;
import exceptions.FlightCreationException;
import exceptions.LoadException;
import exceptions.MyIOException;
import io.IO;

public class LoadPanel extends JPanel{
	
	private JTextField filenameField;
	private JButton loadBtn;
	private MainWindow parent;
	private JLabel label;
	
	public LoadPanel(MainWindow parent) {
		this.parent = parent;
		
		label = new JLabel("Filename: ");
		
		filenameField = new JTextField(30);

		loadBtn = new JButton("Load");
		
		add(label);
		add(filenameField);
		add(loadBtn);

		loadBtn.addActionListener(e -> {
			try {
				load();
			} catch (AirportVisualizerException exception) {
				ExceptionPane.showException(this, exception);
			}
		});
	}
	
	private void load() throws LoadException, MyIOException, AirportCreationException, FlightCreationException {
		String filename = filenameField.getText().trim();
		if (filename.isEmpty()) throw new LoadException("Filename empty.");
		IO io = IO.getIO(filename);
		Storage.loadStorage(io, filename);
		parent.refreshSideLists();
	}
}
