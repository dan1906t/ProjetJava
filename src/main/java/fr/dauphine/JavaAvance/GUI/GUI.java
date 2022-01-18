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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import java.text.Format;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.net.URL;
import java.net.URISyntaxException;
import java.io.InputStream;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.Solve.Generator;
import fr.dauphine.JavaAvance.Solve.Checker;
import fr.dauphine.JavaAvance.Solve.Solver;

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
	private GridPanel gridPanel = null;
	private Grid grid;

	private Rectangle separatorBounds;
	private JSeparator separator;

	private Rectangle controlPanelBounds;
	private JPanel controlPanel;
	private GridBagLayout controlLayout;
	private boolean hasControlPanel = false;
	private JFormattedTextField widthField;
	private JFormattedTextField heightField;
	private JFormattedTextField nbccField;
	private JFormattedTextField connectivityField;
	private JButton generateButton;
	private boolean generateButtonActive = false;
	private JButton checkButton;
	private boolean checkButtonActive = false;
	private JFormattedTextField strategyField;
	private JButton solveButton;
	private boolean solveButtonActive = false;
	private JTextArea infoLabel;

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
	 * Constructor. Instanciates a GUI to display one grid.
	 * 
	 * @param grid the grid
	 */
	public GUI(Grid grid) {
		this(grid, false);
	}

	/**
	 * Constructor. Instanciates a GUI with a control panel
	 */
	public GUI() {
		this(null, true);
	}

	/**
	 * Constructor. Instanciates a GUI
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
	 * Change the grid displayed
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
		if(gridPanel != null) {
			gridPanel.setGrid(grid);
		}
		buildGrid();
		buildSeparator();
		resize();
		refresh();
	}

	/**
	 * Refresh the display of the window
	 */
	public void refresh() {
		//Should this be in a invokeLater ?
		if(gridPanel != null) gridPanel.repaint();
		if(separator != null) separator.repaint();
		if(controlPanel != null) controlPanel.repaint();
		frame.repaint();
	}

	/**
	 * Refresh the display of the grid
	 */
	public void refreshGrid() {
		//Should this be in a invokeLater ?
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
	 * Deactivates the buttons of the control panel when a job on a grid is done, or reactivates the buttons when the job is done.
	 *
	 * @param activate true to activate, false to deactivate
	 */
	public void activateAll(boolean activate) {
		if(activate) {
			activateGenerateButton(true);
			if(grid != null) {
				activateCheckButton(true);
				activateSolveButton(true);
			}
			else {
				activateCheckButton(false);
				activateSolveButton(false);
			}
		}
		else {
			activateGenerateButton(false);
			activateCheckButton(false);
			activateSolveButton(false);
		}
	}

	/**
	 * Display an info in the info text area
	 *
	 * @param info the info to display
	 */
	public void displayInfo(String info) {
		if(infoLabel != null) infoLabel.setText(info);
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setTitle("Infinity Loop");

		buildComponents();
		resize();
	}

	/**
	 * Fills the frame with its components
	 */
	private void buildComponents() {
		buildGrid();
		buildSeparator();
		buildControlPanel();
	}

	/**
	 * Builds the grid pannel if not already built and there is a grid
	 */
	private void buildGrid() {
		if(grid != null && gridPanel == null) {
			gridPanel = new GridPanel(grid);
			frame.add(gridPanel);
		}
	}

	/**
	 * Builds the separator if not already built, there is a grid and there is a control pannel
	 */
	private void buildSeparator() {
		if(grid != null && hasControlPanel && separator == null) {
			separator = new JSeparator(SwingConstants.VERTICAL);
			frame.add(separator);
		}
	}

	/**
	 * Builds the control panel if not already built and there is a control pannel
	 */
	private void buildControlPanel() {
		if(hasControlPanel && controlPanel == null) {
			controlPanel = new JPanel();
			frame.add(controlPanel);
			controlLayout = new GridBagLayout();
			controlPanel.setLayout(controlLayout);
			GridBagConstraints c = new GridBagConstraints();

			GUI gui = this;

			//Info label
			infoLabel = new JTextArea(5,10);
			infoLabel.setEditable(false);
			infoLabel.setCursor(null);
			infoLabel.setFocusable(false);
			infoLabel.setWrapStyleWord(true);
			infoLabel.setLineWrap(true);
			infoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			infoLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 0;
			controlPanel.add(infoLabel, c);

			NumberFormat integerFormat = NumberFormat.getIntegerInstance();
			integerFormat.setGroupingUsed(false);
			NumberFormat decimalFormat = DecimalFormat.getInstance();
			decimalFormat.setGroupingUsed(false);

			//Width field
			JLabel widthLabel = new JLabel("width");
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 1;
			controlPanel.add(widthLabel, c);
			widthField = new JFormattedTextField(integerFormat);
			widthField.setValue(new Long(10));
			widthField.setColumns(3);
			c.gridwidth = 1;
			c.gridx = 1;
			c.gridy = 1;
			controlPanel.add(widthField, c);

			//Height field
			JLabel heightLabel = new JLabel("Height");
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 2;
			controlPanel.add(heightLabel, c);
			heightField = new JFormattedTextField(integerFormat);
			heightField.setValue(new Long(10));
			heightField.setColumns(3);
			c.gridwidth = 1;
			c.gridx = 1;
			c.gridy = 2;
			controlPanel.add(heightField, c);

			//NbCC field
			JLabel nbccLabel = new JLabel("NbCC");
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 3;
			controlPanel.add(nbccLabel, c);
			nbccField = new JFormattedTextField(integerFormat);
			nbccField.setValue(new Long(-1));
			nbccField.setColumns(3);
			c.gridwidth = 1;
			c.gridx = 1;
			c.gridy = 3;
			controlPanel.add(nbccField, c);

			//Connectivity field
			JLabel connectivityLabel = new JLabel("Connectivity");
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 4;
			controlPanel.add(connectivityLabel, c);
			connectivityField = new JFormattedTextField(decimalFormat);
			connectivityField.setValue(new Double(0.5));
			connectivityField.setColumns(4);
			c.gridwidth = 1;
			c.gridx = 1;
			c.gridy = 4;
			controlPanel.add(connectivityField, c);

			//Generate button
			generateButton = new JButton("Generate");
			generateButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if(generateButtonActive) {
						infoLabel.setText("");
						//Get parameters from field
						Long widthObject = null;
						Long heightObject = null;
						Long nbccObject = null;
						Double connectivityObject = null;
						try {
							widthField.commitEdit();
							widthObject = (Long)widthField.getValue();
							heightField.commitEdit();
							heightObject = (Long)heightField.getValue();
							nbccField.commitEdit();
							nbccObject = (Long)nbccField.getValue();
							connectivityField.commitEdit();
							connectivityObject = (Double)connectivityField.getValue();
						} catch (ParseException e) {}
						int width = widthObject != null ? widthObject.intValue() : 10;
						int height = heightObject != null ? heightObject.intValue() : 10;
						int nbcc = nbccObject != null ? nbccObject.intValue() : -1;
						double connectivity = connectivityObject != null ? connectivityObject.doubleValue() : -1;

						if(width < 1) {
							infoLabel.setText("[Error] width should be higher than or equal to 1, value "+width);
						}
						else if(height < 1) {
							infoLabel.setText("[Error] length should be higher than or equal to 1, value "+height);
						}
						else if(nbcc < -1) {
							infoLabel.setText("[Error] nbcc should be higher than or equal to -1, value "+nbcc);
						}
						else if(connectivity < 0 && connectivity > 1) {
							infoLabel.setText("[Error] connectivity should be between 0 and 1, value "+connectivity);
						}
						else {
							//Deactivate the buttons, run in a new thread
							activateAll(false);
							Runnable task = new Runnable() {
								public void run() {
									Generator.generateLevelForGui(gui, width, height, nbcc, connectivity);
								}
							};
							new Thread(task).start();
						}
					}
				}
			});
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 5;
			controlPanel.add(generateButton, c);

			//Check button
			checkButton = new JButton("Check");
			checkButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if(checkButtonActive) {
						infoLabel.setText("");
						Checker.checkLevelForGUI(gui, grid);
					}
				}
			});
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 6;
			controlPanel.add(checkButton, c);

			//Strategy field
			JLabel strategyLabel = new JLabel("Strategy");
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 7;
			controlPanel.add(strategyLabel, c);
			strategyField = new JFormattedTextField(integerFormat);
			strategyField.setValue(new Long(0));
			strategyField.setColumns(1);
			c.gridwidth = 1;
			c.gridx = 1;
			c.gridy = 7;
			controlPanel.add(strategyField, c);

			//Solve button
			solveButton = new JButton("Solve");
			solveButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if(solveButtonActive) {
						infoLabel.setText("");
						//Get parameter from field
						Long strategyObject = null;
						try {
							strategyField.commitEdit();
							strategyObject = (Long)strategyField.getValue();
						} catch (ParseException e) {}
						int strategy = strategyObject != null ? strategyObject.intValue() : 0;

						//Deactivate the buttons, run in a new thread
						activateAll(false);
						Runnable task = new Runnable() {
							public void run() {
								Solver.solveLevelForGUI(gui, grid, strategy);
							}
						};
						new Thread(task).start();
					}
				}
			});
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 8;
			controlPanel.add(solveButton, c);

			activateAll(true);
		}
	}

	/**
	 * Activates/Deactivate the generate button
	 *
	 * @param activate true to activate, false to deactivate
	 */
	private void activateGenerateButton(boolean activate) {
		if(hasControlPanel) {
			generateButtonActive = activate;
			generateButton.setEnabled(activate);
		}
	}

	/**
	 * Activates/Deactivate the check button
	 *
	 * @param activate true to activate, false to deactivate
	 */
	private void activateCheckButton(boolean activate) {
		if(hasControlPanel) {
			checkButtonActive = activate;
			checkButton.setEnabled(activate);
		}
	}

	/**
	 * Activates/Deactivate the solve button
	 *
	 * @param activate true to activate, false to deactivate
	 */
	private void activateSolveButton(boolean activate) {
		if(hasControlPanel) {
			solveButtonActive = activate;
			solveButton.setEnabled(activate);
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
		if(controlPanel != null) {
			controlPanelBounds.width = controlLayout.preferredLayoutSize(controlPanel).width;
			controlPanelBounds.height = controlLayout.preferredLayoutSize(controlPanel).height;
		}
		else {
			controlPanelBounds.width = hasControlPanel?CTRLPANEL_MIN_WIDTH:0;
			controlPanelBounds.height = hasControlPanel?CTRLPANEL_MIN_HEIGHT:0;
		}

		if(grid != null) {
			if(grid.getWidth() * windowInsideBounds.height > grid.getHeight() * (windowInsideBounds.width - controlPanelBounds.width - separatorBounds.width)) {
				//Grid larger than higher compared to screen, Stick left and right borders of window to left and right border of screen
				gridBounds.x = windowInsideBounds.x;
				gridBounds.width = windowInsideBounds.width - controlPanelBounds.width - separatorBounds.width;
				caseWidth = (double) gridBounds.width / grid.getWidth();
				caseHeight = caseWidth;
				gridBounds.height = (int)Math.round(caseHeight * grid.getHeight());
				windowInsideBounds.height = Math.max(controlPanelBounds.height,gridBounds.height);
				windowOutsideBounds.height = windowInsideBounds.height + frameInsets.top + frameInsets.bottom;

				gridBounds.x = windowInsideBounds.x;
				gridBounds.y = windowInsideBounds.y + (windowInsideBounds.height - gridBounds.height) / 2;
			}
			else {
				//Grid higher than larger compared to screen, Stick top and bottom borders of window to top and bottom border of screen
				gridBounds.y = windowInsideBounds.y;
				gridBounds.height = windowInsideBounds.height;
				caseHeight = (double) gridBounds.height / grid.getHeight();
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

		/* //DEBUG.
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
	 * Handles the display of the grid. setDimensions should be called after instantiation or a new grid is set
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
		 * Sets the grid
		 */
		public void setGrid(Grid grid) {
			this.grid = grid;
		}

		/**
		 * Sets the dimention of the grid, its cases, and generates images for each type of piece in each orientation
		 */
		public void setDimensions(Rectangle gridBounds) {
			caseWidth = (double) gridBounds.width / grid.getWidth();
			caseHeight = (double) gridBounds.height / grid.getHeight();

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
		 * Get the given image with the given dimensions
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
