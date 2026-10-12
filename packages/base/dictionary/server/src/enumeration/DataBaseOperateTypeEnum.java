package com.guhecloud.cube.base.dictionary.server.enumeration;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-11  15:58
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum DataBaseOperateTypeEnum {
    ADD("添加操作"),
    DELETE("删除操作"),
    UPDATE("更新操作");
    private final String remarks;

    DataBaseOperateTypeEnum(String remarks) {
        this.remarks = remarks;
    }
}
