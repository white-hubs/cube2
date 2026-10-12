package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * APP安全工作情况情况
 *
 * @author lanchun
 * @date 2024/4/7 15:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppSafetyWorkSituationVO {

    /**
     * 报警任务响应率统计
     */
    private WorkSituationVO alarmDispose;
    /**
     * 隐患处理率统计
     */
    private WorkSituationVO hiddenDangerDispose;

    /**
     * 风险源建档率统计
     */
    private WorkSituationVO riskSourcesFillDispose;

    /**
     * 小散工程异常率
     */
    private WorkSituationVO projectError;
    /**
     * 安全巡更频率
     */
    private WorkSituationVO patrolFrequency;
    /**
     * 安全巡检频率
     */
    private WorkSituationVO inspectFrequency;
    /**
     * 安全预案制度
     */
    private WorkSituationVO emergencyFile;
    /**
     * 安全教育频率
     */
    private WorkSituationVO educationFrequency;

    /**
     * 情况统计基础信息
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WorkSituationVO {

        /**
         * 总数
         */
        private Integer totalNum;

        /**
         * 比率
         */
        @Decimal
        private BigDecimal ratio;

        /**
         * 环比
         */
        @Decimal
        private BigDecimal chainRatio;
    }
}
