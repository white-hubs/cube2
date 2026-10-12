package com.guhecloud.cube.base.flow.form.enumeration;

import lombok.Getter;

/**
 * @author bq
 * @date 2022/09/26
 * @description 表单定义json中的关键字
 */
@Getter
public enum FormJsonEnum {
    // json中的key
    JSON_KEY_SCHEMA("schema"),
    JSON_KEY_PROPERTIES("properties"),
    JSON_KEY_TYPE("type"),
    JSON_KEY_ITEMS("items"),
    JSON_KEY_DYNAMIC_DATA("dynamic_data"),
    JSON_KEY_URL("url"),
    JSON_KEY_METHOD("method"),
    JSON_KEY_PARAMS("params"),
    JSON_KEY_INSIDE("inside"),

    // 认证方式
    AUTH_TYPE("auth_type"),
    AUTH_TYPE_BASIC("Basic Auth"),
    AUTH_TYPE_BEARER("Bearer Token"),
    AUTH_TYPE_NO("No Auth"),

    // 认证参数
    AUTH_PARAMS("auth_params"),
    AUTH_PARAMS_USERNAME("Username"),
    AUTH_PARAMS_PASSWORD("Password"),
    AUTH_PARAMS_TOKEN("Token"),

    // header头信息
    HEADER_AUTHORIZATION("Authorization"),
    HEADER_BASIC("Basic "),
    HEADER_BEARER("Bearer "),
    HEADER_TENANT("tenant"),
    HEADER_TERMINAL("terminal"),

    // 分隔符
    SEPARTOR(":"),

    BUSINESS_ATTR("business_attr");

    String value;

    FormJsonEnum(String value) {
        this.value = value;
    }

}
