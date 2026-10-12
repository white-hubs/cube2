package com.guhecloud.platform.base.standard.exception;

public enum ErrorTypeEnum implements ErrorSpec {
    USER_NOT_FOUND("USER_NOT_FOUND", "用户不存在", "ACCOUNT"),
    USER_NOT_ACTIVE("USER_NOT_ACTIVE", "用户未启用", "ACCOUNT"),
    USER_LOCKED("USER_LOCKED", "用户被锁", "ACCOUNT"),
    USER_PASSWORD_ERROR("USER_PASSWORD_ERROR", "用户密码错误", "PASSWORD"),
    USER_PASSWORD_ERROR_TOO_MANY_TIMES("USER_PASSWORD_ERROR_TOO_MANY_TIMES", "用户密码错误次数过多", "PASSWORD"),
    USER_NOT_BOUND("USER_NOT_BOUND", "用户未绑定", "ACCOUNT"),
    USER_IS_BLANK("USER_IS_BLANK", "用户不能为空", "ACCOUNT"),
    USER_NOT_BOUND_TO_TEL("USER_NOT_BOUND_TO_TEL", "该手机未绑定用户", "ACCOUNT"),
    CAPTCHA_EXPIRE("CAPTCHA_EXPIRE", "验证码已失效", "CAPTCHA"),
    CAPTCHA_ERROR("CAPTCHA_ERROR", "验证码已错误", "CAPTCHA"),
    SMS_CODE_EXPIRE("SMS_CODE_EXPIRE", "手机验证码已失效", "SMS"),
    SMS_CODE_VERIFY_ERROR_TOO_MANY("SMS_CODE_VERIFY_ERROR_TOO_MANY", "手机验证次数过多，请稍候重试", "SMS"),
    SMS_CODE_SEND_TOO_MANY("SMS_CODE_SEND_TOO_MANY", "手机发送次数过多，请稍候重试", "SMS"),
    SMS_CODE_ERROR("SMS_CODE_ERROR", "手机验证码错误", "SMS"),
    USER_PASSWORD_EMPTY("PASSWORD_EMPTY", "密码不能为空", "PASSWORD"),
    USER_HAS_BIND_TEL("USER_HAS_BIND_TEL", "用户已经绑定了手机号", "ACCOUNT"),
    WECHAT_BIND_USER_NOT_FOUND("WECHAT_BIND_USER_NOT_FOUND", "当前微信未绑定其他用户", "ACCOUNT"),
    SMS_AUTH_EXPIRE("SMS_AUTH_EXPIRE", "手机号授权已过期,请重新登录", "SMS"),
    WECHAT_AUTH_EXPIRE("WECHAT_AUTH_EXPIRE", "微信授权已过期,请重新登录", "WECHAT"),
    USER_HAS_BIND_WECHAT("USER_HAS_BIND_WECHAT", "用户已绑定微信", "ACCOUNT"),
    SMS_CODE_SEND_ERROR("SMS_CODE_SEND_ERROR", "手机验证码发送失败", "SMS"),
    OTHER_USER_HAS_BIND_TEL("OTHER_USER_HAS_BIND_TEL", "其他用户已绑定该手机号", "ACCOUNT"),
    LOGIN_INFO_ERROR("LOGIN_INFO_ERROR", "登录信息有误", "ACCOUNT");

    private final String code;
    private String msg;
    private final String errType;

    ErrorTypeEnum(String code, String msg, String errType) {
        this.code = code;
        this.msg = msg;
        this.errType = errType;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getErrType() {
        return errType;
    }

    @Override
    public String errorCode() {
        return code;
    }

    @Override
    public String errorMessage() {
        return msg;
    }

    @Override
    public void setErrorMessage(String message) {
        this.msg = message;
    }
}
