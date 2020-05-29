package com.wanghz.mymvc.controller;

import com.wanghz.mymvc.annotation.Controller;
import com.wanghz.mymvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller("testController")
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/test")
    public void test(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("test 呵呵呵呵");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("test1");
    }
}
