package demo.dataInit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;


/**
 * 1. 需要一个jar包 :  hamcrest-core-1.3.jar
 * 2. 修改windows host 文件
 *    C:\Windows\System32\drivers\etc\hosts
 *    192.168.157.11 bigdata11
 */
public class TestHBase {

	@Test
	public void testCreateTable() throws Exception{
		//配置ZK的地址信息
		Configuration conf = new Configuration();

		conf.set("hbase.zookeeper.quorum", "192.168.157.11");

		//得到HBase的客户端
		HBaseAdmin client = new HBaseAdmin(conf);

		//创建表的描述符
		HTableDescriptor htd = new HTableDescriptor(TableName.valueOf("mytable"));

		//创建列族
		htd.addFamily(new HColumnDescriptor("info"));
		htd.addFamily(new HColumnDescriptor("grade"));

		//创建表
		client.createTable(htd);

		client.close();
	}

	@Test
	public void testPut() throws Exception{
		//配置ZK的地址信息
		Configuration conf = new Configuration();
		conf.set("hbase.zookeeper.quorum", "192.168.157.11");

		//得到HBase的客户端
		HTable client = new HTable(conf,"mytable");

		//构造一个Put对象, 参数：rowkey
		Put put = new Put(Bytes.toBytes("id001"));
//		put.addColumn(family,     列族
//				      qualifier,  列
//				      value)      值

		put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("Tom"));

		client.put(put);
		//一次插入多条记录：client.put(List<Put>);

		client.close();
	}

	@Test
	public void testGet() throws Exception{
		//配置ZK的地址信息
		Configuration conf = new Configuration();
		conf.set("hbase.zookeeper.quorum", "192.168.157.11");

		//得到HBase的客户端
		HTable client = new HTable(conf,"mytable");

		//构造一个Get对象，指定rowkey
		Get get = new Get(Bytes.toBytes("id001"));

		//查询
		Result r = client.get(get);

		//取出数据
		String name = Bytes.toString(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("name")));
		System.out.println(name);

		client.close();
	}

	@Test
	public void testScan() throws Exception{
		//配置ZK的地址信息
		Configuration conf = new Configuration();
		conf.set("hbase.zookeeper.quorum", "192.168.157.11");

		//得到HBase的客户端
		HTable client = new HTable(conf,"mytable");

		//定义一个扫描器
		Scan scan = new Scan();
		//scan.setFilter(filter) 定义一个过滤器

		//通过扫描器查询数据
		ResultScanner rs = client.getScanner(scan);

		for(Result r:rs){
			String name = Bytes.toString(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("name")));
			System.out.println(name);
		}

		client.close();
	}
}










