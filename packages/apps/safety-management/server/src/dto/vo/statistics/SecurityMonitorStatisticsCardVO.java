package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.Data;

/**
 * @author wql
 * @describe 安防监控统计卡片
 * @date 2023/9/8 15:21
 */
@Data
public class SecurityMonitorStatisticsCardVO {
    /**
     * 安防报警总数
     */
    private Integer totalNum;
    /**
     * 未响应报警数
     */
    private Integer noResponseNum;
    /**
     * 已处理报警数
     */
    private Integer completeNum;
    /**
     * 超时未处理报警数
     */
    private Integer overtimeUntreatedNum;
    /**
     * 报警正确率
     */
    private Double accuracyRatio;
}
