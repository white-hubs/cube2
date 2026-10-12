package com.guhecloud.cube.srv.gen.maintain.server.common.enumeration;

/**
 * @author lzy
 * @date 2022/4/28
 */
public enum TicketSourceTypeEnum {

    HIDDENDANGER("隐患排查"),

    FAULT("故障报修");

    private final String desc;

    TicketSourceTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDescription() {
        return this.desc;
    }

}
