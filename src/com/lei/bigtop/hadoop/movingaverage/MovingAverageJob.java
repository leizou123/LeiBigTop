package com.lei.bigtop.hadoop.movingaverage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.lei.bigtop.hadoop.util.HadoopUtil;
import com.lei.bigtop.hadoop.util.JavaUtil;

//home:tmp lei$ rm -rf /tmp/hadoop-lei/dfs
//home:tmp lei$ hadoop namenode -format

// hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.bigtop.hadoop.movingaverage.MovingAverageJob /Users/lei/work/hadoop-example/movingaverage/input/goog_price_2.csv /Users/lei/work/hadoop-example/movingaverage/output 
// hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.bigtop.hadoop.movingaverage.MovingAverageJob 
public class MovingAverageJob extends Configured implements Tool {

	public static final String DEF_OUTPUT_DIR_NAME = "/tmp/output";
	public static final String DEF_INPUT_DIR_NAME = "/Users/lei/work/hadoop-example/movingaverage/input/";

	
	public static final String DEF_LOCAL_FILE_NAME = "/Users/lei/work/hadoop-example/movingaverage/input/goog_price_2.csv";
	public static final String DEF_LOCAL_OUTPUT_DIR = "/Users/lei/work/hadoop-example/movingaverage/output";
	
	public static void removeDirectory (JobConf conf, String dirName) throws IOException {
	     Configuration confLocal = ( conf==null ) ? new Configuration() : conf;
	     FileSystem fs = FileSystem.get(confLocal);
	     Path filenamePath = new Path(dirName);
	     
	     if ( fs.exists(filenamePath) ) {
	    	 fs.delete(filenamePath, true);
	     }
	}
	


	@Override
	public int run(String[] args) throws Exception {

		System.out.println("\n\nMovingAverageJob\n");

		JobConf conf = new JobConf(getConf(), MovingAverageJob.class);
		conf.setJobName("MovingAverageJob");

		conf.setMapOutputKeyClass(TimeseriesKey.class);
		conf.setMapOutputValueClass(TimeseriesDataPoint.class);

		conf.setMapperClass(MovingAverageMapper.class);
		conf.setReducerClass(MovingAverageReducer.class);

		conf.setPartitionerClass(NaturalKeyPartitioner.class);
		conf.setOutputKeyComparatorClass(CompositeKeyComparator.class);
		conf.setOutputValueGroupingComparator(NaturalKeyGroupingComparator.class);

		
		List<String> other_args = new ArrayList<String>();
		for(int i=0; i < args.length; ++i) {
		     try {
		       if ("-m".equals(args[i])) {
		       	
		         conf.setNumMapTasks(Integer.parseInt(args[++i]));
		         
		       } else if ("-r".equals(args[i])) {
		       	
		         conf.setNumReduceTasks(Integer.parseInt(args[++i]));
		    	   		    	   
		       } else {
		       	
		         other_args.add(args[i]);
		         
		       }
		     } catch (NumberFormatException except) {
		       System.out.println("ERROR: Integer expected instead of " + args[i]);
		       return printUsage();
		     } catch (ArrayIndexOutOfBoundsException except) {
		       System.out.println("ERROR: Required parameter missing from " + args[i-1]);
		       return printUsage();
		     }
		}

		

		// Make sure there are exactly 2 parameters left.
		if ( ! ( other_args.size() == 2 || other_args.size() == 0 ) ) {
			System.out.println("ERROR: Wrong number of parameters: " + other_args.size() + " instead of 2 or 0.");
			return printUsage();
		} 

		String inputDir = null;
		String inputFileName = null;
		String outputDir = null;

		if (other_args.size() == 2) {
			inputFileName = other_args.get(0);
			outputDir = other_args.get(1);
		} else {
			inputFileName = DEF_LOCAL_FILE_NAME;
			outputDir = DEF_OUTPUT_DIR_NAME;
		}

		HadoopUtil.copyLocalFile (inputFileName, inputFileName, conf);
		inputDir = JavaUtil.getFilePath (inputFileName);
		HadoopUtil.removeDirectory (conf, outputDir);
		
		
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		conf.setCompressMapOutput(true);

		FileInputFormat.setInputPaths(conf, inputDir);
		FileOutputFormat.setOutputPath(conf, new Path(outputDir));

		JobClient.runJob(conf);

		HadoopUtil.copyHDFSDirToLocal (conf, outputDir, outputDir ) ;
		
		return 0;
	}

	static int printUsage() {
		System.out
				.println("MovingAverageJob [-m <maps>] [-r <reduces>] <input> <output>");
		ToolRunner.printGenericCommandUsage(System.out);
		return -1;
	}

	public static void main(String[] args) throws Exception {

		int res = ToolRunner.run(new Configuration(), new MovingAverageJob(), args);
		System.exit(res);

	}

}
