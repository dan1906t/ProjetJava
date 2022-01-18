package fr.dauphine.JavaAvance.Main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import fr.dauphine.JavaAvance.GUI.GUI;
import fr.dauphine.JavaAvance.GUI.Grid;
import fr.dauphine.JavaAvance.Solve.Checker;
import fr.dauphine.JavaAvance.Solve.Generator;
import fr.dauphine.JavaAvance.Solve.Solver;




import java.io.IOException;
import fr.dauphine.JavaAvance.GUI.GridFiles;





/**
 * Parser of the program
 */
public class Main {
	private static String inputFile = null;
	private static String outputFile = null;
	private static Integer width = -1;
	private static Integer height = -1;
	private static Integer maxcc = -1;
	private static Integer nbThread = 1;
	private static Integer algo = 2;
	
	public static void main(String[] args) {
		Options options = new Options();
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		
		options.addOption("i", "gui", false, "Opens the GUI.");
		options.addOption("g", "generate ", true, "Generate a grid of size height x width.");
		options.addOption("c", "check", true, "Check whether the grid in <arg> is solved.");
		options.addOption("s", "solve", true, "Solve the grid stored in <arg>.");

		options.addOption("o", "output", true, "Store the generated or solved grid in <arg>. (Use only with --generate and --solve.)");
		options.addOption("x", "nbcc", true, "Maximum number of connected components. (Use only with --generate.)");
		options.addOption("y", "connectivity", true, "Connectivity used to generate the grid, float between 0 and 1, the higher the more connections it will have. Default: 0.5. (Use only with --generate.)");
		//options.addOption("t", "threads", true, "Maximum number of solver threads. (Use only with --solve.)");
		options.addOption("u", "strategy", true, "Stragey for solving. Integer. Default 0. (Use only with --solve.)");
		options.addOption("p", "performances", false, "Display the performances of the solver. (Use only with --solve and without --verbose and --display.)");
		options.addOption("v", "verbose", false, "Print more informations to console");
		options.addOption("d", "display", false, "Display on GUI");
		options.addOption("h", "help", false, "Display this help");
		
		try {
			cmd = parser.parse( options, args);
		} catch (ParseException e) {
			System.err.println("Error: invalid command line format.");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "phineloopgen", options );
			System.exit(1);
		}

		try {
			if( (cmd.hasOption( "g" )?1:0) + (cmd.hasOption( "c" )?1:0) + (cmd.hasOption( "s" )?1:0) + (cmd.hasOption( "i" )?1:0) > 2) {
				throw new ParseException("You must specify only one of the following options: --generate --check --solve --gui ");
			}
			if( cmd.hasOption( "g" ) ) {
				System.out.println("Running phineloop generator.");

				String[] gridformat = cmd.getOptionValue( "g" ).split("x");
				try {
					width = Integer.parseInt(gridformat[0]);
					height = Integer.parseInt(gridformat[1]); 
				} catch (NumberFormatException e) {throw new ParseException("--generate argument must be widthxheight where width and height are integers");}

				if(! cmd.hasOption("o")) throw new ParseException("Missing mandatory --output argument.");
				outputFile = cmd.getOptionValue( "o" );
				
				int nbcc = -1;
				if(cmd.hasOption("x")) {
					try {
						nbcc = Integer.parseInt(cmd.getOptionValue( "x" ));
					} catch (NumberFormatException e) {throw new ParseException("--nbcc argument must be an integer");}
					//the number of connected components can be at most width*length/2 because the most efficient way to make a lot of components is to make "dominos" with two ONECONN.
					//search "mutilated chessboard problem" for an idea of what it looks like
					if(nbcc < -1 || nbcc > width*height/2) {throw new ParseException("--nbcc argument must be between -1 and "+width+"*"+height+"/2 , value "+nbcc+")");}
				}

				double connectivity = 0.5;
				if(cmd.hasOption("y")) {
					try {
						connectivity = Double.parseDouble(cmd.getOptionValue( "y" ));
					} catch (NumberFormatException e) {throw new ParseException("--connectivity argument must be a floating number");}
					if(connectivity < 0 || connectivity > 1) {throw new ParseException("--connectivity argument must be between 0 and 1, value "+connectivity+")");}
				}

				// generate grid and store it to outputFile
				Generator.generateLevel(width, height, nbcc, connectivity, outputFile, cmd.hasOption("v"), cmd.hasOption("d"));
			}
			else if( cmd.hasOption( "s" ) ) {

				System.out.println("Running phineloop solver.");
				inputFile = cmd.getOptionValue( "s" );

				if(! cmd.hasOption("o")) throw new ParseException("Missing mandatory --output argument.");
				outputFile = cmd.getOptionValue( "o" );

				int strategy = Solver.STRATEGY_PICKLOWESTLIBERTY;
				if(cmd.hasOption("u")) {
					try {
						strategy = Integer.parseInt(cmd.getOptionValue( "u" ));
					} catch (NumberFormatException e) {throw new ParseException("--strategy argument must be an integer");}
				}

				if(cmd.hasOption("p") && (cmd.hasOption("v") || cmd.hasOption("d"))) throw new ParseException("--performance can't be set at the same time as --verbose or --display");

				// load grid from inputFile, solve it and store result to outputFile
				Solver.solveLevel(inputFile, outputFile, strategy, cmd.hasOption("p"), cmd.hasOption("v"), cmd.hasOption("d"));
			}
			else if( cmd.hasOption( "c" )) {
				System.out.println("Running phineloop checker.");

				inputFile = cmd.getOptionValue( "c" );

        // load grid from inputFile and check if it is solved
				Checker.checkLevel(inputFile, cmd.hasOption("v"), cmd.hasOption("d"));
			}
			else {
				//No options or i option, display GUI
				GUI gui = new GUI();
				gui.start();
			}
		} catch (ParseException e) {
			System.err.println("Error parsing commandline : " + e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "phineloopgen", options );
			System.exit(1); // exit with error
		}
		if (!cmd.hasOption( "i" ) && !cmd.hasOption( "d" ) && (cmd.hasOption( "g" ) || cmd.hasOption( "c" ) || cmd.hasOption( "s" ))) {
			System.exit(0); // exit with success
		}
	}
}
