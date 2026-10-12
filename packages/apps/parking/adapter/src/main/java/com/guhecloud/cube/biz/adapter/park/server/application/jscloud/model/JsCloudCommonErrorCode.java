package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model;

import lombok.Getter;

/**
 * 捷顺云开放平台 - 公共错误码枚举
 */
@Getter
public enum JsCloudCommonErrorCode {

    SUCCESS(10000, "success", "成功"),

    UNKNOWN_ERROR(20000, "jop.unknown-error", "业务服务发生未知异常"),
    REMOTE_SERVICE_UNABLE(20000, "jop.remote-service-unable-access-error", "业务服务不可用"),
    SERVICE_NOT_AVAILABLE(20000, "jop.service-not-available", "路由不可用"),
    GATEWAY_RESPONSE_TIMEOUT(20000, "jop.gateway-response-timeout", "网关响应超时"),
    ACCESS_LIMIT(20000, "isv.access-limit", "请求被限流"),

    MISSING_METHOD(40001, "isv.missing-method", "缺少方法名参数"),
    MISSING_SIGNATURE(40001, "isv.missing-signature", "缺少签名参数"),
    MISSING_SIGNATURE_TYPE(40001, "isv.missing-signature-type", "缺少签名类型参数"),
    MISSING_APP_ID(40001, "isv.missing-app-id", "缺少 appId 参数"),
    MISSING_TIMESTAMP(40001, "isv.missing-timestamp", "缺少时间戳参数"),
    MISSING_PROJECT(40001, "isv.missing-project", "缺少项目编码参数"),
    MISSING_ABILITY(40001, "isv.missing-ability", "缺少能力编码参数"),

    INVALID_METHOD(40002, "isv.invalid-method", "不存在的接口编码"),
    BAD_REQUEST(40002, "isv.bad_request", "非法的请求方式"),
    INVALID_METHOD_REMOTE(40002, "isv.invalid-method-remote", "不存在的业务接口"),
    INVALID_FORMAT(40002, "isv.invalid-format", "无效的数据格式"),
    INVALID_SIGNATURE_TYPE(40002, "isv.invalid-signature-type", "无效的签名类型"),
    INVALID_SIGNATURE(40002, "isv.invalid-signature", "无效的签名"),
    INVALID_APP_ID(40002, "isv.invalid-app-id", "无效的 appId 参数"),
    INVALID_ABILITY(40002, "isv.invalid-ability", "该能力不可用"),
    INVALID_TIMESTAMP(40002, "isv.invalid-timestamp", "无效的时间戳参数"),
    INVALID_CHARSET(40002, "isv.invalid-charset", "字符集错误"),
    MISSING_SIGNATURE_CONFIG(40002, "isv.missing-signature-config", "验签配置错误"),

    REMOTE_SERVICE_ERROR_UNKNOWN(40004, "remote.service-error-unknown", "业务服务返回异常"),

    ROUTE_NO_PERMISSIONS(40006, "isv.route-no-permissions", "没有接口访问权限"),
    APP_ABILITY_NO_PERMISSIONS(40006, "isv.app-ability-no-permissions", "应用未开通此开放能力"),
    PROJECT_ABILITY_NO_PERMISSIONS(40006, "isv.project-ability-no-permissions", "项目没有此开放能力访问权限"),
    ACCESS_FORBIDDEN(40006, "isv.access-forbidden", "该应用禁止访问"),
    IP_FORBIDDEN(40006, "isv.ip-forbidden", "IP 禁止访问");

    private final int code;
    private final String error;
    private final String messageZh;

    JsCloudCommonErrorCode(int code, String error, String messageZh) {
        this.code = code;
        this.error = error;
        this.messageZh = messageZh;
    }

    public static JsCloudCommonErrorCode from(String code, String error) {
        if (code == null || error == null) {
            return null;
        }
        for (JsCloudCommonErrorCode value : values()) {
            if (String.valueOf(value.code).equals(code) && value.error.equals(error)) {
                return value;
            }
        }
        return null;
    }
}

