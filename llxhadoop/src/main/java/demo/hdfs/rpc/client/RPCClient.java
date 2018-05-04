package demo.hdfs.rpc.client;


import java.net.InetSocketAddress;

import demo.hdfs.rpc.server.MyBusiness;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;



public class RPCClient {

    public static void main(String[] args) throws Exception {
        // 调用Server的功能
        MyBusiness proxy = RPC.getProxy(MyBusiness.class,    //调用的接口
                MyBusiness.versionID,       //版本的ID ，必须跟服务器端一致
                new InetSocketAddress("localhost", 7788),    //服务器端的地址
                new Configuration());

        System.out.println(proxy.sayHello("Tom"));
    }

}
