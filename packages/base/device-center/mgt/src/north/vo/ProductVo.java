package com.guhecloud.cube.iot.mgt.north.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductVo {
    private String prodId;
    private String prodNumber;
    private String prodName;
    private String prodCategoryCode;
    private String prodCategoryName;
    private String prodBrandCode;
    private String prodBrandName;
    private String prodModel;
    private Long prodIcon;
    private Long prodPic;
    private String remark;

    private List<ProductPropVo> productPropVoList;
    private List<ProductEventVo> productEventVoList;
    private List<ProductMethodVo> productMethodVoList;
}
