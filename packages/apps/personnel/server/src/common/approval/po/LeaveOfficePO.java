package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po;

import annotation.FormField;
import lombok.Data;

import java.util.List;

/**
 * @author bq
 * @date 2023/02/28
 * @description 离职审批表
 */
@Data
public class LeaveOfficePO {

    @FormField(label = "离职岗位")
    private String postId;

    @FormField(label = "离职部门")
    private Long organizationId;

    @FormField(label = "离职员工")
    private List<String> staffIds;

    @FormField(label = "上班截止时间")
    private String time;

    /**
     * 申请人
     */
    private Long applicantId;

    /**
     * 通用数据表的ID
     */
    private Long flowFormDataId;

    private Integer tenant;

}