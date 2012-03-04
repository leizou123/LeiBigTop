package com.lei.bigtop.hadoop.countwarrantycall;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


import com.lei.bigtop.hadoop.util.HadoopUtil;

//
// hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.bigtop.hadoop.countwarrantycall.CountWarrantyCallJob  -DSVC_NAME=Warranty -Dmain.config.file="/Users/lei/Documents/workspace/HadoopSample/conf/WarrantyDataCount.properties" /Users/lei/AppleDoc/Project_Warranty/Data/hadoopdata/ /Users/lei/AppleDoc/Project_Warranty/Data/output
//
// com.lei.bigtop.hadoop.countwarrantycall.CountWarrantyCallJob
public class CountWarrantyCallJob extends Configured implements Tool {

	static public String CountWarrantyCallJob_TEMP_OUTPUT_DIR = "/tmp/CountWarrantyCallJob/data";
	
	public void configure(JobConf job) {
		String inputFile = job.get("map.input.file");

		String SVC_NAME = job.get("SVC_NAME");
		System.out.println("lei - " + SVC_NAME);
		
		String propFile = job.get("main.config.file");
		System.out.println("lei - propFile " + propFile);
	
	}

	public int run(String[] args) throws Exception {
		
		JobConf job = new JobConf(getConf(), CountResponseSerialNumberV2.class);
		
		System.out.println("start");
		System.out.println("input args");
		for (String arg : args) {
			System.out.println("arg - " + arg);
		}

		String SVC_NAME = job.get("SVC_NAME");
		System.out.println("lei - " + SVC_NAME);
		
		String propFile = job.get("main.config.file");
		System.out.println("lei - propFile " + propFile);

		System.out.println("copy config file [" + propFile + "] to HDFS file [" + propFile + "]");
		HadoopUtil.copyLocalFile (propFile, propFile, job);
		String inputDir = args[0];
		
		// copy local directory to dest directory 
		System.out.println("copy local directory [" + inputDir + "] to HDFS directory [" + inputDir + "]");
		HadoopUtil.copyLocalDirToHDFS (job, inputDir, inputDir );
		
		System.out.println("create HDFS directory [" + CountWarrantyCallJob_TEMP_OUTPUT_DIR + "]");
		HadoopUtil.removeDirectory (job, CountWarrantyCallJob_TEMP_OUTPUT_DIR);
		

		
		String[] argsCountResponseSerialNumberV2 = new String [4];
		argsCountResponseSerialNumberV2[0] = args[0];
		argsCountResponseSerialNumberV2[1] = CountWarrantyCallJob_TEMP_OUTPUT_DIR;
		argsCountResponseSerialNumberV2[2] = SVC_NAME;
		argsCountResponseSerialNumberV2[3] = propFile;
		System.out.println("start CountResponseSerialNumberV2 Job" );
		int res = ToolRunner.run(new Configuration(), new CountResponseSerialNumberV2(), argsCountResponseSerialNumberV2);
		System.out.println("CountResponseSerialNumberV2 Job ends return code [" + res + "]");
		
		String outputDir = args[1];
		HadoopUtil.removeDirectory (job, outputDir);
		String[] argsCountResponseSerialNumber10Sec = new String[3];
		argsCountResponseSerialNumber10Sec[0] = CountWarrantyCallJob_TEMP_OUTPUT_DIR;
		argsCountResponseSerialNumber10Sec[1] = outputDir;
		argsCountResponseSerialNumber10Sec[2] = propFile;
		int resCountResponseSerialNumber10Sec = ToolRunner.run(new Configuration(), new CountResponseSerialNumber10Sec(), argsCountResponseSerialNumber10Sec);
		System.out.println("CountResponseSerialNumber10Sec Job ends return code [" + resCountResponseSerialNumber10Sec + "]");
		
		// copy local directory to dest directory 
		System.out.println("copy HDFS directory [" + outputDir + "] to local directory [" + outputDir + "]");
		
		HadoopUtil.copyHDFSDirToLocal (job, outputDir, outputDir ) ;
		return 0;
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
	    
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length != 2) {
	      System.err.println("Usage: CountWarrantyCallJob <local_in_dir> <local_out_dir>");
	      System.exit(2);
	    }
	    

		int res = ToolRunner.run(new Configuration(), new CountWarrantyCallJob(), args);
		System.exit(res);
	}
}
