package com.test.ticket;

/**
 * @Author: cxx
 *
 * （一）对同一个数量进行操作
 * （二）对同一个对象进行操作
 * （三）回调方法使用
 * （四）线程同步，死锁问题
 * （五）线程通信
 *  题目：三个售票窗口同时出售20张票;
 *  1.票数要使用同一个静态值
    2.为保证不会出现卖出同一个票数，要java多线程同步锁。
    设计思路：1.创建一个站台类Station，继承Thread，重写run方法，在run方法里
    面执行售票操作！售票要使用同步锁：即有一个站台卖这张票时，其他站台要等这张票卖完！
 * @Date: 2018/4/10 16:19
 */
public class Station extends Thread {
    //1.给线程取名字
    public Station(String name){
        super(name);
    }

    //2.给票设置静态值
    static int ticket = 100;

    //3.创建静态钥匙
    static Object cxx = "xx";
    static long startTime = System.currentTimeMillis();

    //4.重写run方法，实现买票操作
    @Override
    public void run(){

        while (ticket>=0){
            //必须使用一个锁, 进去的人会把钥匙拿在手上，出来后才把钥匙拿让出来
            synchronized (cxx){
                if (ticket>0){
                    System.out.println(getName()+"卖出了第"+ticket+"票");
                    ticket--;
                }else {
                    System.out.println("票卖完啦");
                    long endTime = System.currentTimeMillis();
                    System.out.println("运行时间:"+(endTime-startTime)+"ms");
                    break;
                }
            }
            try {
                //sleep方法调用之后，并没有释放锁。使得线程仍然可以同步控制。sleep不会让出系统资源
              sleep(1);
                //wait是进入线程等待池中等待，让出系统资源
                //IllegalMonitorStateException
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
