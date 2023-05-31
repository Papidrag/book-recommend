package com.test.productconnsumer;

import java.util.List;

/**
 * @Author: cxx
 * @Date: 2018/4/10 17:42
 */
public class Consumer implements Runnable {
    private List<Product> list;
    public Consumer(List<Product> list){
        this.list = list;
    }
    @Override
    public void run() {
        if (list.size()==0){
            try {
                System.out.println("产品全部被消费，等待生产者生产");
                synchronized (list){
                    list.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("消费者消费一个产品");
            list.remove(0);
            synchronized (list) {
                list.notifyAll();
            }
        }
    }
}
