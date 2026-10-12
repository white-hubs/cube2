package com.guhecloud.cube.biz.collection.analysis.gm.model.result;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MerchantInformationResult {
    private Long id;
    private String name;
    private String sex;
    private Integer age;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
    private String createBy;
    /**
     * 商户类型（MerchantTypeEnum）
     */
    private String merchantType;

    /* 采购商 start */
    /**
     * 区域
     */
    private String area;
    /**
     * 经营类型(BusinessScopeEnum)
     */
    private String businessScope;
    /**
     * 采购车类型(VehicleTypeEnum)
     */
    private String vehicleType;
    /* 采购商 end */


    /* 批发商 start */
    /**
     * 籍贯ids
     */
    private String nativePlaceIds;
    /**
     * 籍贯(省市区名称字符串短横线-隔开)
     */
    private String nativePlace;
    /**
     * 上游来源ids
     */
    private String upstreamSourceIds;
    /**
     * 上游来源(省市区名称字符串短横线-隔开)
     */
    private String upstreamSource;
    /**
     * 经营规模
     */
    private Integer businessScaleNumber;
    /* 批发商 end */
}
