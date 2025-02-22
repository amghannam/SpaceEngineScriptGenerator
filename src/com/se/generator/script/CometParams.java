package com.se.generator.script;

/**
 * Contains comet–specific parameters in addition to common generation
 * parameters.
 */
public final class CometParams {
	private final CommonParams commonParams;
	private final double minAxis;
	private final double maxAxis;
	private final int count;
	private final int startingFrom;

	private CometParams(Builder builder) {
		this.commonParams = builder.commonParams;
		this.minAxis = builder.minAxis;
		this.maxAxis = builder.maxAxis;
		this.count = builder.count;
		this.startingFrom = builder.startingFrom;
	}

	public static Builder builder() {
		return new Builder();
	}

	public CommonParams commonParams() {
		return commonParams;
	}

	public double minAxis() {
		return minAxis;
	}

	public double maxAxis() {
		return maxAxis;
	}

	public int count() {
		return count;
	}

	public int startingFrom() {
		return startingFrom;
	}

	public static final class Builder {
		private CommonParams commonParams;
		private CommonParams.Builder commonBuilder;
		private double minAxis;
		private double maxAxis;
		private int count;
		private int startingFrom = 1; // Default sequence start

		private CommonParams.Builder ensureCommonBuilder() {
			if (commonBuilder == null) {
				commonBuilder = CommonParams.builder();
			}
			return commonBuilder;
		}

		// Convenience methods for setting common parameters
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

		public Builder minAxis(double minAxis) {
			this.minAxis = minAxis;
			return this;
		}

		public Builder maxAxis(double maxAxis) {
			this.maxAxis = maxAxis;
			return this;
		}

		public Builder count(int count) {
			this.count = count;
			return this;
		}

		public Builder startingFrom(int startingFrom) {
			this.startingFrom = startingFrom;
			return this;
		}

		public CometParams build() {
			if (commonParams == null) {
				commonParams = ensureCommonBuilder().build();
			}
			return new CometParams(this);
		}
	}
}
