package com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class SecurityDeviceInfoVO {
    @JSONField(serializeFeatures = JSONWriter.Feature.WriteLongAsString)
    private Long id;
    //设备名称
    private String deviceName;
    //设备id
    @JSONField(serializeFeatures = JSONWriter.Feature.WriteLongAsString)
    private Long deviceId;
    //设备code
    private String code;

    //应用
    private String applicationCode;
    //区域id
    private String areaId;

    //区域
    private String areaName;
    //状态
    private Boolean deviceStatus;

    private String communicationStatus;

    //品牌code
    private String brandCode;
    //品牌
    private String brandName;

    /**
     * 第三方设备id
     */
    private String thirdPartyId;
    /**
     * 经纬度
     */
    private String lntAndLat;

    /**
     * 产品品类编号
     */
    private String deviceProdCategoryCode;

    /**
     * 产品品类编号序列号
     */
    private String deviceProdCategoryCodeConcat;

    /**
     * 产品品类名称
     */
    private String deviceProdCategoryName;

    /**
     * 设备产品主键
     */
    @JSONField(serializeFeatures = JSONWriter.Feature.WriteLongAsString)
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    private String deviceProdName;

}
