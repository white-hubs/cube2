package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.Data;

/**
 * @author wql
 * @describe 安全教育统计
 * @date 2023/9/7 19:50
 */
@Data
public class EducationStatisticsVO {
    /**
     * 任务总数
     */
    private Integer totalNum;

    /**
     * 安全教育主题统计
     */
    @Data
    public static class EducationThemeStatistics {
        /**
         * 主题名称
         */
        private String themeName;
        /**
         * 主题占比
         */
        private Double ratio;
    }

}
