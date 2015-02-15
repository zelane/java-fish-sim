/**
 * Represent a location in a rectangular grid.
 * 
 * @author David J. Barnes, Michael Kolling and Matthew Maglennon
 */
public class Location
{
    // Row and column positions.
    private int row;
    private int col;

    /**
     * Represent a row and column.
     * @param row The row.
     * @param col The column.
     */
    public Location(int row, int col)
    {
        this.row = row;
        this.col = col;
    }
    
    /**
     * Implement content equality.
     */
    public boolean equals(Object obj)
    {
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol();
        }
        else {
            return false;
        }
    }
    
    /**
     * Return a string of the form row,column
     * @return A string representation of the location.
     */
    public String toString()
    {
        return row + "," + col;
    }
    
    /**
     * Use the top 16 bits for the row value and the bottom for
     * the column. Except for very big grids, this should give a
     * unique hash code for each (row, col) pair.
     * @return A hashcode for the location.
     */
    public int hashCode()
    {
        return (row << 16) + col;
    }
    
    /**
     * @return The row.
     */
    public int getRow()
    {
        return row;
    }
    
    /**
     * @return The column.
     */
    public int getCol()
    {
        return col;
    }
    
    /**
     * Checks if a given location is equal is this location
     * @param compareLocation Location to compare to this location
     * @return True if equal, false if not.
     */
    public boolean isEqualTo(Location compareLocation)
    {
    	if(this.getCol() == compareLocation.getCol() && this.getRow() == compareLocation.getRow())
    		return true;
    	else
    		return false;
    }
    
    /**
     * Uses a location as translation to produce a new location
     * @param translation Current location
     * @return Location after translation
     */
    public Location translate(Location translation)
    {
    	int row = (this.row + translation.getRow());
		int col = (this.col + translation.getCol());
		if(row > 0 && col > 0)
			return new Location(row, col);
		else return null;
    }
    
    /**
     * Takes a location and calculates the translation needed to get there from this location.
     * @param nextLocation The location to translate to
     * @return Returns the translation as a Location object
     */
    public Location calculateTranslation(Location nextLocation)
    {
		int row = this.row - nextLocation.getRow();
		int col = this.col - nextLocation.getCol();
		return new Location(row, col);
    }
}
