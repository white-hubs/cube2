package com.guhecloud.cube.deliver.project.merchant.enums;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum DevicePropertiesEnum {

    JLDL("jldl", Arrays.asList("jldl_a","jldl_b","jldl_c","jldl_zl")),
    JLDY("jldy", Arrays.asList("jldy_a","jldy_b","jldy_c","jldy_zl")),
    DBWD("dbwd", Arrays.asList("dbwd_a","dbwd_b","dbwd_c","dbwd_zl","dbwd_n")),
    DBGL("dbgl", Arrays.asList("dbgl_a","dbgl_b","dbgl_c","dbgl_zl")),
    LDL("ldl", Arrays.asList("ldl_zl"));

    private String code;
    private List<String> name;

    DevicePropertiesEnum(String code, List<String> name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public static List<String> getSpareEnum(String code) {
        for (DevicePropertiesEnum fireinspEnum : DevicePropertiesEnum.values()) {
            if (fireinspEnum.getCode().equals(code)) return fireinspEnum.getName();
        }
        return new ArrayList<>();
    }
}
