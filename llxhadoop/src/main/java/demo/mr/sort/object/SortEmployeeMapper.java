package demo.mr.sort.object;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
//                                                                k2 就是这个员工对象
public class SortEmployeeMapper extends Mapper<LongWritable, Text, Employee, NullWritable> {

	@Override
	protected void map(LongWritable key1, Text value1, Context context)
			throws IOException, InterruptedException {
		// 数据：7654,MARTIN,SALESMAN,7698,1981/9/28,1250,1400,30
		String data = value1.toString();

		//分词
		String[] words = data.split(",");

		//创建员工的对象
		Employee e = new Employee();

		//设置员工的属性
		//员工号
		e.setEmpno(Integer.parseInt(words[0]));
		//姓名
		e.setEname(words[1]);
		//职位
		e.setJob(words[2]);
		//经理号: 注意：有些员工没有经理
		try{
			e.setMgr(Integer.parseInt(words[3]));
		}catch(Exception ex){
			//没有老板号
			e.setMgr(-1);
		}

		//入职日期
		e.setHiredate(words[4]);
		//薪水
		e.setSal(Integer.parseInt(words[5]));
		//奖金：注意：有些员工没有奖金
		try{
			e.setComm(Integer.parseInt(words[6]));
		}catch(Exception ex){
			//没有奖金
			e.setComm(0);
		}
		//部门号
		e.setDeptno(Integer.parseInt(words[7]));

		//输出
		context.write(e, NullWritable.get());
	}

}














