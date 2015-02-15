import java.awt.*;
import javax.swing.*;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2003.12.22
 */
public class SimulatorView extends JFrame
{
	private static final long serialVersionUID = 1L;

	// Colours used for empty locations.
    //private static final Colour EMPTY_COLOR = Color.white;

    // Colour used for objects that have no defined colour.
    //private static final Colour UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population;
    private OceanView oceanView;
    
    // A statistics object computing and storing simulation information
    private OceanStats stats;

    /**
     * Create a view of the given width and height.
     * @param height The simulation height.
     * @param width The simulation width.
     */
    public SimulatorView(int height, int width)
    {
        stats = new OceanStats();

        setTitle("SimOcean");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setLocation(100, 50);
        //setPreferredSize(new Dimension(736,670));
        //setPreferredSize(new Dimension(976,870));
        setPreferredSize(new Dimension((width*4)+16,(height*4)+70));
        
        oceanView = new OceanView(height, width);

        Container contents = getContentPane();
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(oceanView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
    
    /**
     * Show the current status of the ocean.
     * @param step Which iteration step it is.
     * @param ocean The ocean whose status is to be displayed.
     */
    public void showStatus(int step, Ocean ocean)
    {
        if(!isVisible())
            setVisible(true);

        stepLabel.setText(STEP_PREFIX + step);

        stats.reset();
        oceanView.preparePaint();
        
        for(int row = 0; row < ocean.getHeight(); row++)
        {
            for(int col = 0; col < ocean.getWidth(); col++)
            {
            	Location l = new Location(row, col);
            	OceanSquare os = ocean.getOceanSquareAtLocation(l);
	            if(os.hasFish())
	            {
	            	Fish fish = os.getFish();
	                stats.incrementCount(fish.getClass());
	                oceanView.drawMark(l.getCol(), l.getRow(), fish.getColour());
	            }
	            else if(os.hasRock())
	            {
	            	oceanView.drawMark(l.getCol(), l.getRow(), os.getRock().getColour());
	            }
	            else
	            {
	            	Double planktonDouble = Math.floor((os.getPlanktonLevel()*100)/100);
	            	Float planktonFloat = Float.parseFloat(planktonDouble.toString());
	            	planktonFloat = planktonFloat / 40;
	            	if(planktonFloat < 0) planktonFloat = 0f;
	                oceanView.drawMark(l.getCol(), l.getRow(), Color.getHSBColor(0.50f, planktonFloat, 1f));
	            }
            }
        }        
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(ocean));
        oceanView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Ocean ocean)
    {
        return stats.isViable(ocean);
    }
    
    /**
     * Provide a graphical view of a rectangular ocean. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the ocean.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class OceanView extends JPanel
    {
		private static final long serialVersionUID = 1L;

		private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image oceanImage;

        /**
         * Create a new OceanView component.
         */
        public OceanView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }
        
        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize()))
            {  // if the size has changed...
                size = getSize();
                oceanImage = oceanView.createImage(size.width, size.height);
                g = oceanImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) 
                {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) 
                {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }
        
        /**
         * Paint on grid location on this ocean in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The ocean view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(oceanImage != null) 
            {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) 
                {
                    g.drawImage(oceanImage, 0, 0, null);
                }
                else 
                {
                    // Rescale the previous image.
                    g.drawImage(oceanImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
