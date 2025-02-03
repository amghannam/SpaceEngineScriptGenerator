package com.se.generator.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import com.se.generator.script.CelestialObject;
import com.se.generator.script.CommonParams;

/**
 * Static helper class for writing generated SpaceEngine scripts to {@code .sc}
 * files as output.
 */
public final class ScriptFileWriter {

	private ScriptFileWriter() {
		// Prevent instantiation
	}

	/**
	 * Writes the supplied list of {@code CelestialObject} objects to a file in
	 * SpaceEngine’s canonical {@code .sc} script format.
	 * <p>
	 * The output file is determined by the {@link CommonParams#outputFile()} value
	 * and will be overwritten if it already exists. Each celestial object in the
	 * supplied list is written as a separate line by invoking its {@code toScript}
	 * method with the distance unit and reference plane obtained from the provided
	 * {@code params}.
	 * <p>
	 * Note that if an I/O error occurs during writing, the error is logged to
	 * {@code System.err} and the exception is not propagated.
	 *
	 * @param objects the list of generated {@code CelestialObject} objects to write
	 *                to a file; must not be {@code null}. If the list is empty, an
	 *                empty file is created
	 * @param params  the common generation parameters containing attributes such as
	 *                the output file name, distance unit, and reference plane; must
	 *                not be {@code null}
	 * @throws NullPointerException if either argument is {@code null}
	 * @see CommonParams
	 * @see CelestialObject
	 */
	public static void writeToFile(List<CelestialObject> objects, CommonParams params) {
		Objects.requireNonNull(objects);
		Objects.requireNonNull(params);

		var fileName = params.outputFile();
		try (var writer = Files.newBufferedWriter(Path.of(fileName))) {
			for (var co : objects) {
				writer.write(co.toScript(params.distanceUnit(), params.referencePlane()));
				writer.newLine();
			}
			System.out.println("Script generation complete. Wrote " + objects.size() + " objects to file: " + fileName);
		} catch (IOException e) {
			System.err.println("Error writing file: " + e.getMessage());
		}
	}
}
