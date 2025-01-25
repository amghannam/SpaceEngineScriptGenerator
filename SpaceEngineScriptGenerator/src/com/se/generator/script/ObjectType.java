package com.se.generator.script;

/**
 * Enumerates the various possible celestial object types that can be generated
 * by this tool.
 */
public enum ObjectType {
	/**
	 * Represents a major moon (round object). 
	 */
	MOON("Moon", "M"), 
	
	/**
	 * Represents a minor moon.
	 */
	DWARF_MOON("DwarfMoon", "D"),
	
	/**
	 * Represents an asteroid object.
	 */
	ASTEROID("Asteroid", "A"),

	/**
	 * Represents a barycenter for a comet system, e.g. "Barycenter 'Euthymia.C1'"
	 */
	BARYCENTER("Barycenter", "C"),

	/**
	 * Represents an actual comet body, e.g. "Comet 'Euthymia.C1 A'"
	 */
	COMET("Comet", "C");

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
