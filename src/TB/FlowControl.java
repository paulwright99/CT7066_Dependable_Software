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
	// State model is being used for this run 

	public static StateModel currentStateModel = StateModel.STATEMODEL_NEW;
	
	public enum StateModel
	{
		STATEMODEL_OLD,
		STATEMODEL_NEW;
	}
	
	
	// State attributes
	public int oldState;
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
    
	
	// New state model timer constants
	public static final int PRIORITY_INTERVAL_EW = 5;
	public static final int NORMAL_INTERVAL_EW = 1;
	public static final int NORMAL_INTERVAL_WE = 5;
	
	// New state model count constants
	protected static final int PRIORITY_INTERVAL_NUM = 4;
	

	// Old state model state constants
	protected static final int FLOW_OUT_MIN = 1;
	protected static final int FLOW_OUT = 2;
	protected static final int STOP_OUT = 3;
	protected static final int FLOW_IN = 4;
	protected static final int STOP_IN = 5;

	
	// Old state model timer constraints
	
	public static final int TIME_CLEAR = 14;
	protected static final int MIN_EW = 20;
	protected static final int MAX_WAIT = 10;
	protected static final int TIME_ONE_SECOND = 1;
	
	
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
		if( currentStateModel == StateModel.STATEMODEL_NEW )
		{
			state = stopIn; //initial state
		}
		else
		{
			oldState = STOP_IN;
		}
		
		startTimer(TIME_CLEAR); //initial timer
		carsGoneEW = false;
		intervalsDone = 0;
	} //startRunning
	
    /**
	  performs timed actions according to State Diagram
    */
    public void timeout()
    {
    	if( currentStateModel == StateModel.STATEMODEL_NEW )
    	{
    		state.timeout();
    	}
    	else
    	{
    		switch (oldState)
    		{
    		case FLOW_OUT_MIN:
    			startTimer(TIME_ONE_SECOND);
    			oldState = FLOW_OUT;
    			break;
    		case FLOW_OUT:
    			if (westVehicleSensor.vehicleSensed())
    			{
    				// guard is true
    				startTimer(TIME_CLEAR);
    				eastTrafficLight.turnToRed();
    				oldState = STOP_OUT;
    			}
    			else
    			{
    				// continue in current state
    				startTimer(TIME_ONE_SECOND);
    			}
    			break;
    		case STOP_OUT:
    			startTimer(MAX_WAIT);
    			westTrafficLight.turnToGreen();
    			oldState = FLOW_IN;
    			break;
    		case FLOW_IN:
    			startTimer(TIME_CLEAR);
    			westTrafficLight.turnToRed();
    			oldState = STOP_IN;
    			break;
    		case STOP_IN:
    			startTimer(MIN_EW);
    			eastTrafficLight.turnToGreen();
    			oldState = FLOW_OUT_MIN;
    			break;
    		}
    	}


    } //timeout
}
