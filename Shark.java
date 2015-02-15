import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple model of a shark.
 * Sharks age, move, breed, and die.
 * Sharks eat groper or herring but they prefer groper.
 * Sharks are loners - they prefer not to swim next to each other
 * @author Richard Jones, Michael Kolling and Matthew Maglennon
 */
public class Shark extends Fish
{	
	//Static final shark stats
	private static final double BREEDING_PROBABILITY = 0.009; //The likelihood of a shark breeding in a turn
	private static final int MAX_LITTER_SIZE = 2; //The maximum litter size of a shark
    private static final int AVERAGE_MAX_AGE = 150; //The average maximum age of a shark
	private static final int BREEDING_AGE = 8; //The minimum age a shark must be to breed
    private static final int STAMINA = 300; //The maximum hunger level a shark can achieve before death
	
    //Static variables 
    private static int MAX_AGE = 150; //The maximum age of an instance of shark, default is 50
    private static Color COLOUR = Color.blue; //Set the colour of all shark
    
    //Public static variables
	public static boolean REPORT_DINNER = false; //Determines if a groper reports its dinner to the console	
	public static boolean enableSharkAvoidance = true; //Determines if a shark will seek to avoid other sharks
	
	/**
	 * Shark constructor, only to be called by Fish class
	 */
	protected Shark()
	{
	}
    
	/**
	 * @param randomAge Boolean sets random age if true.
	 * @param location The location of this fish.
	 */
    public Shark(Boolean randomAge, Location location)
	{
		super(randomAge, location);
	}

    /**
     * A shark analyses its surroundings looking for herring or groper. It will replace a herring it finds with a groper if one is found later.
     * @param surroundings List of locations around shark
     * @return Will return location of preferred fish if found, else will return null; 
     */
	protected Location findLocationOfFood(List<Location> surroundings)
	{
    	Fish dinner = null;
    	Location locationOfDinner = null;
		for(Location lookingAt : surroundings)
		{
			Fish potentialDinner = currentOcean.getOceanSquareAtLocation(lookingAt).getFish();
			if(potentialDinner != null && (potentialDinner instanceof Herring || potentialDinner instanceof Groper))
			{
				if(dinner == null)
				{
					if(REPORT_DINNER) thoughts += "Yay" + potentialDinner.toString() + " for dinner.";
					dinner = potentialDinner;
				}
				else
				{
					if(dinner instanceof Herring && potentialDinner instanceof Groper)
					{
						if(REPORT_DINNER) thoughts += " Wait a minute! I'd prefer" + potentialDinner.toString() + " for dinner. ";
						dinner = potentialDinner;
					}
				}
			}
		}
		if(dinner != null)
		{
			eat(dinner);
			locationOfDinner = dinner.getLocation();
		}
		return locationOfDinner;
	}
    
	/**
	 * Will kill a fish and decrease hunger based on type of fish eaten.
	 * Will add actions to thoughts if REPORT_DINNER is true.
	 * @param dinner The fish to be eaten.
	 */
    private void eat(Fish dinner)
    {
    	if(dinner instanceof Herring) decreaseHunger(25);
    	else if(dinner instanceof Groper) decreaseHunger(50);
		dinner.die(ReasonForDeath.EATEN);
		if(REPORT_DINNER) thoughts += "I have eaten" + dinner.toString() + " My hunger is now " + getHunger() + "\n";
    }
    
    /**
     * If sharks are told to avoid other sharks they will look at empty location in their surroundings
     * and preference each one based on how far away they are from other sharks in their surroundings
     * @param surroundings List of locations around shark
     * @return Returns preferred location, either based on shark avoidance or defaulting to a free adjacent location. Can be null if no free locations.
     */
    protected Location chooseNextLocation(List<Location> surroundings)
	{
		Location preferredLocation = updatedOcean.freeAdjacentLocation(this.location);
		
		if(enableSharkAvoidance)
		{
			//Creates lists of nearby sharks and empty locations
			List<Location> locationOfSharks = new ArrayList<Location>();
			List<Location> emptyLocations = new ArrayList<Location>();
			
			for(Location lookingAt : surroundings)
			{
				Fish f = updatedOcean.getFishAt(lookingAt);
				if(f != null)
				{
					if(f instanceof Shark)
					{
						locationOfSharks.add(f.getLocation());
					}
				}
				else if(updatedOcean.getOceanSquareAtLocation(lookingAt).isEmpty())
				{
					emptyLocations.add(lookingAt);
				}
			}
			//If there are sharks nearby
			if(locationOfSharks.size() > 0)
			{
				int mostPreferrabled = 0;
				for(Location emptyLocation : emptyLocations)
				{
					//Determines preferability of a free square based on all the sharks nearby
					int preferability = 0;
					for(Location locationOfShark : locationOfSharks)
					{
						
						preferability = Math.abs(locationOfShark.getCol() - emptyLocation.getCol());
						preferability += Math.abs(locationOfShark.getRow() - emptyLocation.getRow());
						if(preferability > mostPreferrabled)
						{
							preferredLocation = emptyLocation;
							mostPreferrabled = preferability;
						}
					}
				}
			}
		}
		return preferredLocation;
	}
    
    /**
     * @return Returns breeding probability of sharks
     */
    protected double getBreedingProbability()
	{
		return BREEDING_PROBABILITY;
	}
	
    /**
     * @return Returns max litter size of sharks
     */
	protected int getMaxLitterSize()
	{
		return MAX_LITTER_SIZE;
	}
	
	/**
     * @return Returns maximum age of this shark
     */
	protected int getMaxAge()
	{
		return MAX_AGE;
	}
	
	/**
	 * Sets the maximum age of this shark
	 * @param age Maximum age
	 */
	protected void setMaxAge(int age)
	{
		MAX_AGE = age;
	}
	
	/**
     * @return Returns average maximum age of sharks
     */
	protected int getAverageMaxAge()
	{
		return AVERAGE_MAX_AGE;
	}
	
	/**
     * @return Returns minimum breeding age of sharks
     */
	protected int getBreedingAge()
	{
		return BREEDING_AGE;
	}
	
	/**
     * @return Returns stamina of sharks
     */
	protected int getStamina()
	{
		return STAMINA;
	}
    
	/**
     * @return Returns the colour of all sharks
     */
    public Color getColour()
    {
    	return COLOUR;
    }
    
    /**
     * Sets the colour of all shark objects
     * @param colour The colour of all sharks
     */
    static public void setColour(Color colour)
    {
    	COLOUR = colour;
    }
}
