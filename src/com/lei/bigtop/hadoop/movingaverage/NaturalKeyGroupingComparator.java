package com.lei.bigtop.hadoop.movingaverage;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class NaturalKeyGroupingComparator extends WritableComparator {

	protected NaturalKeyGroupingComparator() {
		super(TimeseriesKey.class, true);
	}

	@Override
	public int compare(WritableComparable o1, WritableComparable o2) {

		TimeseriesKey tsK1 = (TimeseriesKey) o1;
		TimeseriesKey tsK2 = (TimeseriesKey) o2;

		return tsK1.getGroup().compareTo(tsK2.getGroup());

	}

}