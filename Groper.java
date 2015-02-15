import java.awt.Color;
import java.util.List;

/**
 * A simple model of a blue groper.
 * Groper age, move, breed, and die.
 * They eat herring.
 * 
 * @author Richard Jones, Michael Kolling and Matthew Maglennon
 */
public class Groper extends Fish
{	
	//Static final groper stats
	private static final double BREEDING_PROBABILITY = 0.03; //The likelihood of a groper breeding in a turn
	private static final int MAX_LITTER_SIZE = 3; //The maximum litter size of a groper
    private static final int AVERAGE_MAX_AGE = 100; //The average maximum age of a Groper
	private static final int BREEDING_AGE = 5; //The minimum age a groper must be to breed
    private static final int STAMINA = 100; //The maximum hunger level a groper can achieve before death
	
	//Static variables 
    private static int MAX_AGE = 100; //The maximum age of an instance of groper, default is 100
    private static Color COLOUR = Color.red; //Set the colour of all groper
	
    //Public static variables
	public static boolean REPORT_DINNER = false; //Determines if a groper reports its dinner to the console
	
	/**
	 * Groper constructor, only to be called by Fish class
	 */
	protected Groper()
	{
	}
	
	/**
	 * @param randomAge Boolean sets random age if true.
	 * @param location The location of this fish.
	 */
    public Groper(Boolean randomAge, Location location)
	{
		super(randomAge, location);
	}

    /**
     * Groper only eat herring and will search their surroundings for them.
     * Once a herring is found it is eaten and its location is returned.
     * @param surroundings List of locations around the groper
     */
	protected Location findLocationOfFood(List<Location> surroundings)
	{
    	Location locationOfDinner = null;
    	for(Location lookingAt : surroundings)
		{
			Fish dinner = currentOcean.getOceanSquareAtLocation(lookingAt).getFish();
			if(dinner != null && dinner instanceof Herring)
			{
				locationOfDinner = dinner.getLocation();
				if(REPORT_DINNER) thoughts += "Yay" + dinner.toString() + " for dinner.";
				eat(dinner);
				return locationOfDinner;
			}
		}
		return locationOfDinner;
	}
    
	/**
	 * Will kill a fish and decrease hunger.
	 * Will add actions to thoughts if REPORT_DINNER is true.
	 * @param dinner The fish to be eaten.
	 */
    private void eat(Fish dinner)
    {
    	super.decreaseHunger(50);
		dinner.die(ReasonForDeath.EATEN);
		if(REPORT_DINNER) thoughts += "I have eaten" + dinner.toString() + " My hunger is now " + getHunger() + "\n";
    }
    
    /**
     * Gropers will just move to the first free location in their surroundings
     * @param surroundings List of locations around the groper
     */
    protected Location chooseNextLocation(List<Location> surroundings)
	{
		return updatedOcean.freeAdjacentLocation(this.location);
	}
    
    /**
     * @return Returns breeding probability of gropers
     */
    protected double getBreedingProbability()
	{
		return BREEDING_PROBABILITY;
	}
	
    /**
     * @return Returns max litter size of gropers
     */
	protected int getMaxLitterSize()
	{
		return MAX_LITTER_SIZE;
	}
	
	/**
     * @return Returns maximum age of this groper
     */
	protected int getMaxAge()
	{
		return MAX_AGE;
	}
	
	/**
	 * Sets the maximum age of this groper
	 * @param age Maximum age
	 */
	protected void setMaxAge(int age)
	{
		MAX_AGE = age;
	}
	
	/**
     * @return Returns average maximum age of gropers
     */
	protected int getAverageMaxAge()
	{
		return AVERAGE_MAX_AGE;
	}
	
	/**
     * @return Returns minimum breeding age of gropers
     */
	protected int getBreedingAge()
	{
		return BREEDING_AGE;
	}
	
	/**
     * @return Returns stamina of gropers
     */
	protected int getStamina()
	{
		return STAMINA;
	}
    
	/**
     * @return Returns the colour of all gropers
     */
    public Color getColour()
    {
    	return COLOUR;
    }
    
    /**
     * Sets the colour of all groper objects
     * @param colour The colour of all gropers
     */
    static public void setColour(Color colour)
    {
    	COLOUR = colour;
    }
}
