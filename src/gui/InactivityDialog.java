package gui;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class InactivityDialog extends JOptionPane{
	
	private int remainingTime;
	private MainWindow parent;
	private JDialog dialog;
	private Timer timer;
	
	public InactivityDialog(MainWindow parent, int time) {
		super("Time remaining: " + time + " seconds!", JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
		this.remainingTime = time;
		this.parent = parent;
		
		this.dialog = this.createDialog(parent, "Inactivity warning!");
		this.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		startTimer();
		
		this.dialog.setVisible(true);
		
		timer.stop();
	}
	
	// Start a timer for remaining warning time until program shuts down
	private void startTimer() {
		timer = new Timer(1000, e -> {
			remainingTime--;
			if (remainingTime <= 0) {
				if (timer != null) timer.stop();
				this.dialog.dispose();
				this.parent.dispose();
			}
			this.setMessage("Time remaining: " + remainingTime + " seconds!");
		});
		timer.start();
	}
	
	public void dispose() {
		this.timer.stop();
		this.dialog.dispose();
	}
}
