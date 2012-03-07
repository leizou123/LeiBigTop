package com.lei.bigtop.hadoop.test

import org.xml.sax.SAXException;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.IOException;
import java.util.List;

import com.lei.bigtop.hadoop.integration.test.BigTopIntegrationTestInterface
import com.lei.bigtop.hadoop.integration.test.BigTopIntegrationTestFactory
import com.lei.bigtop.hadoop.integration.test.BigTopTestCommandInterface
import com.lei.bigtop.hadoop.integration.test.BigTopIntegrationTestFacade

class BigTopTestSuiteXML {

	public static List<BigTopIntegrationTestInterface> readTestSuiteFromXMLv2(String fileName) {
		
		List<BigTopIntegrationTestInterface> testCaseList = new ArrayList<BigTopIntegrationTestInterface>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			Document document = dbf.newDocumentBuilder().parse(new File(fileName));
		  
			Node previousNode = null;
			BigTopIntegrationTestInterface currentTestCase = null;
				  
			NodeList nodeList = document.getElementsByTagName("command");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node currentnode = nodeList.item(i);
				if (  ! (currentnode.getNodeType() == Node.ELEMENT_NODE) ) {
					break;
				}
				if (currentnode.getParentNode()==null || 
					currentnode.getParentNode().getParentNode()==null || 
					currentnode.getParentNode().getParentNode().getParentNode()==null) {
					break;
				}
				
				Node grandparentNode = currentnode.getParentNode().getParentNode();
				
				if (grandparentNode.getParentNode()!=previousNode) {
					if (currentTestCase!=null) testCaseList.add(currentTestCase);
					previousNode = grandparentNode.getParentNode();
					currentTestCase = BigTopIntegrationTestFactory.getInstance().createTestCase();
				}

				if (currentTestCase!=null && grandparentNode!=null) {
					BigTopTestCommandInterface command = BigTopIntegrationTestFacade.getInstance().setTestCaseSuiteDetail (currentTestCase, 
						grandparentNode.getNodeName()+":"+currentnode.getNodeName(), currentnode.getTextContent());
					if ( currentnode.getNextSibling() !=null && command!=null) {
						Node sib = currentnode.getNextSibling();
						while ( sib!=null) {
							if (  ( sib.getNodeType() == Node.ELEMENT_NODE ) ) {
								BigTopIntegrationTestFacade.getInstance().setTestCommandDetail(command, sib.getNodeName(), sib.getTextContent());
							}
							sib = sib.getNextSibling();
						}
					}
				}

				NodeList childNodeList =  grandparentNode.getParentNode().getChildNodes();
		
				for (int j = 0; j < childNodeList.getLength(); j++) {
					Node node2 = childNodeList.item(j);
					if (  ( node2.getNodeType() == Node.ELEMENT_NODE ) ) {
						BigTopIntegrationTestFacade.getInstance().setTestCaseSuiteDetail(currentTestCase, node2.getNodeName(), node2.getTextContent());
					}
				}
			}
			if (currentTestCase!=null) testCaseList.add(currentTestCase);
			
			} catch(ParserConfigurationException pce) {
				pce.printStackTrace();
			} catch(SAXException se) {
				se.printStackTrace();
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
			  
			return testCaseList;
		}

	public static List<BigTopIntegrationTestInterface> readTestSuiteFromXML(String fileName) {
		//return readTestSuiteFromXMLV1(fileName) ;
		return readTestSuiteFromXMLv2(fileName) ;
	}

	public static List<BigTopIntegrationTestInterface> readTestSuiteFromXMLV1(String fileName) {
		
		List<BigTopIntegrationTestInterface> testCaseList = new ArrayList<BigTopIntegrationTestInterface>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			Document document = dbf.newDocumentBuilder().parse(new File(fileName));
		  
			Node previousNode = null;
			BigTopIntegrationTestInterface currentTestCase = null;
				  
			NodeList nodeList = document.getElementsByTagName("command");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node currentnode = nodeList.item(i);
				if (  ! (currentnode.getNodeType() == Node.ELEMENT_NODE) ) {
					break;
				}
				
				Node parentNode = currentnode.getParentNode();
				if (parentNode==null) break;
				Node grandparentNode = parentNode.getParentNode();
				if (grandparentNode==null) break;
					  
				if (grandparentNode!=previousNode) {
					if (currentTestCase!=null) testCaseList.add(currentTestCase);
					previousNode = grandparentNode;
					currentTestCase = BigTopIntegrationTestFactory.getInstance().createTestCase();
				}

				if (currentTestCase!=null && parentNode!=null) {
					BigTopTestCommandInterface command = BigTopIntegrationTestFacade.getInstance().setTestCaseSuiteDetail (currentTestCase, currentnode.getParentNode().getNodeName()+":"+currentnode.getNodeName(), currentnode.getTextContent());
					if ( currentnode.getNextSibling() !=null && command!=null) {
						Node sib = currentnode.getNextSibling();
						while ( sib!=null) {
							if (  ( sib.getNodeType() == Node.ELEMENT_NODE ) ) {
								//System.out.println( "name - " + sib.getNodeName() + " value - " + sib.getTextContent() );
								if (sib.getNodeName().equals("command-comparator-type") ) {
									command.setComparatorClass(sib.getTextContent());
								} else if (sib.getNodeName().equals("command-comparator-compare-to") ) {
									command.setCommandComparator(sib.getTextContent());
								}
							}
							sib = sib.getNextSibling();
						}
					}

				}
				


				NodeList childNodeList =  grandparentNode.getChildNodes();
		
				for (int j = 0; j < childNodeList.getLength(); j++) {
					Node node2 = childNodeList.item(j);
					if (  ( node2.getNodeType() == Node.ELEMENT_NODE ) ) {
						BigTopIntegrationTestFacade.getInstance().setTestCaseSuiteDetail(currentTestCase, node2.getNodeName(), node2.getTextContent());
					}
				}
			}
			if (currentTestCase!=null) testCaseList.add(currentTestCase);
			
			} catch(ParserConfigurationException pce) {
				pce.printStackTrace();
			} catch(SAXException se) {
				se.printStackTrace();
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
			  
			return testCaseList;
		}
		
		public static void main(String[] args) {

//			List<BigTopIntegrationTestInterface> testCaseList = BigTopTestSuiteXML.readTestSuiteFromXMLV1("./bigtop-testcases1.xml");
//			for (BigTopIntegrationTestInterface t: testCaseList) {
//				System.out.println( t );
//				for ( BigTopTestCommandInterface s: t.getCommandList() ) {
//					System.out.println( s.getCommand() );
//					if ( s.getComparatorClass() !=null) {
//						System.out.println( "ComparatorClass - " + s.getComparatorClass() );
//					}
//
//					if (s.getCommandComparator()!=null) {
//						System.out.println( "CommandComparator - " + s.getCommandComparator() );
//					}
//
//				}
//			}

			List<BigTopIntegrationTestInterface> testCaseList = BigTopTestSuiteXML.readTestSuiteFromXMLv2("./bigtop-testcases2.xml");
			for (BigTopIntegrationTestInterface t: testCaseList) {
				System.out.println( t );
				for ( BigTopTestCommandInterface s: t.getCommandList() ) {
					System.out.println( s.getCommand() );
					if ( s.getComparatorClass() !=null) {
						System.out.println( "ComparatorClass - " + s.getComparatorClass() );
					}

					if (s.getCommandComparator()!=null) {
						System.out.println( "CommandComparator - " + s.getCommandComparator() );
					}

				}
			}

		}
}
