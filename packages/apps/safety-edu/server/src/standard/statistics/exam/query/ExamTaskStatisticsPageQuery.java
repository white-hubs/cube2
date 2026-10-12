package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.query;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * 考试统计分页列表查询参数
 *
 * @author lanchun
 * @date 2023/4/20 17:35
 */
@Data
public class ExamTaskStatisticsPageQuery extends Paging {

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
     * 主题
     */
    private Long themeId;

    /**
     * 考试日期
     */
    private Period period;

    /**
     * 考试任务主键集合
     */
    private Set<Long> examTaskIds;

    /**
     * 考试部门
     */
    private Long organizationId;
}
