package com.guhecloud.cube.srv.gen.loose.works.dto;

import lombok.Data;

/**
 * @author wql
 * @describe 流程申请单
 * @date 2023/9/22 11:43
 */
@Data
public class ProjectApplyDetail {
    /**
     * 发起人
     */
    private Long applicantId;
    /**
     * 发起部门
     */
    private Long[] applicantDeptId;
    /**
     * 小散工程名称
     */
    private String projectName;
    /**
     * 小散工程内容
     */
    private String projectContent;
}
