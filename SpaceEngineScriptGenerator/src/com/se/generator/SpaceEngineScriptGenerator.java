package com.se.generator;

import com.se.generator.service.ScriptGeneratorService;

/**
 * Startup class for the SpaceEngine script generator. This tool generates
 * objects like dwarf moons, asteroids, and moons in a format compatible with
 * SpaceEngine.
 * 
 * @author Ahmed Ghannam
 * @version 1.0 (January 2025)
 * 
 */
public class SpaceEngineScriptGenerator {
	public static void main(String[] args) {
		System.out.println("Welcome! Use this tool to generate comets, astereoids or moons for SpaceEngine.\n");
		new ScriptGeneratorService().run();
	}
}
