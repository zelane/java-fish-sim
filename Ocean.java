import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

    /**
     * Represent an ocean of the given dimensions.
     * The ocean stores an array of squares that hold fish rocks etc...
     * The ocean is used to deliver information about its ocean squares and pass data onto them.
     * 
     * @author Matthew Maglennon
     */

public class Ocean
{
    private static final Random rand = new Random(); //Random object used for creation of random ints etc...
	
	private int height = 0; //Stores the height of the ocean instance.
	private int width = 0; //Stores the width of the ocean instance.
	private OceanSquare oceanSquareArray[][]; //An array to store an ocean square object for each ocean grid point
	
	/**
	 * Construct an ocean object of a given height and width.
	 * @param height Ocean height.
	 * @param width Ocean width.
	 */
    public Ocean(int height, int width)
    {
    	this.height = height;
    	this.width = width;
    	oceanSquareArray = new OceanSquare[height][width];
		initialiseArray();
    }
    
    /**
     * Fills the ocean square array with ocean squares.
     */
    private void initialiseArray()
    {	
    	int rowCount = 0;
    	int colCount = 0;
		for(OceanSquare[] row : oceanSquareArray)
    	{	
    		for(colCount = 0; colCount < row.length; colCount++)
    		{
    			row[colCount] = new OceanSquare(new Location(rowCount, colCount));
    		}
    		rowCount++;
    	}
    }
    
    /**
     * Returns an ocean square object from the ocean squares array at the given location 
     * @param location The location of the required ocean square.
     * @return The ocean square object at the given location
     */
    public OceanSquare getOceanSquareAtLocation(Location location)
    {
    	return oceanSquareArray[location.getRow()][location.getCol()];
    }
    
    /**
     * Calls the regeneratePlankton method of each ocean square in the ocean squares array.
     */
    public void regeneratePlankton()
    {
    	for(OceanSquare[] cols : oceanSquareArray)
    	{
    		for(OceanSquare square : cols)
    		{
    			square.regeneratePlankton();
    		}
    	}
    }
    
    /**
     * Checks if an ocean square at a given location is free.
     * @param location The location of the ocean square you wish to check.
     * @return True is empty, false if not.
     */
    public Boolean isLocationEmpty(Location location)
    {
    	return getOceanSquareAtLocation(location).isEmpty();
    }
    
    /**
     * Checks for and returns the location of an empty ocean square in the 8 squares around a location and the location itself. 
     * @param centerLocation The location around which to check for empty ocean squares.
     * @return The location of an empty ocean square if available, else returns null.
     */
    public Location freeAdjacentLocation(Location centerLocation)
    {
    	List<Location> adjacent = adjacentLocations(centerLocation);
    	
    	for(Location location : adjacent)
    	{
    		if(getOceanSquareAtLocation(location).isEmpty()) return location;
    	}
        // check whether current location is free
        if(getOceanSquareAtLocation(centerLocation).isEmpty())
        {
            return centerLocation;
        } 
        else
        {
            return null;
        }
    }
    
    /**
     * Generates a list of locations around a given location.
     * @param location The location around which to find locations.
     * @return List of locations.
     */
    public List<Location> adjacentLocations(Location location)
    {
        int row = location.getRow();
        int col = location.getCol();
        List<Location> locations = new LinkedList<Location>();
        for(int roffset = -1; roffset <= 1; roffset++)
        {
            int nextRow = row + roffset;
            if(nextRow >= 0 && nextRow < height)
            {
                for(int coffset = -1; coffset <= 1; coffset++)
                {
                    int nextCol = col + coffset;
                    // Exclude invalid locations and the original location.
                    if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0))
                    {
                        locations.add(new Location(nextRow, nextCol));
                    }
                }
            }
        }
        Collections.shuffle(locations, rand);
        return locations;
    }
    
    /**
     * Removes all fish from every ocean square in the ocean squares array.
     */
    public void clearFish()
    {
    	for(OceanSquare[] row : oceanSquareArray)
    	{
    		for(OceanSquare square : row)
    		{
    			square.fishLeaves();
    		}
    	}
    }
    
    /**
     * Adds a fish object to the ocean square at the location the fish itself stores.
     * @param fish The fish to add.
     */
    public void addFish(Fish fish)
    {
    	//if(GetOceanSquareAtLocation(fish.location).IsEmpty())
    		getOceanSquareAtLocation(fish.location).fishArrives(fish);
    }
    
    /**
     * Return the fish at the given location, if any.
     * @param location The location at which to get the fish.
     * @return The fish at the given location, or null if there is none.
     */
    public Fish getFishAt(Location location)
    {       	
    	if(getOceanSquareAtLocation(location).hasFish())
    	{
    		return getOceanSquareAtLocation(location).getFish();
    	}
    	else return null;
    }
    
    /**
     * @return The height of the ocean.
     */
    public int getHeight()
    {
        // put something here
        return height;
    }
    
    /**
     * @return The width of the ocean.
     */
    public int getWidth()
    {
        // and something here
        return width;
    }
}
