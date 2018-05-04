package demo.FileUD;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

public class TestDownload {

	@Test
	public void testDownload() throws Exception{

			//指定NameNode地址
			Configuration conf = new Configuration();
			conf.set("fs.defaultFS", "hdfs://bigdata11:9000");

			//创建一个HDFS的客户端
			FileSystem client = FileSystem.get(conf);

			//从HDFS获取一个输入流
			InputStream in = client.open(new Path("/folder1/a.tar.gz"));

			//创建一个输出流，指向本地进行下载
			OutputStream out = new FileOutputStream("d:\\temp\\aaa.tar.gz");

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
		public void testDownload1() throws Exception{
			//指定NameNode地址
			Configuration conf = new Configuration();
			conf.set("fs.defaultFS", "hdfs://bigdata11:9000");

			//创建一个HDFS的客户端
			FileSystem client = FileSystem.get(conf);

			//从HDFS获取一个输入流
			InputStream in = client.open(new Path("/folder1/a.tar.gz"));

			//创建一个输出流，指向本地进行下载
			OutputStream out = new FileOutputStream("d:\\temp\\bbb.tar.gz");

			//使用HDFS的工具类来简化程序
			IOUtils.copyBytes(in, out, 1024);
		}

}


















