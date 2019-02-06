import java.util.Random;

/******************************************************************************
*  Archery-accuracy simulation.
*
*  @author   Daniel R. Collins (dcollins@superdan.net)
*  @since    2010
*  @version  1.03
******************************************************************************/

public class ArcherySim {

	//--------------------------------------------------------------------------
	//  Constants
	//--------------------------------------------------------------------------

	/** Base range to target (in yards). */
	final double BASE_RANGE = 10.0;

	/** Maximum range considered (in yards). */
	final double MAX_RANGE = 200.0;

	/** Number of shots per course. */
	final int SHOTS_PER_COURSE = 100000;

	//--------------------------------------------------------------------------
	//  Fields
	//--------------------------------------------------------------------------

	/** Random-number generator. */
	Random randomGenerator;

	/** Precision of shooter.*/
	double shooterPrecision = 1.5;

	/** Radius of target in feet.*/
	double targetRadius = 2.0;

	/** Present long-form table? */
	boolean longFormTable = false;
	
	/** Have we successfully initialized? */
	boolean initSuccess = false;

	//--------------------------------------------------------------------------
	//  Constructors
	//--------------------------------------------------------------------------

	/**
	*  Basic constructor.
	*/
   public ArcherySim() {
		randomGenerator = new Random();
   }

	//--------------------------------------------------------------------------
	//  Methods
	//--------------------------------------------------------------------------

	/**
	*  Print usage.
	*/
	public void printUsage () {
		System.out.println("Usage: ArcherySim [precision] [radius] [-L]");
		System.out.println("  Simulates an archer shooting at target with bivariate normal error model.");
		System.out.println("  precision is a value modeling shooter accuracy, e.g.:");
		System.out.println("    1.5 for a shooter with basic-level training");
		System.out.println("    7.5 for a shooter with grand-master skill");
		System.out.println("  radius is the radius of the target in feet, e.g.:");
		System.out.println("    1.5 for a man-sized figure");
		System.out.println("    2.0 for standard archery target");
		System.out.println("    12.0 for long-distance clout competition");
		System.out.println("  Default display is a short table with doubling of ranges;");
		System.out.println("    -L switch forces long/linear table in 10 yard increments");
		System.out.println();		
	}

	/**
	*  Parse arguments.
	*/
	public void parseArgs (String[] args) {

		// Get precision
		if (args.length >= 1) {
			shooterPrecision = getArgDouble(args[0]);
			if (shooterPrecision <= 0.0)
				return;
		}
		
		// Get radius
		if (args.length >= 2) {
			targetRadius = getArgDouble(args[1]);
			if (targetRadius <= 0.0)
				return;
		}
				
		// Get other switches
		for (int i = 2; i < args.length; i++) {
			if (args[i].charAt(0) == '-') {
				switch (args[i].charAt(1)) {
					case 'l': case 'L': longFormTable = true; break;
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
	*/
	double getArgDouble (String s) {
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
	double square (double val) {
		return val * val;
	}

	/**	
	*  Get random error on one axis of a shot.
	*  @return Amount of error in feet.
	*/
	double randomShotError () {
		return randomGenerator.nextGaussian() / shooterPrecision;
	}

	/**	
	*  Fire one random shot.
	*  Models error by separate normal distribution in x and y axes.
	*  @param range Range to target (in yards/meters).
	*  @param radius Radius of the target (in feet).
	*  @return True if shot hit the target.
	*/
	boolean fireOneShot (double range, double radius) {
		double xError = randomShotError();
		double yError = randomShotError();
		double apparentRadius = radius * BASE_RANGE / range;
		return (square(xError) + square(yError) <= square(apparentRadius));
	}

	/**	
	*  Fire a course of shots.
	*  @param range Range to target (in yards/meters).
	*  @param radius Radius of the target (in feet).
	*  @return Ratio of shots that hit target.
	*/
	double fireOneCourse (double range, double radius) {
		int hits = 0;
		for (int i = 0; i < SHOTS_PER_COURSE; i++) {
		if (fireOneShot(range, radius))
			hits++;
		}
		return (double) hits/SHOTS_PER_COURSE;
	}

	/**	
	*  Print table of results.
	*/
	public void printResults () {

		// Header
		System.out.println("ArcherySim Hit Percentages");
		System.out.println("  Shooter Precision: " + shooterPrecision);
		System.out.println("  Target Radius (ft): " + targetRadius);
		System.out.println();
		System.out.println("Range (yd) Hit (%)");
		System.out.println("---------- ------");

		// Body
		double range = BASE_RANGE;
		while (range <= MAX_RANGE) {
			double hitPercent = fireOneCourse(range, targetRadius) * 100;
			System.out.format("   %4.0f      %3.0f\n", range, hitPercent);
			if (longFormTable) {
				range += 10.0;
			}
			else {
				range *= 2;
			}
		}
		System.out.println();
	}
	
	/**
	*  Main application method.
	*/
	public static void main (String[] args) {
		ArcherySim sim = new ArcherySim();
		sim.parseArgs(args);
		if (sim.initSuccess) {
			sim.printResults();
		}
		else {
			sim.printUsage();
		}
	}
}

