package com.guhecloud.cube.base.file.server.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件名后缀
     */
    private String suffix;

    /**
     * URL地址
     */
    private String path;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 关联数量
     */
    private Integer refCount;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 业务主键
     */
    private Long businessKey;

    private String tag;

    private String accessPrefix;
    /**
     * 内网地址
     */
    private String endpointPath;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
