package com.zookeeper.curator;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CuratorWatcherTest {

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
        //第二中方法
        client = CuratorFrameworkFactory.builder().connectString("192.168.88.130")
                .sessionTimeoutMs(60000).connectionTimeoutMs(15000)
                .retryPolicy(retryPolicy).namespace("zookeeper").build();
        //开启连接
        client.start();
    }

    @After
    public void close() {
        if (client != null) {
            client.close();
        }
    }

    /**
     * 演示NodeCache：给指定一个节点注册监听器
     */
    @Test
    public void testNodeCache() throws Exception {
        //1.创建NodeCache对象
        NodeCache nodeCache = new NodeCache(client, "/app1");
        //2.注册监听
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("节点变化了~");
                //获取修改节点后的数据
                byte[] data = nodeCache.getCurrentData().getData();
                System.out.println(new String(data));
            }
        });
        //3.开启监听.如果设置为true，则开启监听是加载缓冲数据
        nodeCache.start(true);
        while(true){

        }
    }

}
