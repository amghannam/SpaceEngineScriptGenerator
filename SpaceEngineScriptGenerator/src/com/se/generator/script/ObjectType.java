package com.se.generator.script;

/**
 * Enumerates the various possible celestial object types that can be generated
 * by this tool.
 */
public enum ObjectType {
	/**
	 * Represents a large (spherical) moon.
	 */
	MOON("Moon", "M"),

	/**
	 * Represents a minor (irregular) moon.
	 */
	DWARF_MOON("DwarfMoon", "D"),

	/**
	 * Represents an asteroidal object. Also used as the default classification for
	 * smaller objects such as comets and minor moons (by SpaceEngine convention).
	 */
	ASTEROID("Asteroid", "A"),

	/**
	 * Represents a single comet object.
	 */
	COMET("Comet", "C"),

	/**
	 * Represents a barycenter group for comets.
	 */
	BARYCENTER("Barycenter", "C");

	private final String formattedName;
	private final String prefix;

	ObjectType(String formattedName, String prefix) {
		this.formattedName = formattedName;
		this.prefix = prefix;
	}

	public String getFormattedName() {
		return formattedName;
	}

	public String getPrefix() {
		return prefix;
	}
}
