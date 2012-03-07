package com.lei.bigtop.hadoop.test

import org.apache.bigtop.itest.shell.Shell

import com.lei.bigtop.hadoop.integration.test.BigTopIntegrationTestInterface
import com.lei.bigtop.hadoop.integration.test.BigTopTestCommandInterface
import com.lei.bigtop.hadoop.integration.test.BigTopIntegrationTestFactory
import com.lei.bigtop.hadoop.integration.test.BigTopIntegrationTestFacade

import org.apache.hadoop.cli.util.ComparatorBase

class RunHadoopTestFromXMLFile {
	
	
	private static String stripOutLeadingBracket (String str) {
		if (str==null) return str;
		if (str.length()<2) return str;
		if (str.startsWith("[") && str.endsWith("]")) {
			return str.substring(1,str.length()-1)
		} else {
			return str;
		}
		
	}
	
	static Shell sh = new Shell("/bin/bash -s");
	
	public static void runTestInXMLFile(String fielName) {
		List<BigTopIntegrationTestInterface> testCaseList = BigTopTestSuiteXML.readTestSuiteFromXML(fielName);
		System.out.println("Run test suite in XML file [" + fielName + "]");
		for (BigTopIntegrationTestInterface t: testCaseList) {
			//System.out.println( t );
			println("Run test case name [" + t.getTestName() + "]");
			println("Run test case description [" + t.getTestName() + "]");
			
			for ( BigTopTestCommandInterface command: t.getCommandList() ) {
				println "Command line ["  + command.getCommand() + "]"
				
				sh.exec(command.getCommand());
				String stdout = sh.getOut();
				
				if ( command.getComparatorClass() !=null && command.getCommandComparator()!=null ) {
					System.out.println( "ComparatorClass - " + command.getComparatorClass() );
					System.out.println( "CommandComparator - " + command.getCommandComparator() );
					println "CommandComparator line ["  + command.getCommandComparator() + "]"
					sh.exec(command.getCommandComparator());
					String stdout2 = sh.getOut();
					System.out.println( "CommandComparator return code is " + sh.getRet() + " Output is " + stdout2 );
					ComparatorBase compare = BigTopIntegrationTestFacade.getInstance().getComparatorClass(command.getComparatorClass());
					if (compare==null) {
						System.out.println( "No such ComparatorClass - " + command.getComparatorClass() );
					} else {
						if (stdout.length()>=2 && stdout2.length()>=2 ) {
							boolean ret = compare.compare( stripOutLeadingBracket (stdout) , stripOutLeadingBracket(stdout2) );
							if (ret) {
								System.out.println( "  SUCCESS! \n  actual output - " + stdout + " \n  expected -" + stdout2 + "  \n  compare class - "+ command.getComparatorClass());
							} else {
								System.out.println( "  FAIL! \n  actual output - " + stdout + " \n  expected -" + stdout2 + "  \n  compare class - "+ command.getComparatorClass());
							}
						}
					}
				} else {
				
					if (sh.getRet()==0)
						System.out.println( "SUCCESS! return code is " + sh.getRet() + " Output is " + stdout);
					else 
						System.out.println( "FAIL! return code is " + sh.getRet() + " Output is " + stdout);
					

				}
				
			}
		}
	}

	public static void main(String[] args) {
		if (args==null || args.length==0 ) {
			runTestInXMLFile("./bigtop-testcases.xml");
		} else {
			for (String filename : args) {
				runTestInXMLFile(filename);
			}
		}

	}
}
