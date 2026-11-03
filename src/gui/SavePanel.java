package gui;

import javax.swing.*;

import airportVisualizer.Storage;
import exceptions.AirportVisualizerException;
import exceptions.MyIOException;
import io.IO;

public class SavePanel extends JPanel{
	
	private JTextField filenameField;
	private JButton saveBtn;
	private JLabel label;
	
	public SavePanel() {
		
		label = new JLabel("Filename: ");
		
		filenameField = new JTextField(30);

		saveBtn = new JButton("Save");

		
		add(label);
		add(filenameField);
		add(saveBtn);
		
		saveBtn.addActionListener(e -> {
			try {
				save();
			} catch (AirportVisualizerException exception) {
				ExceptionPane.showException(this, exception);
				//JOptionPane.showMessageDialog(this, exception.getMessage(), exception.getName(), JOptionPane.ERROR_MESSAGE);
			}
		});
		
	}
	private void save() throws MyIOException{
		String filename = filenameField.getText().trim();
		if (filename.isEmpty()) return;
		IO io = IO.getIO(filename);
		Storage.saveStorage(io, filename);
	}
}
