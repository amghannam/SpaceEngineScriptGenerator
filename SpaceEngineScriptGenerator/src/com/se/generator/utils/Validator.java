package com.se.generator.utils;

import java.util.function.Predicate;

/**
 * Generic utility class for validating input values using custom validation
 * rules.
 *
 * <p>
 * This class provides a single generic method to validate any value by applying
 * a user-supplied {@code Predicate}. If the predicate returns {@code false}, an
 * {@link IllegalArgumentException} is thrown with an error message (which can
 * be user-specified).
 *
 * <p>
 * Example usage:
 * 
 * <pre>
 * // Validate that an integer is positive:
 * Validator.validate(positiveValue, value -> value > 0, "Value must be positive");
 *
 * // Validate that a double array representing a range is valid (min and max are non-negative and min &lt;= max):
 * double[] range = { min, max };
 * Validator.validate(range, a -> a[0] >= 0 && a[1] >= 0 && a[0] <= a[1], "Invalid range: [" + min + ", " + max + "]");
 * </pre>
 *
 * @author Ahmed Ghannam
 * @version 1.0
 */
public final class Validator {

	private static final String DEFAULT_MESSAGE = "Error: The input value does not satisfy the indicated validation rule.";

	private Validator() {
		// Prevent instantiation
	}

	/**
	 * Validates the specified value using the given validation rule. If the
	 * validation rule returns {@code false} for the value, an
	 * {@link IllegalArgumentException} is thrown with a generic error message.
	 *
	 * @param <T>            the type of the value to validate
	 * @param value          the value to validate
	 * @param validationRule a {@code Predicate} that returns {@code true} if the
	 *                       value is valid, or {@code false} otherwise
	 * @throws IllegalArgumentException if the validation rule returns {@code false}
	 */
	public static <T> void validate(T value, Predicate<T> validationRule) {
		validate(value, validationRule, DEFAULT_MESSAGE);
	}

	/**
	 * Validates the specified value using the given validation rule. If the
	 * validation rule returns {@code false} for the value, an
	 * {@link IllegalArgumentException} is thrown with the specified error message.
	 *
	 * @param <T>            the type of the value to validate
	 * @param value          the value to validate
	 * @param validationRule a {@code Predicate} that returns {@code true} if the
	 *                       value is valid, or {@code false} otherwise
	 * @param errorMessage   the error message to include in the exception if the
	 *                       value is invalid
	 * @throws IllegalArgumentException if the validation rule returns {@code false}
	 */
	public static <T> void validate(T value, Predicate<T> validationRule, String errorMessage) {
		if (!validationRule.test(value)) {
			throw new IllegalArgumentException(errorMessage);
		}
	}
}
