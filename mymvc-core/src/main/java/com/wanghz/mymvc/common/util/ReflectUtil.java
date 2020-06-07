package com.wanghz.mymvc.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanghz.mymvc.exception.ParameterConvertException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射类工具
 *
 * @author wanghz
 */
public class ReflectUtil {

    public static Class[] normalTypeList = new Class[]{String.class, int.class, Integer.class, float.class, Float.class,
            double.class, Double.class, char.class, short.class, Short.class};

    public static Object[] getRealParameters(Method method, HttpServletRequest request, HttpServletResponse response) {

        Parameter[] parameters = method.getParameters();
        Class<?>[] clazzArr = method.getParameterTypes();
        Object[] tmpParameters = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];

            String paramName = param.getName();

            /*for (Class<?> clazz : clazzArr) {
                try {
                    tmpParameters[i] = wrapRealParameter(clazz, paramName, request, response);
                } catch (ParameterConvertException e) {
                    e.printStackTrace();
                }
            }*/
            try {
                tmpParameters[i] = wrapRealParameter(clazzArr[i], paramName, request, response);
            } catch (ParameterConvertException e) {
                e.printStackTrace();
            }
        }

        return tmpParameters;
    }

    public static Object wrapRealParameter(Class<?> clazz, String paramName, HttpServletRequest request, HttpServletResponse response) throws ParameterConvertException {

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
        String value = request.getParameter(paramName);
        Object object = null;
        try {
            if (isNormalType) {

                if (clazz.equals(String.class)) {
                    object = value;
                }
                else if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
                    object = Integer.parseInt(value);
                }
                else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
                    object = Float.parseFloat(value);
                }
                else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
                    object = Double.parseDouble(value);
                }
                else if (clazz.equals(short.class) || clazz.equals(Short.class)) {
                    object = Short.parseShort(value);
                }
                else if (clazz.equals(byte.class) || clazz.equals(Byte.class)) {
                    object = Byte.parseByte(value);
                }
            } else {
                object = JSON.parseObject(request2Json(request).toJSONString(), clazz);
            }
        } catch (Exception e) {
//            systemErrorTracelogger.error("[ {} ] 参数:[ {} ] 实参内容:[ {} ] 格式化出错", controllerInfo.getReqUrl(),parameterInfo.getParamaterName(), value, e);
            throw new ParameterConvertException("参数：[" + paramName + "] , 数据类型错误");
        }
        return object;

    }

    public static Map<String, Object> req2Map(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> map.put(key, value[0]));
        return map;
    }

    /**
     * 将request参数值转为json
     */
    public static JSONObject request2Json(HttpServletRequest request) {
        JSONObject requestJson = new JSONObject();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
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
            requestJson.put(paramName, sb.toString());
        }
        return requestJson;
    }


}
