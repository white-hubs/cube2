package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预案类型设备分页列表返回参数
 *
 * @author lanchun
 * @date 2022/12/1 14:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyTypeDevicePageVO {
    /**
     * 设备id
     */
    private Long id;
    /**
     * 设备编号
     */
    private String code;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 区域
     */
    private String areaId;
    /**
     * 区域名称
     */
    private String areaName;
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
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    private String deviceProdName;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 设备所属预案规则名称
     */
    private String emergencyConfigs;

}
