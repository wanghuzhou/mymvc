package com.wanghz.mymvc.common.enums;

public enum ResponseCode {

	/**
	 * 操作成功
	 */
	OK(0, "操作成功"),

	PARAM_NULL_ERROR(100, "参数不能为空"),

	SOURCE_ERROR(101, "无效的source"),

	SEND_SMS_ERROR(102, "短信发送失败"),

	CODE_EXPIRE(103, "验证码已经过期，请重新获取"),

	CODE_ERROR(104, "验证码不正确,重新输入"),

	VALIDATE(105, "数据格式校验错误"),

	WX_ERROR(106, "微信处理出错"),

	AUTHORITY(107, "接口访问权限受限"),

	FIND_USERID_ERROR(108, "获取当前用户ID失败"),

	DATA_NOTFOUND_ERROR(109, "查找数据为空"),

	DATA_NOTRIGHT_ERROR(110, "没有该数据权限"),

	USER_NOTFOUND_EROOR(111, "获取用户失败或查找不到该用户"),

//	DOCTOR_INFO_EROOR(112, "用户不存在或者未认证"),

	CANCEL_ORDER_ERROR(113, "取消订单失败"),

	ORDER_NOEND_ERROR(114, "订单未结束"),

	CALL_AUDIT_PRES_INTERFACE(115, "调用审方接口出错"),

	AUDIT_PRES_FAIL(116, "审方不通过"),

	ESIGIN_ERROR(117, "电子签名出错"),

	PRES_ORDER_ERROR(118, "处方订单出错"),

	GET_STORE_INFO_ERROR(119, "获取药房信息失败"),
	/**
	 * 未知异常
	 */
	UNKNOWN_ERROR(500, "服务器出错啦");
	/**
	 * 状态码
	 */
	private Integer code;
	/**
	 * 内容
	 */
	private String message;

	ResponseCode(Integer code, String message) {
		this.code = code;
		this.message = message;
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

}
