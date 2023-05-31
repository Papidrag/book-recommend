package com.test.atm;

/**
 * @Author: cxx
 * @Date: 2018/4/10 17:16
 */
public class MainTest {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Person personA = new Person(bank,"A");
        Person personB = new Person(bank,"B");
        personA.start();
        personB.start();
    }
}
