package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 素材VO
 */
@Data
public class MaterialVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 所属文件夹ID
     */
    private Long pathId;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件扩展名
     */
    private String ext;

    /**
     * MIME类型
     */
    private String mimeType;


    /**
     * 文件大小（字节）
     */
    private Integer size;

    /**
     * 存储路径/URL
     */
    private Long fileId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    private String url;

}
