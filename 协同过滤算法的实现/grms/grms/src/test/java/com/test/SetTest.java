package com.test;

import java.util.TreeSet;

/**
 * @Author: cxx
 * @Date: 2018/4/10 15:24
 */
public class SetTest {

    public static void main(String[] args) {
        TreeSet set = new TreeSet();
        set.add(9);
        set.add(-2);
        set.add(3);
        set.add(10);
        System.out.println(set);//[-2, 5, 9, 10]
        //输出集合中第一个元素
        System.out.println(set.first());
    }
}
