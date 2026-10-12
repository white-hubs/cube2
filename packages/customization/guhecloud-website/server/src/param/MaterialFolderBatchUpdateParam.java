package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

import java.util.List;

/**
 * 素材文件夹批量修改参数
 */
@Data
public class MaterialFolderBatchUpdateParam {

    /**
     * 要修改的文件夹ID列表
     */
    private List<Long> ids;

    /**
     * 父文件夹ID（不传则不修改）
     */
    private Long pathId;

    /**
     * 文件夹名称（不传则不修改）
     */
    private String name;

    /**
     * 排序（不传则不修改）
     */
    private Integer sort;

}
