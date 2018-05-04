package demo.hdfs.proxy.datasource;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class MyDataSourcePool implements DataSource {


	//初始化一下：创建10个链接放入连接池
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://192.168.157.11:3306/hive";
	private static String user = "hiveowner";
	private static String password = "Welcome_1";

	//定义一个链表来保存10个链接
	private static LinkedList<Connection> dataSource = new LinkedList<>();
	static{
		try{
			//注册驱动
			Class.forName(driver);

			for(int i=0;i<10;i++){
				dataSource.add(DriverManager.getConnection(url, user, password));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}


	@Override
	public Connection getConnection() throws SQLException {
		// 表示从连接池中获取链接
//		if(dataSource.size() > 0){
//			//有链接，这里返回的链接是真正的对象，现在返回一个代理对象，并且重写close方法
//			return dataSource.removeFirst();
//		}else{
//			throw new SQLException("系统忙，请稍后......");
//		}

		if(dataSource.size() > 0){
			//真正对象
			Connection conn = dataSource.removeFirst();

			//生成代理对象
			Connection proxy = (Connection) Proxy.newProxyInstance(MyDataSourcePool.class.getClassLoader(),
					conn.getClass().getInterfaces(),
					new InvocationHandler() {

						@Override
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							// 重写close方法
							if(method.getName().equals("close")){
								//将链接还池
								System.out.println("将链接还池");
								dataSource.add(conn);
								return null;
							}else{
								//其他方法直接 调用真正的对象完成
								return method.invoke(conn, args);
							}
						}
					});

			return proxy;
		}else{
			throw new SQLException("系统忙，请稍后......");
		}

	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


}
