package com.lei.bigtop.hadoop.calsum;

import java.io.IOException;
import java.util.Iterator;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class CalSumMapperJSON extends MapReduceBase
	implements Mapper<LongWritable, Text, Text, DoubleWritable>{

    public void map(LongWritable l, Text text, OutputCollector<Text, DoubleWritable> output, Reporter report) throws IOException
    {
        String line = text.toString();

        JSONObject obj2 = (JSONObject) JSONSerializer.toJSON(line);
		Iterator<String> keys = obj2.keys();
		while (keys.hasNext()) {
			String k = keys.next();
			Double d = obj2.getDouble(k);
			output.collect(new Text(k), new DoubleWritable(d));
		}
        
    }
}
