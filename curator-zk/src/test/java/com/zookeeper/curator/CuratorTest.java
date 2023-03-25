package com.zookeeper.curator;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

public class CuratorTest {
    /*
    建立连接
     */

    @Test
    public void testConnect(){
        /*
        重试策略
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3,10);
        //第一种方法
//        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.88.130",
//                60000, 15000, retryPolicy);

        //第二中方法
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.88.130")
                .sessionTimeoutMs( 60000).connectionTimeoutMs(15000).retryPolicy(retryPolicy).namespace("zookeeper").build();
        //开启连接
        client.start();
    }
}
