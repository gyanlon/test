package com.newhope.nlbp.core.job.support;

public enum JobStatus {

	ENABLE("1"), DISABLE("0");

	private String value;

	private JobStatus(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}
