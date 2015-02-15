/**
 * Ocean square objects make up the ocean. They are used to store actors, rocks and have a plankton level.
 *  
 * @author Matthew Maglennon
 */

public class OceanSquare
{
	private Rock rock; //Stores a rock object that may be in the square
	private Fish fish; //Stores a fish object that may be in the square
	@SuppressWarnings("unused")
	private Location location; //Ocean squares are aware of their location
	private float plankton; //Stores plankton level as a float
	
	/**
	 * Ocean square constructor
	*@param location The location of the ocean square in the ocean.
	*/ 
	public OceanSquare(Location location)
	{
		plankton = 4;
		this.location = location;
	}
	
	/**
	 * Regenerates plankton by 0.1, to be called each step.
	 * Will not exceed a plankton level of 10.
	 */
	public void regeneratePlankton()
	{
		if(plankton < 10)	plankton+=0.1;
	}
	
	/**
	 * Reduces the plankton level by a specific amount. Will not go below 0 level.
	 * @param amount The amount to reduce to plankton level by.
	 */
	public void reducePlankton(int amount)
	{
		if(plankton > 0) plankton -= amount;
	}
	
	/**
	 * Returns the level of plankton of this square.
	 * @return Plankton level of this square.
	 */
	public float getPlanktonLevel()
	{
		return plankton;
	}
	
	/**
	 * Checks if any fish or rocks are in this square and returns true if not.
	 * @return True if empty, False if not.
	 */
	public Boolean isEmpty()
	{
		if(fish == null && rock == null) return true;
		else return false;
	}
	
	/**
	 * Returns the fish in this square.
	 * @return Fish if one is present, null if one is not.
	 */
	public Fish getFish()
	{
		if(fish != null) return fish;
		else return null;
	}
	
	/**
	 * Checks for fish in this square.
	 * @return True if fish is present, false if not.
	 */
	public Boolean hasFish()
	{
		if(fish!=null) return true;
		else return false;
	}
	
	/**
	 * Checks for a rock in this square
	 * @return True if rock is present, false if not.
	 */
	public Boolean hasRock()
	{
		if(rock!=null) return true;
		else return false;
	}
	
	/**
	 * Assigns a new Rock object to variable rock.
	 */
	public void addRock()
	{
		rock = new Rock();
	}
	
	/**
	 * Returns the rock object
	 * @return Returns the rock stored in this ocean square
	 */
	public Rock getRock()
	{
		return rock;
	}
	
	/**
	 * Takes a Fish object and assigns it to the fish variable.
	 * @param f Fish to store in this square.
	 */
	public void fishArrives(Fish f)
	{
		fish = f;
	}
	
	/**
	 * Removes the fish stored in this square by setting the fish variable to null.
	 */
	public void fishLeaves()
	{
		fish = null;
	}
}
