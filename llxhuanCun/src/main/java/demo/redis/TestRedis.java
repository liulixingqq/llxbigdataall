package demo.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestRedis{

@Test
public void testTransaction(){
    //创建一个客户端
    Jedis jedis = new Jedis("192.168.157.11", 6379);

    //定义事务
    Transaction tc = null;
    try{
        //开启事务
        tc = jedis.multi();

        //转账
        tc.decrBy("tom", 100);
        tc.incrBy("mike",100);

        //提交事务
        tc.exec();
    }catch(Exception ex){
        if(tc != null){
            tc.discard();
        }
    }

    jedis.disconnect();
}
}
