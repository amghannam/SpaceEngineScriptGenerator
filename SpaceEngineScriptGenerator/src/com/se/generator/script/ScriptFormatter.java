package com.se.generator.script;

import java.util.Objects;

/**
 * The {@code ScriptFormatter} class provides utility methods for converting a
 * {@code CelestialObject} into a SpaceEngine-compatible script. It formats the
 * object's label, physical properties, and orbital elements according to the
 * .sc file specification used by SpaceEngine.
 * <p>
 * For example, if the object type is COMET, only the radius will be printed in
 * the physical block; if the object is a BARYCENTER, no physical properties are
 * printed.
 * </p>
 * <p>
 * This class is a final utility class with only static methods and cannot be
 * instantiated.
 * </p>
 * 
 * @see CelestialObject
 * @see PhysicalProperties
 * @see OrbitalElements
 */
public final class ScriptFormatter {

	private ScriptFormatter() {
		// Prevent instantiation
	}

	/**
	 * Formats the given {@code CelestialObject} into a SpaceEngine-compatible
	 * script string.
	 * <p>
	 * The formatted script includes the object label (based on its type), parent
	 * body declaration, a physical block (if applicable), and an orbit block
	 * containing the orbital elements. The formatting of the physical block varies
	 * by object type (for example, comets only display the radius, while
	 * barycenters do not display any physical properties). The orbit block is
	 * formatted using the provided distance unit and reference plane.
	 * </p>
	 *
	 * @param co             the {@code CelestialObject} to be formatted into script
	 *                       form
	 * @param distanceUnit   the unit in which the semi-major axis is represented
	 *                       (e.g., "km" or "AU")
	 * @param referencePlane the reference plane to be used in the orbit block
	 *                       (e.g., "Equator")
	 * @return a String containing the SpaceEngine-compatible script representation
	 *         of the celestial object
	 */
	public static String format(CelestialObject co, 
			String distanceUnit, 
			String referencePlane) {
		var sb = new StringBuilder();

		// Label based on object type
		var objectLabel = (Objects.nonNull(co.getType())) ? co.getType().getFormattedName() : "Object";
		sb.append(String.format("%s \"%s\"\n{\n", objectLabel, co.getName()));
		sb.append(String.format("    ParentBody\t\t\"%s\"\n", co.getParentBody()));

		appendPhysicalBlock(sb, co);
		appendOrbitBlock(sb, co, distanceUnit, referencePlane);

		sb.append("}\n");
		return sb.toString();
	}

	/*
	 * -------------------------------------------------------------------------
	 * Private Helpers
	 * -------------------------------------------------------------------------
	 */

	private static void appendPhysicalBlock(StringBuilder sb, CelestialObject co) {
		var props = co.getPhysicalProperties();
		if (Objects.isNull(props)) {
			return;
		}

		var objClass = (Objects.isNull(co.getClassification()) || co.getClassification().isBlank()) ? "Asteroid"
				: co.getClassification();
		sb.append(String.format("    Class\t\t\"%s\"\n", objClass));

		// For barycenters, do not print any physical properties.
		if (co.getType() == ObjectType.BARYCENTER) {
			return;
		}

		// For comets, only output the radius. The rest is automatically calculated by
		// SpaceEngine.
		if (co.getType() == ObjectType.COMET) {
			if (props.radius() > 0) {
				sb.append(String.format("    Radius\t\t%.8f\n\n", props.radius()));
			}
		} else {
			// (For other types, print all properties.)
			if (props.mass() > 0) {
				sb.append(String.format("    Mass\t\t%e\n", props.mass()));
			}
			if (props.radius() > 0) {
				sb.append(String.format("    Radius\t\t%.8f\n\n", props.radius()));
			}
			if (props.rotationPeriod() > 0) {
				sb.append(String.format("    RotationPeriod\t%.8f\n", props.rotationPeriod()));
			}
			sb.append("\n");
		}
	}

	private static void appendOrbitBlock(StringBuilder sb, 
			CelestialObject co, 
			String distanceUnit,
			String referencePlane) {
		var oe = co.getOrbitalElements();
		if (Objects.isNull(oe)) {
			return;
		}

		sb.append("    Orbit\n");
		sb.append("    {\n");
		sb.append(String.format("        Epoch\t\t%.8f\n", oe.epoch()));

		if ("km".equalsIgnoreCase(distanceUnit)) {
			sb.append(String.format("        SemiMajorAxisKm\t%.8f\n", oe.semiMajorAxis()));
		} else {
			sb.append(String.format("        SemiMajorAxis\t%.8f\n", oe.semiMajorAxis()));
		}

		sb.append(String.format("        Eccentricity\t%.16f\n", oe.eccentricity()));
		sb.append(String.format("        Inclination\t%.8f\n", oe.inclination()));
		sb.append(String.format("        AscendingNode\t%.8f\n", oe.ascendingNode()));
		sb.append(String.format("        ArgOfPericen\t%.8f\n", oe.argOfPericenter()));
		sb.append(String.format("        RefPlane\t\"%s\"\n", referencePlane));
		sb.append(String.format("        MeanAnomaly\t%.8f\n", oe.meanAnomaly()));
		sb.append("    }\n");
	}
}
