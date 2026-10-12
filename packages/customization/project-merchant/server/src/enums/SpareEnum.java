package com.guhecloud.cube.deliver.project.merchant.enums;


public enum SpareEnum {

    STATUS_NORMAL("spareStatus_normal", "正常"),
    STATUS_TOO_LOW("spareStatus_tooLow", "过低"),
    STATUS_TOO_HIGH("spareStatus_tooHigh", "过高"),
    ;

    private String code;
    private String name;

    SpareEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getSpareEnum(String code) {
        for (SpareEnum fireinspEnum : SpareEnum.values()) {
            if (fireinspEnum.getCode().equals(code)) return fireinspEnum.getName();
        }
        return "";
    }
}
