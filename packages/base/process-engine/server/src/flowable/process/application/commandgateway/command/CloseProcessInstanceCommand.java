package com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.command;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pengjian
 * @date 2022/9/1 21:58
 * @description
 */
@Getter
public class CloseProcessInstanceCommand {
    private final String processInstanceId;
    private final String businessKey;
    @Setter
    private String comment;

    public CloseProcessInstanceCommand(String processInstanceId, String businessKey) {

        if (StringUtils.isEmpty(processInstanceId) && StringUtils.isEmpty(businessKey)) {
            throw new ServiceException("processInstanceId与businessKey不能同时为空");
        }

        this.processInstanceId = processInstanceId;
        this.businessKey = businessKey;
    }
}
