package com.se.generator.script;

/**
 * Represents baseline object parameters.
 */
public final class CommonGenerationParams {
	private final String parentBody;
	private final String distanceUnit;
	private final String referencePlane;
	private final String outputFile;

	private CommonGenerationParams(Builder builder) {
		this.parentBody = builder.parentBody;
		this.distanceUnit = builder.distanceUnit;
		this.referencePlane = builder.referencePlane;
		this.outputFile = builder.outputFile;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private String parentBody;
		private String distanceUnit;
		private String referencePlane;
		private String outputFile;

		public Builder parentBody(String s) {
			this.parentBody = s;
			return this;
		}

		public Builder distanceUnit(String s) {
			this.distanceUnit = s;
			return this;
		}

		public Builder referencePlane(String s) {
			this.referencePlane = s;
			return this;
		}

		public Builder outputFile(String s) {
			this.outputFile = s;
			return this;
		}

		public CommonGenerationParams build() {
			return new CommonGenerationParams(this);
		}
	}

	// Getters
	public String parentBody() {
		return parentBody;
	}

	public String distanceUnit() {
		return distanceUnit;
	}

	public String referencePlane() {
		return referencePlane;
	}

	public String outputFile() {
		return outputFile;
	}
}
