package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 隐患任务统计
 *
 * @author wql
 * @describe 隐患任务统计
 * @date 2023/11/12 13:35
 */
@Data
public class HiddenTaskStatisticsVO {
    /**
     * 隐患级别
     */
    private List<HiddenStatistic> hiddenLevel;
    /**
     * 隐患分类
     */
    private List<HiddenStatistic> hiddenClassify;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class HiddenStatistic {
        /**
         * 统计项名称
         */
        private String name;
        /**
         * 统计项值
         */
        private Long num;
        /**
         * 占比
         */
        private Double ratio;
    }
}
