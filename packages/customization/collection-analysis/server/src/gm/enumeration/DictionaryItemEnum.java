package com.guhecloud.cube.biz.collection.analysis.gm.enumeration;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-16  11:28
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum DictionaryItemEnum {
    /**
     * 字典项枚举
     */
    GM_VISUALIZATION_DATA_URL("光明数据采集接口字典项");

    private final String remarks;

    DictionaryItemEnum(String remarks) {
        this.remarks = remarks;
    }
}
