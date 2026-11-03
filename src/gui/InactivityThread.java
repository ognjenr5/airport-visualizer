package gui;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class InactivityThread extends Thread {

	static final int INACTIVITY_TIMER = 60;
	static final int WARNING_TIMER = 5;

	private MainWindow mainWindow;
	private InactivityDialog inactivityDialog;
	
	// Is inactivity paused
	private boolean paused;
	
	// For nesting pauses
	private int pauseCount;
	
	// Current remaining time
	private int time;

	public InactivityThread(MainWindow mainWindow) {
		this.paused = false;
		this.pauseCount = 0;
		this.mainWindow = mainWindow;
		this.mainWindow.setInactivityThread(this);

		setDaemon(true);

		// Global listener to catch KEY_PRESSED, MOUSE_PRESSED and MOUSE_RELEASED
		// On those user actions, wake up the thread
		Toolkit.getDefaultToolkit().addAWTEventListener(e -> {
			int eventId = e.getID();
			if (eventId == MouseEvent.MOUSE_PRESSED || eventId == MouseEvent.MOUSE_RELEASED
					|| eventId == KeyEvent.KEY_PRESSED) {
				this.interrupt();
			}
		}, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
	}

	@Override
	public void run() {
		// Set time to how much it should let user not be active
		time = INACTIVITY_TIMER;
		while (true) {
			try {
				synchronized (this) {
					while (paused)
						this.wait();
				}
				
				// Decrease remaining time and sleep
				Thread.sleep(1000);
				time--;
				
				// If time has reached warning remaining time, create a dialog to remind user
				if (time == WARNING_TIMER)
					this.inactivityDialog = new InactivityDialog(mainWindow, time);
			} catch (InterruptedException e) {
				
				// If thread was interrupted, restart time
				time = INACTIVITY_TIMER;
				
				// Delete the dialog if it was created
				if (inactivityDialog != null) {
					inactivityDialog.dispose();
					inactivityDialog = null;
				}
			}
		}
	}

	// Pause inactivity
	public synchronized void pause() {
		this.paused = true;
		this.pauseCount++;
	}

	// Un-pause only when all nested pauses are un-paused
	public synchronized void unpause() {
		if (this.pauseCount == 0) return;
		this.pauseCount--;
		if (this.pauseCount > 0) return;
		this.paused = false;
		time = INACTIVITY_TIMER;
		this.notify();
	}
}
