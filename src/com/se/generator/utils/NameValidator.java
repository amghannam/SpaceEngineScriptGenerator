package com.se.generator.utils;

import java.util.regex.Pattern;

/**
 * Special utility class for validating user-supplied names using a combination
 * of pre-compiled regular expressions. Internally, this class invokes the
 * generic {@link Validator} to perform the required validation.
 *
 * <p>
 * For all user-supplied names, the following rules are enforced:
 * <ul>
 * <li>The name must not be {@code null}.</li>
 * <li>Length must be at least 1 and at most 20 characters.</li>
 * <li>The name must start with a letter.</li>
 * <li>The name must contain only letters, digits, and spaces.</li>
 * </ul>
 *
 * @author Ahmed Ghannam
 * @version 1.0
 */
public final class NameValidator {

	// Ensures the string starts with a letter.
	private static final Pattern STARTS_WITH_LETTER = Pattern.compile("^[a-zA-Z].*");

	// Ensures the string contains only letters, digits, and spaces.
	private static final Pattern VALID_CHARACTERS = Pattern.compile("^[a-zA-Z0-9 ]+$");

	private NameValidator() {
		// Prevent instantiation
	}

	/**
	 * Validates a generic name (which is assumed to be a moon or a parent body
	 * name) according to the following rules:
	 * <ul>
	 * <li>The name must not be {@code null}.</li>
	 * <li>Length must be at least 1 and at most 20 characters.</li>
	 * <li>The name must start with a letter.</li>
	 * <li>The name must contain only letters, digits, and spaces.</li>
	 * </ul>
	 *
	 * @param name      the name to validate
	 * @param fieldName a label for the name (used in error messages)
	 * @throws IllegalArgumentException if the name does not meet any of the
	 *                                  criteria
	 */
	public static void validate(String name, String fieldName) {
		// Check that the name is not null.
		Validator.validate(name, n -> n != null, fieldName + " cannot be null.");
		// Check length is at least 1.
		Validator.validate(name, n -> n.length() >= 1, fieldName + " must be at least 1 character long.");
		// Check length is at most 20.
		Validator.validate(name, n -> n.length() <= 20, fieldName + " must be at most 20 characters long.");
		// Check that the name starts with a letter.
		Validator.validate(name, n -> STARTS_WITH_LETTER.matcher(n).matches(),
				fieldName + " must start with a letter.");
		// Check that the name contains only letters, digits, and spaces.
		Validator.validate(name, n -> VALID_CHARACTERS.matcher(n).matches(),
				fieldName + " must contain only letters, digits, and spaces.");
	}
}
