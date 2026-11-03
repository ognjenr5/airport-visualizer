package gui;

import java.awt.Component;

import javax.swing.JOptionPane;

import exceptions.AirportVisualizerException;

public class ExceptionPane extends JOptionPane{
	
	static InactivityThread inactivityThread;
	
	static void setInactivityThread(InactivityThread it) {
		inactivityThread = it;
	}
	
	// Class that handles showing exception dialogs and blocking inactivity when shown
	static void showException(Component component, AirportVisualizerException exception){
		inactivityThread.pause();
		JOptionPane.showMessageDialog(component, exception.getMessage(), exception.getName(), JOptionPane.ERROR_MESSAGE);
		inactivityThread.unpause();
	}
}
