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

import com.lei.bigtop.hadoop.util.HadoopUtil;

public class CalSumJSONJob extends Configured implements Tool {

	public int run(String[] args) throws Exception {


		JobConf conf = new JobConf(getConf(), CalSum.class);
		conf.setJobName("CalSumJSONJob");
		 	
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(DoubleWritable.class);
		 	
		conf.setMapperClass(CalSumMapperJSON.class);
		conf.setCombinerClass(CalSumReducer.class);
		conf.setReducerClass(CalSumReducer.class);
		 	
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
		      System.err.println("Usage: CalSumJSONJob <local_in_dir> <local_out_dir>");
		      return -1;
		} 	

		HadoopUtil.copyLocalDirToHDFS (conf, otherArgs[0], otherArgs[0]) ;		 	
		HadoopUtil.removeDirectory(conf, otherArgs[1]);
		
		FileInputFormat.setInputPaths(conf, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(conf, new Path(otherArgs[1]));
		 	
		JobClient.runJob(conf);

		//System.out.println("Copying");
		HadoopUtil.copyHDFSDirToLocal (conf, otherArgs[1], otherArgs[1] ) ;

		return 0;
	}
	
	public static void main(String[] args) throws Exception {

		int res = ToolRunner.run(new Configuration(), new CalSumJSONJob(), args);
		
		System.exit(res);
	}
}


