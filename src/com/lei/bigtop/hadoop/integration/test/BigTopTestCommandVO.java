package com.lei.bigtop.hadoop.integration.test;

public class BigTopTestCommandVO implements BigTopTestCommandInterface {
	private String command;
	private String commandComparator;
	private String comparatorClass;
	
	public BigTopTestCommandVO (String command) {
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getCommandComparator() {
		return commandComparator;
	}
	public void setCommandComparator(String commandComparator) {
		this.commandComparator = commandComparator;
	}
	public String getComparatorClass() {
		return comparatorClass;
	}
	public void setComparatorClass(String comparatorClass) {
		this.comparatorClass = comparatorClass;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BigTopTestCommandVO [command=").append(command)
				.append(", commandComparator=").append(commandComparator)
				.append(", comparatorClass=").append(comparatorClass)
				.append("]");
		return builder.toString();
	}
	
	public static void main(String[] args) throws Exception {
		String actual1 = "[Number of Maps  = 5, Samples per Map = 5, Wrote input for Map #0, Wrote input for Map #1, Wrote input for Map #2, Wrote input for Map #3, Wrote input for Map #4, Starting Job, Job Finished in 19.25 seconds, Estimated value of Pi is 3.68000000000000000000]";
		String expect1 = "[3.68]";
		int compareOutput = actual1.indexOf(expect1);
		System.out.println(compareOutput);
	}
	
}
