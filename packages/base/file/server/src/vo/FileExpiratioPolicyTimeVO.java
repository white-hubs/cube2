package com.guhecloud.cube.base.file.server.vo;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-12  14:53
 * @Description:
 * @Version: 1.0
 */
@Data
public class FileExpiratioPolicyTimeVO {
    /**
     * 基于时间删除（N天）
     */
    private Integer multipleDays;
    /**
     * 延期时间（N天）
     */
    private Integer delayDays;
}
