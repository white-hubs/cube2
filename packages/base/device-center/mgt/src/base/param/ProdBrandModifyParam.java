package com.guhecloud.cube.iot.mgt.base.param;

import lombok.Data;

@Data
public class ProdBrandModifyParam {
    private Long id;
    private Long parentId;
    private Integer sort;
    private String itemLabel;
    //品牌图片
    private String itemValue;
    private String remark;

    public CommonDictItemModifyParam toCommonDictItemModifyParam() {
        CommonDictItemModifyParam commonDictItemModifyParam = new CommonDictItemModifyParam();
        commonDictItemModifyParam.setId(id);
        commonDictItemModifyParam.setParentId(parentId);
        commonDictItemModifyParam.setSort(sort);
        commonDictItemModifyParam.setItemLabel(itemLabel);
        commonDictItemModifyParam.setItemValue(itemValue);
        commonDictItemModifyParam.setRemark(remark);
        return commonDictItemModifyParam;
    }
}