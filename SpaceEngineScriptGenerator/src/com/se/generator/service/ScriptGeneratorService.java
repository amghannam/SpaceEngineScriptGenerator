package com.se.generator.service;

import java.util.ArrayList;
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
import com.se.generator.utils.Validator;

/**
 * The {@code ScriptGeneratorService} class orchestrates the workflow for
 * generating various script files based on user input. It is responsible for:
 * <ul>
 * <li>Displaying a menu of available script generation options (e.g., Dwarf
 * Moon, Asteroid, Regular Moon, Comet Cloud).</li>
 * <li>Gathering common parameters such as parent body, distance unit, and
 * reference plane.</li>
 * <li>Prompting for and validating additional parameters specific to each
 * script type (e.g., ensuring numerical inputs are positive, within valid
 * ranges, or fractional when required), using the
 * {@link com.se.generator.utils.Validator} for validation.</li>
 * <li>Delegating the creation of scripts to the appropriate methods in the
 * {@link com.se.generator.script.ScriptGenerator}.</li>
 * </ul>
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

	// Error message templates
	private static final String ERROR_POSITIVE_INT = "Invalid input. Please enter a positive number greater than zero.";
	private static final String ERROR_POSITIVE_DOUBLE_TEMPLATE = "Invalid input for {%s}. Please enter a positive number greater than zero.";
	private static final String ERROR_RANGE_TEMPLATE = "Invalid range for {%s}: [%s, %s].";
	private static final String ERROR_FRACTION_RANGE_TEMPLATE = "Invalid range for {%s}: [%s, %s]. Values must be between 0 and 1 with min <= max.";
	private static final String ERROR_INCLINATION_RANGE_TEMPLATE = "Invalid range for {%s}: [%s, %s]. Values must be between 0 and 360 with min <= max.";

	// Predicates for common range validations:
	private static final Predicate<double[]> DEFAULT_RANGE_PREDICATE = createRangePredicate(0, Double.MAX_VALUE);
	
	private static final Predicate<double[]> FRACTION_RANGE_PREDICATE = createRangePredicate(
			Constants.MIN_ECCENTRICITY,
			Constants.MAX_ECCENTRICITY); // Also used for albedo as both are in [0, 1]
	
	private static final Predicate<double[]> INCLINATION_RANGE_PREDICATE = createRangePredicate(
			Constants.MIN_INCLINATION, 
			Constants.MAX_INCLINATION);

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
		System.out.println("\n- Select the type of object you'd like to generate:");
		System.out.println("1. Dwarf Moon");
		System.out.println("2. Asteroid");
		System.out.println("3. Moon (Regular Moons)");
		System.out.println("4. Comet Cloud");
		System.out.println("0. Exit");
	}

	private CommonInputParams gatherCommonParams() {
		var parentBody = inputReader.promptString("- Enter the parent body name: ");
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
			names.add(inputReader.promptString("- Moon name: "));
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

	/**
	 * Prompts for the semi-major axis range.
	 *
	 * @return an array of two doubles where index 0 is min axis and index 1 is max
	 *         axis
	 */
	private double[] promptAxisRange() {
		return promptValidatedRange("semi-major axis", 
				"- Min semi-major axis: ", 
				"- Max semi-major axis: ");
	}

	/**
	 * Prompts for the eccentricity range.
	 *
	 * @return an array of two doubles where index 0 is min eccentricity and index 1
	 *         is max eccentricity
	 */
	private double[] promptEccentricityRange() {
		return promptFractionRange("eccentricity", 
				"- Min eccentricity (0-1): ", 
				"- Max eccentricity (0-1): ");
	}

	/**
	 * Prompts for the inclination range.
	 *
	 * @return an array of two doubles where index 0 is min inclination and index 1
	 *         is max inclination
	 */
	private double[] promptInclinationRange() {
		return promptValidatedRange("inclination", 
				"- Min inclination (deg): ", 
				"- Max inclination (deg): ");
	}

	/**
	 * Prompts for an integer and validates that it is positive (> 0) using the
	 * generic validator.
	 *
	 * @param promptMessage the message to display when prompting the user
	 * @return a positive integer entered by the user
	 */
	private int promptPositiveInt(String promptMessage) {
		return promptAndValidate(() -> inputReader.promptInt(promptMessage),
				value -> Validator.validate(value, (Integer v) -> v > 0, ERROR_POSITIVE_INT));
	}

	/**
	 * Prompts for a double and validates that it is positive (> 0) using the
	 * generic validator.
	 *
	 * @param promptMessage the message to display when prompting the user
	 * @param fieldName     the name of the field being validated (used in error
	 *                      messages)
	 * @return a positive double entered by the user
	 */
	private double promptPositiveDouble(String promptMessage, String fieldName) {
		return promptAndValidate(() -> inputReader.promptDouble(promptMessage), 
				value -> Validator.validate(value, (Double v) -> v > 0, String.format(ERROR_POSITIVE_DOUBLE_TEMPLATE, fieldName)));
	}

	/**
	 * Generic helper method that repeatedly prompts the user via the supplied
	 * prompt function, validates the returned value with the provided validator,
	 * and only returns once a valid value is entered.
	 *
	 * @param promptSupplier a {@code Supplier} that prompts and returns a value
	 * @param validator      a {@code Consumer} that validates the value (throwing
	 *                       an exception if invalid)
	 * @param <T>            the type of the value to prompt and validate
	 * @return a validated value of type T
	 */
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

	/**
	 * Prompts for a range (min and max values) and validates the input. For
	 * "inclination" input, it uses the {@code INCLINATION_RANGE_PREDICATE},
	 * otherwise it uses the {@code DEFAULT_RANGE_PREDICATE}.
	 *
	 * @param fieldName the name of the field being validated
	 * @param promptMin the prompt for the minimum value
	 * @param promptMax the prompt for the maximum value
	 * @return an array of two doubles where index 0 is min and index 1 is max
	 */
	private double[] promptValidatedRange(String fieldName, String promptMin, String promptMax) {
		if ("inclination".equalsIgnoreCase(fieldName)) {
			return promptRange(fieldName, 
					promptMin, 
					promptMax, 
					INCLINATION_RANGE_PREDICATE,
					ERROR_INCLINATION_RANGE_TEMPLATE);
		}
		return promptRange(fieldName, 
				promptMin, 
				promptMax, 
				DEFAULT_RANGE_PREDICATE, 
				ERROR_RANGE_TEMPLATE);
	}

	/**
	 * Prompts for a fractional range (min and max values between 0 and 1) and
	 * validates the input.
	 *
	 * @param fieldName the name of the field being validated
	 * @param promptMin the prompt for the minimum value
	 * @param promptMax the prompt for the maximum value
	 * @return an array of two doubles where index 0 is min and index 1 is max
	 */
	private double[] promptFractionRange(String fieldName, String promptMin, String promptMax) {
		return promptRange(fieldName, 
				promptMin, 
				promptMax, 
				FRACTION_RANGE_PREDICATE, 
				ERROR_FRACTION_RANGE_TEMPLATE);
	}

	/**
	 * Helper method that repeatedly prompts for a range (min and max values) and
	 * validates them using the given predicate and error message template.
	 *
	 * @param fieldName     the name of the field being validated
	 * @param promptMin     the prompt for the minimum value
	 * @param promptMax     the prompt for the maximum value
	 * @param predicate     the predicate that validates the array {min, max}
	 * @param errorTemplate the error message template, formatted with
	 *                      <b>fieldName</b>, <b>min</b>, and <b>max</b>
	 * @return an array of two doubles where index 0 is min and index 1 is max
	 */
	private double[] promptRange(String fieldName, 
			String promptMin, 
			String promptMax, 
			Predicate<double[]> predicate,
			String errorTemplate) {
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
