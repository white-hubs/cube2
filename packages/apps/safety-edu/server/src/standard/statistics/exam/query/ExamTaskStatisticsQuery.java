package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.query;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * 考试任务统计参数
 *
 * @author lanchun
 * @date 2023/4/20 19:45
 */
@Data
public class ExamTaskStatisticsQuery {

    private String type;

    /**
     * 时间范围
     */
    private Period period;
}
