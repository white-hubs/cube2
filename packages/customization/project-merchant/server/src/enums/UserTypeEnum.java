package com.guhecloud.cube.deliver.project.merchant.enums;


public enum UserTypeEnum {
    USER_TYPE_STAFF("userType_staff", "员工"),
    USER_TYPE_COMPANY("userType_store", "商户"),
    USER_TYPE_PURCHASER("userType_purchaser", "采购商");

    private String code;

    private String name;

    UserTypeEnum(String code, String name) {
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

    public static String getUserTypeEnum(String code) {
        for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
            if (userTypeEnum.getCode().equals(code)) {
                return userTypeEnum.getName();
            }
        }
        return "";
    }
}
