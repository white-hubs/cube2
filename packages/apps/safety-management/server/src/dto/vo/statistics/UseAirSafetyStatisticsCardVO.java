package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 用气管理统计卡片
 * @date 2023/9/8 14:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UseAirSafetyStatisticsCardVO {
    /**
     * 燃气报警总数
     */
    private Integer totalNum;
    /**
     * 未响应报警数
     */
    private Integer noResponseNum;
    /**
     * 超时未处理报警数
     */
    private Integer overtimeUntreatedNum;
    /**
     * 超时报警数
     */
    private Integer timeoutNum;
    /**
     * 报警正确率
     */
    private Double accuracyRatio;
    /**
     * 易燃设备建档数
     */
    private Integer deviceFilingNum;
}
