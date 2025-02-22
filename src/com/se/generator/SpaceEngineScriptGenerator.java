package com.se.generator;

import com.se.generator.service.ScriptGeneratorService;

/**
 * Startup class for the SpaceEngine script generator.
 * <p>
 * This tool generates objects such as moons, asteroids, and comets in a format
 * that is compatible with SpaceEngine. It gathers user input for the necessary
 * parameters and produces a script file which can then be loaded into
 * SpaceEngine.
 * 
 * @author Ahmed Ghannam
 * @version 1.0 (January 2025)
 */
public class SpaceEngineScriptGenerator {
	public static void main(String[] args) {
		System.out.println("Welcome! Use this tool to generate astereoids, moons, or comets for SpaceEngine.\n");
		new ScriptGeneratorService().run();
	}
}
