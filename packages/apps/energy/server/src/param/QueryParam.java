package com.guhecloud.cube.srv.gen.energymanagement.server.param;

/**
 * @author wql
 * @describe influxDB时间段查询参数
 * @date 2023/4/11 10:29
 */

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QueryParam {
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 图表起始数
     */
    private Integer flag;
    /**
     * 图表结束数
     */
    private Integer flagTime;

    private String timeFlag;

    /**
     * 回归预测算法训练数据查询条件
     */
    private List<Map<String, String>> historyTimeParameter;

}
