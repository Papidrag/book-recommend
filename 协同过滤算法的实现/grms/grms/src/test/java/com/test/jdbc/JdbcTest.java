package com.test.jdbc;

import java.sql.*;

/**
 * @Author: cxx
 * @Date: 2018/4/10 18:22
 */
public class JdbcTest {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement cmd = null;
        ResultSet rs = null;
        try {
            long start = System.currentTimeMillis();
            conn = JdbcUtil.getConnection();
            conn.setAutoCommit(false);
            /*cmd = conn.prepareStatement("insert into news (title,context) values (?,?)");
            for (int i = 0; i < 1000; i++) {//100万条数据
                cmd.setString(1, "test"+i);
                cmd.setString(2, "values");
                cmd.addBatch();
                if (i%500==0){
                    System.out.println(i);
                    cmd.executeBatch();
                }
            }
*/
            cmd = conn.prepareStatement("DELETE FROM news");
            cmd.executeUpdate();
            conn.commit();
            JdbcUtil.close(conn,cmd,rs);
            long end = System.currentTimeMillis();
            System.out.println("花费的时间："+(end - start));//158918毫秒
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }
}