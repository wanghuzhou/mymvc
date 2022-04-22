package com.wanghz.mymvc.servlet;

import com.wanghz.mymvc.common.enums.ResponseCode;
import com.wanghz.mymvc.common.util.JsonUtil;
import com.wanghz.mymvc.common.util.ReflectUtil;
import com.wanghz.mymvc.context.ContextScanHelper;
import com.wanghz.mymvc.domain.ResponseBean;
import com.wanghz.mymvc.exception.GlobalExceptionHandler;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@WebServlet(name = "dispatcherServlet", urlPatterns = "/*", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(globalExceptionHandler);

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.replace(contextPath, "");

        Method method = ContextScanHelper.urlMethodMap.get(path);
        if (method != null) {
            String classFullName = ContextScanHelper.methodPackageMap.get(method);
            String controllerName = ContextScanHelper.nameMap.get(classFullName);
            Object controller = ContextScanHelper.instanceMap.get(controllerName);
            method.setAccessible(true);
            try {
                ThreadContext.put("reqCode", UUID.randomUUID().toString());
                Object[] objects = ReflectUtil.getRealParameters(method, req, resp);
//                logger.info("入参：{}", JSON.toJSONString(objects));//当参数为request，无法json序列化
                Object obj = method.invoke(controller, objects);
                String jsonStr = JsonUtil.toJSONString(obj);
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write(jsonStr);
                logger.info("出参：{}", jsonStr);
                ThreadContext.remove("reqCode");
            } /*catch (IllegalAccessException e) {
                logger.error("非法访问, ", e);
            } catch (InvocationTargetException e) {
                logger.error("调用目标异常", e);
                resp.getWriter().write(JSON.toJSONString(ResponseBean.ofError(ResponseCode.UNKNOWN_ERROR)));
            }*/ catch (Exception e) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                resp.getWriter().write(JsonUtil.toJSONString(ResponseBean.ofError(ResponseCode.UNKNOWN_ERROR)));
            }
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

    }


}
