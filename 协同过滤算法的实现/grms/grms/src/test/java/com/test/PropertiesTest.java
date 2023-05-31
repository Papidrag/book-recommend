package com.test;
import java.util.*;

/**
 * @Author: cxx
 * @Date: 2018/4/10 11:06
 */
public class PropertiesTest {
    //1.properties->继承hashtable
    public static void PropertiesTest(){
        Properties props = new Properties();
        props.put("key1","value1");
        props.setProperty("key2","value2");
        props.list(System.out);
        System.out.println(props.get("key1"));
        System.out.println(props.getProperty("key1"));
    }

    //2.set
    public static void setDemo(){
        int[] array = {1,2,3,4,5,6,7,7};
        List list = new ArrayList();
        for (int a:array){
            list.add(a);
        }
        System.out.println(list.toString());
        Set<Integer> set = new HashSet<>();
        set.addAll(list);
        System.out.println(set);
    }

    //vector
    public static void vectorDemo(){
        Vector<String> vector = new Vector<>(20);
        vector.add("math");
        vector.add("chinse");
        vector.add("english");
        System.out.println(vector.capacity()+":"+vector.size());
    }

    //栈  继承vector
    public static void stackDemo(){
        Stack<Integer> stack = new Stack<>();
        for (int i=0;i<10;i++){
            stack.push(i);
        }
        System.out.println(stack);
        System.out.println(stack.pop());
        stack.peek();
        System.out.println(stack.pop());
        System.out.println(stack.search(1));
    }


    /*=======================*/
    public static void collectionDemo(){
       List<Integer> list = new ArrayList<>();
        list.add(34);
        list.add(55);
        list.add(56);
        list.add(89);
        list.add(12);
        list.add(23);
        list.add(126);
        System.out.println(list);
        Collections.reverse(list);
        System.out.println(list);
        System.out.println(Collections.max(list));
        List<String> list2 = Arrays.asList("1","2");
        System.out.println(Collections.binarySearch(list2,"1"));
        List<String> list3 = Arrays.asList("copy1,copy2,copy3".split(","));
        System.out.println(list3);
    }


    public static void hashMapDemo(){
        Map<Integer, String> hm =new HashMap<Integer, String>();
        hm.put(1, "lichen");
        hm.put(2, "huwang");
        hm.put(3, "lizhou");
        hm.put(4, "sunwei");
        hm.put(5, "likai");
        hm.put(6, "hutao");
        Iterator<Integer> itr1 = hm.keySet().iterator();
        Iterator<String> itr2 =hm.values().iterator();
        Iterator<Map.Entry<Integer, String>> itr3 =hm.entrySet().iterator();

        while (itr1.hasNext()){System.out.println(itr1.next());}
        while (itr2.hasNext()){System.out.println(itr2.next());}
        while (itr3.hasNext()){
            Map.Entry<Integer,String> cxx=itr3.next();
            System.out.println(cxx.getValue()+":"+cxx.getKey() );}
    }



    public static void main(String[] args) {
        PropertiesTest p = new PropertiesTest();
        PropertiesTest.setDemo();
    }
}
