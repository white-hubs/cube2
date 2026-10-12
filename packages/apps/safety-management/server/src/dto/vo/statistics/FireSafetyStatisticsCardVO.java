package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 消防管理统计卡片
 * @date 2023/9/8 14:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FireSafetyStatisticsCardVO {
    /**
     * 消防报警总数
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
     * 消防设备维保到期数
     */
    private Integer deviceFilingNum;

}
