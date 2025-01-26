package com.se.generator.io;

import java.util.Scanner;

/**
 * Special class that handles user input with validation and prompting.
 */
public class InputReader {

	private final Scanner scanner = new Scanner(System.in);

	/**
	 * Prompts the user to select a distance unit and returns that distance unit.
	 * 
	 * @return the selected distance unit
	 */
	public String promptDistanceUnit() {
		while (true) {
			System.out.println("\nDistance unit selection:");
			System.out.println("1. au");
			System.out.println("2. km");

			int choice = (int) promptDouble("- Enter your choice (1-2): ");
			switch (choice) {
			case 1 -> {
				return "AU";
			}
			case 2 -> {
				return "km";
			}
			default -> System.out.println("Invalid choice. Please select 1 or 2.");
			}
		}
	}

	/**
	 * Prompts the user to select a moon class.
	 *
	 * @return the selected moon class as a string
	 */
	public String promptMoonClass() {
		while (true) {
			System.out.println("\nClass selection");
			System.out.println("1. Ferria");
			System.out.println("2. Carbonia");
			System.out.println("3. Terra");
			System.out.println("4. Aquaria");

			int choice = (int) promptDouble("- Enter your choice (1-4): ");
			switch (choice) {
			case 1 -> {
				return "Ferria";
			}
			case 2 -> {
				return "Carbonia";
			}
			case 3 -> {
				return "Terra";
			}
			case 4 -> {
				return "Aquaria";
			}
			default -> System.out.println("Invalid choice. Please select 1, 2, 3, or 4.");
			}
		}
	}

	/**
	 * Prompts the user to select a reference plane.
	 *
	 * @return the selected reference plane as a string
	 */
	public String promptReferencePlane() {
		while (true) {
			System.out.println("\nReference plane selection");
			System.out.println("1. Static");
			System.out.println("2. Fixed");
			System.out.println("3. Equator");
			System.out.println("4. Ecliptic");
			System.out.println("5. Laplace");
			System.out.println("6. Extrasolar");

			int choice = (int) promptDouble("- Enter your choice (1-6): ");
			switch (choice) {
			case 1 -> {
				return "Static";
			}
			case 2 -> {
				return "Fixed";
			}
			case 3 -> {
				return "Equator";
			}
			case 4 -> {
				return "Ecliptic";
			}
			case 5 -> {
				return "Laplace";
			}
			case 6 -> {
				return "Extrasolar";
			}
			default -> System.out.println("Invalid choice. Please select 1, 2, 3, 4, 5, or 6.");
			}
		}
	}

	/**
	 * Prompts the user for a double value.
	 *
	 * @param prompt the message to display
	 * @return the entered double value
	 */
	public double promptDouble(String prompt) {
		while (true) {
			try {
				System.out.print(prompt);
				return Double.parseDouble(scanner.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid number.");
			}
		}
	}

	/**
	 * Prompts the user for a string value.
	 *
	 * @param prompt the message to display
	 * @return the entered string value
	 */
	public String promptString(String prompt) {
		System.out.print(prompt);
		return scanner.nextLine().trim();
	}
}
