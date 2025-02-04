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
 * {@code IllegalArgumentException} is thrown with an error message (which can
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

	private static final String DEFAULT_MESSAGE = "The input value does not satisfy the indicated condition.";

	private Validator() {
		// Prevent instantiation
	}

	/**
	 * Validates the specified <b>value</b> against the specified <b>condition</b>.
	 * If the condition evaluates to {@code false} for the value, an
	 * {@code IllegalArgumentException} is thrown with a generic error message.
	 *
	 * @param <T>       the type of the value to validate
	 * @param value     the value to validate; must not be {@code null}
	 * @param condition the validation rule, which is a {@code Predicate} that
	 *                  returns {@code true} if the value is valid, or {@code false}
	 *                  otherwise; must not be {@code null}
	 * @throws IllegalArgumentException if the specified value fails to satisfy the
	 *                                  indicated condition
	 * @throws NullPointerException     if either argument is {@code null}
	 */
	public static <T> void validate(T value, Predicate<T> condition) {
		validate(value, condition, DEFAULT_MESSAGE);
	}

	/**
	 * Validates the specified <b>value</b> against the specified <b>condition</b>.
	 * If the condition evaluates to {@code false} for the value, an
	 * {@code IllegalArgumentException} is thrown with the specified error message.
	 *
	 * @param <T>          the type of the value to validate
	 * @param value        the value to validate; must not be {@code null}
	 * @param condition    the validation rule, which is a {@code Predicate} that
	 *                     returns {@code true} if the value is valid, or
	 *                     {@code false} otherwise; must not be {@code null}
	 * @param errorMessage the error message to include in the exception if the
	 *                     value is invalid
	 * @throws IllegalArgumentException if the specified value fails to satisfy the
	 *                                  indicated condition
	 * @throws NullPointerException     if either <b>value</b> or <b>condition</b>
	 *                                  is {@code null}
	 */
	public static <T> void validate(T value, Predicate<T> condition, String errorMessage) {
		Objects.requireNonNull(value, "'value' must not be null");
		Objects.requireNonNull(condition, "'condition' must not be null");

		if (!condition.test(value)) {
			throw new IllegalArgumentException(errorMessage);
		}
	}
}
