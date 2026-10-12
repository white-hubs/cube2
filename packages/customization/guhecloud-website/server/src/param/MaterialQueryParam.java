package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

/**
 * 素材查询参数
 */
@Data
public class MaterialQueryParam {

    /**
     * 所属文件夹ID
     */
    private Long pathId;

    /**
     * 文件名（模糊查询）
     */
    private String name;

    /**
     * 文件扩展名
     */
    private String ext;

}
