package com.se.generator.utils;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Generic utility class for validating input values using custom validation
 * rules.
 *
 * <p>
 * This class provides a single generic method to validate any value by applying
 * a user-supplied {@code Predicate}. If the predicate returns {@code false}, an
 * {@code IllegalArgumentException} is thrown with a basic error message (which
 * can be user-specified).
 *
 * <p>
 * Example usage:
 * 
 * <pre>
 * // Validate that a double array representing a range is valid (i.e. min &lt;= max):
 * double[] range = { min, max };
 * Validator.validate(range, a -> a[0] <= a[1]); // Displays a default error message
 * 
 * // Validate that an integer is positive (using a custom error message):
 * Validator.validate(positiveValue, value -> value > 0, "Value must be positive");
 * </pre>
 * 
 * @author Ahmed Ghannam
 * @version 1.0
 */
public final class Validator {

	private static final String DEFAULT_ERROR_MESSAGE = "The value {%s} does not satisfy the condition.";

	private Validator() {
		// Prevent instantiation
	}

	/**
	 * Validates the specified value against the specified condition. If the
	 * condition evaluates to {@code false} for the value given, an
	 * {@code IllegalArgumentException} is thrown with a default error message that
	 * includes the invalid value.
	 * 
	 * <p>
	 * Note that the value to check is permitted to be {@code null}; it is left up
	 * to the caller to decide if this is acceptable by defining an appropriate
	 * condition.
	 * 
	 * @param <T>       the type of the value to validate
	 * @param value     the value to validate or test; may be {@code null}
	 * @param condition the validation rule, which is a {@code Predicate} that
	 *                  returns {@code true} if the value is valid; must not be
	 *                  {@code null}
	 * @throws IllegalArgumentException if the specified value fails to satisfy the
	 *                                  indicated condition
	 * @throws NullPointerException     if <b>condition</b> is {@code null}
	 */
	public static <T> void validate(T value, Predicate<T> condition) {
		validate(value, condition, String.format(DEFAULT_ERROR_MESSAGE, value));
	}

	/**
	 * Validates the specified value against the specified condition. If the
	 * condition evaluates to {@code false} for the value given, an
	 * {@code IllegalArgumentException} is thrown with the specified error message.
	 *
	 * <p>
	 * Note that the value to check is permitted to be {@code null}; it is left up
	 * to the caller to decide if this is acceptable by defining an appropriate
	 * condition.
	 *
	 * @param <T>          the type of the value to validate
	 * @param value        the value to validate or test; may be {@code null}
	 * @param condition    the validation rule, which is a {@code Predicate} that
	 *                     returns {@code true} if the value is valid; must not be
	 *                     {@code null}
	 * @param errorMessage the error message to include in the exception if the
	 *                     value is invalid; must not be {@code null}
	 * @throws IllegalArgumentException if the specified value fails to satisfy the
	 *                                  indicated condition
	 * @throws NullPointerException     if <b>condition</b> or <b>errorMessage</b>
	 *                                  is {@code null}
	 */
	public static <T> void validate(T value, Predicate<T> condition, String errorMessage) {
		Objects.requireNonNull(condition, "validation condition must not be null");
		Objects.requireNonNull(errorMessage, "error message must not be null");

		if (!condition.test(value)) {
			throw new IllegalArgumentException(errorMessage);
		}
	}
}
