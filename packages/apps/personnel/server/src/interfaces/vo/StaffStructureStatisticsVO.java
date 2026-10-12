package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bq
 * @date 2023/02/21
 * @description 人员概览-结构统计
 */
@Data
public class StaffStructureStatisticsVO {

    // 年龄梯度
    public static final String AGE18_22 = "18-22";
    public static final String AGE22_26 = "23-27";
    public static final String AGE26_30 = "28-32";
    public static final String AGE30_34 = "33-37";
    public static final String AGE34_38 = "38-42";
    public static final String AGE38_42 = "43-47";
    public static final String AGE42 = "48及以上";

    // 司龄
    public static final String SENIORITY1 = "1年以下";
    public static final String SENIORITY1_3 = "1-3年";
    public static final String SENIORITY3_5 = "4-6年";
    public static final String SENIORITY5_8 = "7-9年";
    public static final String SENIORITY8 = "10年及以上";

    /**
     * 总数
     */
    private int total;

    /**
     * 部门分布
     */
    private List<Statistics> deptDistr;

    /**
     * 性别分布
     */
    private List<Statistics> genderDistr;

    /**
     * 状态分布
     */
    private List<Statistics> statusDistr;

    /**
     * 学历分布
     */
    private List<Statistics> eduDistr;

    /**
     * 年龄分布
     */
    private List<Statistics> ageDistr;

    /**
     * 司龄分布
     */
    private List<Statistics> inductionDateDistr;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Statistics {

        /**
         * 名称
         */
        private String name;

        /**
         * 数量
         */
        private int num;
    }

    public List<Statistics> initAgeDistr() {
        return buildStatistics(AGE18_22, AGE22_26, AGE26_30, AGE30_34, AGE34_38, AGE38_42, AGE42);
    }

    public List<Statistics> initInductionDateDistr() {
        return buildStatistics(SENIORITY1, SENIORITY1_3, SENIORITY3_5, SENIORITY5_8, SENIORITY8);
    }

    private List<Statistics> buildStatistics(String... params) {
        List<Statistics> statistics = new ArrayList<>();
        for (String param : params) {
            statistics.add(new Statistics(param, 0));
        }
        return statistics;
    }

}