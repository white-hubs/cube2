package com.guhecloud.cube.biz.adapter.broadcast.server.application.tianjin.enumeration;

import lombok.Getter;

@Getter
public enum BaoLunV7700ResultEnums {
    SUCCESS(200, "成功"),
    NO_AUTH(401, "没有权限"),
    NOT_FOUND(404, "路由不存在"),
    INVALID_PARAM(422, "参数验证错误"),
    SERVER_ERR(500, "请求异常");

    private final int code;

    private final String desc;

    BaoLunV7700ResultEnums(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
