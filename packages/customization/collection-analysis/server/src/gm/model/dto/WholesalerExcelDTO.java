package com.guhecloud.cube.biz.collection.analysis.gm.model.dto;

import lombok.Data;

@Data
public class WholesalerExcelDTO {
    private String name;
    /**
     * 性别(SexEnum)
     */
    private String sex;
    private Integer age;

    /**
     * 省
     */
    private String nativePlaceProvince;
    /**
     * 市
     */
    private String nativePlaceCity;

    /**
     * 省
     */
    private String upstreamSourceProvince;
    /**
     * 市
     */
    private String upstreamSourceCity;

    /**
     * 经营规模
     */
    private Integer businessScaleNumber;
}
