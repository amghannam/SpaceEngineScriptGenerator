package com.se.generator.script;

import java.util.List;
import java.util.Objects;

/**
 * Encapsulates parameters needed for generating Regular Moon objects.
 */
public final class RegularMoonParams {

	private final CommonParams commonParams;

	private final List<String> names;
	private final List<Double> radii;
	private final List<Double> distances;
	private final List<String> classes;

	private final double minEccentricity;
	private final double maxEccentricity;
	private final double minInclination;
	private final double maxInclination;
	private final double minBondAlbedo;
	private final double maxBondAlbedo;

	private RegularMoonParams(Builder b) {
		this.commonParams = b.commonParams;
		this.names = b.names;
		this.radii = b.radii;
		this.distances = b.distances;
		this.classes = b.classes;
		this.minEccentricity = b.minEccentricity;
		this.maxEccentricity = b.maxEccentricity;
		this.minInclination = b.minInclination;
		this.maxInclination = b.maxInclination;
		this.minBondAlbedo = b.minBondAlbedo;
		this.maxBondAlbedo = b.maxBondAlbedo;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private CommonParams.Builder commonBuilder;
		private CommonParams commonParams;

		private List<String> names;
		private List<Double> radii;
		private List<Double> distances;
		private List<String> classes;

		private double minEccentricity;
		private double maxEccentricity;
		private double minInclination;
		private double maxInclination;
		private double minBondAlbedo;
		private double maxBondAlbedo;

		private CommonParams.Builder ensureCommonBuilder() {
			if (Objects.isNull(commonBuilder)) {
				commonBuilder = CommonParams.builder();
			}

			return commonBuilder;
		}

		// Common fields
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

		// Unique fields
		public Builder names(List<String> list) {
			this.names = list;
			return this;
		}

		public Builder radii(List<Double> list) {
			this.radii = list;
			return this;
		}

		public Builder distances(List<Double> list) {
			this.distances = list;
			return this;
		}

		public Builder classes(List<String> list) {
			this.classes = list;
			return this;
		}

		public Builder minEccentricity(double e) {
			this.minEccentricity = e;
			return this;
		}

		public Builder maxEccentricity(double e) {
			this.maxEccentricity = e;
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

		public Builder minBondAlbedo(double a) {
			this.minBondAlbedo = a;
			return this;
		}

		public Builder maxBondAlbedo(double a) {
			this.maxBondAlbedo = a;
			return this;
		}

		public RegularMoonParams build() {
			if (Objects.isNull(commonParams)) {
				this.commonParams = ensureCommonBuilder().build();
			}

			return new RegularMoonParams(this);
		}
	}

	// Getters
	public CommonParams commonParams() {
		return commonParams;
	}

	public List<String> names() {
		return names;
	}

	public List<Double> radii() {
		return radii;
	}

	public List<Double> distances() {
		return distances;
	}

	public List<String> classes() {
		return classes;
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

	public double minBondAlbedo() {
		return minBondAlbedo;
	}

	public double maxBondAlbedo() {
		return maxBondAlbedo;
	}
}
