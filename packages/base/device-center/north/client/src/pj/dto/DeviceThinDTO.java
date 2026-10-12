package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class DeviceThinDTO {
    private String deviceId;
    private String deviceParentId;
    private String deviceNumber;
    private String deviceName;
    private String productId;
    private String productName;
    private String prodModel;//产品型号名称
    @JSONField(serialize = false)
    private Long prodBrandId;//产品品牌ID
    private String prodBrandCode;//产品品牌
    private String prodBrandName;//产品品牌名称
    private String prodBrandPath;//产品品牌code级联

    private String prodCategoryId;
    private String prodCategoryCode;
    private String prodCategoryName;
    private String prodCategoryNameConcat;

    private String tags;

    /**
     * path 的分隔符
     */
    public static final String PATH_SPLIT = "@@";
}