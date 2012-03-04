package com.lei.bigtop.hadoop.test

import org.apache.bigtop.itest.shell.Shell

class RunHadoopTest {
	static Shell sh = new Shell("/bin/bash -s");
	
	public static void main(String[] args) {
		String commandLine = "";
		for (String arg : args) {
			commandLine += " " + arg;
		}
		System.out.println( "commandLine [" + commandLine + "]");
		sh.exec(commandLine);
		if (sh.getRet()==0) {
			System.out.println( "SUCCESS! return code is " + sh.getRet());
		} else {
			System.out.println( "FAIL! return code is " + sh.getRet());
		}
	}
}
