package demo.FileUD;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

/**
 * 使用HDFS的Java API创建一个目录
 *
 * 权限的问题：
 * org.apache.hadoop.security.AccessControlException: Permission denied:
 * user=lenovo, access=WRITE, inode="/folder1":root:supergroup:drwxr-xr-x
 *
 * 四种办法解决：
 * 1、设置执行程序的用户是：root（HADOOP_USER_NAME）
 * 2、使用Java的-D参数: HADOOP_USER_NAME
 * 3、使用命令改变目录的权限：hdfs dfs -chmod 777 /folder2
 * 4、参数：dfs.permissions  ---> false
 */
public class CreateFileTest {
    @Test
    public void test1() throws Exception{
        //办法一：设置执行程序的用户是：root
        System.setProperty("HADOOP_USER_NAME", "root");

        //指定NameNode地址
        Configuration conf = new Configuration();
        //如果要使用主机名，需要配置Windows的host文件
        //C:\Windows\System32\drivers\etc\hosts文件
        conf.set("fs.defaultFS", "hdfs://bigdata11:9000");

		/*
		 * 还有一种写法：IP地址
		 * conf.set("fs.defaultFS", "hdfs://192.168.157.11:9000");
		 */

        //创建一个HDFS的客户端
        FileSystem client = FileSystem.get(conf);
        //创建目录
        client.mkdirs(new Path("/folder1"));

        //关闭客户端
        client.close();
    }

    @Test
    public void test2() throws Exception{
        //指定NameNode地址
        Configuration conf = new Configuration();
        //如果要使用主机名，需要配置Windows的host文件
        //C:\Windows\System32\drivers\etc\hosts文件
        conf.set("fs.defaultFS", "hdfs://bigdata11:9000");

		/*
		 * 还有一种写法：IP地址
		 * conf.set("fs.defaultFS", "hdfs://192.168.157.11:9000");
		 */

        //创建一个HDFS的客户端
        FileSystem client = FileSystem.get(conf);
        //创建目录
        client.mkdirs(new Path("/folder2"));

        //关闭客户端
        client.close();
    }

    @Test
    public void test3() throws Exception{
        //指定NameNode地址
        Configuration conf = new Configuration();
        //如果要使用主机名，需要配置Windows的host文件
        //C:\Windows\System32\drivers\etc\hosts文件
        conf.set("fs.defaultFS", "hdfs://bigdata11:9000");

		/*
		 * 还有一种写法：IP地址
		 * conf.set("fs.defaultFS", "hdfs://192.168.157.11:9000");
		 */

        //创建一个HDFS的客户端
        FileSystem client = FileSystem.get(conf);
        //创建目录
        client.mkdirs(new Path("/folder2/folder3"));

        //关闭客户端
        client.close();
    }

    @Test
    public void test4() throws Exception{
        //指定NameNode地址
        Configuration conf = new Configuration();
        //如果要使用主机名，需要配置Windows的host文件
        //C:\Windows\System32\drivers\etc\hosts文件
        conf.set("fs.defaultFS", "hdfs://bigdata11:9000");

		/*
		 * 还有一种写法：IP地址
		 * conf.set("fs.defaultFS", "hdfs://192.168.157.11:9000");
		 */

        //创建一个HDFS的客户端
        FileSystem client = FileSystem.get(conf);
        //创建目录
        client.mkdirs(new Path("/folder4"));

        //关闭客户端
        client.close();
    }

    @Test
    public void test5() throws Exception{
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://bigdata11:9000");
        FileSystem fileSystem = FileSystem.get(conf);
        fileSystem.mkdirs(new Path("/llxtest"));
    }
}
