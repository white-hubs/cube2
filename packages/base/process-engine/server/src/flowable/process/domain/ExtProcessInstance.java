package com.guhecloud.cube.base.flow.flowable.process.domain;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ApplicationId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.BusinessId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.StartUserInfo;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengjian
 * @date 2022/9/5 16:16
 * @description
 */

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ExtProcessInstance {
    private String id;

    private String processDefinitionKey;

    private StartUserInfo startUserInfo;

    @Setter
    private String instanceName;

    private ProcessInstance processInstance;

    private BusinessId businessId;

    private ApplicationId applicationId;

    /**
     * 实例数据
     */
    private Object data;

    private String result;
    /**
     * key: taskDefinitionKey
     * Object: data
     */
    @Setter
    private Map<String, Object> dataMap = new HashMap<>();

    private ExtProcessInstancePO extProcessInstancePO;

    public String getResult() {
        return (String) processInstance.getProcessVariables().get(VariableConstant.RESULT);
    }

    public String getData() {
        return (String) processInstance.getProcessVariables().get(VariableConstant.DATA);
    }
}
