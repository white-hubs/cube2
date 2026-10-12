package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command;

import com.guhecloud.apps.linkage.metadata.enumeration.MessageTargetNotifyEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AssignUserCommand {

    /**
     * 指派对象类型
     */
    @NotNull(message = "指派的对象类型为空")
    private MessageTargetNotifyEnum targetType;

    /**
     * 触发对象配置
     */
    @NotNull(message = "指派的用户不能为空")
    @Size(min = 1, message = "指派的用户不能小于1")
    private Set<String> targetIds;
}
