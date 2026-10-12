package com.guhecloud.cube.srv.gen.loose.works.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wql
 * @describe 流程签约单
 * @date 2023/9/22 11:44
 */
@Data
public class ProjectSignContractDetail {
    /**
     * 发起人
     */
    private Long applicantId;
    /**
     * 发起人部门
     */
    private Long[] applicantDeptId;
    /**
     * 小散工程名称
     */
    private String projectName;
    /**
     * 请示id
     */
    private Long applyId;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同编号
     */
    private String contractNum;
    /**
     * 合同甲方
     */
    private String firstParty;
    /**
     * 合同乙方
     */
    private String partyB;
    /**
     * 合同金额
     */
    private Long money;
    /**
     * 完成时间
     */
    private LocalDateTime finishTime;
    /**
     * 监管人
     */
    private Long[] superviseUserId;
    /**
     * 监管部门
     */
    private List<Long> superviseDepId;
}
