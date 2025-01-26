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
		assignNamesInSortedOrder(objects, params.commonParams().parentBody(), params.objectType());
		ScriptFileWriter.writeToFile(objects, 
				params.commonParams().distanceUnit(), 
				params.commonParams().referencePlane(),
				params.commonParams().outputFile());
	}

	/**
	 * Generates barycenters and minimal child comets (a "comet cloud"), writing
	 * them all to a .sc file. Barycenters and comets are logically grouped and
	 * combined into a single list, sorted by ascending semi-major axis.
	 *
	 * @param params The {@link CometCloudParams} specifying how many barycenters,
	 *               their orbital ranges, and the output file location
	 */
	public static void writeCometSystems(CometCloudParams params) {
		ScriptFileWriter.writeToFile(generateCometSystems(params), 
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
			double ecc = randomInRange(params.minEcc(), params.maxEcc());
			double inc = randomInRange(params.minInc(), params.maxInc());
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
	 * Generates a list of celestial objects (barycenters and comets) based on the
	 * provided parameters.
	 *
	 * @param params the parameters for generating comet systems
	 * @return a list of celestial objects ready to be written to the .sc file
	 */
	private static List<CelestialObject> generateCometSystems(CometCloudParams params) {
		var groups = new ArrayList<BarycenterGroup>();

		// 1. Generate barycenters with temporary names and their comets
		for (int i = 1; i <= params.barycenterCount(); i++) {
			var tempBaryName = String.format("%s.TempC%d", params.commonParams().parentBody(), i);
			var barycenter = createBarycenter(tempBaryName, params);
			var comets = createChildComets(tempBaryName);
			groups.add(new BarycenterGroup(barycenter, comets));
		}

		// 2. Sort the groups based on the barycenters' semi-major axes (ascending
		// order)
		sortBySemiMajorAxis(groups, group -> group.barycenter().getOrbitalElements());

		// 3. Assign final names after sorting
		assignFinalNames(groups, params.commonParams().parentBody());

		// 4. Flatten the list: barycenter followed by its comets
		var finalList = new ArrayList<CelestialObject>();
		for (BarycenterGroup group : groups) {
			finalList.add(group.barycenter());
			finalList.addAll(group.comets());
		}

		return finalList;
	}

	/**
	 * Creates a barycenter with a temporary name.
	 *
	 * @param baryName the temporary name for the barycenter
	 * @param params   the parameters for generating the barycenter
	 * @return a {@link CelestialObject} representing the barycenter
	 */
	private static CelestialObject createBarycenter(String baryName, CometCloudParams params) {
		double sma = randomInRange(params.minAxis(), params.maxAxis());
		double ecc = randomInRange(0.6, 0.95);
		double inc = randomInRange(100, 160);
		double asc = randomInRange(Constants.MIN_ASCENDING_NODE, Constants.MAX_ASCENDING_NODE);
		double arg = randomInRange(Constants.MIN_ARG_OF_PERICEN, Constants.MAX_ARG_OF_PERICEN);
		double ma = randomInRange(Constants.MIN_MEAN_ANOMALY, Constants.MAX_MEAN_ANOMALY);

        var pPropsBary = PhysicalProperties.builder()
        		.build(); // Minimal physical properties
        
        var oElemsBary = OrbitalElements.builder()
                .epoch(Constants.DEFAULT_EPOCH)
                .semiMajorAxis(sma)
                .eccentricity(ecc)
                .inclination(inc)
                .ascendingNode(asc)
                .argOfPeriapsis(arg)
                .meanAnomaly(ma)
                .build();

        return CelestialObject.builder()
                .type(ObjectType.BARYCENTER)
                .name(baryName) // Temporary name
                .parentBody(params.commonParams().parentBody())
                .physicalProperties(pPropsBary)
                .orbitalElements(oElemsBary)
                .build();
    }

	/**
	 * Creates child comets with temporary names associated with a given barycenter.
	 *
	 * @param baryName the temporary name of the parent barycenter
	 * @return a list of {@link CelestialObject} representing the comets
	 */
	private static List<CelestialObject> createChildComets(String baryName) {
		var comets = new ArrayList<CelestialObject>();

		// Create Comet A
		var cometA = buildComet(baryName, "A");
		comets.add(cometA);

		// Create Comet B
		var cometB = buildComet(baryName, "B");
		comets.add(cometB);

		return comets;
	}

	/**
	 * Builds a comet with a temporary name.
	 *
	 * @param baryName the temporary name of the parent barycenter
	 * @param suffix   the suffix for the comet ("A" or "B")
	 * @return a {@link CelestialObject} representing the comet
	 */
	private static CelestialObject buildComet(String baryName, String suffix) {
		double sma = randomInRange(0.0005, 0.01);
		double ecc = randomInRange(Constants.MIN_ECCENTRICITY, Constants.MAX_ECCENTRICITY);
		double inc = randomInRange(Constants.MIN_INCLINATION, Constants.MAX_INCLINATION);
		double asc = randomInRange(Constants.MIN_ASCENDING_NODE, Constants.MAX_ASCENDING_NODE);
		double arg = randomInRange(Constants.MIN_ARG_OF_PERICEN, Constants.MAX_ARG_OF_PERICEN);
		double mean = randomInRange(Constants.MIN_MEAN_ANOMALY, Constants.MAX_MEAN_ANOMALY);

        var pPropsComet = PhysicalProperties.builder()
                .mass(randomInRange(1.0e-11, 1.0e-9))
                .radius(randomInRange(2.0, 15.0))
                .rotationPeriod(randomInRange(1.0, 12.0))
                .build();

        var oElemsComet = OrbitalElements.builder()
                .epoch(Constants.DEFAULT_EPOCH)
                .semiMajorAxis(sma)
                .eccentricity(ecc)
                .inclination(inc)
                .ascendingNode(asc)
                .argOfPeriapsis(arg)
                .meanAnomaly(mean)
                .build();

        // Temporary name; will be updated later
        var tempCometName = String.format("%s.Temp%s", baryName, suffix);

        return CelestialObject.builder()
                .type(ObjectType.COMET)
                .name(tempCometName) // Temporary name
                .parentBody(baryName)
                .classification("Asteroid")
                .physicalProperties(pPropsComet)
                .orbitalElements(oElemsComet)
                .build();
    }

	/**
	 * Renames objects in ascending order of their orbital axis, e.g.
	 * parentBody.DM1, parentBody.DM2, etc.
	 */
	private static void assignNamesInSortedOrder(List<CelestialObject> objects, String parentBody,
			ObjectType objectType) {
		for (int i = 0; i < objects.size(); i++) {
			var finalName = String.format("%s.%s%d", parentBody, objectType.getPrefix(), i + 1);
			objects.get(i).setName(finalName);
		}
	}

	/**
	 * Assigns sequential names to barycenters and their associated comets based on
	 * sorted order.
	 *
	 * @param groups     the list of sorted {@link BarycenterGroup}
	 * @param parentBody the name of the parent body (e.g., "Euthymia")
	 */
	private static void assignFinalNames(List<BarycenterGroup> groups, String parentBody) {
		for (int i = 0; i < groups.size(); i++) {
			int baryIndex = i + 1;
			var group = groups.get(i);

			// Assign final name to barycenter (e.g., "Euthymia.C1")
			var finalBaryName = String.format("%s.C%d", parentBody, baryIndex);
			group.barycenter().setName(finalBaryName);

			// Assign names to comets (e.g., "Euthymia.C1 A", "Euthymia.C1 B")
			for (int j = 0; j < group.comets().size(); j++) {
				var cometSuffix = (j == 0) ? "A" : "B";
				var finalCometName = String.format("%s %s", finalBaryName, cometSuffix);
				group.comets().get(j).setName(finalCometName);
			}
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
	
	/**
	 * A record to group a barycenter with its associated comets. Records are
	 * immutable and provide a concise syntax for data carriers.
	 */
	private static record BarycenterGroup(CelestialObject barycenter, List<CelestialObject> comets) {
	}
}
