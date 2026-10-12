package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 素材文件夹VO
 */
@Data
public class MaterialFolderVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 父文件夹ID
     */
    private Long pathId;

    /**
     * 文件夹名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 子文件夹列表
     */
    private List<MaterialFolderVO> children;

}
