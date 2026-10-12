package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 部门成员维度巡检任务完成统计。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectTaskDepartmentMemberCompletionStatVO {

    /**
     * 成员 id。
     */
    private Long memberId;

    /**
     * 成员姓名。
     */
    private String memberName;

    /**
     * 任务总数。
     */
    private Long taskTotal;

    /**
     * 已完成任务数。
     */
    private Long completedTaskTotal;

    /**
     * 完成率。
     */
    private BigDecimal completionRate;

    /**
     * 完成情况展示文本。
     */
    private String completionText;
}
