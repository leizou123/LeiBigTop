package com.lei.bigtop.hadoop.countwarrantycall;

public interface WarrantyAuditCompareI {

	long getTime();
	String getSerialNumber();
	String getServicePartNumber();
	String getCoverageTypeCode();
	String getConsumerAppID();
	
}
