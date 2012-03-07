package com.lei.bigtop.hadoop.integration.test;

public interface BigTopTestCommandInterface {

	String getCommand();
	
	void setCommand(String command);
	
	String getCommandComparator();
	
	void setCommandComparator(String commandComparator);
	
	String getComparatorClass();
	
	void setComparatorClass(String comparatorClass);

	
}
