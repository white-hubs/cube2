package com.guhecloud.cube.base.file.server.param;

import lombok.Data;

import java.util.List;

@Data
public class FileRefCreateParam {

    /**
     * 业务主键
     */
    private Long businessKey;

    /**
     * 场景编码
     */
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
     * 文件主键（兼容之前的逻辑）
     */
    private Long fileId;

    /**
     * 标记
     */
    private String tag;
}
