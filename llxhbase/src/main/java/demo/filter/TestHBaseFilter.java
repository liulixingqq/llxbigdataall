package demo.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/4/14
 */
public class TestHBaseFilter {

    @Test
    public void testSingleColumnValueFilter() throws Exception{
        //列值过滤器: 查询薪水等于3000的员工
        Configuration conf= new Configuration();
        conf.set("hbase.zookeeper.quorum", "192.168.157.11");

        HTable table = new HTable(conf,"emp");

        SingleColumnValueFilter filter =
                new SingleColumnValueFilter(
                        Bytes.toBytes("empinfo"),
                        Bytes.toBytes("sal"),
                        CompareFilter.CompareOp.EQUAL,
                        Bytes.toBytes("3000"));


        //创建扫描器，并添加过滤器
        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner rs = table.getScanner(scan);

        for(Result r:rs){
            String name = Bytes.toString(r.getValue(Bytes.toBytes("empinfo"), Bytes.toBytes("ename")));
            System.out.println(name);
        }
        table.close();
    }

    @Test
    public void testColumnPrefixFilter() throws Exception{
        //列名前缀过滤器  查询员工的姓名：  select ename from emp;

        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "192.168.157.11");

        //得到客户端
        HTable table = new HTable(conf,"emp");

        ColumnPrefixFilter ename = new ColumnPrefixFilter(Bytes.toBytes("ename"));

        Scan scan = new Scan();
        scan.setFilter(ename);
        ResultScanner rs = table.getScanner(scan);
        for(Result r:rs){
            String name = Bytes.toString(r.getValue(Bytes.toBytes("empinfo"), Bytes.toBytes("ename")));

            //获取员工的薪水
            String sal = Bytes.toString(r.getValue(Bytes.toBytes("empinfo"), Bytes.toBytes("sal")));

            System.out.println(name+"\t"+sal);
        }
        table.close();

    }

    @Test
    public void testMultipleColumnPrefixFilter() throws Exception {
        //多个列名前缀过滤器
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "192.168.157.11");

        //得到客户端
        HTable table = new HTable(conf,"emp");

        //二维数组
        byte[][] names ={Bytes.toBytes("ename"),Bytes.toBytes("sal")};

        MultipleColumnPrefixFilter filter = new MultipleColumnPrefixFilter(names);

        Scan scan = new Scan();
        scan.setFilter(filter);

        //查询数据
        ResultScanner rs = table.getScanner(scan);
        for(Result r:rs){
            String name = Bytes.toString(r.getValue(Bytes.toBytes("empinfo"), Bytes.toBytes("ename")));

            //获取员工的薪水
            String sal = Bytes.toString(r.getValue(Bytes.toBytes("empinfo"), Bytes.toBytes("sal")));

            System.out.println(name+"\t"+sal);
        }

        table.close();


    }

    @Test
    public void testRowFilter() throws Exception {
        //行过滤器询员工号7839的信息
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "192.168.157.11");

        //得到客户端
        HTable table = new HTable(conf,"emp");


        RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("7839"));

        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner rs = table.getScanner(scan);

        for(Result r:rs){
            String name = Bytes.toString(r.getValue(Bytes.toBytes("empinfo"), Bytes.toBytes("ename")));

            //获取员工的薪水
            String sal = Bytes.toString(r.getValue(Bytes.toBytes("empinfo"), Bytes.toBytes("sal")));

            System.out.println(name+"\t"+sal);
        }

        table.close();

    }

    @Test
    public void testFilter() throws Exception {
		/*
		 * 查询工资等于3000的员工姓名      select ename from emp where sal=3000;
		 * 1、列值过滤器：工资等于3000
		 * 2、列名前缀过滤器：姓名
		 */

        //配置ZooKeeper
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "192.168.157.11");

        //得到客户端
        HTable table = new HTable(conf,"emp");

        SingleColumnValueFilter f = new SingleColumnValueFilter(Bytes.toBytes("empinfo"), Bytes.toBytes("sal"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes("3000"));

        //第二个过滤器：列名前缀 姓名
        ColumnPrefixFilter filter2 = new ColumnPrefixFilter(Bytes.toBytes("ename"));

        //创建一个FliterList
        //Operator.MUST_PASS_ALL 相当于 and
        //Operator.MUST_PASS_ONE 相当于 or

        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        filterList.addFilter(f);
        filterList.addFilter(filter2);

        Scan scan = new Scan();
        scan.setFilter(filterList);

        ResultScanner rs = table.getScanner(scan);

        for(Result r:rs){
            String name = Bytes.toString(r.getValue(Bytes.toBytes("empinfo"), Bytes.toBytes("ename")));

            //获取员工的薪水
            String sal = Bytes.toString(r.getValue(Bytes.toBytes("empinfo"), Bytes.toBytes("sal")));

            System.out.println(name+"\t"+sal);
        }

        table.close();


    }


    }
