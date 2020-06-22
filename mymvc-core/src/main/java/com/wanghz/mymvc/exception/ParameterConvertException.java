package com.wanghz.mymvc.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求参数格式化出错
 *
 * @author wanghz
 */
public class ParameterConvertException extends Exception {
    private static Logger logger = LoggerFactory.getLogger(ParameterConvertException.class);

    public ParameterConvertException(String message) {
        super(message);
        logger.error("参数转换异常: {}", message);
    }
}