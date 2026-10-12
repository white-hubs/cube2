package com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.command;

import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ApplicationId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.BusinessId;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengjian
 * @date 2022/8/29 11:03
 * @description 启动流程实例命令
 */
@Data
public class StartProcessInstanceCommand {

    public StartProcessInstanceCommand() {

    }

    private String instanceName;

    /**
     * APPID
     */
    @NotNull
    private ApplicationId appId;

    /**
     * 启动用户,默认是当前用户id
     */
    private String startAccount;

    /**
     * 流程key
     */
    @NotBlank
    private String processDefinitionKey;

    /**
     * 业务标识
     */
    @NotNull
    private BusinessId businessId;

    private String data;

    private String formInstanceId;

    private String remark = "";

    private String type;
    private Long formDefinitionId;

    private String submitName;
    private boolean publicForm;
    /**
     * 变量
     */
    private Map<String, Object> variables = new HashMap<>();
    /**
     * 发起人指定的审批人
     */
    private Map<String, String> approveUsers = new HashMap<>();

}
