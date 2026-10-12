package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo;

import lombok.Data;

/**
 * 考试任务主题占比统计返回参数
 *
 * @author lanchun
 * @date 2023/4/20 19:42
 */
@Data
public class ExamTaskThemeRatioStatisticsVO {

    private Long id;

    /**
     * 主题名称
     */
    private String name;

    /**
     * 数量
     */
    private Integer totalNum;

    /**
     * 占比
     */
    private Float ratio;
}
