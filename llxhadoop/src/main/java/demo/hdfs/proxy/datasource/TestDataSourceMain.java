package demo.hdfs.proxy.datasource;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDataSourceMain {

	public static void main(String[] args) throws Exception {
		//创建一个连接池
		MyDataSourcePool pool = new MyDataSourcePool();

		//取出链接使用
		for(int i=0;i<11;i++){
			Connection conn = pool.getConnection();

			System.out.println("第"+i + "个："+ conn);

			//关闭
			conn.close(); //----> 调用的是真正对象Connection.close方法  把该链接还给数据库，而不是还给连接池
		}

	}


}
