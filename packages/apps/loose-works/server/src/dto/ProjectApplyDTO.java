package com.guhecloud.cube.srv.gen.loose.works.dto;

import annotation.FormField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 工程申请表单
 * @date 2023/9/18 15:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProjectApplyDTO {
    @FormField(label = "发起人", ignore = true)
    private Long applicantId;
    @FormField(label = "发起部门")
    private Long applicantDeptId;
    @FormField(label = "小散工程名称")
    private String projectName;
    @FormField(label = "小散工程内容")
    private String projectContent;

}
