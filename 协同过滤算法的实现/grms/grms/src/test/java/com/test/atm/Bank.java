package com.test.atm;

/**
 * @Author: cxx
 * @Date: 2018/4/10 17:04
 * 示例二：两个人AB通过一个账户A在柜台取钱和B在ATM机取钱！
 * 程序分析：钱的数量要设置成一个静态的变量。两个人要取的同一个对象值
 */
public class Bank {
    //假设账户有1000块
    static int count = 1000;

    //柜台Counter取钱100
    public void counter(int money,String name){
        Bank.count-=money;
        System.out.println(name+"->柜台->取走了"+money+"钱，还剩下"+Bank.count);
    }
    //ATM取钱200
    public void atm(int money,String name){
        Bank.count-=money;
        System.out.println(name+"->ATM->取走了"+money+"钱，还剩下"+Bank.count);
    }
}
