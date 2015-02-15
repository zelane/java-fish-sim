import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author Matthew Maglennon
 * @version 15/11/2010
 * 
 * A fish based predator prey simulator consisting of an Ocean made of OceanSquares containing various classes of fish.
 * Each fish is treated as an Actor and is asked to act on a looping basis.
 * The simulators default values have been set to give a balanced simulation and good visual experience.
 * The simulation does take until approximately 500 steps for fish to develop naturalistic movement patterns and behaviours
 * Design was influenced by Chapter 10 of Objects First with Java, foxes-and-rabbits-v2
 */

public class Simulator
{
    private Ocean currentOcean; //Reference to current ocean for this simulator
    private Ocean updatedOcean; //Reference to updated ocean for this simulator
    private List<Actor> actors; //List of all actors in the ocean
    private List<Actor> newActors; //List to store actors to be added to the actors list per step
    private SimulatorView simView; //Simulator view object, used for interface
    public static boolean fishingTrip = false; //Set as true to enable party mode
        
    /**
     * Create the simulator and run it.
     */
    public static void main(String[] args) 
    {
        //Simulator sim = new Simulator(50, 60);
    	Simulator sim = new Simulator(150, 180);
    	//Simulator sim = new Simulator(170, 204);
        sim.run(1000000, 10);
    }
    
    /**
     * Creates a new simulator of given height and width. Dimensions influence the simulator view.
     * @param height The height of the simulator
     * @param width The width of the simulator
     */
    public Simulator(int height, int width)
    {
        currentOcean = new Ocean(height, width);
        updatedOcean = new Ocean(height, width);
        actors = new ArrayList<Actor>();
        newActors = new ArrayList<Actor>();
        simView = new SimulatorView(height, width);
        // define in which colour fish should be shown
        Shark.setColour(Color.getHSBColor(0.55f, 0.5f, 0.3f));
        Groper.setColour(Color.getHSBColor(0.65f, 0.6f, 1f));
        Herring.setColour(Color.getHSBColor(0.55f, 0.4f, 0.8f));
        // set various static variables of fish classes and the simulator itself
        Herring.enableGrouping = true;
        Herring.enableDirectionMaintenance = true;
        Shark.enableSharkAvoidance = true;
        Simulator.fishingTrip = false;
    }
    
    /**
     * Runs the simulation.
     * @param steps The number of steps to go through in the simulation
     * @param timePerStep The pause in ms between each step. Adjust for slower simulations.
     */
    public void run(int steps, int timePerStep)
    {
    	setupOcean(currentOcean);
        int c = 1;
        
        //for each step whilst the simulation is still viable
        while(c <= steps  && simView.isViable(currentOcean))
        {
        	newActors.clear();
        	
        	//for each actor in the actors list
        	for(Iterator<Actor> it = actors.iterator(); it.hasNext();)
        	{
        		Actor a = it.next();
        		
        		//if the actor is alive, ask them to act. Output their thoughts to the console if they have any.
        		if(a.isAlive())
        		{
	        		String thoughts = a.act(currentOcean, updatedOcean, newActors);
	        		if(thoughts != "")	System.out.println("Step: " + c + " |" + a.toString() + " says " + thoughts);
	        	}
        		//if the actor is not alive, remove them for the list.
        		else 
        			it.remove();
        	}
        	actors.addAll(newActors); //add all new born animals to the actors list
        	updatedOcean.regeneratePlankton(); //regenerate plankton in the updated ocean
        	
        	//if a pause between steps is required, sleep the thread for this time.
        	try
        	{
        		Thread.sleep(timePerStep);
        	}
        	catch(InterruptedException e)
        	{
        	}
        	
        	if(fishingTrip  || c > 1000) activatePartyMode();
	        
        	//Make the current ocean the updated ocean and update the visual display
        	Ocean temp = currentOcean;
        	currentOcean = updatedOcean;
            updatedOcean = temp;
            updatedOcean.clearFish();
        	simView.showStatus(c, currentOcean);
        	c++;
        }
    }
    
    /**
     * TOP SECRET PARTY FUNCTION
     * Causes herring to increase their hue by 0.009 changing their colour.
     */
    private void activatePartyMode()
    {
    	Herring herring = new Herring(true, null);
    	float[] hsbvals = new float[3];
    	hsbvals = Color.RGBtoHSB(herring.getColour().getRed(), herring.getColour().getGreen(), herring.getColour().getBlue(), hsbvals);
    	hsbvals[0] = hsbvals[0] + 0.009f;
    	Herring.setColour(Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2]));
    }
    
    /**
     * Populates the ocean with fish and rocks
     * @param ocean The ocean object to populate
     */
    private void setupOcean(Ocean ocean)
    {
    	populate(ocean);
        //addRandomRocks(5, 10);
        //addRocks(new RockPattern(currentOcean.getWidth()/2 - 25, currentOcean.getHeight()/2 - 25, currentOcean.getWidth()/2 + 25, currentOcean.getHeight()/2 + 25));
    }
    
    /**
     * Used to create a starting line up of fish for an ocean simulation.
     * Loops through each ocean square in the passed ocean and based on probability adds a selection of fish.
     * These fish are also added to the list of actors.
     * Based on foxes-and-rabbits-v2
     * @param ocean The ocean object to populate.
     */
    private void populate(Ocean ocean)
    {
        Random rand = new Random();
        ocean.clearFish();
        for(int row = 0; row < ocean.getHeight(); row++)
        {
            for(int col = 0; col < ocean.getWidth(); col++)
            {
            	Location location = new Location(row, col);
            	if(ocean.getOceanSquareAtLocation(location).isEmpty())
	            	{
	            	if(rand.nextDouble() <= 0.0015)
	                {
	                    Shark shark = new Shark(true, location);
	                    actors.add(shark);
	                    ocean.addFish(shark);
	                }
	                else if(rand.nextDouble() <= 0.004)
	                {
	                    Herring herring = new Herring(true, location);
	                    actors.add(herring);
	                    ocean.addFish(herring);
	                }
	                else if(rand.nextDouble() <= 0.0015)
	                {
	                    Groper groper = new Groper(true, location);
	                    actors.add(groper);
	                    ocean.addFish(groper);
	                }
            	}
                // else leave the location empty.
            }
        }
        //Shuffle the actors so that they will act in a random order.
        Collections.shuffle(actors);
    }
    
    /**
     * Divides the ocean up into random size segments and adds random sized rocks to the center of these segments.
     * @param number Number of rocks to add.
     * @param maxSize Maximum size of rocks, must be based on number of segments.
     */
    @SuppressWarnings("unused")
	private void addRandomRocks(int number, int maxSize)
    {
    	Random random = new Random();
    	int oceanSegmentWidth = updatedOcean.getWidth() / number;
    	int oceanSegmentHeight = updatedOcean.getHeight() / number;
    	for(int c = 0; c <= updatedOcean.getWidth() - oceanSegmentWidth; c += oceanSegmentWidth)
    	{
    		for(int c2 = 0; c2 <= updatedOcean.getHeight() - oceanSegmentHeight; c2 += oceanSegmentHeight)
    		{
	    		int randomRockSize = random.nextInt(maxSize) + 1;
	    		int x1 = (c + (oceanSegmentWidth/2)) - randomRockSize/2;
	    		int x2 = (c + (oceanSegmentWidth/2)) + randomRockSize/2;
	    		int y1 = (c2 + (oceanSegmentHeight/2)) - randomRockSize/2;
	    		int y2 = (c2 + (oceanSegmentHeight/2)) + randomRockSize/2;
	    		RockPattern rockPattern = new RockPattern(x1,y1,x2,y2);
	    		addRocks(rockPattern);
    		}
    	}
    }
    
    /**
     * Adds a rocks to ocean squares to assemble a given rock pattern.
     * @param rockPattern Rock pattern to assemble.
     */
    private void addRocks(RockPattern rockPattern)
    {
    	for(Location rockLocation : rockPattern.getRockPattern())
    	{
    		currentOcean.getOceanSquareAtLocation(rockLocation).addRock();
    		updatedOcean.getOceanSquareAtLocation(rockLocation).addRock();
    	}
    }
}