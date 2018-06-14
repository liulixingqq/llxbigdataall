package demo.FileUD;

import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

public class TestMetaData {

    @Test
    public void test1() throws Exception{
        //获取HDFS的目录信息
        //指定NameNode地址
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://bigdata11:9000");

        //创建一个HDFS的客户端
        FileSystem client = FileSystem.get(conf);

        FileStatus[] fsList =  client.listStatus(new Path("/folder1"));
        for(FileStatus s: fsList){
            System.out.println("文件还是目录？" + (s.isDirectory()?"目录":"文件"));
            System.out.println(s.getPath().toString());
        }
    }

    @Test
    public void test2() throws Exception{
        //获取文件的数据块信息

        //指定NameNode地址
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://bigdata11:9000");

        //创建一个HDFS的客户端
        FileSystem client = FileSystem.get(conf);

        //获取文件：/folder1/a.tar.gz数据块的信息
        FileStatus fs = client.getFileStatus(new Path("/folder1/a.tar.gz"));

        //通过文件的FileStatus获取相应的数据块信息
		/*
		 * 0: 表示从头开始获取
		 * fs.getLen(): 表示文件的长度
		 * 返回值：数据块的数组
		 */
        BlockLocation[] blkLocations = client.getFileBlockLocations(fs, 0, fs.getLen());
        for(BlockLocation b: blkLocations){
            //数据块的主机信息: 数组，表示同一个数据块的多个副本（冗余）被 保存到了不同的主机上
            System.out.println(Arrays.toString(b.getHosts()));

            //获取的数据块的名称
            System.out.println(Arrays.toString(b.getNames()));
        }

        client.close();
    }
}
























