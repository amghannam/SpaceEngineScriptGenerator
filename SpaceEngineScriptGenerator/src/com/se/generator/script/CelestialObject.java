package com.se.generator.script;

/**
 * Represents a celestial object (such as a Moon, DwarfMoon, Asteroid, or Comet)
 * with identification, physical properties, and orbital elements.
 * <p>
 * The object's type, name, parent body, and classification are used for
 * identification. The physical properties (mass, radius, albedos, rotation
 * period) and the orbital elements (epoch, semi‐major axis, eccentricity,
 * inclination, ascending node, argument of pericenter, mean anomaly, period)
 * describe its physical state and orbit. The {@code satellite} flag indicates
 * whether this object is a satellite (for example, a child comet belonging to a
 * barycenter group).
 */
public class CelestialObject {

	// Identification
	private ObjectType type;
	private String name;
	private String parentBody;
	private String classification;

	// Physical and orbital properties (by composition)
	private PhysicalProperties physicalProperties;
	private OrbitalElements orbitalElements;

	// Indicates if this object is a satellite (e.g. a child comet in a
	// barycenter group)
	private boolean satellite;

	private CelestialObject() {
		// Prevent instantiation
	}

	/**
	 * Returns a SpaceEngine–compatible script representation of this celestial
	 * object.
	 * <p>
	 * The output script includes the object's identification, a physical block
	 * (with selective output depending on the object type), and an orbit block. The
	 * formatting is adjusted based on the provided distance unit and reference
	 * plane.
	 *
	 * @param distanceUnit   the unit for distances (for example, "km" or "AU")
	 * @param referencePlane the reference plane for the orbital elements (for
	 *                       example, "Equator")
	 * @return a formatted script string representing this celestial object
	 */
	public String toScript(String distanceUnit, String referencePlane) {
		return ScriptFormatter.format(this, distanceUnit, referencePlane);
	}

	// -----------------------------------------------------
	// Getters
	// -----------------------------------------------------

	public ObjectType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getParentBody() {
		return parentBody;
	}

	public String getClassification() {
		return classification;
	}

	public PhysicalProperties getPhysicalProperties() {
		return physicalProperties;
	}

	public OrbitalElements getOrbitalElements() {
		return orbitalElements;
	}

	public boolean isSatellite() {
		return satellite;
	}

	// -----------------------------------------------------
	// Setters
	// -----------------------------------------------------

	/**
	 * Sets a new name for this celestial object.
	 * <p>
	 * This method is useful for renaming objects after creation (for example, after
	 * sorting).
	 *
	 * @param newName the new name to assign to the object
	 */
	public void setName(String newName) {
		this.name = newName;
	}

	/**
	 * Sets a new parent body for this celestial object.
	 * <p>
	 * This is useful for reassigning the parent body—for instance, when updating a
	 * child comet’s parent after a barycenter is renamed.
	 *
	 * @param newParentBody the new parent body name
	 */
	public void setParentBody(String newParentBody) {
		this.parentBody = newParentBody;
	}

	/**
	 * Sets whether this celestial object is considered a satellite.
	 *
	 * @param satellite {@code true} to mark the object as a satellite;
	 *                  {@code false} otherwise
	 */
	public void setSatellite(boolean satellite) {
		this.satellite = satellite;
	}

	// -----------------------------------------------------
	// Builder
	// -----------------------------------------------------

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private final CelestialObject co;

		public Builder() {
			co = new CelestialObject();
		}

		public Builder type(ObjectType type) {
			co.type = type;
			return this;
		}

		public Builder name(String name) {
			co.name = name;
			return this;
		}

		public Builder parentBody(String parentBody) {
			co.parentBody = parentBody;
			return this;
		}

		public Builder classification(String classification) {
			co.classification = classification;
			return this;
		}

		public Builder physicalProperties(PhysicalProperties pp) {
			co.physicalProperties = pp;
			return this;
		}

		public Builder orbitalElements(OrbitalElements oe) {
			co.orbitalElements = oe;
			return this;
		}

		public Builder satellite(boolean satellite) {
			co.satellite = satellite;
			return this;
		}

		public CelestialObject build() {
			return co;
		}
	}
}
