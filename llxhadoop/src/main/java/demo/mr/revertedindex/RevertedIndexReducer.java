package demo.mr.revertedindex;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class RevertedIndexReducer extends Reducer<Text, Text, Text, Text> {

	@Override
	protected void reduce(Text k3, Iterable<Text> v3, Context context)
			throws IOException, InterruptedException {
		// 对combiner数据value，拼加
		String str = "";

		for(Text t:v3){
			str = "("+t.toString()+")" + str;
		}

		//输出
		context.write(k3, new Text(str));
	}
}
