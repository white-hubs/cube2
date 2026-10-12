package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.open;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 考试任务查询参数
 *
 * @author lanchun
 * @date 2023/4/19 18:04
 */
@Data
public class ExamTaskOpenQuery extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 考试类型
     */
    private ExamTaskTypeEnum examTaskType;

    /**
     * 考试状态
     */
    private ExamTaskStatusEnum examTaskStatus;

    /**
     * 主题
     */
    private Long themeId;

    /**
     * 考试日期
     */
    private Period period;

    /**
     * 用户身份唯一标识
     */
    private String userIdentify;

    /**
     * 市场租户编号
     */
    private Integer marketTenant;

    /**
     * 租户编号
     */
    private Integer tenant;
}
