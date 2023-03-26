package com.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import javax.sound.midi.Track;
import java.util.concurrent.TimeUnit;

public class Ticket12306 implements Runnable {
    private int tickets = 10;//数据库票数

    private InterProcessMutex lock;

    public  Ticket12306(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3, 10);

        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.88.130")
                .sessionTimeoutMs(60000).connectionTimeoutMs(15000)
                .retryPolicy(retryPolicy).namespace("zookeeper").build();
        //开启连接
        client.start();
        lock = new InterProcessMutex(client,"/lock");
    }

    @Override
    public void run() {
        while (true) {
            try {
                //获取锁
                lock.acquire(3, TimeUnit.SECONDS);
                if (tickets > 0) {
                    System.out.println(Thread.currentThread() + ":" + tickets);
                    Thread.sleep(100);
                    tickets--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }
}
