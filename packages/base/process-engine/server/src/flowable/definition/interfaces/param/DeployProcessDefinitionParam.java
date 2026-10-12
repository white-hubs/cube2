package com.guhecloud.cube.base.flow.flowable.definition.interfaces.param;

import com.guhecloud.cube.base.flow.flowable.definition.domain.entity.Copy;
import com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject.ApprovalWay;
import com.guhecloud.cube.base.flow.flowable.extension.node.NodeType;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/8/31 13:03
 * @description
 */
@Data
public class DeployProcessDefinitionParam {
    private List<ApprovalTaskNodeParam> taskNodes;

    /**
     * 表单信息
     */
    private String formKey;

    private String note;

    @Data
    public static class ApprovalTaskNodeParam {
        private String taskName;
        private String taskKey;
        private NodeType nodeType;
        private String processDefinitionKey;
        private String assigneeUserExpression;
        private Copy copy;
        /**
         * 表单操作权限  -- json格式
         */
        private String data;

        /**
         * 审批方式
         */
        private ApprovalWay approvalWay;

        /**
         * 支持审批文本意见
         */
        private boolean supportText;

        /**
         * 手写签名
         */
        private boolean supportHandwrittenSignature;

    }
}
