package com.se.generator.script;

/**
 * Represents the basic orbital parameters for a celestial object.
 * <p>
 * This class encapsulates the key elements that describe an orbit:
 * <ul>
 * <li>{@code epoch} - The reference time (usually given as a Julian Date) at
 * which these orbital elements are defined.</li>
 * <li>{@code semiMajorAxis} - The semi-major axis of the orbit, which defines
 * the size of the orbit.</li>
 * <li>{@code eccentricity} - A dimensionless value that describes the shape of
 * the orbit (0 for a circular orbit, between 0 and 1 for elliptical
 * orbits).</li>
 * <li>{@code inclination} - The tilt of the orbit in degrees relative to the
 * reference plane.</li>
 * <li>{@code ascendingNode} - The longitude of the ascending node (in degrees),
 * indicating where the orbit passes upward through the reference plane.</li>
 * <li>{@code argOfPericenter} - The argument of pericenter (in degrees), which
 * specifies the angle from the ascending node to the point of closest
 * approach.</li>
 * <li>{@code meanAnomaly} - The mean anomaly (in degrees) at the epoch,
 * representing the fraction of the orbital period that has elapsed since
 * pericenter passage.</li>
 * </ul>
 */
public final class OrbitalElements {
	private final double epoch;
	private final double semiMajorAxis;
	private final double eccentricity;
	private final double inclination;
	private final double ascendingNode;
	private final double argOfPericenter;
	private final double meanAnomaly;

	private OrbitalElements(Builder builder) {
		this.epoch = builder.epoch;
		this.semiMajorAxis = builder.semiMajorAxis;
		this.eccentricity = builder.eccentricity;
		this.inclination = builder.inclination;
		this.ascendingNode = builder.ascendingNode;
		this.argOfPericenter = builder.argOfPericenter;
		this.meanAnomaly = builder.meanAnomaly;
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
		private double argOfPericenter;
		private double meanAnomaly;

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

		public Builder argOfPericenter(double arg) {
			this.argOfPericenter = arg;
			return this;
		}

		public Builder meanAnomaly(double ma) {
			this.meanAnomaly = ma;
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

	public double argOfPericenter() {
		return argOfPericenter;
	}

	public double meanAnomaly() {
		return meanAnomaly;
	}
}
