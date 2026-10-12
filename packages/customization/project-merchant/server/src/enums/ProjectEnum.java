package com.guhecloud.cube.deliver.project.merchant.enums;


public enum ProjectEnum {
    CODE_HQ("hq", "总部"),
    CODE_SNPH("snph", "平湖海吉星"),
    CODE_SNSH("snsh", "上海农批"),
    CODE_SNCD("sncd", "成都农批"),
    MODEL_NP("model_NP", "农批市场"),
    MODEL_JM("model_JM", "集贸市场");

    private String code;

    private String name;

    ProjectEnum(String code, String name) {
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

    public static String getProjectCodeEnum(String code) {
        for (ProjectEnum projectEnum : ProjectEnum.values()) {
            if (projectEnum.getCode().equals(code)) {
                return projectEnum.getName();
            }
        }
        return "";
    }
}
