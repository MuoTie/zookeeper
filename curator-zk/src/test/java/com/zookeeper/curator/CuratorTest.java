package com.zookeeper.curator;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorTest {

    private CuratorFramework client;
    /*
    建立连接
     */

    @Before
    public void testConnect() {
        /*
        重试策略
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3, 10);
        //第一种方法
//        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.88.130",
//                60000, 15000, retryPolicy);

        //第二中方法
        client = CuratorFrameworkFactory.builder().connectString("192.168.88.130")
                .sessionTimeoutMs(60000).connectionTimeoutMs(15000)
                .retryPolicy(retryPolicy).namespace("zookeeper").build();
        //开启连接
        client.start();
    }

    /*
    创建节点： create 持久  临时  顺序  数据
    1.基本创建 create().forpath("")
    2.创建节点 带有数据
    3.设置节点类型
    4.创建多级节点 /app1/p1
     */
    @Test
    public void testCreat1() throws Exception {
        //1.基本创建
        //如果创建节点没有指定数据，则默认将当前客户端的IP作为数据存储

        String path = client.create().forPath("/app1");
        System.out.println(path);

    }

    @Test
    public void testCreat2() throws Exception {
//        2.创建节点 带有数据
        String path = client.create().forPath("/app2", "hehe".getBytes());
        System.out.println(path);

    }   @Test
    public void testCreat3() throws Exception {
//        3.设置节点类型
        //默认类型：持久化
        String path = client.create().withMode(CreateMode.EPHEMERAL).forPath("/app3");
        System.out.println(path);
//        4.创建多级节点 /app1/p1

    }

    @Test
    public void testCreat4() throws Exception {
//        4.创建多级节点 /app1/p1
        String path = client.create().creatingParentsIfNeeded().forPath("/app4/p1");
        System.out.println(path);

    }

    @After
    public void close() {
        if (client != null) {
            client.close();
        }
    }
}
