package com.se.generator.script;

import java.util.Objects;

/**
 * Encapsulates parameters for generating Dwarf Moons or Asteroids, collectively
 * known as Generic Objects.
 */
public final class GenericObjectParams {

	private final CommonGenerationParams commonParams;
	private final ObjectType objectType;

	private final double minAxis;
	private final double maxAxis;
	private final double minEccentricity;
	private final double maxEccentricity;
	private final double minInclination;
	private final double maxInclination;
	private final int count;
	private final int startNumber; // First value in sequence (e.g. DwarfMoon.D1, DwarfMoon.D2, etc.)

	private GenericObjectParams(Builder b) {
		this.commonParams = b.commonParams;
		this.objectType = b.objectType;

		this.minAxis = b.minAxis;
		this.maxAxis = b.maxAxis;
		this.minEccentricity = b.minEccentricity;
		this.maxEccentricity = b.maxEccenctricity;
		this.minInclination = b.minInclination;
		this.maxInclination = b.maxInclination;
		this.count = b.count;
		this.startNumber = b.startNumber;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private CommonGenerationParams commonParams;
		private ObjectType objectType;

		private double minAxis;
		private double maxAxis;
		private double minEccentricity;
		private double maxEccenctricity;
		private double minInclination;
		private double maxInclination;
		private int count;
		private int startNumber;

		private CommonGenerationParams.Builder commonBuilder;

		private CommonGenerationParams.Builder ensureCommonBuilder() {
			if (Objects.isNull(commonBuilder)) {
				commonBuilder = CommonGenerationParams.builder();
			}

			return commonBuilder;
		}

		// Expose common-param builder methods:
		public Builder parentBody(String s) {
			ensureCommonBuilder().parentBody(s);
			return this;
		}

		public Builder distanceUnit(String s) {
			ensureCommonBuilder().distanceUnit(s);
			return this;
		}

		public Builder referencePlane(String s) {
			ensureCommonBuilder().referencePlane(s);
			return this;
		}

		public Builder outputFile(String s) {
			ensureCommonBuilder().outputFile(s);
			return this;
		}

		// Unique fields:
		public Builder objectType(ObjectType t) {
			this.objectType = t;
			return this;
		}

		public Builder minAxis(double d) {
			this.minAxis = d;
			return this;
		}

		public Builder maxAxis(double d) {
			this.maxAxis = d;
			return this;
		}

		public Builder minEccentricity(double e) {
			this.minEccentricity = e;
			return this;
		}

		public Builder maxEccentricity(double e) {
			this.maxEccenctricity = e;
			return this;
		}

		public Builder minInclination(double i) {
			this.minInclination = i;
			return this;
		}

		public Builder maxInclination(double i) {
			this.maxInclination = i;
			return this;
		}

		public Builder count(int c) {
			this.count = c;
			return this;
		}

		public Builder startingFrom(int startNumber) {
			this.startNumber = startNumber;
			return this;
		}

		public GenericObjectParams build() {
			if (Objects.isNull(this.commonParams)) {
				this.commonParams = ensureCommonBuilder().build();
			}

			return new GenericObjectParams(this);
		}
	}

	// Getters
	public CommonGenerationParams commonParams() {
		return commonParams;
	}

	public ObjectType objectType() {
		return objectType;
	}

	public double minAxis() {
		return minAxis;
	}

	public double maxAxis() {
		return maxAxis;
	}

	public double minEccentricity() {
		return minEccentricity;
	}

	public double maxEccentricity() {
		return maxEccentricity;
	}

	public double minInclination() {
		return minInclination;
	}

	public double maxInclination() {
		return maxInclination;
	}

	public int count() {
		return count;
	}

	public int startNumber() {
		return startNumber;
	}
}
