package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门维度巡检任务完成统计。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectTaskDepartmentCompletionStatVO {

    /**
     * 部门 id。
     */
    private Long deptId;

    /**
     * 部门名称。
     */
    private String deptName;

    /**
     * 任务总数。
     */
    private Long taskTotal;

    /**
     * 已完成任务数。
     */
    private Long completedTaskTotal;
}
