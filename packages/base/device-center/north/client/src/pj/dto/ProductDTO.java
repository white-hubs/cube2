package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
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

    private List<ProductPropDTO> productPropVoList;
    private List<ProductEventDTO> productEventVoList;
    private List<ProductMethodDTO> productMethodVoList;
}
