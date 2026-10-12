package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

/**
 * 素材参数
 */
@Data
public class MaterialParam {

    /**
     * 主键ID（编辑时必填）
     */
    private Long id;

    /**
     * 所属文件夹ID
     */
    private Long pathId;
    /**
     * 文件id
     * */
    private Long fileId;

}
