package demo.mr.distinct;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DistinctReducer extends Reducer<Text, NullWritable, Text, NullWritable> {

	@Override
	protected void reduce(Text k3, Iterable<NullWritable> v3,Context context) throws IOException, InterruptedException {
		context.write(k3, NullWritable.get());
	}
}
