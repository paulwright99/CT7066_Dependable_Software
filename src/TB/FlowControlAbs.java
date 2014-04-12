package TB;

import java.awt.event.*;
import javax.swing.*;

/**
  Abstract version of FlowControl<p>
  controls the flow of traffic over the bridge
  @author Peter Annesley
  @version 1.2 February 2005
 */
public abstract class FlowControlAbs extends Thread implements ActionListener
{
	private Timer myTimer;


	/**	
	  run is called when the thread is started
	 */
	public void run()  
	{
		startRunning();
		myWait();
	} //run

	/**
      performs actions when the thread is started
	 */
	protected abstract void startRunning();

	/**
	  performs timed actions according to State Diagram
	 */
	protected abstract void timeout();

	/**
	  start a timer for a given number of seconds
	  @param seconds the duration of the timer
	 */
	public void startTimer(int seconds)
	{
		myTimer = new Timer((int)((double)(seconds * 1000) / TB.timeMultiplier), this);
		myTimer.start();
	} //startTimer

	/**
      actionPerformed is called when the timer elapses
	 */
	public void actionPerformed(ActionEvent e)
	{

		myTimer.stop();
		myTimer = null;
		timeout();

	} //actionPerformed

	/**
	  wait for a signal
	 */
	private synchronized void myWait()
	{
		try
		{
			wait();
		}
		catch (InterruptedException e)
		{
			System.out.println(e.toString());
		}
	} //myWait

	/**
      notify a signal
	 */
	public synchronized void myNotify()
	{
		notify();
	} //myNotify
}

