package com.se.generator.script;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import com.se.generator.io.ScriptFileWriter;

/**
 * Core class responsible for generating {@link CelestialObject} instances in
 * the standard SpaceEngine format.
 */
public final class ScriptGenerator {

	private static final Random RNG = ThreadLocalRandom.current();

	private ScriptGenerator() {
		// Prevent instantiation
	}

	/**
	 * Generates {@link CelestialObject} objects representing regular moons, then
	 * writes them to a .sc file specified by {@link RegularMoonParams}.
	 *
	 * @param params a {@link RegularMoonParams} instance containing all necessary
	 *               parameters for creating regular moons
	 */
	public static void writeRegularMoons(RegularMoonParams params) {
		var moons = generateRegularMoons(params);
		sortBySemiMajorAxis(moons, rm -> rm.getOrbitalElements());
		
		ScriptFileWriter.writeToFile(moons, 
				params.commonParams().distanceUnit(), 
				params.commonParams().referencePlane(),
				params.commonParams().outputFile());
	}

	/**
	 * Generates and writes generic objects (Dwarf Moons or Asteroids) to a file,
	 * based on the provided {@link GenericObjectParams}.
	 *
	 * @param params the {@link GenericObjectParams} controlling object count,
	 *               orbital ranges, and the output file
	 */
	public static void writeGenericObjects(GenericObjectParams params) {
		var objects = generateGenericObjects(params);
		sortBySemiMajorAxis(objects, go -> go.getOrbitalElements());
		
		assignNamesInSortedOrder(objects, 
				params.commonParams().parentBody(), 
				params.objectType(), 
				params.startNumber());
		
		ScriptFileWriter.writeToFile(objects, 
				params.commonParams().distanceUnit(), 
				params.commonParams().referencePlane(),
				params.commonParams().outputFile());
	}

	/*
	 * -------------------------------------------------------------------------
	 * Private Helpers
	 * -------------------------------------------------------------------------
	 */

	/**
	 * Generates a list of regular moon celestial objects based on the provided
	 * parameters.
	 *
	 * @param params an instance of {@link RegularMoonParams} containing the
	 *               necessary data for generating regular moons, including names,
	 *               radii, distances, classifications, and orbital element ranges.
	 * @return a {@link List} of {@link CelestialObject} instances representing the
	 *         generated moons.
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
			double asc = randomInRange(Constants.MIN_ASCENDING_NODE, Constants.MAX_ASCENDING_NODE);
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
					.argOfPeriapsis(arg)
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

	/**
	 * Generates a list of generic celestial objects based on the provided
	 * parameters.
	 *
	 * @param params an instance of {@link GenericObjectParams} containing the
	 *               necessary data for generating generic objects, including
	 *               orbital element ranges, physical properties ranges, object
	 *               type, and parent body information.
	 * @return a {@link List} of {@link CelestialObject} instances representing the
	 *         generated objects.
	 */
	private static List<CelestialObject> generateGenericObjects(GenericObjectParams params) {
		int count = params.count();
		var result = new ArrayList<CelestialObject>(count);
		for (int i = 0; i < count; i++) {
			double axis = randomInRange(params.minAxis(), params.maxAxis());
			double ecc = randomInRange(params.minEccentricity(), params.maxEccentricity());
			double inc = randomInRange(params.minInclination(), params.maxInclination());
			double asc = randomInRange(Constants.MIN_ASCENDING_NODE, Constants.MAX_ASCENDING_NODE);
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
					.argOfPeriapsis(arg)
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
	 * Generates a random double value in the specified range and returns it.
	 * 
	 * @return the generated double value
	 */
	private static double randomInRange(double min, double max) {
		return min + (max - min) * RNG.nextDouble();
	}
}
