package com.guhecloud.cube.property.management.client.property.vo.assembly;

import lombok.Data;

/**
 * 通用分类;
 *
 * @author : lijie
 * @date : 2023-3-2
 */
@Data
public class AssemblyClassifyVO {
    /**
     * 组件
     */
    private Long id;
    /**
     * 类型（资产、仓库等）
     */
    private String type;
    /**
     * 名称
     */
    private String name;
    /**
     * 备注
     */
    private String remarks;
}