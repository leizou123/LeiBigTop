package com.lei.bigtop.hadoop.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaUtil {

	static final private String KEY_SPACE_NUM_PATTERN_STR = "[a-z]([\\s]+)([0-9.]+)";

	static private List<File> getFileListingNoSort( File aStartingDir) throws FileNotFoundException 
	{
		List<File> result = new ArrayList<File>();
		File[] filesAndDirs = aStartingDir.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);
		for(File file : filesDirs) {
			result.add(file); //always add, even if directory
			if ( ! file.isFile() ) {
				//must be a directory
	        	// recursive call!
				List<File> deeperList = getFileListingNoSort(file);
				result.addAll(deeperList);
			}
		}
		return result;
	}

	static private void validateDirectory (File aDirectory) throws FileNotFoundException 
	{
		  
		  if (aDirectory == null) {
			  throw new IllegalArgumentException("Directory should not be null.");
		  }
		  if (!aDirectory.exists()) {
			  throw new FileNotFoundException("Directory does not exist: " + aDirectory);
		  }
		  if (!aDirectory.isDirectory()) {
			  throw new IllegalArgumentException("Is not a directory: " + aDirectory);
		  }
		  if (!aDirectory.canRead()) {
			  throw new IllegalArgumentException("Directory cannot be read: " + aDirectory);
		  }
	}

	
	static public List<File> getFileListing(File aStartingDir) throws FileNotFoundException {
	    validateDirectory(aStartingDir);
	    List<File> result = getFileListingNoSort(aStartingDir);
	    Collections.sort(result);
	    return result;
	}


	public static Map <String, List<Double>> getKeyValueListFromDir(String dirName) {
		Map <String, List<Double>> mapKeyValueList = new HashMap <String, List<Double>>();
		BufferedReader in = null;
		
		try {
			File startingDirectory= new File(dirName);
			List<File> files = getFileListing(startingDirectory);
			for (File file : files ) {
				if (file.isFile()) {
					in = new BufferedReader(new FileReader(file.getAbsolutePath() ));
					String line = null;
					while (( line = in.readLine()) != null) {
						if (line==null || line.length()==0) continue;
						if (! isStringKeyNumSeparatedBySpace (line) ) continue;
						StringTokenizer st = new StringTokenizer(line);
						String k = (String) st.nextElement();
						Double d = Double.valueOf( (String)st.nextElement());
						List<Double> list = (mapKeyValueList.get(k)==null)?  new ArrayList<Double> () : mapKeyValueList.get(k);
						list.add(d);
					}
					in.close();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if ( in!=null ) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return mapKeyValueList;
	}

	public static boolean compareFileContentSum(String sourceDir, String destDir) {
		  Map <String, List<Double>> src = getKeyValueListFromDir(sourceDir);
		  Map <String, List<Double>> dest = getKeyValueListFromDir(destDir);
		  return compareKeyValueList(src, dest);
	}
	
	public static boolean compareKeyValueList(Map <String, List<Double>> source, Map <String, List<Double>> dest) {
		  
		if (source==null || dest==null) return false;
		if ( source.keySet().size()!=dest.keySet().size() ) 
			return false;
			
		Iterator<String> it = source.keySet().iterator();
		while (it.hasNext()) {
			String k = it.next();
			List<Double> listSource = source.get(k);
			List<Double> listDest = dest.get(k);
			if (listSource==null || listDest==null) return false;
			
			double sourceSum = 0;
			for (Double d : listSource) sourceSum += d.doubleValue();
			double destSum = 0;
			for (Double d : listDest)  destSum += d.doubleValue();
			if (sourceSum!=destSum) return false;
		}
		
		return true;
	}

	static public boolean createDirectoryIfNotExists(String dirName) {
		boolean existBefore = true;
		File f = new File(dirName);
		existBefore = f.exists();
		if (! existBefore) {
			new File(dirName).mkdirs();
		}
		
		return existBefore;
	}

	public static String getFilePath(String fileName, String pathSeparator) {
	    int sep = fileName.lastIndexOf(pathSeparator);
	    return fileName.substring(0, sep);
		
	}
	
	static public String getDirectoryName(String dirName) {
		String pathName = dirName;
		
		if (dirName.startsWith(".")) {
			String currentDir = getCurrentDirectory();
			pathName = dirName.replaceFirst(".", currentDir);
		}
		
		return pathName; 
	}
	
	static public String getCurrentDirectory() {
		String dirName = "";
		File dir1 = new File (".");
		try {
			dirName = dir1.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dirName;
	}

	public static String getFilePath(String fileName) {
	    return getFilePath(fileName, "/");
	}
	
	static public int prepareLocalData (String pathName) {
		System.out.println("current path [" + pathName + "]");
		createDirectoryIfNotExists(pathName);
		String fileName = pathName.endsWith( System.getProperty("file.separator") ) ? pathName+"input.txt" : pathName+System.getProperty("file.separator")+"input.txt";
		String[] keys = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l","m"};
		System.out.println("data file [" + fileName + "]");
		return prepareLocalData (fileName, keys, 300, 100);
	}

	public static boolean isStringKeyNumSeparatedBySpace(String inputString) {

        boolean isMatched = false;

        if (inputString != null && inputString.trim().length() > 0) {

        	Pattern pattern = Pattern.compile(KEY_SPACE_NUM_PATTERN_STR,
        			Pattern.CASE_INSENSITIVE | Pattern.DOTALL );

        	// return true if it passes
            Matcher matcher = pattern.matcher(inputString);
        	isMatched = matcher.find();
        }

        return isMatched;
    }

	
	static public int prepareLocalData (String fileName, String[] keys, int num, int range) {
		int count=0;
		PrintWriter out = null;
		
		try {
			out = new PrintWriter(new FileWriter(fileName));
			Random randomGenerator = new Random();
			for (int idx = 1; idx <= num; ++idx) {
				int randomInt = randomGenerator.nextInt(100);
				int randomIdx = randomGenerator.nextInt(keys.length);
				out.println(keys[randomIdx] + " " + new Double(randomInt) );
				count++;
			}
		    
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
		return count;
	}

	public static void main(String[] args) throws Exception {
		if ( JavaUtil.compareFileContentSum("/Users/lei/Documents/workspace/LeiBigTop/data", "/Users/lei/Documents/workspace/LeiBigTop/output") ) {
			System.out.println("SUCCESS");
		} else {
			System.out.println("FAIL");
		}
			
	}
}
