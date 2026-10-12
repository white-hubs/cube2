package com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.enums;

public enum CallTypeEnum {
    DORMITORY_BUILDING("dormitoryBuilding", "监舍楼"),
    FACTORY("factory", "工厂");

    private String code;
    private String desc;

    CallTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(String code) {
        for (CallTypeEnum callTypeEnum : CallTypeEnum.values()) {
            if (callTypeEnum.getCode().equals(code)) {
                return callTypeEnum.getDesc();
            }
        }
        return null;
    }
}
