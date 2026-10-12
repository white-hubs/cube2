package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lrj
 * @Description 仪表管理
 * @date 2022-07-04 15:12:36
 */
@Data
public class MeterDeviceParam {

    //设备区域id
    private String deviceAreaId;
    //设备区域
    private String deviceArea;
    //上级设备
    private String parentName;
    //上级设备id
    private String parentId;
    //上期用量
    private BigDecimal lastReadeValue;
    //本期用量
    private BigDecimal nowReadeValue;
    //本次抄表时间
    private LocalDateTime nowReadeTime;
    //上期抄表时间
    private LocalDateTime lastReadeTime;
    //设备类型id
    private String deviceTypeId;
    //设备名称
    private String deviceName;
    //设备类型
    private String deviceType;
    //设备id
    private String deviceId;
    // 设备品牌(产品信息->品牌名称)
    private String productBrandName;
    // 产品id
    private Long productId;
    // 产品名称
    private String productName;
    // 产品分类全路径
    private String productCategoryPathCode;
    // 产品分类名称
    private String productCategoryName;
    /**
     * 仪表类型
     */
    private String instrumentType;
    /**
     * 仪表编码
     */
    private String code;
    //读数
    private BigDecimal readeValue;

}