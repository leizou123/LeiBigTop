package com.lei.bigtop.hadoop.countwarrantycall;

public interface WarrantyDataCountedI {

	boolean isCounted (WarrantyAuditCompareI a, WarrantyAuditCompareI b);
}
