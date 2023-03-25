package com.zookeeper.curator;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.*;
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


    /**
     * 演示PathChilderCache：监听某个节点的所有子节点
     */
    @Test
    public void testNPathChilderCache() throws Exception {
        //设置监听对象
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/app2",true);
        //绑定监听
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println("子节点变化了");
                System.out.println(event);
                //监听子节点的变更，并且拿到子节点变更后的数据
                //1.获取类型
                PathChildrenCacheEvent.Type type = event.getType();
                //2.判断类型是否是updata
                if(type.equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)){
                    System.out.println("子节点变化了");
                    byte[] data = event.getData().getData();
                    System.out.println(new String(data));

                }
            }
        });
        //开启监听
        pathChildrenCache.start();

        while (true) {

        }
    }

    /**
     * 演示 TreeCache: 监听某个节点自己和所有子节点
     */
    @Test
    public void testTreeCache() throws Exception {
        //1.创建子节点
        TreeCache treeCache = new TreeCache(client,"/app2");
        //2.注册监听
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                System.out.println("节点变化了~");
                System.out.println(event);
            }
        });
        //3.开启监听
        treeCache.start();

        while (true) {

        }
    }

}
