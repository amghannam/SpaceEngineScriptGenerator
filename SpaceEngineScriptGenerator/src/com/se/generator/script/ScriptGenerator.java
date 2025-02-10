package com.se.generator.script;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import com.se.generator.io.ScriptFileWriter;

/**
 * The {@code ScriptGenerator} class is the central engine for generating
 * SpaceEngine-compatible script files for celestial objects. It provides static
 * methods to create scripts for various object types (such as {@code Moon},
 * {@code Asteroid}, and {@code Comet}) by generating random physical and
 * orbital properties within defined ranges. The generated script output is then
 * written to a file using {@code ScriptFileWriter}.
 * <p>
 * All methods in this class are static and the class cannot be instantiated.
 */
public final class ScriptGenerator {

	private static final Random RNG = ThreadLocalRandom.current();

	private ScriptGenerator() {
		// Prevent instantiation
	}

	/**
	 * Generates {@code CelestialObject} objects representing regular moons based on
	 * the specified parameters and writes the resulting SpaceEngine script to a
	 * file.
	 * <p>
	 * The method generates the moon objects using the parameters in the provided
	 * {@code RegularMoonParams}, sorts them by semi-major axis, and then writes the
	 * output using the common parameters (distance unit, reference plane, and
	 * output file) from {@code RegularMoonParams}.
	 *
	 * @param params a {@code RegularMoonParams} instance containing all necessary
	 *               parameters for creating regular moons
	 * @throws NullPointerException if <b>params</b> is {@code null}
	 */
	public static void writeRegularMoons(RegularMoonParams params) {
		var moons = generateRegularMoons(Objects.requireNonNull(params));
		sortBySemiMajorAxis(moons, rm -> rm.getOrbitalElements());
		ScriptFileWriter.writeToFile(moons, params.commonParams());
	}

	/**
	 * Generates and writes generic celestial objects (such as dwarf moons or
	 * asteroids) to a file.
	 * <p>
	 * The method creates objects based on the settings provided in the
	 * {@code GenericObjectParams}, sorts them by their semi-major axis, assigns
	 * sequential names using the parent body and a starting number, and then writes
	 * the generated script to the output file specified in the common parameters.
	 *
	 * @param params a {@code GenericObjectParams} instance controlling the object
	 *               count, orbital ranges, and the output file
	 * @throws NullPointerException if <b>params</b> is {@code null}
	 */
	public static void writeGenericObjects(GenericObjectParams params) {
		var objects = generateGenericObjects(Objects.requireNonNull(params));
		sortBySemiMajorAxis(objects, go -> go.getOrbitalElements());
		
		assignNamesInSortedOrder(objects, 
				params.commonParams().parentBody(), 
				params.objectType(), 
				params.startNumber());
		
		ScriptFileWriter.writeToFile(objects, params.commonParams());
	}

	/**
	 * Generates a SpaceEngine-compatible script for a set of comets based on the
	 * given parameters.
	 * <p>
	 * The comet generation process uses the provided {@code CometParams} (which
	 * wraps a {@code CommonParams} instance along with comet-specific parameters)
	 * to determine the common values (such as parent body, distance unit, and
	 * reference plane) and the comet-specific values (the minimum and maximum
	 * semi-major axis, total count, and starting sequence number).
	 * <p>
	 * A random percentage between 10% and 20% of the total comet count is used to
	 * form groups. Each group produces a barycenter with two associated comet
	 * satellites, while any remaining comets are generated as single objects. For
	 * each comet, only the radius is generated as a physical property, and other
	 * physical parameters (such as period, mass, and obliquity) are omitted. The
	 * complete script is then written to a file via {@code ScriptFileWriter}.
	 *
	 * @param params a {@code CometParams} object containing the necessary
	 *               parameters for generating the comet script
	 * @throws NullPointerException if <b>params</b> is {@code null}
	 */
	public static void writeComets(CometParams params) {
		var commonParams = Objects.requireNonNull(params).commonParams();
		int baseCount = params.count();
		var objects = new ArrayList<CelestialObject>(estimateTotalObjects(baseCount));

		// Compute the number of comet groups as a random 10–20% of the base
		// count, but ensure that at least one group is generated.
		int groups = Math.max(1, (int) Math.floor(baseCount * (RNG.nextDouble() * 0.1 + 0.1)));
		int singles = baseCount - groups;
		int seq = params.startingFrom();

		seq = addGroupedComets(objects, commonParams, params, seq, groups);
		seq = addSingleComets(objects, commonParams, params, seq, singles);

		ScriptFileWriter.writeToFile(objects, commonParams);
	}

	/*
	 * -------------------------------------------------------------------------
	 * Private Helpers
	 * -------------------------------------------------------------------------
	 */

	private static List<CelestialObject> generateRegularMoons(RegularMoonParams params) {
		int count = params.names().size();
		var objects = new ArrayList<CelestialObject>(count);

		for (int i = 0; i < count; i++) {
			var moonName = params.names().get(i);
			double radius = params.radii().get(i);
			double dist = params.distances().get(i);
			var moonClass = params.classes().get(i);

			// Orbital angles
			double ecc = randomInRange(params.minEccentricity(), params.maxEccentricity());
			double inc = randomInRange(params.minInclination(), params.maxInclination());
			double asc = estimateAscendingNode(inc);
			double arg = randomInRange(Constants.MIN_ARG_OF_PERICEN, Constants.MAX_ARG_OF_PERICEN);
			double ma = randomInRange(Constants.MIN_MEAN_ANOMALY, Constants.MAX_MEAN_ANOMALY);

			// Albedos
			double bond = randomInRange(params.minBondAlbedo(), params.maxBondAlbedo());
			double geom = bond + 0.04;

			var pProps = PhysicalProperties.builder()
					.radius(radius)
					.albedoBond(bond)
					.albedoGeom(geom)
					.build();

			var oElems = OrbitalElements.builder()
					.epoch(Constants.DEFAULT_EPOCH)
					.semiMajorAxis(dist)
					.eccentricity(ecc)
					.inclination(inc)
					.ascendingNode(asc)
					.argOfPericenter(arg)
					.meanAnomaly(ma)
					.build();

			var moon = CelestialObject.builder()
					.type(ObjectType.MOON)
					.name(moonName)
					.parentBody(params.commonParams().parentBody())
					.classification(moonClass)
					.physicalProperties(pProps)
					.orbitalElements(oElems)
					.build();

			objects.add(moon);
		}
		
		return objects;
	}
	
	private static List<CelestialObject> generateGenericObjects(GenericObjectParams params) {
		int count = params.count();
		var result = new ArrayList<CelestialObject>(count);
		for (int i = 0; i < count; i++) {
			double axis = randomInRange(params.minAxis(), params.maxAxis());
			double ecc = randomInRange(params.minEccentricity(), params.maxEccentricity());
			double inc = randomInRange(params.minInclination(), params.maxInclination());
			double asc = estimateAscendingNode(inc);
			double arg = randomInRange(Constants.MIN_ARG_OF_PERICEN, Constants.MAX_ARG_OF_PERICEN);
			double ma = randomInRange(Constants.MIN_MEAN_ANOMALY, Constants.MAX_MEAN_ANOMALY);

			double radius = randomInRange(Constants.MIN_GENERIC_RADIUS, Constants.MAX_GENERIC_RADIUS);
			double bond = randomInRange(0.07, 0.09);
			double geom = bond + 0.05;

			var pProps = PhysicalProperties.builder()
					.radius(radius)
					.albedoBond(bond)
					.albedoGeom(geom)
					.build();

			var oElems = OrbitalElements.builder()
					.epoch(Constants.DEFAULT_EPOCH)
					.semiMajorAxis(axis)
					.eccentricity(ecc)
					.inclination(inc)
					.ascendingNode(asc)
					.argOfPericenter(arg)
					.meanAnomaly(ma)
					.build();

			var obj = CelestialObject.builder()
					.type(params.objectType())
					.name(Constants.PLACEHOLDER_NAME)
					.parentBody(params.commonParams().parentBody())
					.physicalProperties(pProps)
					.orbitalElements(oElems)
					.build();

			result.add(obj);
		}
		
		return result;
	}
	
	// Comet generation logic methods
	
	private static int addGroupedComets(List<CelestialObject> objects, 
			CommonParams common,
			CometParams params, 
			int seq, 
			int groups) {
		for (int i = 0; i < groups; i++) {
			var groupName = common.parentBody() + ".C" + seq++;
			objects.add(createBarycenter(common, params, groupName));
			double baseArg = randomInRange(Constants.MIN_ARG_OF_PERICEN, Constants.MAX_ARG_OF_PERICEN);
			double argOffset = randomInRange(15, 25);
			objects.add(createCometSatellite(params, groupName, " A", baseArg));
			objects.add(createCometSatellite(params, groupName, " B", (baseArg + argOffset) % 360));
		}
		return seq;
	}

	private static int addSingleComets(List<CelestialObject> objects, 
			CommonParams common,
			CometParams params, 
			int seq, 
			int singles) {
		for (int i = 0; i < singles; i++) {
			var name = common.parentBody() + ".C" + seq++;
			objects.add(createSingleComet(common, params, name));
		}
		return seq;
	}
	
	private static CelestialObject createBarycenter(CommonParams common, 
			CometParams params,
			String groupName) {
		var orbit = generateOrbit(params.minAxis(), params.maxAxis());
		return CelestialObject.builder()
				.type(ObjectType.BARYCENTER)
				.name(groupName)
				.parentBody(common.parentBody())
				.orbitalElements(orbit)
				.build();
	}

	private static CelestialObject createCometSatellite(CometParams params, 
			String groupName, String suffix,
			double argOfPeriapsis) {
		var baseOrbit = generateOrbit(params.minAxis(), params.maxAxis());
		
		var pProps = PhysicalProperties.builder()
				.radius(randomInRange(Constants.MIN_COMET_RADIUS, Constants.MAX_COMET_RADIUS))
				.build();
		
		var oElems = OrbitalElements.builder()
				.epoch(baseOrbit.epoch())
				.semiMajorAxis(baseOrbit.semiMajorAxis())
				.eccentricity(baseOrbit.eccentricity())
				.inclination(baseOrbit.inclination())
				.ascendingNode(baseOrbit.ascendingNode())
				.argOfPericenter(argOfPeriapsis)
				.meanAnomaly(baseOrbit.meanAnomaly())
				.build();
		
		return CelestialObject.builder()
				.type(ObjectType.COMET)
				.name(groupName + suffix)
				.parentBody(groupName)
				.classification("Asteroid")
				.physicalProperties(pProps)
				.orbitalElements(oElems)
				.satellite(true)
				.build();
	}

	private static CelestialObject createSingleComet(CommonParams common, 
			CometParams params,
			String name) {
		var oElems = generateOrbit(params.minAxis(), params.maxAxis());
		
		var pProps = PhysicalProperties.builder()
				.radius(randomInRange(Constants.MIN_COMET_RADIUS, Constants.MAX_COMET_RADIUS))
				.build();
		
		return CelestialObject.builder()
				.type(ObjectType.COMET)
				.name(name)
				.parentBody(common.parentBody())
				.classification("Asteroid")
				.physicalProperties(pProps)
				.orbitalElements(oElems)
				.satellite(false)
				.build();
	}

	private static OrbitalElements generateOrbit(double minAxis, double maxAxis) {
		// Ensure the ascending node is set properly
		double inc = randomInRange(Constants.MIN_COMET_INCL, Constants.MAX_COMET_INCL);
		double asc = estimateAscendingNode(inc);
		
		return OrbitalElements.builder()
				.epoch(Constants.DEFAULT_EPOCH)
				.semiMajorAxis(randomInRange(minAxis, maxAxis))
				.eccentricity(randomInRange(Constants.MIN_COMET_ECC, Constants.MAX_COMET_ECC))
				.inclination(inc)
				.ascendingNode(asc) // Zero if inc is zero
				.argOfPericenter(randomInRange(Constants.MIN_ARG_OF_PERICEN, Constants.MAX_ARG_OF_PERICEN))
				.meanAnomaly(randomInRange(Constants.MIN_MEAN_ANOMALY, Constants.MAX_MEAN_ANOMALY))
				.build();
	}

	// Supporting internal utils 
	
	/**
	 * Renames objects in ascending order of their orbital distance, starting with
	 * the specified start value (e.g. parentBody.D1, parentBody.D2, etc.).
	 */
	private static void assignNamesInSortedOrder(List<CelestialObject> objects, String parentBody,
			ObjectType objectType, int startNumber) {
		for (int i = 0; i < objects.size(); i++) {
			var finalName = String.format("%s.%s%d", parentBody, objectType.getPrefix(), startNumber++);
			objects.get(i).setName(finalName);
		}
	}

	/**
	 * A generic helper that sorts a list of any type {@code T} by ascending
	 * semi-major axis. We supply a function that extracts the orbital elements from
	 * {@code T}, then compare by {@code semiMajorAxis}.
	 *
	 * @param list         the list to sort
	 * @param orbExtractor a function that extracts {@link OrbitalElements} from T
	 */
	private static <T> void sortBySemiMajorAxis(List<T> list, Function<T, OrbitalElements> orbExtractor) {
		list.sort(Comparator.comparingDouble(t -> orbExtractor.apply(t).semiMajorAxis()));
	}

	/**
	 * Ensures that the ascending node is zero if the inclination is zero.
	 * 
	 * @param inc the inclination
	 * @return the value of the ascending node, which is random if and only if the
	 *         inclination is greater than zero
	 */
	private static double estimateAscendingNode(double inc) {
		double minAsc = Constants.MIN_ASCENDING_NODE; // Zero
		double maxAsc = Constants.MAX_ASCENDING_NODE;
		return inc > minAsc ? randomInRange(minAsc, maxAsc) : minAsc;
	}

	/**
	 * Generates a random double value in the specified range and returns it.
	 * 
	 * @return the generated double value
	 */
	private static double randomInRange(double min, double max) {
		return min + (max - min) * RNG.nextDouble();
	}

	/**
	 * Computes the approximate worst-case initial capacity for the list of comet
	 * objects to be generated.
	 * <p>
	 * Given the user-specified base count (the number of comet entries requested),
	 * a random grouping mechanism designates a certain percentage (between 10% and
	 * 20%) of that count to form comet groups. Each group produces one barycenter
	 * plus two satellite comets, effectively adding two extra objects per group.
	 * This method uses a worst-case assumption of 20% grouping (rounded down, with
	 * at least 1 group) to ensure the {@code ArrayList} is created with sufficient
	 * initial capacity.
	 * <p>
	 * The total capacity is calculated as: <blockquote>baseCount + 2 * Math.max(1,
	 * floor(baseCount * 0.2))</blockquote>
	 *
	 * @param baseCount the base number of comet entries requested by the user
	 * @return the computed total capacity to be used as the initial capacity for
	 *         the list of generated objects
	 */
	private static int estimateTotalObjects(int baseCount) {
		int worstCaseGroups = Math.max(1, (int) Math.floor(baseCount * 0.2));
		return baseCount + 2 * worstCaseGroups;
	}
}
