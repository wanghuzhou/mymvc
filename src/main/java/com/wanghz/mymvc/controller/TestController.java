package com.wanghz.mymvc.controller;

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
}
