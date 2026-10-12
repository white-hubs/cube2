package com.guhecloud.cube.base.flow.flowable.resolver.tool;

import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ApplicationId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.BusinessId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.StartUserInfo;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.ChildNode;
import lombok.Data;
import lombok.Setter;
import org.activiti.engine.runtime.ProcessInstance;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/3/2 14:20
 * @Description: 单个流程不变数据信息实体
 */
@Data
public class ProcessInfo {

    private String id;

    private String processDefinitionId;

    private String instanceName;

    private BusinessId businessId;

    private ApplicationId applicationId;
    /**
     * 发起人信息
     */
    private StartUserInfo startUserInfo;

    /**
     * 流程节点配置信息
     */
    private ChildNode childNode;

    private LocalDateTime startTime;

    /**
     * 去重配置
     */
    private Boolean deDuplication;

    /**
     * 表单配置
     */
    private String formJson;

    /**
     * 审批标题
     */
    private String backlogTitle;

    /**
     * 结果
     */
    private String result;

    /**
     * 流程信息
     */
    private ExtProcessInstancePO extProcessInstancePO;
}
