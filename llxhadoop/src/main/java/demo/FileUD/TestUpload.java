package demo.FileUD;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

public class TestUpload {
	@Test
	public void testUpload1() throws Exception{
		//指定NameNode地址
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://bigdata11:9000");

		//创建一个HDFS的客户端
		FileSystem client = FileSystem.get(conf);

		//构造一个输出流，指向HDFS
		OutputStream out = client.create(new Path("/folder1/a.tar.gz"));

		//构造一个输入流，从本地文件读入数据
		InputStream in = new FileInputStream("d:\\temp\\hadoop-2.7.3.tar.gz");

		//下面是Java的基本IO
		//缓冲区
		byte[] buffer = new byte[1024];
		//长度
		int len = 0;
		while((len=in.read(buffer)) > 0){
			//表示读入了数据，再输出
			out.write(buffer,0,len);
		}

		out.flush();

		out.close();
		in.close();
	}

	@Test
	public void testUpload2() throws Exception{
		//使用HDFS的工具类来简化程序
		//指定NameNode地址
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://bigdata11:9000");

		//创建一个HDFS的客户端
		FileSystem client = FileSystem.get(conf);

		//构造一个输出流，指向HDFS
		OutputStream out = client.create(new Path("/folder1/b.tar.gz"));

		//构造一个输入流，从本地文件读入数据
		InputStream in = new FileInputStream("d:\\temp\\hadoop-2.7.3.tar.gz");

		//使用HDFS的工具类来简化程序
		IOUtils.copyBytes(in, out, 1024);
	}
}
















