package com.guhecloud.cube.iot.mgt.base.vo;

import lombok.Data;

import java.util.List;

@Data
public class DictItemTreeVo {
    private Long id;
    private String logicId;
    private Long dictId;
    private Long itemParentId;
    private Integer itemSort;
    private String itemLevel;
    private String itemLabel;
    private String itemValue;
    private Integer status;
    private String remark;

    private List<DictItemTreeVo> children;

    /**
     * 关联的字典项<br/>
     * eg: 产品品类关联的产品品牌
     */
    private DictItemTreeVo associatedItem;
}
