package com.hao.dao;

import com.hao.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author 刘思豪
 * @create 2022/6/17 12:23
 */
@Repository
public interface UserDao {
    /**
     * 根据用户名查找普通用户
     * @param uname
     * @return
     */
    @Select("SELECT * FROM `user`\n" +
            "where uname=#{uname} and password=#{password}")
    public User findByName(@Param("uname") String uname, @Param("password") String password);

}
