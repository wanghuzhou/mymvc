package com.wanghz.mymvc.webtest.controller;

import com.wanghz.mymvc.annotation.Controller;
import com.wanghz.mymvc.annotation.Qualifier;
import com.wanghz.mymvc.annotation.RequestMapping;
import com.wanghz.mymvc.common.util.ReflectUtil;
import com.wanghz.mymvc.domain.ResponseBean;
import com.wanghz.mymvc.webtest.dao.UserMapper;
import com.wanghz.mymvc.webtest.entity.User;
import com.wanghz.mymvc.webtest.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {

    @Qualifier("userMapper")
    private UserMapper userMapper;

    @Qualifier("userService")
    private UserService userService;

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

    @RequestMapping("/jdbctest")
    public ResponseBean jdbctest(User user) {
//        user = userMapper.getUserByID(user.getId());
        user = userService.getUserByID(user.getId());
        return ResponseBean.ofSuccess(user);
    }

    @RequestMapping("/test1")
    public ResponseBean test1(User user, String city) {
        user.setCity(city);
        return ResponseBean.ofSuccess(user);
    }

    @RequestMapping("/getReqInfo")
    public Map<String, Object> test2(HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        Map<String, String> header = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headName = headerNames.nextElement();
            header.put(headName, request.getHeader(headName));
        }
        map.put("header", header);

        Map<String, String> form = new HashMap<>();
        Enumeration<String> formNames = request.getParameterNames();
        while (formNames.hasMoreElements()) {
            String headName = formNames.nextElement();
            form.put(headName, request.getParameter(headName));
        }
        map.put("formBody", form);
        map.put("json", ReflectUtil.getReqBodyString(request));

        return map;
    }
}
