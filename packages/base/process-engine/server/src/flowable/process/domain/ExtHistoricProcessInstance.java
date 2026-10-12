package com.guhecloud.cube.base.flow.flowable.process.domain;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ApplicationId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.BusinessId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.StartUserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.activiti.engine.history.HistoricProcessInstance;

/**
 * @author pengjian
 * @date 2022/10/27 10:05
 * @description
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExtHistoricProcessInstance {
    private String processDefinitionKey;

    private HistoricProcessInstance processInstance;

    private BusinessId businessId;

    private ApplicationId applicationId;

    private StartUserInfo startUserInfo;

    private String processInstanceName;
    /**
     * 实例数据
     */
    private Object data;

    private String result;

    /**
     * 来源工单
     */
    private String sourceProcessId;

    public Object getOriginalData() {
        return this.data;
    }

    public String getResult() {
        return (String) processInstance.getProcessVariables().get(VariableConstant.RESULT);
    }

    public String getData() {
        return (String) processInstance.getProcessVariables().get(VariableConstant.DATA);
    }
}
