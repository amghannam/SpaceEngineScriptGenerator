package com.se.generator.script;

/**
 * Holds various defaults used across the application.
 */
public final class Constants {

	private Constants() {
		// Prevent instantiation
	}

	/**
	 * For unnamed objects.
	 */
	public static final String PLACEHOLDER_NAME = "GeneratedObject";

	/**
	 * Default orbital epoch for the generated SpaceEngine scripts
	 */
	public static final double DEFAULT_EPOCH = 2451545.0;

	// Angles in degrees
	public static final double MIN_ECCENTRICITY = 0.0;
	public static final double MAX_ECCENTRICITY = 1.0;
	public static final double MIN_INCLINATION = 0.0;
	public static final double MAX_INCLINATION = 360.0;
	public static final double MIN_ASCENDING_NODE = 0.0;
	public static final double MAX_ASCENDING_NODE = 360.0;
	public static final double MIN_ARG_OF_PERICEN = 0.0;
	public static final double MAX_ARG_OF_PERICEN = 360.0;
	public static final double MIN_MEAN_ANOMALY = 0.0;
	public static final double MAX_MEAN_ANOMALY = 360.0;

	// Physical properties
	public static final double MIN_GENERIC_RADIUS = 0.1;
	public static final double MAX_GENERIC_RADIUS = 60.0;
	public static final double MIN_ALBEDO = 0.0;
	public static final double MAX_ALBEDO = 1.0;
	
    // Comet-specific constants (only using eccentricity, inclination, and radius)
    public static final double MIN_COMET_ECC = 0.4;      // Minimum eccentricity for comets
    public static final double MAX_COMET_ECC = 0.95;     // Maximum eccentricity for comets

    public static final double MIN_COMET_INCL = 0.0;     // Minimum inclination (in degrees)
    public static final double MAX_COMET_INCL = 180.0;   // Maximum inclination (in degrees)

    public static final double MIN_COMET_RADIUS = 5.0;   // Minimum radius for a comet
    public static final double MAX_COMET_RADIUS = 10.0;  // Maximum radius for a comet
}
