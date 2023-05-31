package com.hao.service;

import com.hao.pojo.User;

/**
 * @Author 刘思豪
 * @create 2022/6/17 12:25
 */
public interface UserService {
    public User findByName(String uname, String password);
}
