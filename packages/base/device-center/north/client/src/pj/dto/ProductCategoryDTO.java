package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryDTO {

    private String categoryCode;
    private String categoryCodeConcat;
    private String categoryName;

    private List<ProductCategoryDTO> children;
}
