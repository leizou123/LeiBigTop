package com.lei.bigtop.hadoop.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;

//com.lei.bigtop.hadoop.util.HadoopUtil
public class HadoopUtil {

//	public static void removeDirectory (JobConf conf, String dirName) throws IOException {
//	     Configuration confLocal = ( conf==null ) ? new Configuration() : conf;
//	     FileSystem fs = FileSystem.get(confLocal);
//	     Path filenamePath = new Path(dirName);
//	     
//	     if ( fs.exists(filenamePath) ) {
//	    	 fs.delete(filenamePath, true);
//	     }
//	}

	public static void copyLocalFile (String srcFileName, String destFileName, JobConf conf) throws IOException {
		
		Configuration hdfsConf = (conf==null) ? new Configuration() : conf;
	    FileSystem fs = FileSystem.get(hdfsConf);
	    Path hdfsPath = new Path(destFileName);
	    fs.copyFromLocalFile(new Path(srcFileName), hdfsPath);
	    
	    DistributedCache.addCacheFile(hdfsPath.toUri(), hdfsConf);
	}

	public static boolean deleteLocalDir(String dirName) {
		File startingDirectory= new File(dirName);
		return deleteDir(startingDirectory);
	}
	
	// Deletes all files and subdirectories under dir.
	// Returns true if all deletions were successful.
	// If a deletion fails, the method stops attempting to delete and returns false.
	public static boolean deleteDir(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }

	    // The directory is now empty so delete it
	    return dir.delete();
	}

	static public List<File> getFileListing(File aStartingDir) throws FileNotFoundException {
	    validateDirectory(aStartingDir);
	    List<File> result = getFileListingNoSort(aStartingDir);
	    Collections.sort(result);
	    return result;
	}


	static private List<File> getFileListingNoSort( File aStartingDir) throws FileNotFoundException 
	{
		List<File> result = new ArrayList<File>();
		File[] filesAndDirs = aStartingDir.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);
		for(File file : filesDirs) {
			if ( file.isFile() ) {
				result.add(file); //always add, even if directory
			}
	    }
	    return result;
	}
	

	/**
	* Directory is valid if it exists, does not represent a file, and can be read.
	*/
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

	public static void removeDirectory (Configuration conf, String dirName) throws IOException {
		Configuration confLocal = ( conf==null ) ? new Configuration() : conf;
	     FileSystem fs = FileSystem.get(confLocal);
	     Path filenamePath = new Path(dirName);
	     
	     if ( fs.exists(filenamePath) ) {
	    	 fs.delete(filenamePath, true);
	     }

	}
	
	public static void removeDirectory (JobConf conf, String dirName) throws IOException {
	     Configuration confLocal = ( conf==null ) ? new Configuration() : conf;
	     removeDirectory (confLocal, dirName);
	     //DistributedCache.addCacheFile(hdfsPath.toUri(), hdfsConf);
	}

	public static void createDirectory (JobConf conf, String dirName) throws IOException {
	     Configuration confLocal = ( conf==null ) ? new Configuration() : conf;
	     FileSystem fs = FileSystem.get(confLocal);
	     Path filenamePath = new Path(dirName);
	     
	     if (! fs.exists(filenamePath) ) {
	    	 fs.create(filenamePath);
	     }
	}

	public static void copyLocalDirToHDFS (JobConf conf, String srcPathName, String destPathName ) throws IOException {
		
		Configuration hdfsConf = (conf==null) ? new Configuration() : conf;
		removeDirectory (hdfsConf, destPathName);
	    FileSystem fs = FileSystem.get(hdfsConf);
	    Path hdfsPath = new Path(destPathName);
	    fs.copyFromLocalFile(new Path(destPathName), hdfsPath);
	    DistributedCache.addCacheFile(hdfsPath.toUri(), hdfsConf);
	}

	public static void copyHDFSDirToLocal (JobConf conf, String srcPathName, String destPathName ) throws IOException {
		
		boolean delFlag = deleteLocalDir(destPathName);
		Configuration hdfsConf = (conf==null) ? new Configuration() : conf;
	    FileSystem fs = FileSystem.get(hdfsConf);
	    Path hdfsPath = new Path(srcPathName);
	    Path localPath = new Path(destPathName);
	    fs.copyToLocalFile(hdfsPath, localPath);
	}

	public static void main(String[] args) throws Exception {
		System.out.println("start");
		copyLocalDirToHDFS (null, "/Users/lei/work/hadoop-example/input", "/Users/lei/work/hadoop-example/input" ) ;
		
		System.out.println("step2");
		copyHDFSDirToLocal (null, "/Users/lei/work/hadoop-example/input", "/Users/lei/work/hadoop-example/output" ) ;
		
		System.out.println("end");
		
//		System.out.println("here");
//		//createDirectory (null, "/tmp/tmp/tmp/1/1/1/1");
//		//createDirectory (null, "/tmp/tmp/");
//		File startingDirectory= new File("/Users/lei/work/hadoop-example/input/");
//		List<File> files = getFileListing(startingDirectory);
//
//		//print out all file names, in the the order of File.compareTo()
//		for(File file : files ) {
//			if (file.isFile())
//				System.out.println("File: " + file);
//			else 
//				System.out.println("Dir: " + file);
//		}

	}

}
