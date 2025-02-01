package com.se.generator.service;

import java.util.ArrayList;

import com.se.generator.io.InputReader;
import com.se.generator.script.CometGenerationParams;
import com.se.generator.script.CommonGenerationParams;
import com.se.generator.script.GenericObjectParams;
import com.se.generator.script.ObjectType;
import com.se.generator.script.RegularMoonParams;
import com.se.generator.script.ScriptGenerator;
import com.se.generator.utils.Validator;

/**
 * Primary service class that puts together user input and invokes the
 * appropriate script-generation methods.
 */
public class ScriptGeneratorService {

	private final InputReader inputReader = new InputReader();

	/**
	 * Runs the script generation workflow by prompting the user for input.
	 */
	public void run() {
		while (true) {
			System.out.println("\nSelect the type of object you'd like to generate:");
			System.out.println("1. Dwarf Moon");
			System.out.println("2. Asteroid");
			System.out.println("3. Moon (Regular Moons)");
			System.out.println("4. Comet Cloud");
			System.out.println("0. Exit");

			double choice = inputReader.promptDouble("- Enter your choice: ");
			if (choice == 0) {
				System.out.println("Exiting...");
				return;
			}

			// Common mandatory user inputs for all generation:
			var parentBody = inputReader.promptString("- Enter the parent body name: ");
			var distanceUnit = inputReader.promptDistanceUnit();
			var referencePlane = inputReader.promptReferencePlane();

			switch ((int) choice) {
			case 1 -> {
				// Generate Dwarf Moons
				handleGenericObjectGeneration(ObjectType.DWARF_MOON, parentBody, distanceUnit, referencePlane);
			}
			case 2 -> {
				// Generate Asteroids
				handleGenericObjectGeneration(ObjectType.ASTEROID, parentBody, distanceUnit, referencePlane);
			}
			case 3 -> {
				// Generate "regular" (major) moons
				handleMoonGeneration(parentBody, distanceUnit, referencePlane);
			}
			case 4 -> {
				handleCometGeneration(parentBody, distanceUnit, referencePlane);
			}
			default -> System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	private void handleMoonGeneration(String parentBody, String distanceUnit, String referencePlane) {
		int count = inputReader.promptInt("- Enter the number of Regular Moons to generate: ");

		var names = new ArrayList<String>(count);
		var radii = new ArrayList<Double>(count);
		var distances = new ArrayList<Double>(count);
		var classes = new ArrayList<String>(count);

		for (int i = 0; i < count; i++) {
			System.out.printf("\n--- Moon %d ---\n", i + 1);
			names.add(inputReader.promptString("- Moon name: "));
			radii.add(inputReader.promptDouble("- Radius (km): "));
			distances.add(inputReader.promptDouble("- Orbital distance (" + distanceUnit + "): "));
			classes.add(inputReader.promptMoonClass());
		}

		double minEcc = inputReader.promptDouble("- Min eccentricity (0-1): ");
		double maxEcc = inputReader.promptDouble("- Max eccentricity (0-1): ");
		Validator.validateRange(minEcc, maxEcc, "eccentricity");

		double minInc = inputReader.promptDouble("- Min inclination (deg): ");
		double maxInc = inputReader.promptDouble("- Max inclination (deg): ");
		Validator.validateRange(minInc, maxInc, "inclination");

		double minBond = inputReader.promptDouble("- Min Bond albedo (0-1): ");
		double maxBond = inputReader.promptDouble("- Max Bond albedo (0-1): ");
		Validator.validateRange(minBond, maxBond, "Bond albedo");

		var fileName = parentBody + "_Moons.sc";

		var moonParams = RegularMoonParams.builder()
				.parentBody(parentBody)
				.distanceUnit(distanceUnit)
				.referencePlane(referencePlane)
				.names(names)
				.radii(radii)
				.distances(distances)
				.classes(classes)
				.minEccentricity(minEcc)
				.maxEccentricity(maxEcc)
				.minInclination(minInc)
				.maxInclination(maxInc)
				.minBondAlbedo(minBond)
				.maxBondAlbedo(maxBond)
				.outputFile(fileName)
				.build();

		ScriptGenerator.writeRegularMoons(moonParams);
	}

	private void handleGenericObjectGeneration(ObjectType objectType, String parentBody, String distanceUnit,
			String referencePlane) {
		double minAxis = inputReader.promptDouble("- Min semi-major axis: ");
		double maxAxis = inputReader.promptDouble("- Max semi-major axis: ");
		Validator.validateRange(minAxis, maxAxis, "semi-major axis");

		double minEcc = inputReader.promptDouble("- Min eccentricity (0-1): ");
		double maxEcc = inputReader.promptDouble("- Max eccentricity (0-1): ");
		Validator.validateRange(minEcc, maxEcc, "eccentricity");

		double minInc = inputReader.promptDouble("- Min inclination (deg): ");
		double maxInc = inputReader.promptDouble("- Max inclination (deg): ");
		Validator.validateRange(minInc, maxInc, "inclination");

		int count = inputReader.promptInt("- Number of objects: ");
		int startNumber = inputReader.promptInt("- Starting value in generated sequence: ");
		var fileName = parentBody + "_" + objectType.getFormattedName() + ".sc";

		var genParams = GenericObjectParams.builder()
				.objectType(objectType)
				.parentBody(parentBody)
				.distanceUnit(distanceUnit)
				.referencePlane(referencePlane)
				.minAxis(minAxis)
				.maxAxis(maxAxis)
				.minEccentricity(minEcc)
				.maxEccentricity(maxEcc)
				.minInclination(minInc)
				.maxInclination(maxInc)
				.count(count)
				.startingFrom(startNumber)
				.outputFile(fileName)
				.build();

		ScriptGenerator.writeGenericObjects(genParams);
	}
	
    private void handleCometGeneration(String parentBody, String distanceUnit, String referencePlane) {
		double minAxis = inputReader.promptDouble("- Min semi-major axis: ");
		double maxAxis = inputReader.promptDouble("- Max semi-major axis: ");
		int baseCount = inputReader.promptInt("- Base number of objects to generate: ");
		Validator.validateRange(minAxis, maxAxis, "semi-major axis");

		var outputFile = parentBody + "_CometCloud.sc";
		var common = CommonGenerationParams.builder()
				.parentBody(parentBody)
				.distanceUnit(distanceUnit)
				.referencePlane(referencePlane)
				.outputFile(outputFile)
				.build();

		// Build comet–specific parameters
		var cometParams = CometGenerationParams.builder()
				.commonParams(common)
				.minAxis(minAxis)
				.maxAxis(maxAxis)
				.count(baseCount)
				.build();

		ScriptGenerator.writeComets(cometParams);
	}
}
