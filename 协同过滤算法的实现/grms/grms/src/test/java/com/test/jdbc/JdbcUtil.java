package com.test.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @Author: cxx
 * @Date: 2018/4/10 19:30
 */
public class JdbcUtil {
    private static String url;
    private static String user;
    private static String password;

    static{
        Properties prop = new Properties();
        InputStream is= JdbcUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
        try {
            prop.load(is);
            Class.forName(prop.getProperty("jdbc.dirver"));
            url = prop.getProperty("jdbc.url");
            user = prop.getProperty("jdbc.username");
            password = prop.getProperty("jdbc.password");
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //释放资源
    public static void close(Connection conn, Statement stat, ResultSet rs){
        if(conn != null){
            try {conn.close();} catch (SQLException e) {e.printStackTrace();}
        }
        if(stat != null){
            try {stat.close();} catch (SQLException e) {e.printStackTrace();}
        }
        if(rs != null){
            try {rs.close();} catch (SQLException e) {e.printStackTrace();}
        }
    }

    public static void main(String[] args) {
        System.out.println(JdbcUtil.getConnection());
    }
}
