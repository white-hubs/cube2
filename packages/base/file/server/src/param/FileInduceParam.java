package com.guhecloud.cube.base.file.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author lijie
 * @date 2024/12/11
 */
@Data
public class FileInduceParam {
    /**
     * 场景code
     */
    @NotNull(message = "场景不能为空")
    private String sceneCode;

    /**
     * 文件主键集合
     */
    private List<Long> fileIdList;

    /**
     * 文件主键ids
     */
    private String fileIdStr;
    /**
     * 文件关联key
     */
    private Long businessKey;
}
