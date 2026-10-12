package com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/9 9:26
 * @Description:
 */

@Data
public class ProcessInstanceHistoricalQuery {

    /**
     * 导出表格名称
     */
    private String exportName;
    /**
     * 流程定义id
     */
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
     * 发起人名称
     */
    private String username;
    /**
     * 发起人部门
     */
    private Long organizationId;
    /**
     * 提交时间
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
    /**
     * 后端批量查询使用
     */
    private List<String> processInstanceIdsList;
}
