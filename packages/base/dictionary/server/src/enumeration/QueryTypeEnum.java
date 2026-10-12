package com.guhecloud.cube.base.dictionary.server.enumeration;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2024-11-20  09:48
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum QueryTypeEnum {
    ALL("全部"),
    PERSONAL("个人");
    private final String remark;

    QueryTypeEnum(String remark) {
        this.remark = remark;
    }
}
