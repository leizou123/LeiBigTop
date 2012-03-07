package com.lei.bigtop.hadoop.integration.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BigTopIntegrationTestVO implements BigTopIntegrationTestInterface {

	private String testName="";
	private String testDesc="";

	private List<BigTopTestCommandInterface> preTestCommandList = new ArrayList<BigTopTestCommandInterface>();
	private List<BigTopTestCommandInterface> integrationTestCommandList = new ArrayList<BigTopTestCommandInterface>();
	private List<BigTopTestCommandInterface> postTestCommandList = new ArrayList<BigTopTestCommandInterface>();

    
	public BigTopIntegrationTestVO() {
		
	}
	
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getTestDesc() {
		return testDesc;
	}
	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}
	public List<BigTopTestCommandInterface> getPreTestCommandList() {
		return preTestCommandList;
	}
	public void setPreTestCommandList(List<BigTopTestCommandInterface> preTestCommandList) {
		this.preTestCommandList = preTestCommandList;
	}
	public List<BigTopTestCommandInterface> getIntegrationTestCommandList() {
		return integrationTestCommandList;
	}
	public void setIntegrationTestCommandList(List<BigTopTestCommandInterface> integrationTestCommandList) {
		this.integrationTestCommandList = integrationTestCommandList;
	}
	public List<BigTopTestCommandInterface> getPostTestCommandList() {
		return postTestCommandList;
	}
	public void setPostTestCommandList(List<BigTopTestCommandInterface> postTestCommandList) {
		this.postTestCommandList = postTestCommandList;
	}

	public BigTopTestCommandInterface addToPreTestCommandList (String command) {
		if (command==null) return null;
		if (command.trim().length()==0) return null;
		BigTopTestCommandInterface testCommand = new BigTopTestCommandVO(command);
		preTestCommandList.add(testCommand);
		return testCommand;
	}

	public BigTopTestCommandInterface addToIntegrationTestCommandList (String command) {
		if (command==null) return null;
		if (command.trim().length()==0) return null;
		BigTopTestCommandInterface testCommand = new BigTopTestCommandVO(command);
		integrationTestCommandList.add(testCommand);
		return testCommand;
	}

	public BigTopTestCommandInterface addToPostTestCommandList (String command) {
		if (command==null) return null;
		if (command.trim().length()==0) return null;
		BigTopTestCommandInterface testCommand = new BigTopTestCommandVO(command);
		postTestCommandList.add(testCommand);
		return testCommand;
	}

	

	public List <BigTopTestCommandInterface> getCommandList() {
		return new ArrayList<BigTopTestCommandInterface>() { { addAll(preTestCommandList); addAll(integrationTestCommandList); addAll(postTestCommandList); } };

	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BigTopIntegrationTestVO [testName=").append(testName)
				.append(", testDesc=").append(testDesc)
				.append(", preTestCommandList=");

				for (BigTopTestCommandInterface c : preTestCommandList) builder.append(c + "; ");
				builder.append(", integrationTestCommandList=");
				for (BigTopTestCommandInterface c : integrationTestCommandList) builder.append(c + "; ");
				builder.append(", postTestCommandList="); 
				for (BigTopTestCommandInterface c : postTestCommandList) builder.append(c + "; ");
				builder.append("]");
		
		return builder.toString();
	}

}
