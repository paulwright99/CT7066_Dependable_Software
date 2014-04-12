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
public class FlowControl extends FlowControlAbs implements FlowState
{
	// State attributes
	public FlowState state;
	public FlowState flowOutPriority;
	public FlowState flowOut;
	public FlowState stopOut;
	public FlowState flowIn;
	public FlowState stopIn;
	
	
	public TrafficLight eastTrafficLight;
	public TrafficLight westTrafficLight;
	protected VehicleSensor eastVehicleSensor;
	protected VehicleSensor westVehicleSensor;
	
	public static boolean carsGoneEW;
	public int intervalsDone;
	

	protected static final int WEST_END = 0;
	protected static final int EAST_END = 1;
    
	
	//timer constants
	public static final int PRIORITY_INTERVAL_EW = 5;
	public static final int NORMAL_INTERVAL_EW = 1;
	public static final int NORMAL_INTERVAL_WE = 5;
	public static final int TIME_CLEAR = 12;
	
	
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

		flowOutPriority = new FlowOutPriority(this);
		flowOut = new FlowOut(this);
		stopOut = new StopOut(this);
		flowIn = new FlowIn(this);
		stopIn = new StopIn(this);
	
	} //FlowControl
	
    /**
      performs actions when the thread is started
	*/
    protected void startRunning()
	{
		state = stopIn; //initial state
		startTimer(TIME_CLEAR); //initial timer
		carsGoneEW = false;
		intervalsDone = 0;
	} //startRunning
	
    /**
	  performs timed actions according to State Diagram
    */
	public void timeout()
	{
		state.timeout();
	} //timeout
}
