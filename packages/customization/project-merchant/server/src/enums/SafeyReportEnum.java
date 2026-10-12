package com.guhecloud.cube.deliver.project.merchant.enums;


public enum SafeyReportEnum {

/*    public static final String TYPE_SCENE = "scene";
    public static final String TYPE_DEVICE = "device";
    public static final String TYPE_STORE = "store";*/

    STATUS_NOT_FILLED_IN("safetyReport_notFilledIn", "未填写"),
    STATUS_FILLED_IN("safetyReport_filledIn", "已填写"),
    ;

    private String code;
    private String name;

    SafeyReportEnum(String code, String name) {
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

    public static String getFireinspEnum(String code) {
        for (SafeyReportEnum fireinspEnum : SafeyReportEnum.values()) {
            if (fireinspEnum.getCode().equals(code)) return fireinspEnum.getName();
        }
        return "";
    }
}
