package com.lei.bigtop.hadoop.integration.test;

final public class BigTopIntegrationTestFactory {
	static private BigTopIntegrationTestFactory self=null;
	

	
	private BigTopIntegrationTestFactory() {}
	public static BigTopIntegrationTestFactory getInstance() {
		if (self == null) {
			synchronized (BigTopIntegrationTestFactory.class) {
				if (self == null) 
					self = new BigTopIntegrationTestFactory();
			}
		}
		return self;
	}

	
	public BigTopIntegrationTestInterface createTestCase () {
		return new BigTopIntegrationTestVO();
	}
	

}
