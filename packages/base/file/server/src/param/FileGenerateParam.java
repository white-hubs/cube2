package com.guhecloud.cube.base.file.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @author lzy
 * @date 2022/1/7
 */
@Data
public class FileGenerateParam {
    /**
     * 文件名称
     */
    @NotNull(message = "文件名称不能为空")
    private String fileName;
    /**
     * 文件场景
     */
    private String sceneCode;
    /**
     * 文件存储桶
     */
    @Deprecated
    private String businessKey;
}
