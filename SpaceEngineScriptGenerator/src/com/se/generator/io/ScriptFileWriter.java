package com.se.generator.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.se.generator.script.CelestialObject;

/**
 * Static helper class for writing generated SpaceEngine scripts to .sc files as
 * output.
 */
public final class ScriptFileWriter {

	private ScriptFileWriter() {
		// Prevent instantiation
	}

	/**
	 * Writes the provided list of {@link CelestialObject} objects to the specified
	 * output file.
	 *
	 * <p>
	 * The file will be written in SpaceEngine's .sc format, with options to specify
	 * distance units and reference planes.
	 * </p>
	 *
	 * @param objects        the list of celestial objects to write to the file
	 * @param distanceUnit   the unit of distance to use in the script (e.g., AU,
	 *                       km)
	 * @param referencePlane the reference plane for celestial objects (e.g.,
	 *                       ecliptic)
	 * @param fileName       the name of the output file
	 */
	public static void writeToFile(List<CelestialObject> objects, 
			String distanceUnit, 
			String referencePlane,
			String fileName) {
		try (BufferedWriter writer = Files.newBufferedWriter(Path.of(fileName))) {
			for (var co : objects) {
				writer.write(co.toScript(distanceUnit, referencePlane));
				writer.newLine();
			}
			System.out.println("Script generation complete. Wrote " + objects.size() + " objects to file: " + fileName);
		} catch (IOException e) {
			System.err.println("Error writing file: " + e.getMessage());
		}
	}
}
