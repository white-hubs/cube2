package com.guhecloud.cube.biz.work.center.client.message;

import com.alibaba.fastjson2.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 工作任务流程流转消息
 */
@Data
@SuperBuilder(toBuilder = true)
@JSONType(seeAlso = { WorkTaskInitMessage.class, WorkTaskFlowCirculationMessage.class, WorkTaskDelMessage.class })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "operationType",
        visible = true
)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = WorkTaskInitMessage.class, name = "INSERT"),
                @JsonSubTypes.Type(value = WorkTaskFlowCirculationMessage.class, name = "CHANGE"),
                @JsonSubTypes.Type(value = WorkTaskDelMessage.class, name = "DELETE")
        }
)
@AllArgsConstructor
@NoArgsConstructor
public class WorkTaskFlowMessage {

    public static final String TOPIC = "WORK_TASK_FLOW";

    /**
     * 业务主键
     */
    protected String businessKey;

    /**
     * 操作类型
     */
    protected WorkTaskFlowOperationEnum operationType;
}
