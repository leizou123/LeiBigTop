package com.lei.hadoop.countavg;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class CountAvgReducer extends MapReduceBase
        implements Reducer<Text, DoubleWritable, Text, DoubleWritable>
{

	@Override
	public void reduce(Text key, Iterator<DoubleWritable> values,
			OutputCollector<Text, DoubleWritable> output, Reporter reporter)
			throws IOException {
        double sum = 0;
        double counter = 0.0;

        while(values.hasNext())
        {
            DoubleWritable value = (DoubleWritable)values.next();
            sum += value.get();
            counter++;
        }
        output.collect(key, new DoubleWritable((sum/counter)));
		
	}
}