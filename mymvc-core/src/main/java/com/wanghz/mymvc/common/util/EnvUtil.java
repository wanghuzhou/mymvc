package com.wanghz.mymvc.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;


/**
 * 环境变量支持类
 *
 * @author wanghz
 */
public class EnvUtil {


    /**
     * 根据环境变量名称获取环境变量值
     *
     * @param envName 环境变量名称
     * @return 环境变量值
     */
    public static String getEnvByName(String envName) {
        if (StringUtils.isAnyEmpty(envName)) {
            return StringUtils.EMPTY;
        } else {
            return System.getenv(envName);
        }
    }

    /**
     * 获取所有环境变量键值对数据
     *
     * @return 环境变量键值对数据
     */
    public static Map<String, String> getEnvs() {
        return System.getenv();
    }


}
