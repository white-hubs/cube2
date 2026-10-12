package com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log;

import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject.ApprovalCommentConfig;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.TaskFeedbackLogDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskLog {
    /**
     * 节点唯一标识
     */
    private String nodeKey;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 审批意见
     */
    private ApprovalCommentConfig approvalCommentConfig;

    /**
     * 当前节点操作记录
     */
    private List<TaskOperation> taskOperations;

    /**
     * 非当前节点展示的用户
     */
    private List<Operator> assigneeList;

    /**
     * 是否能指定审批人
     */
    private Boolean preAddSign = false;

    /**
     * 是否能发起人自选
     */
    private Boolean selfSelect = false;

    /**
     * 发起人自选范围
     */
    private SelfSelectRange selectRange;

    /**
     * 评论集合
     */
    private List<TaskFeedbackLogDTO> feedbackLogs;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelfSelectRange {
        /**
         * 范围类型
         */
        private String type;
        /**
         * id集合
         */
        List<String> ids;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskOperation {

        /**
         * 评论唯一键
         */
        private List<TaskFeedbackLogDTO> feedbackLogs;
        /**
         * 当前节点历史处理信息
         */
        private Operation operation;
        /**
         * 当前节点的剩余处理人
         */
        private Operator residualOperator;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Operation {
        /**
         * 任务操作类型
         */
        private String type;
        /**
         * 任务操作类型名称
         */
        private String name;
        /**
         * 审批意见
         */
        private ApprovalOpinion approvalOpinion;

        /**
         * 操作人
         */
        private Operator operator;

        private LocalDateTime createTime;

        /**
         * 操作别名
         */
        private String typeAlias;

        /**
         * 操作权限
         */
        private String perm;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Operator {
        /**
         * 用户id
         */
        private Long id;
        /**
         * 用户名
         */
        private String name;

        /**
         * 是否能减签
         */
        private Boolean preReduceSign;

        public Operator(Long id, String name) {
            this.id = id;
            this.name = name;
            this.preReduceSign = false;
        }
    }
}
