package TB;

/**
  Tewkester Bridge configuration system
  @author Peter Annesley
  @version 1.1 February 2006
*/
public class TBconfiguration
{
    //constants
	private static final int WEST_END = 0;
	private static final int EAST_END = 1;
		
 	public static void main(String args[]) throws InterruptedException
	{
        FlowControl myFlowControl;
        TrafficLight eastTrafficLight;
        TrafficLight westTrafficLight;
	    VehicleSensor eastVehicleSensor;
	    VehicleSensor westVehicleSensor;
  
		// create the Graphical User Interface      
		TB myTB = new TB();

		// create objects needed for this system
		eastTrafficLight = new FrenchTrafficLight(EAST_END);
		westTrafficLight = new FrenchTrafficLight(WEST_END);
		eastVehicleSensor = new VehicleSensor(EAST_END);
		westVehicleSensor = new VehicleSensor(WEST_END);
		myFlowControl = new FlowControl(
			eastTrafficLight, westTrafficLight,
			eastVehicleSensor, westVehicleSensor);

		// start the threads in this system
		myFlowControl.start();
		eastTrafficLight.start();
		westTrafficLight.start();

		// start the GUI animation
		myTB.startAnimation();
	} //main
} //TBconfiguration
