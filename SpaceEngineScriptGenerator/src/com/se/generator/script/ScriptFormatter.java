package com.se.generator.script;

import java.util.Objects;

/**
 * Utility class for formatting a {@link CelestialObject} into a
 * SpaceEngine-compatible .sc script text.
 */
public final class ScriptFormatter {

	private ScriptFormatter() {
		// Prevent instantiation
	}

	/**
	 * Produces the .sc script text for the given {@link CelestialObject}, including
	 * lines for Barycenter, Comet, Asteroid, etc.
	 *
	 * @param co             the object to format
	 * @param distanceUnit   "AU" or "km" for how to represent the semi-major axis
	 * @param referencePlane e.g. "Equator", used in the Orbit block
	 * @return a string containing the .sc script representation of the object
	 */
	public static String format(CelestialObject co, String distanceUnit, String referencePlane) {
		StringBuilder sb = new StringBuilder();

		// 1) Label
		String objectLabel = determineLabel(co);

		// 2) Header
		sb.append(String.format("%s \"%s\"\n{\n", objectLabel, co.getName()));
		sb.append(String.format("    ParentBody\t\"%s\"\n", co.getParentBody()));

		// 3) Physical / classification lines
		appendPhysicalBlock(sb, co);

		// 4) Orbit block
		appendOrbitBlock(sb, co, distanceUnit, referencePlane);

		sb.append("}\n");
		return sb.toString();
	}

	/* -------------------- Private Helpers below -------------------- */

	private static String determineLabel(CelestialObject co) {
		if (co.getType() == ObjectType.BARYCENTER) {
			return "Barycenter";
		} else if (co.getType() == ObjectType.COMET) {
			return "Comet";
		}
		// fallback for MOON, DWARF_MOON, ASTEROID, etc.
		return Objects.nonNull(co.getType()) ? co.getType().getFormattedName() : "Object";
	}

	private static void appendPhysicalBlock(StringBuilder sb, CelestialObject co) {
		ObjectType type = co.getType();
		PhysicalProperties props = co.getPhysicalProperties();

		// If no physical properties exist, skip
		if (props == null) {
			return;
		}

		// Barycenter typically doesn't list radius or mass
		if (type == ObjectType.BARYCENTER) {
			// No physical block for barycenter
			return;
		}

		// Print classification if not null
		String objClass = (co.getClassification() == null || co.getClassification().isBlank()) ? "Asteroid"
				: co.getClassification();
		sb.append(String.format("    Class\t\"%s\"\n", objClass));

		// Print mass for comets or asteroids if available
		if (type == ObjectType.COMET || type == ObjectType.ASTEROID || type == ObjectType.DWARF_MOON) {
			if (props.mass() > 0) {
				sb.append(String.format("    Mass\t%e\n", props.mass()));
			}
		}

		// Radius
		if (props.radius() > 0) {
			sb.append(String.format("    Radius\t%.8f\n\n", props.radius()));
		}

		// Bond and geometric albedo
		if (props.albedoBond() > 0) {
			sb.append(String.format("    AlbedoBond\t%.8f\n", props.albedoBond()));
		}
		if (props.albedoGeom() > 0) {
			sb.append(String.format("    AlbedoGeom\t%.8f\n\n", props.albedoGeom()));
		}

		// Rotation period / obliquity
		if (props.rotationPeriod() > 0) {
			sb.append(String.format("    RotationPeriod\t%.8f\n", props.rotationPeriod()));
		}
		if (props.obliquity() > 0) {
			sb.append(String.format("    Obliquity\t\t%.8f\n", props.obliquity()));
		}
		sb.append("\n");
	}

	private static void appendOrbitBlock(StringBuilder sb, CelestialObject co, String distanceUnit,
			String referencePlane) {
		OrbitalElements oe = co.getOrbitalElements();
		if (oe == null) {
			// If no orbital elements, skip
			return;
		}

		sb.append("    Orbit\n");
		sb.append("    {\n");
		sb.append(String.format("        Epoch           %.8f\n", oe.epoch()));

		// If object is a comet with a known orbital period:
		if (co.getType() == ObjectType.COMET && oe.period() > 0) {
			sb.append(String.format("        Period          %.8f\n", oe.period()));
		} else {
			// Otherwise, output semi-major axis
			if ("km".equalsIgnoreCase(distanceUnit)) {
				sb.append(String.format("        SemiMajorAxisKm %.8f\n", oe.semiMajorAxis()));
			} else {
				sb.append(String.format("        SemiMajorAxis   %.8f\n", oe.semiMajorAxis()));
			}
		}

		sb.append(String.format("        Eccentricity    %.16f\n", oe.eccentricity()));
		sb.append(String.format("        Inclination     %.8f\n", oe.inclination()));
		sb.append(String.format("        AscendingNode   %.8f\n", oe.ascendingNode()));
		sb.append(String.format("        ArgOfPericen    %.8f\n", oe.argOfPeriapsis()));
		sb.append(String.format("        RefPlane        \"%s\"\n", referencePlane));
		sb.append(String.format("        MeanAnomaly     %.8f\n", oe.meanAnomaly()));

		sb.append("    }\n");
	}
}
