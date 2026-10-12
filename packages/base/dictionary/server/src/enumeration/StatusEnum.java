package com.guhecloud.cube.base.dictionary.server.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum StatusEnum {
    ENABLE("启用"),
    DISABLE("禁用"),
    DELETE("逻辑删除");
    private final String remarks;

    StatusEnum(String remarks) {
        this.remarks = remarks;
    }

    public static List<String> getStatusNameList() {
        return StatusEnum.getStatusNameList(false);
    }

    public static List<String> getStatusNameList(Boolean disable) {
        List<String> statusNameList = new ArrayList<>(2);
        statusNameList.add(ENABLE.name());
        if (ObjectUtils.isNotEmpty(disable) && disable) {
            statusNameList.add(DISABLE.name());
        }
        return statusNameList;
    }
}
