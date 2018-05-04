package demo.hive;

import org.apache.hadoop.hive.ql.exec.UDF;

/**
 *  * 1、进入hive客户端，添加jar包
 * add jar /root/temp/udf.jar;
 * 2、创建临时函数：
 * create temporary function myconcat as 'demo.hive.CheckSalaryGrade'
 *3、 查询使用
 * select myconcat(ename,job) from emp;
 * select ename,sal,checksalary(sal) from emp;
 *4、销毁临时函数：
 * DROP TEMPORARY FUNCTION checksalary;
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/4/15
 */
public class MyConcatString  extends UDF{


    public String evaluate(String a,String b){

        return a.toString()+ "拼接字符串" +b.toString();
    }
}
