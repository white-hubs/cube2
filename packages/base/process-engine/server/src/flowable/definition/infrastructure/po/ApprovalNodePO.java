package com.guhecloud.cube.base.flow.flowable.definition.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject.ApprovalWay;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/9/6 21:55
 * @description
 */
@Data
@TableName("approval_node")
public class ApprovalNodePO {
    private String taskKey;
    private ApprovalWay way;
    private Boolean supportText;
    private Boolean supportHandwrittenSignature;
}
