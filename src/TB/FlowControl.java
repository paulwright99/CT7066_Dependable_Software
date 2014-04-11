package TB;



/**
  Controls the flow of traffic over the bridge
  
  This version uses the following strategy for satisfying the 3 requirements<p>
  
  Req 1: "It must operate safely"<p>
  When traffic is stopped flowing in one direction,
  it waits TIME_CLEAR seconds to allow traffic to clear off the bridge,
  before traffic is allowed to flow in the opposite direction.<p>
  
  Req 2: "It must make efficient use of the bridge"<p>
  Traffic is not allowed to flow into town,
  unless a vehicle is sensed wanting to travel into town.<p>
  
  Req 3: "It must give precedence to traffic leaving town"<p>
  (a) Traffic is allowed to flow out of town for at least MIN_EW seconds<p>
  (b) Traffic wanting to travel out of town,
  should not have to wait for more than MAX_WAIT seconds

  @author Peter Annesley
  @version 1.3 February 2006
  

  
*/
public class FlowControl extends FlowControlAbs
{
	// FlowControl attributes
	protected int state;
	protected TrafficLight eastTrafficLight;
	protected TrafficLight westTrafficLight;
	protected VehicleSensor eastVehicleSensor;
	protected VehicleSensor westVehicleSensor;
	
	public static boolean carsGoneEW;
	protected int intervalsDone;
	


	protected static final int WEST_END = 0;
	protected static final int EAST_END = 1;
    
	// state constants
	protected static final int FLOW_OUT_PRIORITY = 1;
	protected static final int FLOW_OUT = 2;
	protected static final int STOP_OUT = 3;
	protected static final int FLOW_IN = 4;
	protected static final int STOP_IN = 5;
	
	//timer constants
	protected static final int PRIORITY_INTERVAL_EW = 5;
	protected static final int NORMAL_INTERVAL_EW = 1;
	protected static final int NORMAL_INTERVAL_WE = 5;
	protected static final int TIME_CLEAR = 12;
	
	
	// count constants
	protected static final int PRIORITY_INTERVAL_NUM = 4;
	
	/**
	  Constructor for FlowControl
	*/
	public FlowControl(TrafficLight eastTL, TrafficLight westTL,
			VehicleSensor eastVS, VehicleSensor westVS)
	{
		eastTrafficLight = eastTL;
		westTrafficLight = westTL;
		eastVehicleSensor = eastVS;
		westVehicleSensor = westVS;
	} //FlowControl
	
    /**
      performs actions when the thread is started
	*/
    protected void startRunning()
	{
		state = STOP_IN; //initial state
		startTimer(TIME_CLEAR); //initial timer
		carsGoneEW = false;
		intervalsDone = 0;
	} //startRunning
	
    /**
	  performs timed actions according to State Diagram
    */
	protected void timeout()
	{
				
		switch (state)
		{
			case FLOW_OUT_PRIORITY:
				
				if(!carsGoneEW && westVehicleSensor.vehicleSensed())
				{
					// Change traffic flow from east to west
					startTimer(TIME_CLEAR);
					eastTrafficLight.turnToRed();
					state = STOP_OUT;
				}
				else if(intervalsDone >= PRIORITY_INTERVAL_NUM) 
				{
					// Change from priority flow to normal flow
					startTimer(NORMAL_INTERVAL_EW);
					state = FLOW_OUT;
				}
				else
				{
					intervalsDone++;
					startTimer(PRIORITY_INTERVAL_EW);
					carsGoneEW = false;
				}

				break;
			case FLOW_OUT:
				if (westVehicleSensor.vehicleSensed())
				{
					// guard is true
					startTimer(TIME_CLEAR);
					eastTrafficLight.turnToRed();
					state = STOP_OUT;
				}
				else
				{
					// continue in current state
					startTimer(NORMAL_INTERVAL_EW);
				}
				break;
			case STOP_OUT:
				startTimer(TrafficLight.TIME_CHANGE + NORMAL_INTERVAL_WE);
				westTrafficLight.turnToGreen();
				state = FLOW_IN;
				break;
			case FLOW_IN:
				
				if( eastVehicleSensor.vehicleSensed() )
				{
					//Change flow from west to east
					startTimer(TIME_CLEAR);
					westTrafficLight.turnToRed();
					state = STOP_IN;
				}
				else
				{
					startTimer(NORMAL_INTERVAL_WE);
				}
				break;
			case STOP_IN:
				startTimer(TrafficLight.TIME_CHANGE + PRIORITY_INTERVAL_EW);
				eastTrafficLight.turnToGreen();
				state = FLOW_OUT_PRIORITY;
				intervalsDone = 1;
				carsGoneEW = false;
				break;
		}
	} //timeout
}
