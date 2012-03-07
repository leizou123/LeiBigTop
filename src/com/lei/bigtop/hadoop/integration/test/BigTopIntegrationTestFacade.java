package com.lei.bigtop.hadoop.integration.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.cli.util.ComparatorBase;



public class BigTopIntegrationTestFacade {
	
	static private BigTopIntegrationTestFacade self=null;
	static private Map<String, ComparatorBase> mapCompareClassMap = null;
    final static private Map<String, SetTestCommandDetail>  commandNameToValueMapping = new HashMap<String, SetTestCommandDetail>() { 
		private static final long serialVersionUID = -1L;
		{
    		put("command-comparator-type", 	new SetTestComparatorClass() );
    		put("command-comparator-compare-to", 	new SetTestCommandComparator() );
    	}
    };

    private static class SetTestComparatorClass implements SetTestCommandDetail {
    	public void setTestCommandDetail(BigTopTestCommandInterface command, String value) {
    		command.setComparatorClass(value);
    	}
    }

    private static class SetTestCommandComparator implements SetTestCommandDetail {
    	public void setTestCommandDetail(BigTopTestCommandInterface command, String value) {
    		command.setCommandComparator(value);
    	}
    }

	public static BigTopIntegrationTestFacade getInstance() {
		if (self == null) {
			synchronized (BigTopIntegrationTestFacade.class) {
				if (self == null) 
					self = new BigTopIntegrationTestFacade();
					mapCompareClassMap = new HashMap<String, ComparatorBase>();
			}
		}
		return self;
	}

    public void setTestCommandDetail(BigTopTestCommandInterface command, String name, String value) {
    	if (command==null) return;
    	SetTestCommandDetail setHandler = commandNameToValueMapping.get(name);
    	if (setHandler==null) return;
    	setHandler.setTestCommandDetail(command, value);
    }

    
	private BigTopIntegrationTestFacade () {}



    
    final static private Map<String, SetTestCaseDetail>  testCaseNameToValueMapping = new HashMap<String, SetTestCaseDetail>() { 
		private static final long serialVersionUID = 6949726364819585132L;
		{
    		put("test-pre-integration-test:command", 	new AddtoPreIntegrationTestCommandList() );
    		put("test-integration-test:command", 	new AddtoIntegrationTestCommandList() );
    		put("test-post-integration-test:command", 	new AddtoPostIntegrationTestCommandList() );
    		put("test-name", 	new SetIntegrationTestName() );
    		put("test-desc", 	new SetIntegrationTestDesc() );
    	}
    };


    private interface SetTestCaseDetail {
    	BigTopTestCommandInterface setTestDetail(BigTopIntegrationTestInterface testCase, String value);
    } 

    private static class AddtoPreIntegrationTestCommandList implements SetTestCaseDetail {
    	public BigTopTestCommandInterface setTestDetail(BigTopIntegrationTestInterface testCase, String value) {
    		return testCase.addToPreTestCommandList(value);
    	}
    }
    
    private static class AddtoIntegrationTestCommandList implements SetTestCaseDetail {
    	public BigTopTestCommandInterface setTestDetail(BigTopIntegrationTestInterface testCase, String value) {
    		return testCase.addToIntegrationTestCommandList(value);
    	}
    }

    private static class AddtoPostIntegrationTestCommandList implements SetTestCaseDetail {
    	public BigTopTestCommandInterface setTestDetail(BigTopIntegrationTestInterface testCase, String value) {
    		return testCase.addToPostTestCommandList(value);
    	}
    }

    private static class SetIntegrationTestName implements SetTestCaseDetail {
    	public BigTopTestCommandInterface setTestDetail(BigTopIntegrationTestInterface testCase, String value) {
    		testCase.setTestName(value);
    		return null;
    	}
    }

    private static class SetIntegrationTestDesc implements SetTestCaseDetail {
    	public BigTopTestCommandInterface setTestDetail(BigTopIntegrationTestInterface testCase, String value) {
    		testCase.setTestDesc(value);
    		return null;
    	}
    }

    
    
    private interface SetTestCommandDetail {
    	void setTestCommandDetail(BigTopTestCommandInterface command, String value);
    } 



	public BigTopTestCommandInterface setTestCaseSuiteDetail (BigTopIntegrationTestInterface testCase, String name, String value) {
		if (testCase==null || name ==null || value==null) {
			return null;
		}
		SetTestCaseDetail setHandler =  testCaseNameToValueMapping.get(name);
		if (setHandler!=null) {
			return setHandler.setTestDetail(testCase, value);
		}
		else {
			return null;
		}

	}
	

	private ComparatorBase createComparatorClassObject (String strClassName) {
		if (strClassName==null) return null;
		if (strClassName.length()==0) return null;
		ComparatorBase comparatorBase = null;
		try {
			Class<?> clazz = Class.forName(strClassName);
			comparatorBase = (ComparatorBase) clazz.newInstance();	// need to have a default constructor
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return comparatorBase;

	}
	public ComparatorBase getComparatorClass (String strClassName) {
		if (strClassName==null) return null;
		if (strClassName.length()==0) return null;

		ComparatorBase comparatorBase = mapCompareClassMap.get(strClassName);
		if (comparatorBase==null) {
			comparatorBase = createComparatorClassObject(strClassName);
			if (comparatorBase!=null) {
				mapCompareClassMap.put(strClassName, comparatorBase);
			}
		}

		return comparatorBase;
	}

}
