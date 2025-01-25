package com.se.generator.script;

import java.util.Objects;

/**
 * Encapsulates parameters for generating Dwarf Moons or Asteroids.
 */
public final class GenericObjectParams {

	private final CommonGenerationParams commonParams;
	private final ObjectType objectType;

	private final double minAxis;
	private final double maxAxis;
	private final double minEcc;
	private final double maxEcc;
	private final double minInc;
	private final double maxInc;
	private final int count;

	private GenericObjectParams(Builder b) {
		this.commonParams = b.commonParams;
		this.objectType = b.objectType;

		this.minAxis = b.minAxis;
		this.maxAxis = b.maxAxis;
		this.minEcc = b.minEcc;
		this.maxEcc = b.maxEcc;
		this.minInc = b.minInc;
		this.maxInc = b.maxInc;
		this.count = b.count;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private CommonGenerationParams commonParams;
		private ObjectType objectType;

		private double minAxis;
		private double maxAxis;
		private double minEcc;
		private double maxEcc;
		private double minInc;
		private double maxInc;
		private int count;

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

		public Builder minEcc(double d) {
			this.minEcc = d;
			return this;
		}

		public Builder maxEcc(double d) {
			this.maxEcc = d;
			return this;
		}

		public Builder minInc(double d) {
			this.minInc = d;
			return this;
		}

		public Builder maxInc(double d) {
			this.maxInc = d;
			return this;
		}

		public Builder count(int c) {
			this.count = c;
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

	public double minEcc() {
		return minEcc;
	}

	public double maxEcc() {
		return maxEcc;
	}

	public double minInc() {
		return minInc;
	}

	public double maxInc() {
		return maxInc;
	}

	public int count() {
		return count;
	}
}
