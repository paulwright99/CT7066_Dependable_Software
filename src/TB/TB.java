package TB;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
  Tewkester Bridge animation system
  @author Peter Annesley and Vicky Bush
  @version 1.6 January 2013
*/
public class TB extends JFrame
{
    /**
	 * 
	 */
	// My variables
	public static final double timeMultiplier = 5.0;
	private static int statsCrashes;
	private static int statsCarsGoneWE;
	private static int statsCarsGoneEW;
	private static int statsSeconds;
	private static int statsCarsWaitingWE;
	private static int statsCarsWaitingEW;
	private static double statsCarsWaitingWEAverage;
	private static double statsCarsWaitingEWAverage;
	
	
	//private static int eastCars = 0;
	
	
	//constants
	private static final long serialVersionUID = 42L; // for serialization
	
	private static final int MAX_CARS = 12;
	private static final int MAX_LIGHTS = 3;
	private static final int WEST_END = 0;
	private static final int EAST_END = 1;
	
	private static final int CARS_PER_MINUTE_WE = 2;
	private static final int CARS_PER_MINUTE_EW = 8;
	
	private static final int SECONDS_RUNNING = 600;
	

    // GUI variables	
	//private static JButton eastButton;
	//private static JButton westButton;
	private JPanel buttonPanel;
	private JPanel flowPanel;
	private JPanel eastWaitPanel;
	private JPanel carPanel;
	private JPanel roadPanel;
	private JPanel westWaitPanel;
	//private JPanel waitPanel;
	private static JPanel[] eastWaitPos = new JPanel[MAX_CARS];
	private static JPanel[] carPos = new JPanel[MAX_CARS];
	private static JPanel[] westWaitPos = new JPanel[MAX_CARS];
	private JPanel eastLightPanel;
	private static JPanel[] eastLight = new JPanel[MAX_LIGHTS];
	private JPanel westLightPanel;
	private static JPanel[] westLight = new JPanel[MAX_LIGHTS];
	private JLabel buttonLabel;
	private BorderLayout layout;
	
	private static JPanel statisticsPanel;
	private static JLabel displayCrashes;
	private static JLabel displayCarsGoneEW;
	private static JLabel displayCarsGoneWE;
	private static JLabel displaySeconds;
	private static JLabel displayCarsWaitingWE;
	private static JLabel displayCarsWaitingEW;
	private static JLabel displayCarsWaitingWEAverage;
	private static JLabel displayCarsWaitingEWAverage;
	private static JLabel displayCarsGoneTotal;	
	
    //control variables
	private static boolean directionWE = true;
	private static boolean eastGreen = false;
	private static boolean westGreen = false;
	private static boolean[] carPresent = new boolean [MAX_CARS];
	private static boolean[] carWaiting = {false, false};
	private static boolean[] carSensed = {false, false};
	private static boolean[] sensorTriggered = {false, false};
	
	//protected static FlowControl myFlowControl;
		
	//Random number generator
	private static Random random = new Random(1);
	
	
	public TB()
	{
		super("Tewkester Bridge Animator - January 2013");
		
		for (int i = 0; i < MAX_CARS; i++)
		{
			carPresent[i] = false;
		}

		Container c = getContentPane();
		layout = new BorderLayout(10, 10);
		c.setLayout(layout);
		c.setBackground(Color.white);
		
        //set up buttons at bottom of window
		flowPanel = new JPanel();
		flowPanel.setLayout(new BorderLayout(0, 2));
		flowPanel.setBackground(Color.white);
//		buttonPanel = new JPanel();
//		buttonPanel.setLayout(new FlowLayout());
//		buttonPanel.setBackground(Color.white);
//		westButton = new JButton("West");
//		buttonPanel.add(westButton);
//		buttonLabel = new JLabel("indicate arrivals");
//		buttonPanel.add(buttonLabel);
		//eastButton = new JButton("East");
		//buttonPanel.add(eastButton);
//		flowPanel.add(buttonPanel, BorderLayout.CENTER);

		// Statistics display initialisation
		
		statisticsPanel = new JPanel();
		statisticsPanel.setLayout(new GridLayout(0,6));
		statisticsPanel.setBackground(Color.white);
		statsCrashes = 0;
		statsCarsGoneWE = 0;
		statsCarsGoneEW = 0;
		statsSeconds = 0;
		statsCarsWaitingWE = 0;
		statsCarsWaitingEW = 0;
		statsCarsWaitingWEAverage = 0.0;
		statsCarsWaitingEWAverage = 0.0;

		displayCrashes = new JLabel(String.valueOf(statsCrashes));
		displayCarsGoneWE = new JLabel(String.valueOf(statsCarsGoneWE));
		displayCarsGoneEW = new JLabel(String.valueOf(statsCarsGoneEW));
		displaySeconds = new JLabel(String.valueOf(statsSeconds));
		displayCarsWaitingWE = new JLabel(String.valueOf(statsCarsWaitingWE));
		displayCarsWaitingEW = new JLabel(String.valueOf(statsCarsWaitingEW));
		displayCarsWaitingWEAverage = new JLabel(String.valueOf(statsCarsWaitingWEAverage));
		displayCarsWaitingEWAverage = new JLabel(String.valueOf(statsCarsWaitingEWAverage));
		displayCarsGoneTotal = new JLabel(String.valueOf(statsCarsGoneEW + statsCarsGoneWE));
		
		
		// Row 2
		statisticsPanel.add(new JLabel(""));
		statisticsPanel.add(new JLabel(""));
		if( FlowControl.currentStateModel == FlowControl.StateModel.STATEMODEL_NEW)
		{
				statisticsPanel.add(new JLabel("NEW STATE MODEL"));
		}
		else
		{
				statisticsPanel.add(new JLabel("OLD STATE_MODEL"));
		}
				
		statisticsPanel.add(new JLabel(""));
		statisticsPanel.add(new JLabel(""));
		statisticsPanel.add(new JLabel(""));
		
		// Row 2
		statisticsPanel.add(new JLabel("------------------"));
		statisticsPanel.add(new JLabel(""));
		statisticsPanel.add(new JLabel("------------"));
		statisticsPanel.add(new JLabel(""));
		statisticsPanel.add(new JLabel("------------------"));
		statisticsPanel.add(new JLabel(""));
		
		// Row 1
		statisticsPanel.add(new JLabel("West to East"));
		statisticsPanel.add(new JLabel(""));
		statisticsPanel.add(new JLabel("General"));
		statisticsPanel.add(new JLabel(""));
		statisticsPanel.add(new JLabel("East to West"));
		statisticsPanel.add(new JLabel(""));
		
		// Row 2
		statisticsPanel.add(new JLabel("------------------"));
		statisticsPanel.add(new JLabel(""));
		statisticsPanel.add(new JLabel("------------"));
		statisticsPanel.add(new JLabel(""));
		statisticsPanel.add(new JLabel("------------------"));
		statisticsPanel.add(new JLabel(""));
		
		// Row 3
		statisticsPanel.add(new JLabel("Cars waiting currently:"));
		statisticsPanel.add(displayCarsWaitingWE);
		statisticsPanel.add(new JLabel("Crashes:"));
		statisticsPanel.add(displayCrashes);
		statisticsPanel.add(new JLabel("Cars waiting currently:"));
		statisticsPanel.add(displayCarsWaitingEW);
		
		// Row 4		
		statisticsPanel.add(new JLabel("Cars gone:"));
		statisticsPanel.add(displayCarsGoneWE);
		statisticsPanel.add(new JLabel("Seconds gone:"));
		statisticsPanel.add(displaySeconds);
		statisticsPanel.add(new JLabel("Cars gone:"));
		statisticsPanel.add(displayCarsGoneEW);
		
		// Row 5
		statisticsPanel.add(new JLabel("Cars waiting average:"));
		statisticsPanel.add(displayCarsWaitingWEAverage);
		statisticsPanel.add(new JLabel("Total cars Gone:"));
		statisticsPanel.add(displayCarsGoneTotal);
		statisticsPanel.add(new JLabel("Cars waiting average:"));
		statisticsPanel.add(displayCarsWaitingEWAverage);
		
		// Row 6
		statisticsPanel.add(new JLabel("Cars arriving/min:"));
		statisticsPanel.add(new JLabel(String.valueOf(CARS_PER_MINUTE_WE)));
		statisticsPanel.add(new JLabel(""));
		statisticsPanel.add(new JLabel(""));
		statisticsPanel.add(new JLabel("Cars arriving/min:"));
		statisticsPanel.add(new JLabel(String.valueOf(CARS_PER_MINUTE_EW)));

		


		
		flowPanel.add(statisticsPanel, BorderLayout.SOUTH);
		
		
		
        //set up road with wait areas above & below
		carPanel = new JPanel();
		carPanel.setLayout(new GridLayout(1, MAX_CARS, 0, 2));
		eastWaitPanel = new JPanel();
		eastWaitPanel.setLayout(new GridLayout(1, MAX_CARS, 0, 0));
		eastWaitPanel.setBackground(Color.white);
		westWaitPanel = new JPanel();
		westWaitPanel.setLayout(new GridLayout(1, MAX_CARS, 0, 2));
		westWaitPanel.setBackground(Color.white);
		for (int i = 0; i < MAX_CARS; i++)
		{
			eastWaitPos[i] = new JPanel();
			eastWaitPos[i].setBackground(Color.black);
			eastWaitPos[i].setVisible(false);
			eastWaitPanel.add(eastWaitPos[i]);
		}
		for (int i = 0; i < MAX_CARS; i++)
		{
			carPos[i] = new JPanel();
			carPos[i].setBackground(Color.black);
			carPos[i].setVisible(false);
			carPanel.add(carPos[i]);
		}
		for (int i = 0; i < MAX_CARS; i++)
		{
			westWaitPos[i] = new JPanel();
			westWaitPos[i].setBackground(Color.black);
			westWaitPos[i].setVisible(false);
			westWaitPanel.add(westWaitPos[i]);
		}
        
        //set up road above buttons
		roadPanel = new JPanel();
		roadPanel.setLayout(new BorderLayout(0, 5));
		roadPanel.setBackground(Color.gray);
		roadPanel.add(carPanel, BorderLayout.CENTER);
		roadPanel.add(westWaitPanel, BorderLayout.NORTH);
		roadPanel.add(eastWaitPanel, BorderLayout.SOUTH);
		flowPanel.add(roadPanel, BorderLayout.NORTH);
		c.add(flowPanel, BorderLayout.SOUTH);
        
        //set up east traffic lights at left of window
		eastLightPanel = new JPanel();
		eastLightPanel.setBackground(Color.black);
		eastLightPanel.setLayout(new GridLayout(MAX_LIGHTS + 1, 1, 10, 10));
		for (int i = 0; i < MAX_LIGHTS; i++)
		{
			eastLight[i] = new JPanel();
			eastLightPanel.add(eastLight[i]);
		}
		eastLight[0].setBackground(Color.red);
		eastLight[1].setBackground(Color.yellow);
		eastLight[2].setBackground(Color.green);
		c.add(eastLightPanel, BorderLayout.EAST);
		
        //set up west traffic lights at right of window
		westLightPanel = new JPanel();
		westLightPanel.setBackground(Color.black);
		westLightPanel.setLayout(new GridLayout(MAX_LIGHTS + 1, 1, 10, 10));
		for (int i = 0; i < MAX_LIGHTS; i++)
		{
			westLight[i] = new JPanel();
			westLightPanel.add(westLight[i]);
		}
		westLight[0].setBackground(Color.red);
		westLight[1].setBackground(Color.yellow);
		westLight[2].setBackground(Color.green);
		westLight[0].setVisible(false);
		c.add(westLightPanel, BorderLayout.WEST);
		
        // set up button handlers
//		ButtonHandler handler = new ButtonHandler();
//		eastButton.addActionListener(handler);
//		westButton.addActionListener(handler);
		
        //set size of window
        //needed to adjust Xwidth to make cars fit right-hand edge
		setSize(850, 400);
		setVisible(true);
	} //TB
 
 	//public static void main(String args[]) throws InterruptedException
 	public void startAnimation() throws InterruptedException
	{
		System.out.println("TB starting");
		
		addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					//myFlowControl.myNotify();
					System.exit(0);
				}
			}
		);
		
		boolean continuing = true; 
        
		//start the main animation loop
		while (statsSeconds < SECONDS_RUNNING)
		{
			Thread.sleep((long)(1000.0/timeMultiplier));


			// Automatically set cars to arrive at west end
			if ( (random.nextInt(60) + 1) <= CARS_PER_MINUTE_WE )
			{
				statsCarsWaitingWE++;
			}


			if( !carWaiting[WEST_END] && !carSensed[WEST_END] && statsCarsWaitingWE > 0)
			{

				carWaiting[WEST_END] = true;
				carSensed[WEST_END] = true;
				sensorTriggered[WEST_END] = false;
				westWaitPos[0].setVisible(true);
				westWaitPos[1].setVisible(true);
			}
			

			// Automatically set cars to arrive at east end
			if ( (random.nextInt(60) + 1) <= CARS_PER_MINUTE_EW )
			{
				statsCarsWaitingEW++;
			}


			if( !carWaiting[EAST_END] && !carSensed[EAST_END] && statsCarsWaitingEW > 0)
			{

				carWaiting[EAST_END] = true;
				carSensed[EAST_END] = true;
				sensorTriggered[EAST_END] = false;
				eastWaitPos[westWaitPos.length - 1].setVisible(true);
				eastWaitPos[westWaitPos.length - 2].setVisible(true);
			}
			
			
			
			if (directionWE)
			{
                // direction of flow is west-to-east
                
				// move any cars currently on bridge
				for (int i = MAX_CARS - 2; i >= 0; i--)
				{
					carPresent[i + 1] = carPresent[i];
				}
                carPresent[0] = false;
                
				if (westGreen && carWaiting[WEST_END])
				{
					// move car onto bridge
                    carWaiting[WEST_END] = false;
                    westWaitPos[0].setVisible(false);
                    westWaitPos[1].setVisible(false);
                    carPresent[0] = true;
                    carPresent[1] = true;
   			
                    statsCarsGoneWE++;
                    statsCarsWaitingWE--;
				}
                
                if (carSensed[WEST_END]
                && (!carWaiting[WEST_END])
                && (!carPresent[0])
                && (!carPresent[1])
                )
                {
                    //car has just moved out of sensor range
                    carSensed[WEST_END] = false;
                    //westButton.setEnabled(true);
				}
			}
			else
			{
                // direction of flow is east-to-west
                
				// move any cars currently on bridge
				for (int i = 0; i <= MAX_CARS - 2; i++)
				{
					carPresent[i] = carPresent[i + 1];
				}
                carPresent[MAX_CARS - 1] = false;
                
				if (eastGreen && carWaiting[EAST_END])
				{
                    // move car onto bridge
					carWaiting[EAST_END] = false;
                    eastWaitPos[eastWaitPos.length - 1].setVisible(false);
                    //carWaiting[EAST_END - 1] = false;
                    eastWaitPos[eastWaitPos.length - 2].setVisible(false);
                    carPresent[MAX_CARS - 1] = true;
                    carPresent[MAX_CARS - 2] = true;
                
                    FlowControl.carsGoneEW = true;
				
                    statsCarsGoneEW++;
                    statsCarsWaitingEW--;
				}
                
                if (carSensed[EAST_END]
                && (!carWaiting[EAST_END])
                && (!carPresent[MAX_CARS - 1])
                && (!carPresent[MAX_CARS - 2]))
                {
                    //car has just moved out of sensor range
                    carSensed[EAST_END] = false;
                    //eastButton.setEnabled(true);
				}
			}
			
			// display cars in new positions
			for (int i = 0; i < MAX_CARS; i++)
			{
				carPos[i].setVisible(carPresent[i]);
			}
		
			// Update stats display
			statsSeconds++;
			
			
			statsCarsWaitingWEAverage = ((double)statsCarsWaitingWE + (statsCarsWaitingWEAverage * (double)(statsSeconds - 1))) / (double)statsSeconds; 
			statsCarsWaitingEWAverage = ((double)statsCarsWaitingEW + (statsCarsWaitingEWAverage * (double)(statsSeconds - 1))) / (double)statsSeconds; 
			
			displayCarsWaitingWE.setText(String.valueOf(statsCarsWaitingWE));
			displayCrashes.setText(String.valueOf(statsCrashes));
			displayCarsGoneWE.setText(String.valueOf(statsCarsGoneWE));
			displayCarsGoneEW.setText(String.valueOf(statsCarsGoneEW));
			displaySeconds.setText(String.valueOf(statsSeconds));
			displayCarsWaitingEW.setText(String.valueOf(statsCarsWaitingEW));
			displayCarsGoneTotal.setText(String.valueOf(statsCarsGoneEW + statsCarsGoneWE));
			displayCarsWaitingWEAverage.setText(String.format("%.2f", statsCarsWaitingWEAverage));
			displayCarsWaitingEWAverage.setText(String.format("%.2f", statsCarsWaitingEWAverage));
			
		}
	
		while(true);
	
	} //main
	
//	private class ButtonHandler implements ActionListener
//	{
//		public void actionPerformed(ActionEvent e)
//		{
//			String end = e.getActionCommand();
//			if (end.equals("East"))
//			{
//				eastButton.setEnabled(false);
//                carWaiting[EAST_END] = true;
//                carSensed[EAST_END] = true;
//                //eastCars++;
//                //System.err.println(eastCars);
//                
//                //carWaiting[EAST_END - 1] = true;
//                //carSensed[EAST_END - 1] = true;
//                sensorTriggered[EAST_END] = false;
//                eastWaitPos[eastWaitPos.length - 1].setVisible(true);
//                eastWaitPos[eastWaitPos.length - 2].setVisible(true);
//			}
//			else
//			{
//				westButton.setEnabled(false);
//                carWaiting[WEST_END] = true;
//                carSensed[WEST_END] = true;
//                sensorTriggered[WEST_END] = false;
//                westWaitPos[0].setVisible(true);
//                westWaitPos[1].setVisible(true);
//			}
//		}
//	} //ButtonHandler
 
 	public static void turnLamp(int end, int lampID, boolean turnOn)
	{
		//lampID is 1, 2, 4 for red, amber, green
		int myLamp = lampID - 1;
		if (end == EAST_END)
		{
			if ((myLamp == 0) || (myLamp == 1))
			{
				eastLight[myLamp].setVisible(turnOn);
			}
			else
			{
				eastLight[2].setVisible(turnOn);
				eastGreen = turnOn;
				if (turnOn)
				{
					// green lamp on
					directionWE = false;
				
					//Check if there are any cars still on the road (potential crashes)
					if( carsOnBridge() ) statsCrashes++;
				}
			}
		}
		else
		{
			if ((myLamp == 0) || (myLamp == 1))
			{
				westLight[myLamp].setVisible(turnOn);
			}
			else
			{
				westLight[2].setVisible(turnOn);
				westGreen = turnOn;
				if (turnOn)
				{
					// green lamp on
					directionWE = true;
					
					//Check if there are any cars still on the road (potential crashes)
					if( carsOnBridge() ) statsCrashes++;
				}
			}
		}
	} //turnLamp
 
 	public static boolean checkWaiting(int end)
	{
        boolean firstSensing;
        
        if (carSensed[end])
        {
            //only return true the first time the car is sensed
            firstSensing = !sensorTriggered[end];
            sensorTriggered[end] = true;
            return firstSensing;
        }
        else
            return false;
	} //checkWaiting
 	
 	public static boolean carsOnBridge()
 	{
 		for( boolean position : carPresent )
 		{
 			if( position == true ) return true;
 		}
 	
 		return false;
 	}
 	
} //TB
