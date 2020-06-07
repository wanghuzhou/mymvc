package com.wanghz.mymvc.webtest.controller;

import com.wanghz.mymvc.annotation.Controller;
import com.wanghz.mymvc.annotation.RequestMapping;
import com.wanghz.mymvc.domain.ResponseBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("testController")
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/test")
    public ResponseBean test(HttpServletRequest request, HttpServletResponse response) {
        return ResponseBean.ofSuccess("我的springmvc框架");
    }

    @RequestMapping("/hello")
    public ResponseBean hello(String name, int age) {
        return ResponseBean.ofSuccess("hello:" + name + ", age:" + age);
    }

    @RequestMapping("/hello/user")
    public ResponseBean helloUser(User user) {
        return ResponseBean.ofSuccess("hello:" + user);
    }
}
