package com.wanghz.mymvc.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.wanghz.mymvc.exception.ParameterConvertException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射类工具
 *
 * @author wanghz
 */
public class ReflectUtil {

    static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

    public static Class[] normalTypeList = new Class[]{String.class, int.class, Integer.class, float.class, Float.class,
            double.class, Double.class, char.class, short.class, Short.class};

    public static Object[] getRealParameters(Method method, HttpServletRequest request, HttpServletResponse response) {

        Parameter[] parameters = method.getParameters();
        Class<?>[] clazzArr = method.getParameterTypes();
        Object[] tmpParameters = new Object[parameters.length];
        String bodyStr = getReqBodyString(request);
        String formStr = reqFormString(request);
        JsonNode jsonObject = StringUtils.isNotBlank(bodyStr) && bodyStr.trim().startsWith("{")
                ? JsonUtil.readTree(bodyStr) : null;
        logger.info("body入参：{}\n表单入参：{}", bodyStr, formStr);

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            String paramName = param.getName();

            try {
                tmpParameters[i] = wrapRealParameter(clazzArr[i], paramName, jsonObject, request, response);
            } catch (ParameterConvertException e) {
                e.printStackTrace();
            }
        }

        return tmpParameters;
    }

    public static Object wrapRealParameter(Class<?> clazz, String paramName, JsonNode jsonObject, HttpServletRequest request, HttpServletResponse response) throws ParameterConvertException {

        if (clazz.equals(HttpServletRequest.class)) {
            return request;
        } else if (clazz.equals(HttpServletResponse.class)) {
            return response;
        }

        boolean isNormalType = false;
        for (Class tmp : normalTypeList) {
            if (tmp.equals(clazz)) {
                isNormalType = true;
                break;
            }
        }
        String value;
        String type = request.getHeader("Content-Type") != null
                && request.getHeader("Content-Type").startsWith("application/json") ? "json" : "form";
        if (type.equals("json")) {
            value = jsonObject.get(paramName).asText();
        } else {
            value = request.getParameter(paramName);
        }
        Object object = null;
        try {
            if (isNormalType) {

                if (clazz.equals(String.class)) {
                    object = value;
                } else if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
                    object = Integer.parseInt(value);
                } else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
                    object = Float.parseFloat(value);
                } else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
                    object = Double.parseDouble(value);
                } else if (clazz.equals(short.class) || clazz.equals(Short.class)) {
                    object = Short.parseShort(value);
                } else if (clazz.equals(byte.class) || clazz.equals(Byte.class)) {
                    object = Byte.parseByte(value);
                }
            } else {
                if (type.equals("json")) {
                    object = JsonUtil.parseObject(value, clazz);
                } else {
                    object = JsonUtil.convertValue(req2Map(request), clazz);
                }
            }
        } catch (Exception e) {
//            systemErrorTracelogger.error("[ {} ] 参数:[ {} ] 实参内容:[ {} ] 格式化出错", controllerInfo.getReqUrl(),parameterInfo.getParamaterName(), value, e);
            throw new ParameterConvertException("参数：[" + paramName + "] , 数据类型错误");
        }
        return object;

    }

    public static String getReqBodyString(HttpServletRequest request) {

        if (request.getHeader("Content-type").contains("form")) {
            return reqFormString(request);
        }

        int length = request.getContentLength();
        if (length <= 0) {
            return "";
        }

        BufferedReader br = null;
        try {
//            br = request.getReader();
            br = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String str;
            while (null != (str = br.readLine())) {
                sb.append(str);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Map<String, Object> req2Map(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> map.put(key, value[0]));
        return map;
    }

    public static String reqFormString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        request.getParameterMap().forEach((key, value) -> sb.append(key).append("=").append(Arrays.toString(value)));
        return sb.toString();
    }

    /**
     * 将request参数值转为json
     */
    public static Map<String, String> request2Json(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] pv = request.getParameterValues(paramName);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pv.length; i++) {
                if (pv[i].length() > 0) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append(pv[i]);
                }
            }
            map.put(paramName, sb.toString());
        }
        return map;
    }


    public static boolean existsClass(final String className) {
        try {
            Class.forName(className);
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }


}
