package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.querygateway.query;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class ExamTaskUserRecordStatisticsQuery extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 考试任务主键
     */
    private Long examTaskId;

    /**
     * 考试类型
     */
    private ExamTaskTypeEnum examTaskType;

    /**
     * 考试状态
     */
    private UserExamStatusEnum examStatus;

    /**
     * 主题
     */
    private Long themeId;

    /**
     * 市场租户编号
     */
    private Integer marketTenant;

    /**
     * 是否及格
     */
    private Boolean passed;

    /**
     * 考试日期
     */
    private Period period;
}
