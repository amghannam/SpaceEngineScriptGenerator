package com.se.generator.script;

/**
 * Represents the orbital parameters for a given object.
 */
public final class OrbitalElements {
	private final double epoch;
	private final double semiMajorAxis;
	private final double eccentricity;
	private final double inclination;
	private final double ascendingNode;
	private final double argOfPeriapsis;
	private final double meanAnomaly;
	private final double period;

	private OrbitalElements(Builder builder) {
		this.epoch = builder.epoch;
		this.semiMajorAxis = builder.semiMajorAxis;
		this.eccentricity = builder.eccentricity;
		this.inclination = builder.inclination;
		this.ascendingNode = builder.ascendingNode;
		this.argOfPeriapsis = builder.argOfPeriapsis;
		this.meanAnomaly = builder.meanAnomaly;
		this.period = builder.period;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private double epoch;
		private double semiMajorAxis;
		private double eccentricity;
		private double inclination;
		private double ascendingNode;
		private double argOfPeriapsis;
		private double meanAnomaly;
		private double period;

		public Builder epoch(double e) {
			this.epoch = e;
			return this;
		}

		public Builder semiMajorAxis(double s) {
			this.semiMajorAxis = s;
			return this;
		}

		public Builder eccentricity(double ecc) {
			this.eccentricity = ecc;
			return this;
		}

		public Builder inclination(double i) {
			this.inclination = i;
			return this;
		}

		public Builder ascendingNode(double an) {
			this.ascendingNode = an;
			return this;
		}

		public Builder argOfPeriapsis(double arg) {
			this.argOfPeriapsis = arg;
			return this;
		}

		public Builder meanAnomaly(double ma) {
			this.meanAnomaly = ma;
			return this;
		}

		public Builder period(double p) {
			this.period = p;
			return this;
		}

		public OrbitalElements build() {
			return new OrbitalElements(this);
		}
	}

	// Getters
	public double epoch() {
		return epoch;
	}

	public double semiMajorAxis() {
		return semiMajorAxis;
	}

	public double eccentricity() {
		return eccentricity;
	}

	public double inclination() {
		return inclination;
	}

	public double ascendingNode() {
		return ascendingNode;
	}

	public double argOfPeriapsis() {
		return argOfPeriapsis;
	}

	public double meanAnomaly() {
		return meanAnomaly;
	}

	public double period() {
		return period;
	}
}
