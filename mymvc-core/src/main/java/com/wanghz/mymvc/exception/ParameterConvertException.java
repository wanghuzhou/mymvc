package com.wanghz.mymvc.exception;

/**
 * 请求参数格式化出错
 * @author wanghz
 */
public class ParameterConvertException extends Exception {

    public ParameterConvertException(String message) {
        super(message);
    }
}