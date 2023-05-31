package com.test;

/**
 * @Author: cxx
 * @Date: 2018/4/10 10:50
 */
public class Test {

    public void test(){
        String a = "test";
        String b = "test";
        String c = new String("test");
        String d = new String("test");
        System.out.println(a==b);
        System.out.println(a==c);
        System.out.println(c==d);
        System.out.println(a.equals(d));
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.test();
    }

}
