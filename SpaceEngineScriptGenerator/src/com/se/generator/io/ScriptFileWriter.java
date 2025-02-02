package com.se.generator.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.se.generator.script.CelestialObject;
import com.se.generator.script.CommonParams;

/**
 * Static helper class for writing generated SpaceEngine scripts to .sc files as
 * output.
 */
public final class ScriptFileWriter {

	private ScriptFileWriter() {
		// Prevent instantiation
	}

	/**
	 * Writes the supplied list of {@code CelestialObject} objects to an output
	 * file, as defined in the specified {@code CommonParams} instance.
	 * <p>
	 * The resulting file will be in SpaceEngine's canonical .sc format.
	 *
	 * @param objects the list of generated objects to write to a file
	 * @param params  the common generation parameters shared by the objects in the
	 *                specified list
	 */
	public static void writeToFile(List<CelestialObject> objects, CommonParams params) {
		var fileName = params.outputFile();
		try (var writer = Files.newBufferedWriter(Path.of(fileName))) {
			for (var co : objects) {
				writer.write(co.toScript(params.distanceUnit(), params.referencePlane()));
				writer.newLine();
			}
			System.out.println("Script generation complete. Wrote " + objects.size() 
								+ " objects to file: " + fileName);
		} catch (IOException e) {
			System.err.println("Error writing file: " + e.getMessage());
		}
	}
}
