package com.lei.hadoop.countavg;


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

// cp ./lib/json-lib-2.4-jdk15.jar /usr/local//hadoop20/share/hadoop/lib/
// rm  /usr/local//hadoop20/share/hadoop/lib/json-lib-2.4-jdk15.jar
// cp ./lib/ezmorph-1.0.6.jar  /usr/local//hadoop20/share/hadoop/lib/
// rm  /usr/local//hadoop20/share/hadoop/lib/ezmorph-1.0.6.jar
// hadoop  jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.hadoop.countavg.CountAvg /Users/lei/Documents/workspace/LeiBigTop/data /Users/lei/Documents/workspace/LeiBigTop/output
// hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.hadoop.countavg.CountAvg -libjars /Users/lei/Documents/workspace/LeiBigTop/lib/ezmorph-1.0.6.jar,/Users/lei/Documents/workspace/LeiBigTop/lib/json-lib-2.4-jdk15.jar  /Users/lei/Documents/workspace/LeiBigTop/data /Users/lei/Documents/workspace/LeiBigTop/output
// hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.hadoop.countavg.CountAvg -libjars ./lib/ezmorph-1.0.6.jar,./lib/json-lib-2.4-jdk15.jar  /Users/lei/Documents/workspace/LeiBigTop/data /Users/lei/Documents/workspace/LeiBigTop/output
public class CountAvg extends Configured implements Tool {

	public int run(String[] args) throws Exception {


		JobConf conf = new JobConf(getConf(), CountAvg.class);
		conf.setJobName("CountAvg");
		 	
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(DoubleWritable.class);
		 	
		conf.setMapperClass(CountAvgMapper.class);
		conf.setCombinerClass(CountAvgReducer.class);
		conf.setReducerClass(CountAvgReducer.class);
		 	
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
		      System.err.println("Usage: CountAvg <local_in_dir> <local_out_dir>");
		      return -1;
		} 	

		HadoopUtil.copyLocalDirToHDFS (conf, otherArgs[0], otherArgs[0]) ;		 	
		HadoopUtil.removeDirectory(conf, otherArgs[1]);
		FileInputFormat.setInputPaths(conf, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(conf, new Path(otherArgs[1]));
		 	
		JobClient.runJob(conf);

		HadoopUtil.copyHDFSDirToLocal (conf, otherArgs[1], otherArgs[1] ) ;

		return 0;
	}
	
	public static void main(String[] args) throws Exception {

		int res = ToolRunner.run(new Configuration(), new CountAvg(), args);
		System.exit(res);
	}
}


