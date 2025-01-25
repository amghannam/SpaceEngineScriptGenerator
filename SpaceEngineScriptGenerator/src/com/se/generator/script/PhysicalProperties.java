package com.se.generator.script;

/**
 * Represents the physical properties for a given object.
 */
public final class PhysicalProperties {
	private final double mass;
	private final double radius;
	private final double albedoBond; // Bond albedo
	private final double albedoGeom; // Geometric albedo
	private final double rotationPeriod;
	private final double obliquity;

	private PhysicalProperties(Builder builder) {
		this.mass = builder.mass;
		this.radius = builder.radius;
		this.albedoBond = builder.albedoBond;
		this.albedoGeom = builder.albedoGeom;
		this.rotationPeriod = builder.rotationPeriod;
		this.obliquity = builder.obliquity;
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
		private double obliquity;

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

		public Builder obliquity(double ob) {
			this.obliquity = ob;
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

	public double obliquity() {
		return obliquity;
	}
}
