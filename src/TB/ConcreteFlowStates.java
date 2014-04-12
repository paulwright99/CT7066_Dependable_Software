package TB;

class FlowOutPriority implements FlowState
{
	FlowControl flowControl;
	
	public FlowOutPriority(FlowControl flowControl)
	{
		this.flowControl = flowControl;
	}
	
	public void timeout()
	{
		if(!FlowControl.carsGoneEW && flowControl.westVehicleSensor.vehicleSensed())
		{
			// Change traffic flow from east to west
			flowControl.startTimer(FlowControl.TIME_CLEAR);
			flowControl.eastTrafficLight.turnToRed();
			flowControl.state = flowControl.stopOut;
		}
		else if(flowControl.intervalsDone >= FlowControl.PRIORITY_INTERVAL_NUM) 
		{
			// Change from priority flow to normal flow
			flowControl.startTimer(FlowControl.NORMAL_INTERVAL_EW);
			flowControl.state = flowControl.flowOut;
		}
		else
		{
			flowControl.intervalsDone++;
			flowControl.startTimer(FlowControl.PRIORITY_INTERVAL_EW);
			FlowControl.carsGoneEW = false;
		}	
	}
}

class FlowOut implements FlowState
{
	FlowControl flowControl;
	
	public FlowOut(FlowControl flowControl)
	{
		this.flowControl = flowControl;
	}

	
	public void timeout()
	{
		if (flowControl.westVehicleSensor.vehicleSensed())
		{
			// guard is true
			flowControl.startTimer(FlowControl.TIME_CLEAR);
			flowControl.eastTrafficLight.turnToRed();
			flowControl.state = flowControl.stopOut;
		}
		else
		{
			// continue in current state
			flowControl.startTimer(FlowControl.NORMAL_INTERVAL_EW);
		}
	}
}

class StopOut implements FlowState
{
	FlowControl flowControl;
	
	public StopOut(FlowControl flowControl)
	{
		this.flowControl = flowControl;
	}

	
	public void timeout()
	{
		flowControl.startTimer(TrafficLight.TIME_CHANGE + FlowControl.NORMAL_INTERVAL_WE);
		flowControl.westTrafficLight.turnToGreen();
		flowControl.state = flowControl.flowIn;
	}
}

class FlowIn implements FlowState
{
	FlowControl flowControl;
	
	public FlowIn(FlowControl flowControl)
	{
		this.flowControl = flowControl;
	}
	
	public void timeout()
	{
		if( flowControl.eastVehicleSensor.vehicleSensed() )
		{
			//Change flow from west to east
			flowControl.startTimer(FlowControl.TIME_CLEAR);
			flowControl.westTrafficLight.turnToRed();
			flowControl.state = flowControl.stopIn;
		}
		else
		{
			flowControl.startTimer(FlowControl.NORMAL_INTERVAL_WE);
		}
	}
}

class StopIn implements FlowState
{
	FlowControl flowControl;
	
	public StopIn(FlowControl flowControl)
	{
		this.flowControl = flowControl;
	}

	
	public void timeout()
	{
		flowControl.startTimer(TrafficLight.TIME_CHANGE + FlowControl.PRIORITY_INTERVAL_EW);
		flowControl.eastTrafficLight.turnToGreen();
		flowControl.state = flowControl.flowOutPriority;
		flowControl.intervalsDone = 1;
		FlowControl.carsGoneEW = false;
	}
}