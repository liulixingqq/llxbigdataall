package demo.zk;

public class TestZKDistributedLock {

    //定义一个共享的资源
    private static int number = 10;
    private static void getNumber(){
        System.out.println("\n\n********** 业务方法开始 **********");
        System.out.println("当前值：" + number);
        number --;

        //睡2秒钟
        try{
            Thread.sleep(2000);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        System.out.println("********** 业务方法结束 **********\n\n");
    }


    public static void main(String[] args) {
        //定义重试的策略（等待策略）
        /*
         * 1000: 等待的时间
         * 10：重试的次数
         */
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 10);

        //创建客户端请求从ZK中获取锁
        CuratorFramework cf = CuratorFrameworkFactory.builder()
                .connectString("192.168.157.11:2181")  //ZK的地址
                .retryPolicy(policy) //定义重试的策略（等待策略）
                .build();

        //启用客户端
        cf.start();

        //获取锁的信息
        final InterProcessMutex lock = new InterProcessMutex(cf,"/mylock");


        // 启动10个线程访问共享资源
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {

                public void run() {
                    try{
                        //请求得到锁: 如果没有得到，就会使用RetryPolicy
                        lock.acquire();

                        //实现访问共享的资源
                        getNumber();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }finally{
                        try {
                            //释放锁
                            lock.release();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
}
