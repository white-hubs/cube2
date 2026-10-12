package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wql
 * @describe 隐患统计
 * @date 2023/9/7 19:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HiddenDangerStatisticsVO {
    /**
     * 隐患总数
     */
    private Integer totalNum;
    /**
     * 待确认隐患数
     */
    private Long toBeConfirmedNum;
    /**
     * 处理中隐患数
     */
    private Long inHandNum;
    /**
     * 隐患分类信息
     */
    private List<HiddenDangerClassifyInfo> classifyNums;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class HiddenDangerClassifyInfo {
        /**
         * 隐患类型
         */
        private String classify;
        /**
         * 隐患数量
         */
        private Long num;

    }
}
