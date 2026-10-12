package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import lombok.Data;

/**
 * 考试任务类型占比统计
 *
 * @author lanchun
 * @date 2023/4/20 19:58
 */
@Data
public class ExamTaskTypeRatioStatisticsVO {

    /**
     * 任务类型
     */
    private ExamTaskTypeEnum examTaskType;

    /**
     * 类型名称
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
