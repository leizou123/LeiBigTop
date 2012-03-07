package com.lei.bigtop.hadoop.calsum;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.lei.bigtop.hadoop.util.*;




// http://www.cloudera.com/blog/2011/01/how-to-include-third-party-libraries-in-your-map-reduce-job/
// com.lei.bigtop.hadoop.calsum.CalSum
//hadoop  jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.bigtop.hadoop.calsum.CalSum /Users/lei/Documents/workspace/LeiBigTop/data /Users/lei/Documents/workspace/LeiBigTop/output
public class CalSum extends Configured implements Tool {

	public int run(String[] args) throws Exception {


		JobConf conf = new JobConf(getConf(), CalSum.class);
		conf.setJobName("CalSum");
		 	
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(DoubleWritable.class);
		 	
		conf.setMapperClass(CalSumMapper.class);
		conf.setCombinerClass(CalSumReducer.class);
		conf.setReducerClass(CalSumReducer.class);
		 	
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
		      System.err.println("Usage: CalSum <local_in_dir> <local_out_dir>");
		      return -1;
		} 	

		String inputDir = JavaUtil.getDirectoryName(otherArgs[0]); 
		String outputDir = JavaUtil.getDirectoryName(otherArgs[1]);
		
		JavaUtil.prepareLocalData(inputDir);
		
		HadoopUtil.copyLocalDirToHDFS (conf, inputDir, inputDir) ;		 	
		HadoopUtil.removeDirectory(conf, outputDir);
		
		FileInputFormat.setInputPaths(conf, new Path(inputDir));
		FileOutputFormat.setOutputPath(conf, new Path(outputDir));
		 	
		JobClient.runJob(conf);
		HadoopUtil.copyHDFSDirToLocal (conf, outputDir, outputDir ) ;
		
		if ( JavaUtil.compareFileContentSum(inputDir, outputDir) )
			return 0;
		else {
			return -1;
		}
	}
	
	public static void main(String[] args) throws Exception {

		int res = ToolRunner.run(new Configuration(), new CalSum(), args);
		
		System.exit(res);
	}
}


