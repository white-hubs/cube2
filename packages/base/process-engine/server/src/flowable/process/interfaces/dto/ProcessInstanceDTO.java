package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.StartUserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * @author pengjian
 * @date 2022/10/25 16:52
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInstanceDTO {
    /**
     * 表单单号
     */
    private Object formNo;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 启动用户id
     */
    private Long startUserId;
    private Long organizationId;

    private String organizationName;
    /**
     * 启动用户信息
     */
    private StartUserInfo startUserInfo;

    /**
     * 表单实例id
     */
    private Long formInstanceId;
    /**
     * 启动用户名称
     */
    private String startUserName;
    /**
     * 提交时间
     */
    private LocalDateTime submitedTime;
    /**
     * 最后任务完成时间
     */
    private LocalDateTime lastTaskCompletedTime;
    /**
     * 流程实例的状态
     */
    private String status;
    /**
     * 当前激活节点名称
     */
    private String activeNodeName;
    /**
     * 当前激活节点处理用户
     */
    private String activeNodeAssignee;
    /**
     * 当前用户处理的任务id
     */
    private String currentTaskId;
    /**
     * 变量
     */
    private Map<String, Object> variables;
    /**
     * 流程实例名称
     */
    private String instanceName;

    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最后修改时间
     */
    private LocalDateTime updateTime;

    public StartUserInfo getStartUserInfo() {
        if (Objects.nonNull(this.startUserInfo)) {
            this.startUserInfo.setStartUserName(this.startUserName);
        }
        return startUserInfo;
    }
}
