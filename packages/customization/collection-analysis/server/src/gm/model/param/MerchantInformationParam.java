package com.guhecloud.cube.biz.collection.analysis.gm.model.param;

import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.BusinessScopeEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.MerchantTypeEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.VehicleTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MerchantInformationParam {
    /**
     * 主键
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 商户类型
     *
     * @see MerchantTypeEnum
     */
    @NotEmpty(message = "商户类型不能为空")
    private String merchantType;

    /* 采购商 start */
    /**
     * 区域
     */
    private String area;
    /**
     * 经营类型
     *
     * @see BusinessScopeEnum
     */
    private String businessScope;
    /**
     * 采购车类型
     *
     * @see VehicleTypeEnum
     */
    private String vehicleType;
    /* 采购商 end */


    /* 批发商 start */
    /**
     * 籍贯(省市区ids字符串,隔开)
     */
    private String nativePlaceIds;
    /**
     * 上游来源(省市区id字符串,隔开)
     */
    private String upstreamSourceIds;
    /**
     * 经营规模(吨)
     */
    private Integer businessScaleNumber;
    /* 批发商 end */

    private LocalDateTime createTime;
    private String createBy;
}
