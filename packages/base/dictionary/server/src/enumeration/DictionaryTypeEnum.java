package com.guhecloud.cube.base.dictionary.server.enumeration;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-25  13:48
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum DictionaryTypeEnum {
    DATA_DICTIONARY("列表字典"),
    SYS_DICTIONARY("系统字典"),
    TREE_DICTIONARY("树形字典"),
    USER_DICTIONARY("用户字典"),
    ;
    private final String remarks;

    DictionaryTypeEnum(String remarks) {
        this.remarks = remarks;
    }
}
