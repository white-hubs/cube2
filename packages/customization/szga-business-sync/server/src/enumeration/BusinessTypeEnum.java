package com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-18  11:47
 * @Description:
 * @Version: 1.0
 */
public enum BusinessTypeEnum {
    HIDDENDANGER("隐患排查"),
    FAULT("故障报修"),

    ;
    private String remarks;

    BusinessTypeEnum(String remarks) {
        this.remarks = remarks;
    }

    private static Map<String, BusinessTypeEnum> businessTypeMap = new HashMap<>();

    static {
        businessTypeMap.put(BusinessTypeEnum.FAULT.name(), BusinessTypeEnum.FAULT);
        businessTypeMap.put(BusinessTypeEnum.HIDDENDANGER.name(), BusinessTypeEnum.HIDDENDANGER);
    }

    public static BusinessTypeEnum getbusinessTypeMap(String name) {
        return businessTypeMap.get(name);
    }
}
