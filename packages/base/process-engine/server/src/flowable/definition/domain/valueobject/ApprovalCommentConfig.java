package com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pengjian
 * @date 2022/8/30 10:23
 * @description 审批意见
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalCommentConfig {
    /**
     * 支持审批文本意见
     */
    private boolean supportText;

    /**
     * 手写签名
     */
    private boolean supportHandwrittenSignature;
}
