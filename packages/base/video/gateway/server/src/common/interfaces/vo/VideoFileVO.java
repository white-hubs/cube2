package com.guhecloud.cube.base.videogw.server.common.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 视频文件信息
 *
 * @author lanchun
 * @date 2024/11/28 14:13
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VideoFileVO {

    private Long id;

    /**
     * 文件主键
     */
    private Long fileId;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 本地存储状态
     */
    private String locallyStoredStatus;
}
