package com.guhecloud.cube.base.iot.product.model.v3.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典项分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DictItemPageParam {

    /**
     * 页码(从1开始)
     */
    private Integer pageNumber = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 字典类型ID(必填)
     */
    private Long dictId;

    /**
     * 字典标签(模糊查询)
     */
    private String itemLabel;

    /**
     * 父级ID(查询指定父节点下的子项)
     */
    private Long parentId;

    /**
     * 状态: 1正常 0停用
     */
    private Integer status;

    /**
     * 是否加载子级(用于树形展示)
     */
    private Boolean withChildren = false;

    /**
     * 是否加载关联项(如文件)
     */
    private Boolean withAssociatedItem = false;
}
