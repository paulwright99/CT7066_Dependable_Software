package TB;

/**
  Traffic Light<p>
  Implements standard British traffic light sequences
  @author Peter Annesley
  @version 1.3 November 2005
*/
public class TrafficLight extends TrafficLightAbs
{
	// TrafficLight attributes
	protected int state;
	
	// state constants
	protected static final int SHOWING_RED = 1;
	protected static final int TURNING_GREEN = 2;
	protected static final int SHOWING_GREEN = 3;
	protected static final int TURNING_RED = 4;
	
	//timer constants
	public static final int TIME_CHANGE = 1;

	/**
	  Constructor for TrafficLight
	  @param positionIn indicates which end of the bridge
	*/
	public TrafficLight(int positionIn)
	{
		super(positionIn);
	} //TrafficLight
	
    /**
      performs actions when the thread is started
	*/
    protected void startRunning()
	{
		state = SHOWING_RED; //initial state
		show(RED); //initial action
	} //startRunning

    /**
      turn traffic light to green
	*/
	public void turnToGreen()
	{
		if (state == SHOWING_RED)
		{
			show(RED + AMBER);
			startTimer(TIME_CHANGE);
			state = TURNING_GREEN;
		}
	} //turnToGreen

	/**
      turn traffic light to red
    */
	public void turnToRed()
	{
		if (state == SHOWING_GREEN)
		{
			show(AMBER);
			startTimer(TIME_CHANGE);
			state = TURNING_RED;
		}
	} //turnToRed

    /**
	  performs timed actions according to State Diagram
    */
	protected void timeout()
	{
		switch (state)
		{
			case TURNING_GREEN:
				show(GREEN);
				state = SHOWING_GREEN;
				break;
			case TURNING_RED:
				show(RED);
				state = SHOWING_RED;
				break;
		}
	} //timeout
} //TrafficLight

