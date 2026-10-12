package com.guhecloud.cube.base.flow.flowable.task.application.event.publish;

import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ApplicationId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.BusinessId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/9/5 16:06
 * @description
 */
@NoArgsConstructor
@AllArgsConstructor
public class BacklogMessage {
    public static final String TOPIC = "PROCESS_TASK_BACKLOG";

    private BusinessId businessId;

    private ApplicationId applicationId;

    private String assigneeAccount;

}
