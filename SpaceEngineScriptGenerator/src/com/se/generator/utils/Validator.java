package com.se.generator.utils;

/**
 * Generic utility class for validating ranges and throwing exceptions on
 * invalid input.
 */
public final class Validator {

	private Validator() {
		// Prevent instantiation
	}

	/**
	 * Ensures that min <= max and both are >= 0.
	 *
	 * @param min       the minimum value
	 * @param max       the maximum value
	 * @param fieldName name of the field being validated
	 * @throws IllegalArgumentException if the range is invalid
	 */
	public static void validateRange(double min, double max, String fieldName) {
		if (min < 0 || max < 0 || min > max) {
			throw new IllegalArgumentException("Invalid range for " + fieldName + ": [" + min + ", " + max + "]");
		}
	}
}
