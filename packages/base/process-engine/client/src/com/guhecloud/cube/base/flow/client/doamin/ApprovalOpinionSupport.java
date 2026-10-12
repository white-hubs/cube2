package com.guhecloud.cube.base.flow.client.doamin;

/**
 * 审批意见支持
 */
public interface ApprovalOpinionSupport {
    ApprovalOpinion getApprovalOpinion();

    ApprovalOpinionSupport EMPTY = ApprovalOpinion::new;
}
