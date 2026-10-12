package com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProcessInstancePageQuery extends Paging {
    /**
     * 查询类型（BACKLOG_OF_ME：我待办的，SUBMITTED_OF_ME：我提交的，COPY_OF_ME：抄送我的，ALL：与我相关的）
     */
    private String type;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 状态（PASS：通过，FINISHED：完成，ACTIVE：处理中，REJECT：拒绝，UNDO：撤销）
     */
    private String status;
    /**
     * 发起人
     */
    private String startUserId;

    /**
     * 审批人
     */
    private String assignee;
    /**
     * 发起人名称
     */
    private String username;
    /**
     * 发起人部门
     */
    private Long organizationId;
    /**
     * 提交事件
     */
    private Period submitTime;
    /**
     * 应用编码
     */
    private String applicationCode;
    /**
     * 流程实例id集合,逗号分隔
     */
    private String processInstanceIds;
    private List<String> processInstanceIdsList;

    /**
     * 当前活跃任务名
     */
    private String currentTaskName;
}
