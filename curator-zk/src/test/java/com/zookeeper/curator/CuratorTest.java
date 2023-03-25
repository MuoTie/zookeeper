package com.zookeeper.curator;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
    //============================================================================================


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

    }

    @Test
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


    //============================================================================================

    /**
     * 查询节点：
     * 1.查询数据：get
     * 2.查询子节点： ls
     * 3.查询子节点状态信息： ls -s
     */
    @Test
    public void testGet1() throws Exception {
        byte[] data = client.getData().forPath("/app1");
        System.out.println(new String(data));
    }

    @Test
    public void testGet2() throws Exception {
        //查询子节点： ls
        List<String> path = client.getChildren().forPath("/app4");
        System.out.println(path);
    }

    @Test
    public void testGet3() throws Exception {
        //查询子节点状态信息： ls -s
        Stat status = new Stat();
        System.out.println(status);

        client.getData().storingStatIn(status).forPath("/app1");
        System.out.println(status);
    }

    //============================================================================================


    /**
     * 修改数据
     * 1.修改数据
     * 2.根据版本修改数据
     * @throws Exception
     */
    @Test
    public void testset() throws Exception {
    client.setData().forPath("/app1","itcast".getBytes());
    }
    @Test
    public void testsetForVersion() throws Exception {
        //查询子节点状态信息： ls -s
        Stat status = new Stat();
        client.getData().storingStatIn(status).forPath("/app1");
        //version是通过查询出来的。目的是为了让其他客户端或者线程不干扰我操作
        int version = status.getVersion();
        System.out.println(version);
        client.setData().withVersion(version).forPath("/app1","itcast".getBytes());
    }

    @After
    public void close() {
        if (client != null) {
            client.close();
        }
    }

}
