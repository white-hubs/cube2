package com.guhecloud.cube.property.management.client.property.vo.property;

import lombok.Data;

import java.util.List;

@Data
public class PropertyByDepAll {
    private String totalPrice;
    private String totalNumber;

    private List<PropertyByDepAllPageVO> depAll;
}
