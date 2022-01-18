package fr.dauphine.JavaAvance.GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.util.concurrent.locks.ReentrantLock;
import java.io.File;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.net.URL;
import java.net.URISyntaxException;
import java.io.InputStream;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.Solve.Checker;

/**
 * Handles the GUI
 */
public class GUI {

	private static final int CTRLPANEL_MIN_WIDTH = 200;
	private static final int CTRLPANEL_MIN_HEIGHT = 1000;
	private static final int SEPARATOR_WIDTH = 2;

	private Rectangle windowOutsideBounds;
	private JFrame frame;
	private Rectangle windowInsideBounds;
	private Rectangle gridBounds;
	private GridPanel gridPanel;
	private Grid grid;
	private Rectangle separatorBounds;
	private JSeparator separator;
	private Rectangle controlPanelBounds;
	private JPanel controlPanel;
	private boolean hasControlPanel = false;

	/**
	 * Start the GUI
	 */
	public void start() {
		GUI gui = this;
		Runnable task = new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						gui.frame.setVisible(true);
					}
				});
			}
		};
		new Thread(task).start();
	}

	/**
	 * Create the GUI to display one grid.
	 * 
	 * @param grid the grid
	 */
	public GUI(Grid grid) {
		this(grid, false);
	}

	/**
	 * Create the GUI with a control panel
	 */
	public GUI() {
		this(null, true);
	}

	/**
	 * Create the GUI
	 * 
	 * @param grid the grid to display, or null
	 * @param hasControlPanel true if should display the control panel
	 */
	public GUI(Grid grid, boolean hasControlPanel) {
		this.grid = grid;
		this.hasControlPanel = hasControlPanel;
		frame = new JFrame();

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);

		buildComponents();
		resize();
	}

	/**
	 * Change the grid displayed
	 */
	public void changeGrid(Grid grid) {
		//TODO
	}

	/**
	 * Refresh the display of the grid
	 */
	public void refresh() {
		//Should this be in a invoke later ?
		gridPanel.repaint();
		frame.repaint();
	}

	/**
	 * Gets the mutex to modify the grid. getGridMutex().lock() should be called every time a piece is modified, and getGridMutex().unlock() just after, with a call to refresh() when ready to refresh
	 */
	public ReentrantLock getGridMutex() {
		return gridPanel.gridMutex;
	}

	/**
	 * Fills the frame with its components
	 */
	private void buildComponents() {
		if(grid != null) {
			gridPanel = new GridPanel(grid);
			frame.add(gridPanel);
		}

		if(grid != null && hasControlPanel) {
			separator = new JSeparator(SwingConstants.VERTICAL);
			frame.add(separator);
		}

		if(hasControlPanel) {
			controlPanel = new JPanel();
			frame.add(controlPanel);
			JLabel label = new JLabel("En construction");
			controlPanel.add(label);
		}
	}

	/**
	 * Calculates the dimensions of the frame and its components, resize them and centers them
	 */
	private void resize () {
		frame.pack();
		Insets frameInsets = frame.getInsets();

		Rectangle availableScreenBounds = getAvailableScreenBounds();

		//This will potentially be changed later, gives the maximum space
		windowOutsideBounds = new Rectangle(availableScreenBounds);
		windowInsideBounds = new Rectangle();
		windowInsideBounds.x = 0;
		windowInsideBounds.y = 0;
		//Those two will potentially be changed later, gives the maximum space
		windowInsideBounds.width = windowOutsideBounds.width - frameInsets.left - frameInsets.right;
		windowInsideBounds.height = windowOutsideBounds.height - frameInsets.top - frameInsets.bottom;

		gridBounds = new Rectangle();
		double caseWidth, caseHeight;
		separatorBounds = new Rectangle();
		controlPanelBounds = new Rectangle();

		separatorBounds.width = (hasControlPanel && grid != null)?SEPARATOR_WIDTH:0;
		controlPanelBounds.width = hasControlPanel?CTRLPANEL_MIN_WIDTH:0;
		controlPanelBounds.height = hasControlPanel?CTRLPANEL_MIN_HEIGHT:0;

		if(grid != null) {
			if(grid.getWidth() * windowInsideBounds.height > grid.getHeight() * (windowInsideBounds.width - controlPanelBounds.width - separatorBounds.width)) {
				//Grid larger than higher compared to screen, Stick left and right borders of window to left and right border of screen
				gridBounds.x = windowInsideBounds.x;
				gridBounds.width = windowInsideBounds.width - controlPanelBounds.width - separatorBounds.width;
				caseWidth = gridBounds.width / grid.getWidth();
				caseHeight = caseWidth;
				gridBounds.height = (int)Math.round(caseHeight * grid.getHeight());
				windowInsideBounds.height = Math.min(controlPanelBounds.height,gridBounds.height);
				windowOutsideBounds.height = windowInsideBounds.height + frameInsets.top + frameInsets.bottom;

				gridBounds.x = windowInsideBounds.x;
				gridBounds.y = windowInsideBounds.y + (windowInsideBounds.height - gridBounds.height) / 2;
			}
			else {
				//Grid higher than larger compared to screen, Stick top and bottom borders of window to top and bottom border of screen
				gridBounds.y = windowInsideBounds.y;
				gridBounds.height = windowInsideBounds.height;
				caseHeight = gridBounds.height / grid.getHeight();
				caseWidth = caseHeight;
				gridBounds.width = (int)Math.round(caseWidth * grid.getWidth());
				windowInsideBounds.width = gridBounds.width + separatorBounds.width + controlPanelBounds.width;
				windowOutsideBounds.width = windowInsideBounds.width + frameInsets.left + frameInsets.right;

				gridBounds.x = windowInsideBounds.x;
				gridBounds.y = windowInsideBounds.y;
			}

			separatorBounds.height = windowInsideBounds.height;
			separatorBounds.x = windowInsideBounds.x + gridBounds.width;
			separatorBounds.y = windowInsideBounds.y;

			controlPanelBounds.x = windowInsideBounds.x + gridBounds.width + separatorBounds.width;
			controlPanelBounds.y = windowOutsideBounds.y + (windowOutsideBounds.height - controlPanelBounds.height) / 2;
		}
		else {
			controlPanelBounds.x = windowInsideBounds.x;
			controlPanelBounds.y = windowInsideBounds.y;
			windowInsideBounds.width = controlPanelBounds.width;
			windowInsideBounds.height = controlPanelBounds.height;
			windowOutsideBounds.width = windowInsideBounds.width + frameInsets.left + frameInsets.right;
			windowOutsideBounds.height = windowInsideBounds.height + frameInsets.top + frameInsets.bottom;
		}

		windowOutsideBounds.x = availableScreenBounds.x + ((availableScreenBounds.width - windowOutsideBounds.width) / 2);
		windowOutsideBounds.y = availableScreenBounds.y + ((availableScreenBounds.height- windowOutsideBounds.height) / 2);

		/* DEBUG.
		System.out.println("availableScreenBounds "+availableScreenBounds);
		System.out.println("frame insets "+frameInsets);
		System.out.println("windowOutsideBounds "+windowOutsideBounds);
		System.out.println("windowInsideBounds "+windowInsideBounds);
		System.out.println("gridBounds "+gridBounds);
		System.out.println("controlPanelBounds "+controlPanelBounds);
		*/

		frame.setBounds(windowOutsideBounds);
		if(grid != null) {
			gridPanel.setDimensions(gridBounds);
		}
		if(grid != null && hasControlPanel) {
			separator.setBounds(controlPanelBounds.x, 0, SEPARATOR_WIDTH, windowOutsideBounds.height);
		}
		if(hasControlPanel) {
			controlPanel.setBounds(controlPanelBounds);
		}
	}

	/**
	 * Gets the bounds of the available space on the screen without insets
	 */
	private Rectangle getAvailableScreenBounds () {
		// Screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// Screen insets
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(graphicsDevice.getDefaultConfiguration());
		// Avalaible screen bounds
		Rectangle availableScreenBounds = new Rectangle(screenSize);
		availableScreenBounds.x += screenInsets.left;
		availableScreenBounds.y += screenInsets.top;
		availableScreenBounds.width -= (screenInsets.left + screenInsets.right);
		availableScreenBounds.height -= (screenInsets.top + screenInsets.bottom);
		return availableScreenBounds;
	}

	/**
	 * Handles the display of the grid
	 */
	private class GridPanel extends JPanel {
		public ReentrantLock gridMutex = new ReentrantLock();
		private Grid grid;
		private double caseWidth;
		private double caseHeight;
		private ImageIcon[][] icons;
		private ImageIcon background;

		public GridPanel(Grid grid) {
			super();

			this.grid = grid;

			this.setLayout(null);
		}

		/**
		 * Sets the dimention of the grid, its cases, and generates images for each type of piece in each orientation
		 */
		public void setDimensions(Rectangle gridBounds) {
			caseWidth = gridBounds.width / grid.getWidth();
			caseHeight = gridBounds.height / grid.getHeight();

			this.background = getImageIcon("background.png", gridBounds.width, gridBounds.height);

			this.icons = new ImageIcon[PieceType.getTypeMaxValue() + 1 - PieceType.getTypeMinValue()][Orientation.getOrientationMaxValue() + 1 - Orientation.getOrientationMinValue()];
			int imageNumber = 0;
			for(PieceType t : PieceType.values()) {
				for(Orientation o : t.getListOfPossibleOri()) {
					if(t != PieceType.VOID) {
						this.icons[t.getValue()][o.getValue()] = getImageIcon(imageNumber+".png", (int)Math.round(caseWidth), (int)Math.round(caseHeight));
					}
					imageNumber++;
				}
			}

			this.setBounds(gridBounds);
		}

		/**
		 * Get the given image on the given dimensions
		 * 
		 * @param image
		 * @return an image icon
		 */
		private ImageIcon getImageIcon(String imageName, int width, int height) {
			InputStream imageStream = getClass().getResourceAsStream("/fr/dauphine/JavaAvance/icons/io/"+imageName);
			BufferedImage img = null;
			Image dimg = null;
			try {
				if(imageStream == null) throw new IOException();
				img = ImageIO.read(imageStream);
				dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			} catch (IOException e) {
				System.err.println("Error opening image file "+imageName+" : " + e.getMessage());
				e.printStackTrace();
			}

			return new ImageIcon(dimg);
		}

	  @Override
	  public void paintComponent(Graphics g) {
      super.paintComponent(g);

			//Repaints the background
			background.paintIcon(this, g, 0,0);

			//Paints the pieces of the grid
			gridMutex.lock();
			for(int i=0; i<grid.getHeight(); i++) {
				for(int j=0; j<grid.getWidth(); j++) {
					Piece p = grid.getPiece(i,j);
					if(p != null) {
						PieceType t = p.getType();
						Orientation o = p.getOrientation();
		    		ImageIcon icon = icons[t.getValue()][o.getValue()];
						if(icon != null) {
							icon.paintIcon(this, g, (int)Math.round(j*caseWidth), (int)Math.round(i*caseHeight));
						}
					}
				}
			}
			gridMutex.unlock();
	  }
	}
}
