package com.guhecloud.cube.base.flow.flowable.process.domain.factory;

import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ApplicationId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.BusinessId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.StartUserInfo;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * @author pengjian
 * @date 2022/9/5 20:47
 * @description
 */
public class ExtProcessInstanceFactory {

    public static ExtProcessInstance create(ProcessInstance processInstance,
                                            BusinessId businessId,
                                            ApplicationId applicationId, String data, StartUserInfo startUserInfo
                                           ) {
        return ExtProcessInstance.builder()
                .processDefinitionKey(processInstance.getProcessDefinitionKey())
                .processInstance(processInstance)
                .businessId(businessId)
                .applicationId(applicationId)
                .data(data)
                .startUserInfo(startUserInfo)
                .build();
    }
}
