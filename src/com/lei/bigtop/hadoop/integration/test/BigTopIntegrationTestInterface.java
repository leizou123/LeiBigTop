package com.lei.bigtop.hadoop.integration.test;

import java.util.List;

public interface BigTopIntegrationTestInterface {

	List <BigTopTestCommandInterface> getCommandList();
	
	String getTestName();
	void setTestName(String testName);
	String getTestDesc() ;
	void setTestDesc(String testDesc);

	BigTopTestCommandInterface addToPreTestCommandList (String command);
	BigTopTestCommandInterface addToIntegrationTestCommandList (String command);
	BigTopTestCommandInterface addToPostTestCommandList (String command);

}
