import java.util.List;

/**
 * Interface for actors, defines that they must be able to act, determine their alive status and return their location.
 * @author Matthew Maglennon
 *
 */

public interface Actor
{
	public String act(Ocean currentOcean, Ocean updatedOcean, List<Actor> newActors);
	public Boolean isAlive();
	public Location getLocation();
}
