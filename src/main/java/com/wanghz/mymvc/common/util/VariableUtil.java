package com.wanghz.mymvc.common.util;

import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 变量支持工具类
 *
 * @author wanghz
 */
public class VariableUtil {

//    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.SYSTEM_ERROR_TRACE);

    /**
     * 系统变量配置对象
     */
    static Properties properties;

    /**
     * 系统变量正则匹配器 ${xxx} 模式
     */
    static final Pattern pattern = Pattern.compile("(?<=\\$\\{)(\\S+?)(?=\\})");

    public static void init(String configPath) throws IOException {
        if (StringUtils.isBlank(configPath)) {
            configPath = "classpath:application.properties";
        }
        properties = PropertiesUtil.loadFromSystemPath(configPath);
    }


    public static String translateToString(String str) {
        if (StringUtils.isAnyEmpty(str)) {
            return null;
        }
        String tmpContext = new String(str);
        Matcher matcher = pattern.matcher(str);
        List<String> tmpVariableList = new ArrayList<String>();
        while (matcher.find()) {
            tmpVariableList.add(matcher.group());
        }
        for (String tmpVar : tmpVariableList) {
            String tmpSourceStr = "${" + tmpVar + "}";
            String tmpTargetStr = properties.getProperty(tmpVar);
            if (tmpTargetStr == null) {
                continue;
            }
            tmpContext = tmpContext.replace(tmpSourceStr, tmpTargetStr);
        }
        return tmpContext;
    }

    public static byte[] translateToBytes(String str) {
        if (StringUtils.isAnyEmpty(str)) {
            return null;
        }
        String tmpContext = translateToString(str);

        return tmpContext.getBytes();
    }

    public static String getVariableValue(String key) {
        if (!properties.containsKey(key)) {
//            logger.warn("[{}]从默认配置文件中加载配置项出错", key);
            System.err.println("从默认配置文件中加载配置项出错" + key);
        }
        return properties.getProperty(key);
    }

}
