import java.util.List;
import java.util.Random;

/**
 * The abstract parent class for all other types of fish.
 * Stores variables common to all fish such as age and hunger and
 * establishes methods for shared behaviours, uses abstract methods where subtype specific behaviours are required.
 *  
 * @author Matthew Maglennon
 * @version 15/11/2010
 */
abstract public class Fish extends OceanObject implements Actor
{
	private static final Random random = new Random(); //Static final random object, used in various methods to generate random integers.
		
	//protected references
	protected Ocean currentOcean; //holds a reference to the ocean the fish currently exists in. Used for surroundings analysis etc.
	protected Ocean updatedOcean; //holds a reference to the ocean that is being updated. Used for fish to place themselves in after acting.

	//protected instance variables
	protected int age; //the age of the fish
	protected int hunger; //the hunger level of the fish
	protected String thoughts; //the fishes thoughts as a string
	protected Location location; //the fishes current location
	protected boolean isAlive = true; //whether the fish is alive or not

	/**
	 * Constructor for objects of class Fish. Performs construction applicable to all subclasses of fish.
	 * Only subclasses can call Fish constructors.
	 */
	protected Fish()
	{
		setRandomMaxAge();
	}
	
	/**
	 * @param randomAge Boolean to decided if fish has random age (used for initial ocean populating).
	 * @param location The location of the fish.
	 */
	protected Fish(Boolean randomAge, Location location)
	{
		setRandomMaxAge();
		if(randomAge)
		{			
			setAge(random.nextInt(getMaxAge()));
		}
		this.location = location;
	}
	
	/**
	 * Sets the age variable of a fish to the input.
	 * @param age Desired age of the fish.
	 */
	protected void setAge(int age)
	{
		this.age = age;
	}
	
	/**
	 * Gives the fish a random maximum age variable based on the average maximum age of its species.
	 * 80% to 120%
	 */
	protected void setRandomMaxAge()
	{
		float randomFloat = random.nextInt(4)+1;
		int randomMaxAge = Math.round(getAverageMaxAge() * ((randomFloat/10) + 0.8f));
		setMaxAge(randomMaxAge);
	}
	
	/**
	 * The fish is told to act. All fish perform the same types of actions.
	 * They increase in age, increase in hunger, attempt to breed and decided where to move.
	 * @param currentOcean Reference to the current ocean.
	 * @param updatedOcean Reference to the updated ocean.
	 * @param newFish A list of actors in which to store new born fish.
	 * @return Returns the fishes thoughts during its actions.
	 */
	final public String act(Ocean currentOcean, Ocean updatedOcean, List<Actor> newFish)
    {
		thoughts = "";
		this.updatedOcean = updatedOcean;
		this.currentOcean = currentOcean;
		
		age();
		increaseHunger();		
		breed(newFish);
		decideOnMovement(lookAround());
		
    	return thoughts;
    }
	
	/**
	 * A fish looks around and returns its adjacent locations
	 * @return A list of locations adjacent to the fishes location.
	 */
	protected List<Location> lookAround()
	{
		return currentOcean.adjacentLocations(location);
	}
	
	/**
	 * A fish decides where to move in a preferential order.
	 * First priority is finding food, second is finding a preferred location, third is staying in its current location.
	 * If a fish can neither move nor stay it dies of overcrowding.
	 * @param surroundings A list of locations around the fish.
	 * @return The location a fish wants to move to. Will be null if fish dies.
	 */
	protected Location decideOnMovement(List<Location> surroundings)
	{
		Location potentialLocation = findLocationOfFood(surroundings);
		
		if(potentialLocation == null)
		{
			potentialLocation = chooseNextLocation(surroundings);
		}
		if(potentialLocation != null)
		{
			move(potentialLocation);
		}
		else
			die(ReasonForDeath.OVERCROWDING);
		
		return potentialLocation;
	}
	
	/**
	 * Abstract methods unique to each subtype of fish.
	 */
	abstract protected Location chooseNextLocation(List<Location> surroundings);
	
	abstract protected Location findLocationOfFood(List<Location> surroundings);
	
	/**
	 * A fish modifies its location and adapts the current and updated ocean accordingly
	 * NOTE: Fish must inform the current ocean of their leaving, otherwise they can still be seen as food for nearby fish.
	 * @param location The new location of the fish.
	 */
	protected void move(Location location)
	{		
		currentOcean.getOceanSquareAtLocation(this.location).fishLeaves();
		updatedOcean.getOceanSquareAtLocation(location).fishArrives(this);
		this.location = location;
	}
	
	/**
	 * Increases a fishes hunger by 10. If hunger reaches above a fishes stamina it will die of starvation.
	 */
	
	protected void increaseHunger()
	{
		hunger += 10;
		if(hunger > getStamina()) die(ReasonForDeath.STARVATION);
	}
	
	/**
	 * Decreases a fishes hunger by a given amount.
	 * @param amount The amount to decrease hunger by.
	 */
	
	protected void decreaseHunger(int amount)
	{
		hunger -= amount;
	}
	
	/**
	 * 
	 * @return Hunger of the fish.
	 */
	
	protected int getHunger()
	{
		return hunger;
	}
	
	/**
	 * Increase the fishes age by 1. If the fishes ages goes above the maximum age of the fish it dies of old age.
	 */
	
	protected void age()
	{
		age++;
		if(age > getMaxAge()) die(ReasonForDeath.OLD_AGE);
	}
	
	/**
	 * Calculates the offspring of a fish and adds them to a list.
	 * @param newFish List of actors to store new born fish.
	 */
	
	protected void breed(List<Actor> newFish)
	{
		try { newFish = giveBirth(calculateBirths(), newFish); }
		catch(InstantiationException e)
		{
			System.out.println(e.toString());
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * If a fish is old enough to breed and passes a breeding probability test it returns a random number of births based on the maximum litter size. 
	 * @return Number of births.
	 */
	protected int calculateBirths()
	{
		Random random = new Random();
		int births = 0;
        if(age >= getBreedingAge() && random.nextDouble() <= getBreedingProbability())
        {
            births = random.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
	}
	
	/**
	 * Creates new fish using class reflection and adds them to empty squares in the updatedOcean and to the list of new actors.
	 * @param births Number of fish to create.
	 * @param babyFishs List in which to store the fish as actors
	 * @return Returns the list of baby fish as actors.
	 */
	protected List<Actor> giveBirth(int births, List<Actor> babyFishs) throws InstantiationException, IllegalAccessException
    {
        for(int b = 0; b < births; b++)
        {
        	//Will only place offspring in empty locations.
        	Location freeAdjacentLocation = updatedOcean.freeAdjacentLocation(location);
        	if(freeAdjacentLocation != null)
	        {
        		//The class of the new fish is based on what subclass inherits the method.
	            Fish babyFish = this.getClass().newInstance();
	            //Current ocean and location must be set as newInstance does not accept input variables.
	            babyFish.setCurrentOcean(currentOcean);
	            babyFish.setLocation(freeAdjacentLocation);
	            babyFishs.add(babyFish);
	            updatedOcean.addFish(babyFish);
	        }
        }
        return babyFishs;
    }
	
	/**
	 * Sets the current ocean variable
	 * @param currentOcean The ocean to set as the current ocean.
	 */
	protected void setCurrentOcean(Ocean currentOcean)
	{
		this.currentOcean = currentOcean;
	}
	
	/**
	 * Sets the location of the fish
	 * @param location Location of the fish.
	 */
	protected void setLocation(Location location)
	{
		this.location = location;
	}
	
	/**
	 * Abstract gets for obtaining subclass specific variables generically.
	 */
	
	abstract protected double getBreedingProbability();
	abstract protected int getMaxLitterSize();
	abstract protected int getMaxAge();
	abstract protected void setMaxAge(int age);
	abstract protected int getAverageMaxAge();
	abstract protected int getBreedingAge();
	abstract protected int getStamina();		
	
	/**
	 * Determines if the fish is still alive.
	 * @return True if alive, false if dead.
	 */
	public Boolean isAlive()
	{
		return isAlive;
	}
	
	/**
	 * Causes a fish to die
	 * @param r Enum reason for death, used for statistics, debugging and sim balancing.
	 */
	public void die(ReasonForDeath r)
	{
		isAlive = false;
		//if(this instanceof Herring && r == ReasonForDeath.STARVATION)	System.out.println(this.toString() + " died of: " + r);
	}
	
	/**
	 * @return Returns the location of the fish.
	 */
	public Location getLocation()
	{
		return location;
	}
	
	/**
	 * @return Returns the fish class name with hash code and location.
	 */
	public String toString()
	{
		return this.getClass().toString().replace("class", "") + "[" + this.hashCode() + "]" + "(" + this.location + ")";
	}
}
