import java.awt.Color;

/**
 * All objects stored in ocean squares are Ocean Objects. They must implement a getColour and getLocation method.
 * @author Zelane VII
 *
 */

abstract public class OceanObject
{
	public Location location;
	abstract Color getColour();
	abstract Location getLocation();
}
