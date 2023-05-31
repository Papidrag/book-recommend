package com.test.productconnsumer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cxx
 * @Date: 2018/4/10 17:46
 * 原因：在调用list对象锁的的时候，程序并没有锁住list对象。
 * 让另一个对象执行某种操作以维护其自己的锁，就必须先得到对象的锁。
 */
public class MainTest {
    public static void main(String[] args) {
        //创建产品
        List<Product> list = new ArrayList<>();
        //创建生产者
        Producer pro = new Producer(list);
        //创建消费者
        Consumer con = new Consumer(list);
        for(int i =0;i<=10;i++){
            new Thread(pro).start();
            new Thread(con).start();
        }
    }

}
