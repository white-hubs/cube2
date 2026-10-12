package com.guhecloud.cube.biz.todo.client.message;

import com.alibaba.fastjson2.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 待办任务流程流转消息
 *
 * @author lanchun
 * @date 2023/2/24 15:45
 */
@Data
@SuperBuilder(toBuilder = true)
@JSONType(seeAlso = { TodoTaskInitMessage.class, TodoTaskFlowCirculationMessage.class, TodoTaskDelMessage.class })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "operationType",
        visible = true
)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = TodoTaskInitMessage.class, name = "INSERT"),
                @JsonSubTypes.Type(value = TodoTaskFlowCirculationMessage.class, name = "CHANGE"),
                @JsonSubTypes.Type(value = TodoTaskDelMessage.class, name = "DELETE")
        }
)
@AllArgsConstructor
@NoArgsConstructor
public class TodoTaskFlowMessage {

    public static final String TOPIC = "TODO_TASK_FLOW";

    /**
     * 业务主键
     */
    protected String businessKey;

    /**
     * 操作类型
     */
    protected TodoTaskFlowOperationEnum operationType;
}
