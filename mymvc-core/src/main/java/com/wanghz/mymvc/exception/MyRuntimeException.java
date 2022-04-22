package com.wanghz.mymvc.exception;

/**
 * MyUtil自定义运行时错误
 *
 * @author wanghz
 * @since 2020年10月9日
 */
public class MyRuntimeException extends RuntimeException {

    public MyRuntimeException(String message) {
        super(message);
    }

    public MyRuntimeException(Throwable cause) {
        super(cause);
    }

    public MyRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
