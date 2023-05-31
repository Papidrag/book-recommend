package com.test;

/**
 * @Author: cxx
 * @Date: 2018/4/10 16:06
 */
public class MyThread extends Thread{
    static class MyThread1 extends Thread {
        private String name;

        public MyThread1(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println("name:" + name + " 子线程ID:" + Thread.currentThread().getId());
        }
    }

    public static void main(String[] args) {
        System.out.println("主线程ID:"+Thread.currentThread().getId());
        MyThread1 thread1 = new MyThread1("thread1");
        thread1.start();
        MyThread1 thread2 = new MyThread1("thread2");
        thread2.run();
    }
}
