package com.se.generator.script;

import java.util.Objects;

/**
 * Encapsulates parameters for generating multiple barycenters + child comets.
 * This class is intended for generating comet clouds.
 */
public final class CometCloudParams {

	private final CommonGenerationParams commonParams;
	private final double minAxis;
	private final double maxAxis;
	private final int barycenterCount;

	private CometCloudParams(Builder b) {
		this.commonParams = b.commonParams;
		this.minAxis = b.minAxis;
		this.maxAxis = b.maxAxis;
		this.barycenterCount = b.barycenterCount;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		// We'll hold onto a builder for CommonGenerationParams:
		private CommonGenerationParams commonParams;
		private double minAxis;
		private double maxAxis;
		private int barycenterCount;

		// Methods to allow chaining for common params:
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

		// Comet-specific fields:
		public Builder minAxis(double d) {
			this.minAxis = d;
			return this;
		}

		public Builder maxAxis(double d) {
			this.maxAxis = d;
			return this;
		}

		public Builder barycenterCount(int c) {
			this.barycenterCount = c;
			return this;
		}

		public CometCloudParams build() {
			// Build the commonParams if not already built
			if (Objects.isNull(this.commonParams)) {
				this.commonParams = this.ensureCommonBuilder().build();
			}

			return new CometCloudParams(this);
		}

		// Private helper to lazily instantiate the common params builder
		private CommonGenerationParams.Builder commonBuilder;

		private CommonGenerationParams.Builder ensureCommonBuilder() {
			if (Objects.isNull(commonBuilder)) {
				commonBuilder = CommonGenerationParams.builder();
			}

			return commonBuilder;
		}
	}

	// Getters
	public CommonGenerationParams commonParams() {
		return commonParams;
	}

	public double minAxis() {
		return minAxis;
	}

	public double maxAxis() {
		return maxAxis;
	}

	public int barycenterCount() {
		return barycenterCount;
	}
}
