package com.guhecloud.cube.base.file.server.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-11  16:33
 * @Description:
 * @Version: 1.0
 */
public enum FileStorageStrategyEnum {
    SUSTAIN("持续"),
    TEMPORARY("临时");
    private String remark;

    FileStorageStrategyEnum(String remark) {
        this.remark = remark;
    }

    public static String getFileStorageStrategy(String storageStrategy) {
        if (ObjectUtils.isNotEmpty(storageStrategy)) {
            for (FileStorageStrategyEnum fileStorageStrategyEnum : FileStorageStrategyEnum.values()) {
                if (fileStorageStrategyEnum.name().equals(storageStrategy)) {
                    return fileStorageStrategyEnum.name();
                }
            }
        }
        return TEMPORARY.name();
    }
}
