package TB;

public class FrenchTrafficLight extends TrafficLight
{
	public FrenchTrafficLight(int positionIn)
	{
		super(positionIn);
	}
	
    /**
    turn traffic light to green
	*/
	@Override
	public void turnToGreen()
	{
		if (state == SHOWING_RED)
		{
			show(GREEN);
			state = SHOWING_GREEN;
		}
	} //turnToGreen
	
    /**
	  performs timed actions according to State Diagram
    */
	@Override
	protected void timeout()
	{
		switch (state)
		{
			case TURNING_RED:
				show(RED);
				state = SHOWING_RED;
				break;
		}
	} //timeout
}
