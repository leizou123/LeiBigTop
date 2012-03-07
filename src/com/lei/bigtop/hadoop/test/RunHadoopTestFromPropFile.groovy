package com.lei.bigtop.hadoop.test



import org.apache.bigtop.itest.shell.Shell

class RunHadoopTestFromPropFile {
	static Shell sh = new Shell("/bin/bash -s");
	
	public static void main(String[] args) {
		String commandLine = "";
		for (String arg : args) {
			commandLine += " " + arg;
		}
		

		def props = new Properties()
		new File("hadoop.itest.properties").withInputStream {
			stream -> props.load(stream)
		}
		
		def set = props.keys();
		while (set.hasMoreElements()  ) {
			def key = set.nextElement() ;
			def command  = props.get(key);
			println "Runing test case [" + key + "]"
			println "Command line ["  + command + "]"
			
			sh.exec(command);
			if (sh.getRet()==0) {
				System.out.println( "SUCCESS! return code is " + sh.getRet());
			} else {
				System.out.println( "FAIL! return code is " + sh.getRet());
			}
	
			//println "key - " + key + " value - " + val
		}
		
	}

}
