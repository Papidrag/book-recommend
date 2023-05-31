package com.hao.controller;

import com.hao.pojo.User;
import com.hao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 刘思豪
 * @create 2022/6/17 11:22
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/login")
    public ModelAndView findByName(String userName, String password) {

        ModelAndView mv;
        mv = new ModelAndView();
        User user = userService.findByName(userName, password);
        if (user == null) {
            mv.addObject("message","用户名或密码错误！");
            mv.setViewName("../../index");
        } else {
            mv.addObject("user", user);
            mv.setViewName("head");
        }
        return mv;
    }
}
