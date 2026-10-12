package com.guhecloud.cube.base.iot.product.model.v3.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典项树形视图对象
 * 用于树形选择器等场景
 */
@Data
public class DictItemTreeVO {

    /**
     * 字典项ID
     */
    private Long id;

    /**
     * 字典项编码(v3新增,用于跨环境关联)
     */
    private String itemCode;

    /**
     * 字典类型ID
     */
    private Long dictId;

    /**
     * 字典上级ID
     */
    private Long itemParentId;

    /**
     * 字典排序
     */
    private Integer itemSort;

    /**
     * 层级
     */
    private String itemLevel;

    /**
     * 字典标签(显示名称)
     */
    private String itemLabel;

    /**
     * 字典键值
     */
    private String itemValue;

    /**
     * 状态: 0正常 1停用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 子级字典项列表(树形结构)
     */
    private List<DictItemTreeVO> children;

    /**
     * 关联的字典项
     * 例如: 产品品类关联的产品品牌
     */
    private DictItemTreeVO associatedItem;
}
