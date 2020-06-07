package com.wanghz.mymvc.common.util;


import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;


/**
 * Properties文件访问工具类
 *
 * @author wanghz
 */
public class PropertiesUtil {


    public static Properties loadFromSystemPath(String file) throws IOException {
        Properties properties = new Properties();
        if (StringUtils.isEmpty(file)) {
            return properties;
        }
        if (file.startsWith("classpath:")) {
            file = file.replace("classpath:", "");
        }
        URL url = Thread.currentThread().getContextClassLoader().getResource(file);
        try {
            InputStream in = new FileInputStream(url.getFile());
            if (in != null) {
                properties.load(in);
//                logger.error("配置文件[{}]数据加载完成", file);
                System.out.println("配置文件" + file + "数据加载完成");
                Enumeration<String> propNames = (Enumeration<String>) properties.propertyNames();
                while (propNames.hasMoreElements()) {
                    String tmpPropName = propNames.nextElement();
//                    logger.error("配置文件:[{}] ==> [{}:{}]", file, tmpPropName, properties.getProperty(tmpPropName));
                }
                try {
                    in.close();
                } catch (Exception ex) {
//                    logger.error("[{}]关闭环境变量配置文件IO错误", file, ex);
                }
            }

        } catch (IOException ex) {
//            logger.error("[{}]从系统路径加载文件IO错误", file, ex);
            throw ex;
        }

        return properties;
    }
}