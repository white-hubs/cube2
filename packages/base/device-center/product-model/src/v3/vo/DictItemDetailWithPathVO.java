package com.guhecloud.cube.base.iot.product.model.v3.vo;

import lombok.Data;

/**
 * 字典项详情及路径视图对象
 * 包含从根节点到当前节点的完整路径树和当前节点详情
 */
@Data
public class DictItemDetailWithPathVO {

    /**
     * 从根节点到当前节点的路径树
     * 通过children字段嵌套关联：根 -> 父 -> 当前 -> 当前节点的所有后代
     * 路径上不包含兄弟节点，只包含直系路径
     */
    private DictItemVO path;

    /**
     * 当前字典项详细信息
     */
    private DictItemVO detail;
}
