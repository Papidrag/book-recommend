package com.test.productconnsumer;

import java.util.List;

/**
 * @Author: cxx
 * @Date: 2018/4/10 17:29
 */
public class Producer implements Runnable {
    private List<Product> list;
    public Producer(List<Product> list){
        this.list= list;
    }
    @Override
    public void run() {
        if (list.size()>=10){
            System.out.println("库存已满，等待消费者");
            try {
                synchronized (list) {
                    list.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            list.add(new Product());
            System.out.println("生产者生产一个产品");
            synchronized (list) {
                list.notifyAll();
            }
        }
    }
}
