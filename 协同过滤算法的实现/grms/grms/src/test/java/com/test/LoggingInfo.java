package com.test;

import java.io.*;
import java.util.Date;

/**
 * @Author: cxx
 * @Date: 2018/4/10 14:55
 */
public class LoggingInfo implements Serializable{
    private Date loggingDate = new Date();
    private String uid;
    private transient String pwd;

    LoggingInfo(String user, String password)
    {
        uid = user;
        pwd = password;
    }
    public String toString()
    {
        String password=null;
        if(pwd == null)
        {
            password = "NOT SET";
        }
        else
        {
            password = pwd;
        }
        return "logon info: /n   " + "user: " + uid +
                "/n   logging date : " + loggingDate.toString() +
                "/n   password: " + password;
    }

    public static void main(String args[]){
        LoggingInfo logInfo = new LoggingInfo("admin", "123456");
        System.out.println(logInfo.toString());
        try
        {
            ObjectOutputStream o = new ObjectOutputStream(
                    new FileOutputStream("logInfo.out"));
            o.writeObject(logInfo);
            o.close();
        }
        catch(Exception e) {//deal with exception}
            System.out.println("hello world !");
        }

        try
        {
            ObjectInputStream in =new ObjectInputStream(
                    new FileInputStream("logInfo.out"));
            LoggingInfo logInfo1 = (LoggingInfo)in.readObject();
            System.out.println(logInfo1.toString());
        }
        catch(Exception e) {
//deal with exception
        }

    }
}
