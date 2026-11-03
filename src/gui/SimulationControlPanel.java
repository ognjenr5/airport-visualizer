package gui;

import javax.swing.JButton;
import javax.swing.JPanel;

import exceptions.SimulationException;
import simulation.Simulation;

public class SimulationControlPanel extends JPanel{
	private JButton startBtn;
	private JButton pauseBtn;
	private JButton resetBtn;
	private JButton stopBtn;
	
	private MainWindow parent;
	
	public SimulationControlPanel(MainWindow parent) {
		this.parent = parent;
		
		configureBtns();
	}
	
	private void configureBtns() {
		startBtn = new JButton("Start");
		startBtn.addActionListener(e -> {
			try {
				Simulation.startSimulation();
			} catch (SimulationException exception) {
				ExceptionPane.showException(this, exception);
			}
			
		});
		add(startBtn);
		
		pauseBtn = new JButton("Pause");
		pauseBtn.addActionListener(e -> {
			if(!Simulation.getInstance().togglePause()) return;
			pauseBtn.setText((pauseBtn.getText().equals("Pause")) ? "Unpause" : "Pause");
		});
		add(pauseBtn);
		
		resetBtn = new JButton("Reset");
		resetBtn.addActionListener(e -> {
			try {
				Simulation.restartSimulation();
				pauseBtn.setText("Pause");
			} catch (SimulationException exception) {
				ExceptionPane.showException(this, exception);
			}
			
		});
		add(resetBtn);
		
		stopBtn = new JButton("Stop");
		stopBtn.addActionListener(e -> {
			Simulation.stopSimulation();
			pauseBtn.setText("Pause");
		});
		add(stopBtn);
	}
}
