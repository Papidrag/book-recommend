package com.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: cxx
 * @Date: 2018/4/10 14:16
 */
public class FailFastTest {
    private static List<Integer> list = new ArrayList<>();

    /**
     * 线程1迭代list
     */
    private static  class ThreadOne extends MyThread {
        public void run(){
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()){
                int i = it.next();
                System.out.println("ThreadOne遍历："+i);
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("异常："+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * i=3是修改list
     * @param args
     */
    private static class ThreadTwo extends MyThread {
        public void run(){
            int i =0;
            while (i<6){
                System.out.println("ThreadTwo遍历："+i);
                if (i==3){
                    list.remove(i);
                }
                i++;
            }
        }
    }

    public static void main(String[] args) {
        for(int i = 0 ; i < 10;i++){
            list.add(i);
        }
        new ThreadOne().start();
        new ThreadTwo().start();
    }
}
