package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 能耗采集配置
 * @date 2024-01-26 10:08:36
 */
@Data
public class EnergyCollectConfigVO {

    //
    private Long id;
    //设备分类code
    private String categoryCode;
    //设备分类名称
    private String categoryName;
    //采集方式：auto 自动/hand 手动录入
    private String collectType;
    //1 开启 0 关闭 是否开启采集
    private Boolean collectOn;
    //自定义编码
    private String custCode;
    //燃料code
    private String fuelCode;
    // 仪表回卷周期值
    private BigDecimal rolloverValue;
    // 单次最大合理用量，为空时不限制
    private BigDecimal maxStepUsage;

    // 燃料的参数
    /**
     * 燃料名称
     */
    private String fuelName;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 碳排因子
     */
    private BigDecimal carbonFactor;
    /**
     * 计算系数
     */
    private BigDecimal calculateProportion;
}
