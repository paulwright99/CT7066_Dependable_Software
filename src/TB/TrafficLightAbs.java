package TB;

import java.awt.event.*;
import javax.swing.*;

/**
  Abstract Traffic Light
  @author Peter Annesley
  @version 1.2 February 2005
*/
public abstract class TrafficLightAbs extends Thread implements ActionListener
{
	// TrafficLight attributes
	protected Lamp redLamp;
	protected Lamp amberLamp;
	protected Lamp greenLamp;
	protected Timer myTimer;
	protected int position;
	
	//lamp constants
	protected static final int RED = 1;
	protected static final int AMBER = 2;
	protected static final int GREEN = 4;

	/**
	  Constructor for TrafficLightAbs
	  @param positionIn indicates which end of the bridge
	*/
	public TrafficLightAbs(int positionIn)
	{
		super("Traffic light " + positionIn);
		position = positionIn;
		redLamp = new Lamp(position, RED);
		amberLamp = new Lamp(position, AMBER);
		greenLamp = new Lamp(position, GREEN);
	} //TrafficLight

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
      turn traffic light to green
	*/
	public abstract void turnToGreen();

    /**
      turn traffic light to red
	*/
	public abstract void turnToRed();

    /**
	  performs timed actions according to State Diagram
    */
	protected abstract void timeout();

	//   red-amber-green
    /**
	  show lamp pattern
	  @param rag indicates which lamps to turn on/off <p>
      RED = 1 (bit 0 set)<p>
	  AMBER = 2 (bit 1 set)<p>
	  GREEN = 4 (bit 2 set)<p>
    */
	protected void show(int rag)
	{
		// check red lamp
		if ((rag & RED) == 0)
		{
			redLamp.turnOff();
		}
		else
		{
			redLamp.turnOn();
		}
		// check amber lamp
		if ((rag & AMBER) == 0)
		{
			amberLamp.turnOff();
		}
		else
		{
			amberLamp.turnOn();
		}
		// check green lamp
		if ((rag & GREEN) == 0)
		{
			greenLamp.turnOff();
		}
		else
		{
			greenLamp.turnOn();
		}
	}

    /**
	  start a timer for a given number of seconds
	  @param seconds the duration of the timer
    */
	protected void startTimer(int seconds)
	{
		myTimer = new Timer(seconds * 1000, this); //convert seconds to millisecs
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
	protected synchronized void myWait()
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
} //TrafficLight

