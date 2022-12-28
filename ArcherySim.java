import java.util.Random;

/******************************************************************************
*  Archery-accuracy simulation.
*
*  @author   Daniel R. Collins
*  @since    2010
******************************************************************************/

public class ArcherySim {

	//----------------------------------------------------------------------
	//  Constants
	//----------------------------------------------------------------------

	/** Base range to target (in yards). */
	static final double BASE_RANGE = 10.0;

	/** Maximum range considered (in yards). */
	static final double MAX_RANGE = 200.0;

	/** Number of shots per course. */
	static final int SHOTS_PER_COURSE = 100000;

	/** Number of shots in error table. */
	static final int SHOTS_ERROR_TABLE = 100;

	//----------------------------------------------------------------------
	//  Fields
	//----------------------------------------------------------------------

	/** Random-number generator. */
	private Random randomGenerator;

	/** Precision of shooter.*/
	private double shooterPrecision = 1.5;

	/** Radius of target in feet.*/
	private double targetRadius = 2.0;

	/** Present long-form table? */
	private boolean longFormTable = false;

	/** Present arrays of errors? */
	private boolean errorTable = false;
	
	/** Have we successfully initialized? */
	private boolean initSuccess = false;

	//----------------------------------------------------------------------
	//  Constructors
	//----------------------------------------------------------------------

	/**
	*  Basic constructor.
	*/
	public ArcherySim() {
		randomGenerator = new Random();
	}

	//----------------------------------------------------------------------
	//  Methods
	//----------------------------------------------------------------------

	/**
	*  Print one line.
	*  @param s string to print.
	*/
	public void print(String s) {
		System.out.println(s);
	}

	/**
	*  Print usage.
	*/
	public void printUsage() {
		print("Usage: ArcherySim [precision] [radius] [-L]");
		print("  Simulates an archer shooting at a target "
			+ "with bivariate normal error model.");
		print("  precision value models shooter accuracy, e.g.:");
		print("    1.0 for accuracy of magic fireball");
		print("    1.5 for a archer with basic-level training");
		print("    7.5 for a archer with grand-master skill");
		print("  radius is the radius of the target in feet, e.g.:");
		print("    1.5 for a man-sized figure");
		print("    2.0 for standard archery target");
		print("    12.0 for long-distance clout competition");
		print("  Default display is short table of doubling ranges;");
		print("    -L uses long/linear table in 10 yard increments");
		print("    -E prints errors for every shot");
		print("");
	}

	/**
	*  Parse arguments.
	*  @param args command-line arguments.
	*/
	public void parseArgs(String[] args) {

		// Get precision
		if (args.length >= 1) {
			shooterPrecision = getArgDouble(args[0]);
			if (shooterPrecision <= 0.0) {
				return;
			}
		}
		
		// Get radius
		if (args.length >= 2) {
			targetRadius = getArgDouble(args[1]);
			if (targetRadius <= 0.0) {
				return;
			}
		}
				
		// Get other switches
		for (int i = 2; i < args.length; i++) {
			if (args[i].charAt(0) == '-') {
				switch (args[i].charAt(1)) {
					case 'l': case 'L':
						longFormTable = true; 
						break;
					case 'e': case 'E':
						errorTable = true; 
						break;
					default: return;
				}
			}
			else {
				return;
			}
		}
		
		// Set flag for good initialization
		initSuccess = true;
	}

	/**
	*  Get double from command argument.
	*  @param s string to parse as double.
	*  @return value of s as a double.
	*/
	double getArgDouble(String s) {
		try {
			return Double.parseDouble(s);
		}
		catch (NumberFormatException e) {
			return 0.0;
		}
	}

	/**	
	*  Square a double value.
	*  @param val The value.
	*  @return Square of the value.
	*/
	double square(double val) {
		return val * val;
	}

	/**	
	*  Get random error on one axis of a shot.
	*  @return Amount of error in feet.
	*/
	double randomShotError() {
		return randomGenerator.nextGaussian() / shooterPrecision;
	}

	/**	
	*  Fire one random shot.
	*  Models error by separate normal distribution in x and y axes.
	*  @param range Range to target (in yards/meters).
	*  @param radius Radius of the target (in feet).
	*  @return True if shot hit the target.
	*/
	boolean fireOneShot(double range, double radius) {
		double error = oneShotError(range);
		return error <= radius;
	}

	/**	
	*  Get the error from one random shot.
	*  Models error by separate normal distribution in x and y axes.
	*  @param range Range to target (in yards/meters).
	*  @return Distance from target in feet.
	*/
	double oneShotError(double range) {
		double xError = randomShotError();
		double yError = randomShotError();
		return Math.sqrt(square(xError) + square(yError))
			* range / BASE_RANGE;
	}

	/**	
	*  Fire a course of shots.
	*  @param range Range to target (in yards/meters).
	*  @param radius Radius of the target (in feet).
	*  @return Ratio of shots that hit target.
	*/
	double fireOneCourse(double range, double radius) {
		int hits = 0;
		for (int i = 0; i < SHOTS_PER_COURSE; i++) {
			if (fireOneShot(range, radius)) {
				hits++;
			}
		}
		return (double) hits / SHOTS_PER_COURSE;
	}

	/**	
	*  Print table of results.
	*/
	public void printResults() {

		// Header
		System.out.println("ArcherySim Hit Percentages");
		System.out.println("  Shooter Precision: " + shooterPrecision);
		System.out.println("  Target Radius (ft): " + targetRadius);
		System.out.println();
		System.out.println("Range (yd) Hit (%)");

		// Body
		double range = BASE_RANGE;
		while (range <= MAX_RANGE) {
			double hitPercent = 
				fireOneCourse(range, targetRadius) * 100;
			System.out.format("   %4.0f      %3.0f\n", 
				range, hitPercent);
			if (longFormTable) {
				range += 10;
			}
			else {
				range *= 2;
			}
		}
		System.out.println();
	}

	/**
	*  Print table of error data.
	*/
	public void printErrorTable() {

		// Print header
		for (double range = BASE_RANGE / 2; 
			range <= MAX_RANGE; range *= 2) 
		{
			System.out.print(range + ",");		
		}
		System.out.println();		

		// Print body
		for (int shot = 0; shot < SHOTS_ERROR_TABLE; shot++) {
			for (double range = BASE_RANGE / 2; 
				range <= MAX_RANGE; range *= 2) 
			{
				double error = oneShotError(range);
				System.out.print(error + ",");
			}
			System.out.println();		
		}
		System.out.println();		
	}

	/**
	*  Primary run function.
	*/
	public void run() {
		if (errorTable) {
			printErrorTable();
		}	
		else {
			printResults();		
		}
	}
	
	/**
	*  Main application method.
	*  @param args command-line arguments.
	*/
	public static void main(String[] args) {
		ArcherySim sim = new ArcherySim();
		sim.parseArgs(args);
		if (sim.initSuccess) {
			sim.run();
		}
		else {
			sim.printUsage();
		}
	}
}
