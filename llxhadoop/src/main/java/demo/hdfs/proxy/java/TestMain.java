package demo.hdfs.proxy.java;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestMain {


	public static void main(String[] args) {
		//创建真正的对象
		MyService obj = new MyServiceImpl();

		//obj.method1();  ---> 都是直接调用真正对象
		//obj.method2();  ---> 都是直接调用真正对象

		/*
		 * Object Proxy.newProxyInstance(ClassLoader loader, 类加载器，代理对象跟真正对象是同一个类加载器
                                      Class<?>[] interfaces, 真正对象实现的接口
                                      InvocationHandler h) 如何处理客户端的调用

           		返回值：就是代理对象

		 */
		//生成obj的代理对象，并且重写method2的逻辑
		MyService proxy = (MyService) Proxy.newProxyInstance(TestMain.class.getClassLoader(),
				obj.getClass().getInterfaces(),
				new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						// 表示如何处理客户端的调用
						if(method.getName().equals("method2")){
							//表示客户端调用了method2
							System.out.println("in proxy：method2");
							return null;
						}else{
							//其他方法：不感兴趣
							return method.invoke(obj, args);
						}
					}
				});

		//通过代理对象进行调用
		proxy.method1();
		proxy.method2();

	}

}













