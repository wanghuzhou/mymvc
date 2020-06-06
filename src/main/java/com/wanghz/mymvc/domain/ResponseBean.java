package com.wanghz.mymvc.domain;

import com.wanghz.mymvc.common.enums.ResponseCode;

public class ResponseBean {
	/**
	 * 状态码
	 */
	private Integer code;

	/**
	 * 返回内容
	 */
	private String message;

	/**
	 * 返回数据
	 */
	private Object data;

	/**
	 * 无参构造函数
	 */
	private ResponseBean() {

	}

	/**
	 * 全参构造函数
	 *
	 * @param code    状态码
	 * @param message 返回内容
	 * @param data    返回数据
	 */
	private ResponseBean(Integer code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	/**
	 * 构造一个自定义的API返回
	 *
	 * @param code    状态码
	 * @param message 返回内容
	 * @param data    返回数据
	 * @return ResponseBean
	 */
	public static ResponseBean of(Integer code, String message, Object data) {
		return new ResponseBean(code, message, data);
	}

	public static ResponseBean ofSuccess() {
		return ofStatus(ResponseCode.OK);
	}

	/**
	 * 构造一个成功且带数据的API返回
	 *
	 * @param data 返回数据
	 * @return ResponseBean
	 */
	public static ResponseBean ofSuccess(Object data) {
		return ofStatus(ResponseCode.OK, data);
	}

	/**
	 * 
	 * @param code
	 * @param message
	 * @return
	 */
	public static ResponseBean ofError(Integer code, String message) {
		return new ResponseBean(code, message, null);
	}

	/**
	 * 
	 * @param code
	 * @param message
	 * @return
	 */
	public static ResponseBean ofError(ResponseCode responseCode, String message) {
		return new ResponseBean(responseCode.getCode(), message, null);
	}

	/**
	 * 
	 * @param code
	 * @param message
	 * @return
	 */
	public static ResponseBean ofError(ResponseCode responseCode) {
		return new ResponseBean(responseCode.getCode(), responseCode.getMessage(), null);
	}

	/**
	 * 构造一个成功且自定义消息的API返回
	 *
	 * @param message 返回内容
	 * @return ResponseBean
	 */
	public static ResponseBean ofMessage(String message) {
		return of(ResponseCode.OK.getCode(), message, null);
	}

	/**
	 * 构造一个有状态的API返回
	 *
	 * @param status 状态 {@link Codes}
	 * @return ResponseBean
	 */
	public static ResponseBean ofStatus(ResponseCode responseCode) {
		return ofStatus(responseCode, null);
	}

	/**
	 * 构造一个有状态且带数据的API返回
	 *
	 * @param codes 状态 {@link Codes}
	 * @param data  返回数据
	 * @return ResponseBean
	 */
	public static ResponseBean ofStatus(ResponseCode responseCode, Object data) {
		return of(responseCode.getCode(), responseCode.getMessage(), data);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
