package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 能耗采集配置
 * @date 2024-01-26 10:08:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyCollectConfigParam {

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
    // 燃料类型编码
    private String fuelCode;
    // 仪表回卷周期值
    private BigDecimal rolloverValue;
    // 单次最大合理用量，为空时不限制
    private BigDecimal maxStepUsage;
}
