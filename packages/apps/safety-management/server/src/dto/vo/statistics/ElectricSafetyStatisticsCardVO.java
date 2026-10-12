package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.Data;

/**
 * @author wql
 * @describe 电气安全管理统计卡片
 * @date 2023/9/8 15:38
 */
@Data
public class ElectricSafetyStatisticsCardVO {
    /**
     * 电气报警总数
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
     * 处理中报警数
     */
    private Integer inProcessNum;
    /**
     * 报警正确率
     */
    private Double accuracyRatio;
}
