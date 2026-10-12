package com.guhecloud.cube.srv.gen.maintain.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/20 16:43
 * @Description:
 */
@Data
public class HiddendangerWordOrderStatisticsVO {

    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 隐患类型
     */
    private String hiddenTypeName;
    /**
     * 总数
     */
    private Integer totalNumber = 0;
    /**
     * 待处理数
     */
    private Integer waitNumber = 0;
    /**
     * 已处理数
     */
    private Integer dealNumber = 0;
    /**
     * 处理率
     */
    private String dealRate;
    /**
     * 平均处理时长
     */
    private String avgWaitTime;
    /**
     * 总时长(分钟)
     */
    private Long totalWaitTime;

}
