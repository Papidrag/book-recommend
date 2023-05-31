package com.test.atm;

/**
 * @Author: cxx
 * @Date: 2018/4/10 17:11
 */
public class Person extends Thread {
    //创建银行对象
    private Bank bank;
    private String name;
    // 通过构造器传入银行对象，确保两个人进入的是一个银行
    public Person(Bank bank,String name) {
        this.bank = bank;
        this.name = name;
    }

    @Override
    public void run(){
        while (Bank.count>=100){
            if (name.equals("A")){
                bank.counter(100,name);
            }else {
                bank.atm(200,name);
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
