package com.guhecloud.cube.srv.gen.loose.works.dto;

import annotation.FormField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 工程签约表单
 * @date 2023/9/18 15:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProjectSignContractDTO {
    @FormField(label = "发起人", ignore = true)
    private Long applicantId;
    @FormField(label = "发起人部门")
    private Long applicantDeptId;
    @FormField(label = "小散工程名称")
    private String projectName;
    @FormField(label = "请示id")
    private Long applyId;
    @FormField(label = "合同名称")
    private String contractName;
    @FormField(label = "合同编号")
    private String contractNum;
    @FormField(label = "合同甲方")
    private String firstParty;
    @FormField(label = "合同乙方")
    private String partyB;
    @FormField(label = "合同金额")
    private Integer money;
    @FormField(label = "完成时间")
    private LocalDateTime finishTime;
    @FormField(label = "监管人")
    private String superviseUserId;
    @FormField(label = "监管部门")
    private String superviseDepId;

}
