package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.enumeration;

import lombok.Getter;

@Getter
public enum BaoLunRetEnums {
    SUCCESS(0, "成功"),
    INVALID_INTERFACE(8000, "接口未实现或者无效的接口名"),
    JSESSIONID_INTERFACE(8001, "JSESSIONID 无效"),
    INVALID_PARAM(1000, "参数错误，缺少必须的参数"),
    INVALID_USERNAME_PASSWORD(1001, "用户名、密码不正确"),
    TERMINAL_NOT_ONLINE(1002, "发起呼叫终端不在线"),
    INVALID_TERMINAL(1003, "无效的终端"),
    TERMINAL_BUSY(1004, "发起呼叫终端正忙"),
    INVALID_TERMINAL_CALL_CODE(1005, "无效的终端通话编码"),
    INVALID_SERIAL_PORT(1006, "串口无效"),
    SERIAL_PORT_TIMEOUT(1007, "串口操作超时"),
    SERIAL_PORT_DATE_ERR(1008, "串口数据错误");

    private final int code;

    private final String desc;

    BaoLunRetEnums(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
