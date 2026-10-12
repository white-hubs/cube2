package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

/**
 * 素材文件夹参数
 */
@Data
public class MaterialFolderParam {

    /**
     * 主键ID（编辑时必填）
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

}
