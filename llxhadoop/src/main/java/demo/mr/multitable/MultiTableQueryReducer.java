package demo.mr.multitable;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MultiTableQueryReducer extends Reducer<IntWritable, Text, Text, Text> {

	@Override
	protected void reduce(IntWritable k3, Iterable<Text> v3, Context context)
			throws IOException, InterruptedException {
		//定义变量：保存 部门名称 和 员工姓名
		String dname = "";
		String empNameList = "";

		for(Text v:v3){
			String str = v.toString();

			//找到*号的位置
			int index = str.indexOf("*");
			if(index >= 0){
				//代表的是部门名称
				dname = str.substring(1);
			}else{
				//代表的就是员工姓名
				empNameList = str + ";" + empNameList;
			}
		}

		//输出
		context.write(new Text(dname), new Text(empNameList));
	}
}
















