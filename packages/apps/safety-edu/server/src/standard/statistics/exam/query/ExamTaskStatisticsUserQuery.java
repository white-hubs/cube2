package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.query;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 考试统计用户列表参数
 *
 * @author lanchun
 * @date 2023/4/21 11:09
 */
@Data
public class ExamTaskStatisticsUserQuery extends Paging {

    /**
     * 任务主键
     */
    @NotNull(message = "任务主键不能为空")
    private Long examTaskId;

    /**
     * 通过状态（false:不及格,true:及格）
     */
    private Boolean passed;

    /**
     * 考试状态
     */
    private UserExamStatusEnum examStatus;
}
