import java.awt.Color;

/**
 * Rocks are ocean objects, they can get and set their colour and return their location.
 * @author Matthew Maglennon
 *
 */

public class Rock extends OceanObject
{
	private Location location;
	private static Color COLOUR = Color.gray;
    
    public Color getColour()
    {
    	return COLOUR;
    }
    
    static public void setColour(Color colour)
    {
    	COLOUR = colour;
    }

    public Location getLocation()
    {
    	return location;
    }
}
