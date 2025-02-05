package com.se.generator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.se.generator.io.InputReader;
import com.se.generator.script.CometParams;
import com.se.generator.script.Constants;
import com.se.generator.script.GenericObjectParams;
import com.se.generator.script.ObjectType;
import com.se.generator.script.RegularMoonParams;
import com.se.generator.script.ScriptGenerator;
import com.se.generator.utils.NameValidator;
import com.se.generator.utils.Validator;

/**
 * The {@code ScriptGeneratorService} class is the primary engine that
 * orchestrates the workflow for generating various SpaceEngine script files
 * based on user input.
 *
 * <p>
 * To start the workflow, invoke the {@link #run()} method. The application will
 * continue to prompt the user until the exit command is selected.
 *
 * @author Ahmed Ghannam
 * @version 1.0
 */
public class ScriptGeneratorService {

	private final InputReader inputReader = new InputReader();

	// Common error message templates
	private static final String ERROR_POSITIVE_INT = "Invalid input. Please enter a positive number greater than zero.";
	private static final String ERROR_POSITIVE_DOUBLE_TEMPLATE = "Invalid input for {%s}. Please enter a positive number greater than zero.";
	private static final String ERROR_RANGE_TEMPLATE = "Invalid range for {%s}: [%s, %s].";
	private static final String ERROR_FRACTION_RANGE_TEMPLATE = "Invalid range for {%s}: [%s, %s]. Values must be between 0 and 1 with min <= max.";
	private static final String ERROR_INCLINATION_RANGE_TEMPLATE = "Invalid range for {%s}: [%s, %s]. Values must be between 0 and 360 with min <= max.";

	// Predicates for common range validations:
	private static final Predicate<double[]> DEFAULT_RANGE_PREDICATE = createRangePredicate(0, Double.MAX_VALUE);
	private static final Predicate<double[]> FRACTION_RANGE_PREDICATE = createRangePredicate(Constants.MIN_ECCENTRICITY,
			Constants.MAX_ECCENTRICITY); // Also used for albedo as both are in [0, 1]
	private static final Predicate<double[]> INCLINATION_RANGE_PREDICATE = createRangePredicate(
			Constants.MIN_INCLINATION, Constants.MAX_INCLINATION);

	/**
	 * Runs the main script generation workflow by prompting the user for input.
	 */
	public void run() {
		while (true) {
			displayMenu();
			double choice = inputReader.promptDouble("- Enter your choice: ");
			if (choice == 0) {
				System.out.println("Exiting...");
				return;
			}

			var common = gatherCommonParams();

			switch ((int) choice) {
			case 1 -> handleGenericObjectGeneration(common, ObjectType.DWARF_MOON);
			case 2 -> handleGenericObjectGeneration(common, ObjectType.ASTEROID);
			case 3 -> handleRegularMoonGeneration(common);
			case 4 -> handleCometCloudGeneration(common);
			default -> System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	/*
	 * -------------------------------------------------------------------------
	 * Private Helpers
	 * -------------------------------------------------------------------------
	 */

	private void displayMenu() {
		System.out.println("\n- Specify the type of object to generate:");
		System.out.println("1. Dwarf Moon");
		System.out.println("2. Asteroid");
		System.out.println("3. Moon (Regular Moons)");
		System.out.println("4. Comet Cloud");
		System.out.println("0. Exit");
	}

	private CommonInputParams gatherCommonParams() {
		var parentBody = promptValidatedName("- Enter the parent body name: ", "Parent body name");
		var distanceUnit = inputReader.promptDistanceUnit();
		var referencePlane = inputReader.promptReferencePlane();
		return new CommonInputParams(parentBody, distanceUnit, referencePlane);
	}

	// --- OBJECT GENERATION HELPERS ---

	private void handleGenericObjectGeneration(CommonInputParams common, ObjectType objectType) {
		double[] axisRange = promptAxisRange();
		double minAxis = axisRange[0];
		double maxAxis = axisRange[1];

		double[] eccRange = promptEccentricityRange();
		double minEcc = eccRange[0];
		double maxEcc = eccRange[1];

		double[] incRange = promptInclinationRange();
		double minInc = incRange[0];
		double maxInc = incRange[1];

		int count = promptPositiveInt("- Number of objects: ");
		int seq = promptPositiveInt("- Starting value in generated sequence: ");

		var fileName = common.parentBody() + "_" + objectType.getFormattedName() + "s.sc"; // Append the plural "s"

		var genParams = GenericObjectParams.builder()
				.objectType(objectType)
				.parentBody(common.parentBody())
				.distanceUnit(common.distanceUnit())
				.referencePlane(common.referencePlane())
				.minAxis(minAxis)
				.maxAxis(maxAxis)
				.minEccentricity(minEcc)
				.maxEccentricity(maxEcc)
				.minInclination(minInc)
				.maxInclination(maxInc)
				.count(count)
				.startingFrom(seq)
				.outputFile(fileName)
				.build();

		ScriptGenerator.writeGenericObjects(genParams);
	}

	private void handleRegularMoonGeneration(CommonInputParams common) {
		int count = promptPositiveInt("- Number of Regular Moons to generate: ");

		var names = new ArrayList<String>(count);
		var radii = new ArrayList<Double>(count);
		var distances = new ArrayList<Double>(count);
		var classes = new ArrayList<String>(count);
		for (int i = 0; i < count; i++) {
			System.out.printf("\n--- Moon %d ---\n", i + 1);
			names.add(promptMoonName(common.parentBody(), names));
			radii.add(promptPositiveDouble("- Radius (km): ", "Radius"));
			distances.add(
					promptPositiveDouble("- Orbital distance (" + common.distanceUnit() + "): ", "Orbital distance"));
			classes.add(inputReader.promptMoonClass());
		}

		double[] eccRange = promptEccentricityRange();
		double minEcc = eccRange[0];
		double maxEcc = eccRange[1];

		double[] incRange = promptInclinationRange();
		double minInc = incRange[0];
		double maxInc = incRange[1];

		double[] bondRange = promptFractionRange("Bond albedo", 
				"- Min Bond albedo (0-1): ",
				"- Max Bond albedo (0-1): ");
		double minBond = bondRange[0];
		double maxBond = bondRange[1];

		var fileName = common.parentBody() + "_Moons.sc";

		var moonParams = RegularMoonParams.builder()
				.parentBody(common.parentBody())
				.distanceUnit(common.distanceUnit())
				.referencePlane(common.referencePlane())
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

	private void handleCometCloudGeneration(CommonInputParams common) {
		double[] axisRange = promptAxisRange();
		double minAxis = axisRange[0];
		double maxAxis = axisRange[1];

		int baseCount = promptPositiveInt("- Base number of objects to generate: ");
		var fileName = common.parentBody() + "_CometCloud.sc";

		var cometParams = CometParams.builder()
				.parentBody(common.parentBody())
				.distanceUnit(common.distanceUnit())
				.referencePlane(common.referencePlane())
				.outputFile(fileName)
				.minAxis(minAxis)
				.maxAxis(maxAxis)
				.count(baseCount)
				.build();

		ScriptGenerator.writeComets(cometParams);
	}

	// --- INTERNAL INPUT HANDLING AND VALIDATION ---

	private double[] promptAxisRange() {
		return promptValidatedRange("semi-major axis", 
				"- Min semi-major axis: ", 
				"- Max semi-major axis: ");
	}

	private double[] promptEccentricityRange() {
		return promptFractionRange("eccentricity", 
				"- Min eccentricity (0-1): ", 
				"- Max eccentricity (0-1): ");
	}

	private double[] promptInclinationRange() {
		return promptValidatedRange("inclination", 
				"- Min inclination (deg): ", 
				"- Max inclination (deg): ");
	}

	private int promptPositiveInt(String promptMessage) {
		return promptAndValidate(() -> inputReader.promptInt(promptMessage),
				value -> Validator.validate(value, (Integer v) -> v > 0, ERROR_POSITIVE_INT));
	}

	private double promptPositiveDouble(String promptMessage, String fieldName) {
		return promptAndValidate(() -> inputReader.promptDouble(promptMessage), 
				value -> Validator.validate(value, (Double v) -> v > 0, String.format(ERROR_POSITIVE_DOUBLE_TEMPLATE, fieldName)));
	}

	private <T> T promptAndValidate(Supplier<T> promptSupplier, Consumer<T> validator) {
		while (true) {
			T value = promptSupplier.get();
			try {
				validator.accept(value);
				return value;
			} catch (IllegalArgumentException ex) {
				System.out.println("Error: " + ex.getMessage());
			}
		}
	}

	private double[] promptValidatedRange(String fieldName, String promptMin, String promptMax) {
		if ("inclination".equalsIgnoreCase(fieldName)) {
			return promptAndValidateRange(fieldName, 
					promptMin, 
					promptMax, 
					INCLINATION_RANGE_PREDICATE,
					ERROR_INCLINATION_RANGE_TEMPLATE);
		}
		return promptAndValidateRange(fieldName, 
				promptMin, 
				promptMax, 
				DEFAULT_RANGE_PREDICATE, 
				ERROR_RANGE_TEMPLATE);
	}

	private double[] promptFractionRange(String fieldName, String promptMin, String promptMax) {
		return promptAndValidateRange(fieldName, 
				promptMin, 
				promptMax, 
				FRACTION_RANGE_PREDICATE,
				ERROR_FRACTION_RANGE_TEMPLATE);
	}

	private double[] promptAndValidateRange(String fieldName, String promptMin, String promptMax,
			Predicate<double[]> predicate, String errorTemplate) {
		while (true) {
			double min = inputReader.promptDouble(promptMin);
			double max = inputReader.promptDouble(promptMax);
			try {
				Validator.validate(new double[] { min, max }, predicate,
						String.format(errorTemplate, fieldName, min, max));
				return new double[] { min, max };
			} catch (IllegalArgumentException ex) {
				System.out.println("Error: " + ex.getMessage());
				System.out.println("Please re-enter the values for " + fieldName + ".");
			}
		}
	}

	// --- NAME VALIDATION HELPERS ---

	private String promptValidatedName(String promptMessage, String fieldName) {
		while (true) {
			var name = inputReader.promptString(promptMessage);
			try {
				NameValidator.validate(name, fieldName);
				return name;
			} catch (IllegalArgumentException ex) {
				System.out.println("Error: " + ex.getMessage());
			}
		}
	}

	private String promptMoonName(String parentName, List<String> existingNames) {
		while (true) {
			var name = inputReader.promptString("- Moon name: ");
			try {
				NameValidator.validate(name, "Moon name");
				if (name.equalsIgnoreCase(parentName)) {
					throw new IllegalArgumentException("Moon name must not be the same as the parent body name.");
				}
				for (var en : existingNames) {
					if (name.equalsIgnoreCase(en)) {
						throw new IllegalArgumentException("Moon names must be unique.");
					}
				}
				return name;
			} catch (IllegalArgumentException ex) {
				System.out.println("Error: " + ex.getMessage());
			}
		}
	}

	// --- RANGE VALIDATION HELPER ---

	/**
	 * Creates a predicate that, given an array of two doubles, ensures:
	 * <ul>
	 * <li>a[0] is greater than or equal to {@code lowerBound},</li>
	 * <li>a[1] is less than or equal to {@code upperBound}, and</li>
	 * <li>a[0] is less than or equal to a[1].</li>
	 * </ul>
	 *
	 * @param lowerBound the minimum acceptable value for a[0]
	 * @param upperBound the maximum acceptable value for a[1]
	 * @return a predicate that validates a double array of size 2
	 */
	private static Predicate<double[]> createRangePredicate(double lowerBound, double upperBound) {
		return a -> a[0] >= lowerBound && a[1] <= upperBound && a[0] <= a[1];
	}

	// Internal record to hold common input values
	private record CommonInputParams(String parentBody, String distanceUnit, String referencePlane) {
	}
}
