package com.hao.service;

import com.hao.dao.UserDao;
import com.hao.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 刘思豪
 * @create 2022/6/17 12:25
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    public User findByName(String uname, String password) {
        return userDao.findByName(uname,password);
    }
}
