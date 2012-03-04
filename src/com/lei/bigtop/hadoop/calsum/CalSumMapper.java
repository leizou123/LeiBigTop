package com.lei.bigtop.hadoop.calsum;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import com.lei.bigtop.hadoop.util.JavaUtil;

public class CalSumMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, Text, DoubleWritable>{

    public void map(LongWritable l, Text text, OutputCollector<Text, DoubleWritable> output, Reporter report) throws IOException
    {
        String line = text.toString();

        if ( JavaUtil.isStringKeyNumSeparatedBySpace(line) )  {
        	StringTokenizer st = new StringTokenizer(line);
        	String k = (String) st.nextElement();
        	Double d = Double.valueOf( (String)st.nextElement());
        	output.collect(new Text(k), new DoubleWritable(d));
        }
        
    }
}
