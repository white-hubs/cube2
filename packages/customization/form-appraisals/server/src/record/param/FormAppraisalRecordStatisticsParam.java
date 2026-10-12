package com.guhecloud.cube.srv.gen.appraisals.server.record.param;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalRecordStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 考核记录统计查询参数
 */
@Data
public class FormAppraisalRecordStatisticsParam {
    /**
     * 关键词搜索（规则名称）
     */
    private String keyword;
    /**
     * 考核项目筛选
     */
    private String inspectionItem;
    /**
     * 周期类型
     */
    @NotNull
    private AppraisalPeriodTypeEnum periodType;
    /**
     * 责任部门 ID
     */
    private Long responsibleDepartmentId;
    /**
     * 考核年份
     */
    @NotNull
    private Integer assessmentYear;
    /**
     * 记录状态
     */
    private AppraisalRecordStatusEnum status;
}
