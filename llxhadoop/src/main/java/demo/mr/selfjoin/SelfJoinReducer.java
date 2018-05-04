package demo.mr.selfjoin;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SelfJoinReducer extends Reducer<IntWritable, Text, Text, Text> {

	@Override
	protected void reduce(IntWritable k3, Iterable<Text> v3, Context context)
			throws IOException, InterruptedException {
		//定义变量：老板姓名  员工姓名
		String bossName = "";
		String empNameList = "";

		for(Text t:v3){
			String str = t.toString();

			//判断是否存在*号
			int index = str.indexOf("*");
			if(index >=0 ){
				//表示：老板姓名
				bossName = str.substring(1);
			}else{
				//员工的姓名
				empNameList = str + ";" + empNameList;
			}
		}

		//输出
		//判断：如果存在老板和员工  才输出
		if(bossName.length() > 0 && empNameList.length() > 0 ){
			context.write(new Text(bossName), new Text(empNameList));
		}
	}
}
















