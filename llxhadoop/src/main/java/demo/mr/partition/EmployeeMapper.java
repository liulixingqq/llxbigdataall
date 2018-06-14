package demo.mr.partition;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/4/4
 */
public class EmployeeMapper extends Mapper<LongWritable, Text, IntWritable, Employee> {

    @Override
    protected void map(LongWritable k1, Text v1, Context context) throws IOException, InterruptedException {
        String data = v1.toString();
        String[] words = data.split(",");
// 数据：7654,MARTIN,SALESMAN,7698,1981/9/28,1250,1400,30
        //创建 e对象
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
        //员工号   e 写出到环境变量
        //输出  k2是部门号  v2是员工对象
        context.write(new IntWritable(e.getDeptno()), e);
    }
}
