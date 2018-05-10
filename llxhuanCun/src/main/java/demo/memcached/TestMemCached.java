package demo.memcached;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/5/10
 */

import net.spy.memcached.MemcachedClient;
import org.junit.Test;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class TestMemCached {

    @Test
    public void testInsert() throws Exception{
        //插入一个字符串
        //创建一个客户端
        MemcachedClient client = new MemcachedClient(new InetSocketAddress("192.168.157.11", 11211));

        //插入数据
        Future<Boolean> f = client.set("key1", 0, "HelloWorld");
        if(f.get().booleanValue()){
            //表示插入成功
            client.shutdown();
        }
    }


    @Test
    public void testInsertObject() throws Exception{
        //插入一个对象：(*)必须实现序列化  (*) 不建议保存大对象
        //创建一个客户端
        MemcachedClient client = new MemcachedClient(new InetSocketAddress("192.168.157.11", 11211));

        //插入数据
        Future<Boolean> f = client.set("stu001", 0, new Student());
        if(f.get().booleanValue()){
            //表示插入成功
            client.shutdown();
        }
    }

    @Test
    public void testGet() throws Exception{
        //创建一个客户端
        MemcachedClient client = new MemcachedClient(new InetSocketAddress("192.168.157.11", 11211));
        Object obj = client.get("key1");
        System.out.println(obj);
        client.shutdown();
    }

    @Test
    public void testInsertList() throws Exception{
		/*
		 * 启动三个MemCached实例，客户端的路由算法
		 */
        //创建一个List
        List<InetSocketAddress> list = new ArrayList<>();
        list.add(new InetSocketAddress("192.168.157.11", 11211));
        list.add(new InetSocketAddress("192.168.157.11", 11212));
        list.add(new InetSocketAddress("192.168.157.11", 11213));

        //创建一个客户端
        MemcachedClient client = new MemcachedClient(list);

        //插入20条数据
        for(int i=0;i<20;i++){
            System.out.println("插入的数据是：" + "key"+i+"    "+"value"+i);

            client.set("key"+i, 0, "value"+i);

            //睡1秒
            Thread.sleep(1000);
        }

        //关闭客户端
        client.shutdown();
    }

}

//定义一个类
class Student implements Serializable{

}

