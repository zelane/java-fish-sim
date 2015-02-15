import java.util.ArrayList;
import java.util.List;

/**
 * Rock Patterns are a list of locations in which to place rocks.
 * @author Zelane VII
 *
 */

public class RockPattern
{
	private List<Location> rockLocations = new ArrayList<Location>();
		
	public RockPattern(int x1, int y1, int x2, int y2)
	{
		for(int c = (x1); c < (x2); c++)
		{
			for(int c2 = (y1); c2 < (y2); c2++)
			{
				rockLocations.add(new Location(c2, c));
			}
		}
	}
	
	public List<Location> getRockPattern()
	{
		return rockLocations;
	}
}
