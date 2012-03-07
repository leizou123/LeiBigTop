package com.lei.bigtop.hadoop.integration.test;


public class ExtactComparatorWithoutWhiteSpace extends org.apache.hadoop.cli.util.ExactComparator {
	public boolean compare(String actual, String expected) {
		if (actual==null || expected==null) return false;
		String actual2 = actual.replaceAll("\\s", "").replaceAll(",", "");
		String expected2 = expected.replaceAll("\\s", "").replaceAll(",", "");
		return super.compare(actual2, expected2);
	}
}
