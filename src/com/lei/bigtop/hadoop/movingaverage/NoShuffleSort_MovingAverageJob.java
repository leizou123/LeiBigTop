package com.lei.bigtop.hadoop.movingaverage;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
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

// hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.bigtop.hadoop.movingaverage.NoShuffleSort_MovingAverageJob /Users/lei/work/hadoop-example/movingaverage/input/goog_price_2.csv /Users/lei/work/hadoop-example/movingaverage/output
public class NoShuffleSort_MovingAverageJob extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {

		System.out.println("\n\nNoShuffleSort_MovingAverageJob\n");

		JobConf conf = new JobConf(getConf(), NoShuffleSort_MovingAverageJob.class);
		conf.setJobName("NoShuffleSort_MovingAverageJob");

		// since we only want to group by stock, we'll use the Text Class
		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(TimeseriesDataPoint.class);
		

		conf.setMapperClass(NoShuffleSort_MovingAverageMapper.class);
		conf.setReducerClass(NoShuffleSort_MovingAverageReducer.class);

		List<String> other_args = new ArrayList<String>();
		for (int i = 0; i < args.length; ++i) {
			try {
				if ("-m".equals(args[i])) {

					conf.setNumMapTasks(Integer.parseInt(args[++i]));

				} else if ("-r".equals(args[i])) {

					conf.setNumReduceTasks(Integer.parseInt(args[++i]));

				} else if ("-windowSize".equals(args[i])) {

					conf.set("hadoop.movingaverage.windowSize", args[++i]);

				} else if ("-windowStepSize".equals(args[i])) {
					conf.set("tv.floe.caduceus.hadoop.movingaverage.windowStepSize", args[++i]);
				} else {
					other_args.add(args[i]);
				}
			} catch (NumberFormatException except) {
				System.out.println("ERROR: Integer expected instead of "
						+ args[i]);
				return printUsage();
			} catch (ArrayIndexOutOfBoundsException except) {
				System.out.println("ERROR: Required parameter missing from "
						+ args[i - 1]);
				return printUsage();
			}
		}
		// Make sure there are exactly 2 parameters left.
		if (other_args.size() != 2) {
			System.out.println("ERROR: Wrong number of parameters: "
					+ other_args.size() + " instead of 2.");
			return printUsage();
		}

		conf.setInputFormat(TextInputFormat.class);

		conf.setOutputFormat(TextOutputFormat.class);
		conf.setCompressMapOutput(true);

		String inputDir = null;
		String inputFileName = null;
		String outputDir = null;

		if (other_args.size() == 2) {
			inputFileName = other_args.get(0);
			outputDir = other_args.get(1);
		} else {
			inputFileName = MovingAverageJob.DEF_LOCAL_FILE_NAME;
			outputDir = MovingAverageJob.DEF_OUTPUT_DIR_NAME;
		}

		HadoopUtil.copyLocalFile (inputFileName, inputFileName, conf);
		inputDir = JavaUtil.getFilePath (inputFileName);
		HadoopUtil.removeDirectory (conf, outputDir);

		FileInputFormat.setInputPaths(conf, other_args.get(0));
		FileOutputFormat.setOutputPath(conf, new Path(other_args.get(1)));

		JobClient.runJob(conf);
		
		HadoopUtil.copyHDFSDirToLocal (conf, outputDir, outputDir ) ;
		
		return 0;
	}

	static int printUsage() {
		System.out
				.println("NoShuffleSort_MovingAverageJob [-m <maps>] [-r <reduces>] <input> <output>");
		ToolRunner.printGenericCommandUsage(System.out);
		return -1;
	}

	public static void main(String[] args) throws Exception {

		int res = ToolRunner.run(new Configuration(), new NoShuffleSort_MovingAverageJob(), args);
		System.exit(res);

	}

}
