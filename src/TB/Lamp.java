package TB;

/**
  Lamp for Traffic Light 
  @author Peter Annesley
  @version 1.2 February 2005
*/
public class Lamp 
{
	// Lamp attributes
	private int myEnd;
	private int id;

	/**
	  Constructor for Lamp
	  @param end indicates which end of the bridge
	  @param lampID indicates which colour lamp
	*/
	public Lamp(int end, int lampID)
	{
		myEnd = end;
		id = lampID;
	} //Lamp
	
    /**
	  turn lamp on
    */
	public void turnOn()  
	{
		TB.turnLamp(myEnd, id, true);
	} //turnOn
	
    /**
	  turn lamp off
    */
	public void turnOff()  
	{
		TB.turnLamp(myEnd, id, false);
	} //turnOff

} //Lamp

