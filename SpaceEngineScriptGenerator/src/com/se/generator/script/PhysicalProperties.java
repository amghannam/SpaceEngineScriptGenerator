package com.se.generator.script;

/**
 * Represents the basic physical properties of a celestial object.
 * <p>
 * This class encapsulates key physical attributes:
 * <ul>
 * <li>{@code mass} - The mass of the object (e.g., in kilograms).</li>
 * <li>{@code radius} - The radius of the object (e.g., in kilometers).</li>
 * <li>{@code albedoBond} - The Bond albedo, representing the total fraction of
 * incident light that is scattered in all directions.</li>
 * <li>{@code albedoGeom} - The geometric albedo, indicating the object's
 * brightness as observed directly from above.</li>
 * <li>{@code rotationPeriod} - The time it takes for the object to complete one
 * full rotation (e.g., in hours).</li>
 * </ul>
 */
public final class PhysicalProperties {
	private final double mass;
	private final double radius;
	private final double albedoBond; // Bond albedo
	private final double albedoGeom; // Geometric albedo
	private final double rotationPeriod;

	private PhysicalProperties(Builder builder) {
		this.mass = builder.mass;
		this.radius = builder.radius;
		this.albedoBond = builder.albedoBond;
		this.albedoGeom = builder.albedoGeom;
		this.rotationPeriod = builder.rotationPeriod;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private double mass;
		private double radius;
		private double albedoBond;
		private double albedoGeom;
		private double rotationPeriod;

		public Builder mass(double m) {
			this.mass = m;
			return this;
		}

		public Builder radius(double r) {
			this.radius = r;
			return this;
		}

		public Builder albedoBond(double ab) {
			this.albedoBond = ab;
			return this;
		}

		public Builder albedoGeom(double ag) {
			this.albedoGeom = ag;
			return this;
		}

		public Builder rotationPeriod(double rp) {
			this.rotationPeriod = rp;
			return this;
		}

		public PhysicalProperties build() {
			return new PhysicalProperties(this);
		}
	}

	// Getters
	public double mass() {
		return mass;
	}

	public double radius() {
		return radius;
	}

	public double albedoBond() {
		return albedoBond;
	}

	public double albedoGeom() {
		return albedoGeom;
	}

	public double rotationPeriod() {
		return rotationPeriod;
	}
}
