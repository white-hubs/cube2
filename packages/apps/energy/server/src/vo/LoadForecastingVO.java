package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 负荷预测查询返回数据
 * @date 2023/4/10 18:42
 */
@Data
public class LoadForecastingVO {
    private Integer timeFlag;
    /**
     * 时间段
     */
    private String timeQuantum;
    /**
     * 预测值
     */
    private String predictedValue;
    /**
     * 历史值
     */
    private String historicalValue;
    /**
     * 偏差值
     */
    private String deviationValue;
    /**
     * 偏差百分比
     */
    private String deviationPercent;

    /**
     * 预测总能耗
     */
    private String predictedTotal;
    /**
     * 历史总能耗
     */
    private String historicalTotal;
}
