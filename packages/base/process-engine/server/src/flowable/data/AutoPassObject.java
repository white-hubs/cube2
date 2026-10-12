package com.guhecloud.cube.base.flow.flowable.data;

import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 自动审批判断
 * @date 2023/6/5 13:39
 */
@Data
@SuperBuilder
public class AutoPassObject {
    private boolean isAutoPass;
    private Long assigneeId;
    private ApprovalOpinion approvalOpinion;
}
