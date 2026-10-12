package com.guhecloud.cube.srv.gen.safety.management.education.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class EducationMarketTaskUrlQuery extends Paging {

    /**
     * 任务类型
     * CULTIVATE_TASK 培训
     * EXAM_TASK 考试
     */
    @Size(max = 50, message = "任务类型不能大于50")
    private String taskType;

    @Size(max = 50, message = "关键字不能大于50")
    private String keyword;
}
