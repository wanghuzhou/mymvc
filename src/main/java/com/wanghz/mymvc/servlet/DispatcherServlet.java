package com.wanghz.mymvc.servlet;

import com.alibaba.fastjson.JSON;
import com.wanghz.mymvc.context.ContextScanHelper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@WebServlet(name = "dispatcherServlet", urlPatterns = "/*", loadOnStartup = 1,
        initParams = {@WebInitParam(name = "base-package", value = "com.wanghz.mymvc")})
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.replace(contextPath, "");

        Method method = ContextScanHelper.urlMethodMap.get(path);
        if (method != null) {
            String packageName = ContextScanHelper.methodPackageMap.get(method);
            String controllerName = ContextScanHelper.nameMap.get(packageName);
            Object controller = ContextScanHelper.instanceMap.get(controllerName);
            method.setAccessible(true);
            try {
                Object obj = method.invoke(controller, req, resp);
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write(JSON.toJSONString(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

    }


}
