package com.guhecloud.cube.srv.gen.appraisals.server.record.param;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalRecordStatusEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 考核记录分页查询参数
 * 用于分页查询考核记录列表
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FormAppraisalRecordPageParam extends Paging {
    /**
     * 关键词搜索（支持规则名称模糊匹配）
     */
    private String keyword;
    /**
     * 考核项目筛选
     */
    private String inspectionItem;
    /**
     * 考核周期类型筛选
     */
    private AppraisalPeriodTypeEnum periodType;
    /**
     * 责任部门 ID 筛选
     */
    private Long responsibleDepartmentId;
    /**
     * 一级流程表单分组 ID 筛选
     */
    private Long firstLevelFormGroupId;
    /**
     * 二级流程表单分组 ID 筛选
     */
    private Long secondLevelFormGroupId;
    /**
     * 考核年度筛选
     */
    private Integer assessmentYear;
    /**
     * 考核期数筛选
     */
    private Integer assessmentPeriod;
    /**
     * 生成开始时间筛选
     */
    private LocalDateTime generatedBeginTime;
    /**
     * 生成结束时间筛选
     */
    private LocalDateTime generatedEndTime;
    /**
     * 记录状态筛选
     */
    private AppraisalRecordStatusEnum status;
}
