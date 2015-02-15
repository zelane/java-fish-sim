import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple model of a herring.
 * Herrings age, move, breed, and die.
 * They eat plankton.
 * They exhibit flocking behaviour - they tend to seek company. 
 * 
 * @author Richard Jones, Michael Kolling and Matthew Maglennon
 */
public class Herring extends Fish
{
	//Static final herring stats
	private static final double BREEDING_PROBABILITY = 0.1; //The likelihood of a herring breeding in a turn
	private static final int MAX_LITTER_SIZE = 4; //The maximum litter size of a herring
    private static final int AVERAGE_MAX_AGE = 50; //The average maximum age of a herring
	private static final int BREEDING_AGE = 5; //The minimum age a herring must be to breed
    private static final int STAMINA = 50; //The maximum hunger level a herring can achieve before death
    
    //Static variables
    private static Color COLOUR = Color.green; //Set the colour of all Herring
    
    //Public static variables
    //Could Implement get/set
    public static boolean enableGrouping = true; //Enables herring to aim to flock 
    public static boolean enableDirectionMaintenance = true; //Enables herring to prefer maintaining direction
    
    //Private global variables
    private int MAX_AGE = 50; //The maximum age of an instance of herring, default is 50
    private Location toFollow = null; //The location of a herring that was nearby and has moved
    private Location lastTranslation = null; //A translation location based on the herring last move
    
    /**
	 * Herring constructor, only to be called by Fish class
	 */
    protected Herring()
	{
	}
    
    /**
	 * @param randomAge Boolean sets random age if true.
	 * @param location The location of this fish.
	 */
    public Herring(Boolean randomAge, Location location)
	{
		super(randomAge, location);
	}

    /**
     * Herring feed from the plankton level of their current square.
     * They always return null.
     */
	protected Location findLocationOfFood(List<Location> surroundings)
	{
    	OceanSquare currentSquare = currentOcean.getOceanSquareAtLocation(location);
    	OceanSquare futureSquare = updatedOcean.getOceanSquareAtLocation(location);
    	if(currentSquare.getPlanktonLevel() >= 2)
    	{
    		currentSquare.reducePlankton(2);
    		futureSquare.reducePlankton(2);
    		decreaseHunger(30);
    	}
    	return null;
	}
	
	/**
	 * Sets the location a herring should aim to follow
	 * @param toFollow The location to follow
	 */
	public void setFollowLocation(Location toFollow)
	{
		//if(this.toFollow == null)
			this.toFollow = toFollow;
	}
	
	/**
	 * Herring chose where they would most like to move to, they prefer to follow a leader to maintaining direction.
	 * A leader will already be maintaining direction
     * @param surroundings List of locations around the herring
	 * @return Returns the established best next location, either closest to a leader, maintaining direct, any other free adjacent location or null if none are free.
	 */
	protected Location chooseNextLocation(List<Location> surroundings)
	{
		Location nextLocation = updatedOcean.freeAdjacentLocation(this.location);
		
		if(enableGrouping && toFollow != null)
		{
			Location closestToLeader = calculateNearestToLeader(surroundings, toFollow);
			if(closestToLeader != null)
				nextLocation = closestToLeader;
		}
		else if(enableDirectionMaintenance && lastTranslation != null)
		{
			Location directLocation = this.location.translate(lastTranslation);
			if(directLocation != null && updatedOcean.isLocationEmpty(directLocation))
					nextLocation = directLocation;
		}
		
		//If the herring finds a location to move to it informs nearby herring of its future location and calculates what transformation it will take to get there.
		if(nextLocation != null)
		{
			informNearbyHerring(surroundings, nextLocation);
			lastTranslation = this.location.calculateTranslation(nextLocation);
		}
		//return updatedOcean.freeAdjacentLocation(this.location);
		return nextLocation;
	}
	
	/**
	 * Calculates the nearest location to a given location, used to find a location nearest to a leader herring.
	 * It does this by cross comparing two lists of Locations, empty locations around this herring, and empty locations around the leader herring.
	 * @param surroundings List of locations around the herring.
	 * @param leadersLocation The location this herring aims to get closest to.
	 * @return Returns a free location around the leader if any, else returns null.
	 */
	private Location calculateNearestToLeader(List<Location> surroundings, Location leadersLocation)
	{
		List<Location> emptyLocationsAroundMe = new ArrayList<Location>();
		List<Location> emptyLocationsAroundLeader = new ArrayList<Location>();
		for(Location lookingAt : surroundings)
		{
			if(updatedOcean.isLocationEmpty(lookingAt))
			{
				emptyLocationsAroundMe.add(lookingAt);
			}
		}
		for(Location lookingAt : updatedOcean.adjacentLocations(leadersLocation))
		{
			if(updatedOcean.isLocationEmpty(lookingAt))
			{
				emptyLocationsAroundLeader.add(lookingAt);
			}
		}
		for(Location emptyLocationAroundMe : emptyLocationsAroundMe)
		{
			for(Location emptyLocationAroundLeader : emptyLocationsAroundLeader)
			{
				if(emptyLocationAroundMe.isEqualTo(emptyLocationAroundLeader))
				{
					return emptyLocationAroundMe;
				}
			}
		}
		return null;
	}
	
	/**
	 * Finds fellow herrings in this herrings surroundings and informs them of where it is going
	 * @param surroundings List of locations around the herring
	 * @param futureLocation Where this herring is moving to
	 */
	private void informNearbyHerring(List<Location> surroundings, Location futureLocation)
	{
		for(Location lookingAt : surroundings)
		{
			Fish f = currentOcean.getFishAt(lookingAt);
			if(f != null)
			{
				if(f instanceof Herring)
				{
					((Herring) f).setFollowLocation(futureLocation);
				}
			}
		}
	}
	    	
	 /**
     * @return Returns breeding probability of herrings
     */
    protected double getBreedingProbability()
	{
		return BREEDING_PROBABILITY;
	}
	
    /**
     * @return Returns max litter size of herrings
     */
	protected int getMaxLitterSize()
	{
		return MAX_LITTER_SIZE;
	}
	
	/**
     * @return Returns maximum age of this herring
     */
	protected int getMaxAge()
	{
		return MAX_AGE;
	}
	
	/**
	 * Sets the maximum age of this herring
	 * @param age Maximum age
	 */
	protected void setMaxAge(int age)
	{
		MAX_AGE = age;
	}
	
	/**
     * @return Returns average maximum age of herrings
     */
	protected int getAverageMaxAge()
	{
		return AVERAGE_MAX_AGE;
	}
	
	/**
     * @return Returns minimum breeding age of herrings
     */
	protected int getBreedingAge()
	{
		return BREEDING_AGE;
	}
	
	/**
     * @return Returns stamina of herrings
     */
	protected int getStamina()
	{
		return STAMINA;
	}
    
	/**
     * @return Returns the colour of all herrings
     */
    public Color getColour()
    {
    	return COLOUR;
    }
    
    /**
     * Sets the colour of all herring objects
     * @param colour The colour of all herrings
     */
    static public void setColour(Color colour)
    {
    	COLOUR = colour;
    }
}
