package com.se.generator.script;

/**
 * Represents a celestial object (Moon, DwarfMoon, Asteroid, etc.), with fields
 * for physical and orbital properties.
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

	private CelestialObject() {
		// Prevent instantiation
	}

	/**
	 * Returns a SpaceEngine-compatible script for this object.
	 * 
	 * @return a compatible SpaceEngine script that represents this object
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

	// -----------------------------------------------------
	// Setters
	// -----------------------------------------------------
	
	/**
	 * Allows renaming after creation (useful when sorting then naming).
	 */
	public void setName(String newName) {
		this.name = newName;
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

		public CelestialObject build() {
			return co;
		}
	}
}
