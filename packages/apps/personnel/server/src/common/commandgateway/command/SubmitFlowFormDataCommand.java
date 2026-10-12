package com.guhecloud.cube.srv.gen.personnel.management.server.common.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2023/02/2
 * @description 提交流程表单数据命令
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitFlowFormDataCommand {

    /**
     * 申请人ID
     */
    private Long applicantId;

    /**
     * 申请业务类型（如招聘、转正、离职等）
     */
    private String businessType;

    /**
     * 表单json
     */
    private String jsonForm;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 处理时间
     */
    private LocalDateTime applyDoneTime;

    /**
     * 审批状态
     */
    private String status;

    private Integer tenant;

}
