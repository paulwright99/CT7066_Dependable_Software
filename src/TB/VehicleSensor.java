package TB;

/**
  VehicleSensor 
  @author Peter Annesley
  @version 1.2 February 2005
*/
public class VehicleSensor 
{
	// VehicleSensor attributes
	private int myEnd;

	/**
	  Constructor for VehicleSensor
	  @param end indicates which end of the bridge
	*/
	public VehicleSensor(int end)
	{
		myEnd = end;
	} //FlowControl
	
	/**
      check if vehicle is waiting
	  @return indication of whether a vehicle is currently being sensed<p>
      This has "one-shot" operation<p>
      Returns true the first time a vehicle is sensed<p>
      Then returns false until<p>
      (a) that vehicle is no longer sensed and<p>
      (b) subsequently another vehicle is sensed
    */
	public boolean vehicleSensed()  
	{
		return TB.checkWaiting(myEnd);
	} //vehicleSensed

} //VehicleSensor

