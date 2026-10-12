package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command;

import com.guhecloud.apps.linkage.metadata.param.MessageNotifyParam;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 报警指派
 *
 * @author lanchun
 * @date 2022/12/27 9:39
 */
@Data
public class DynamicAssignAlarmRecordCommand {

    /**
     * 报警主键
     */
    private Long id;

    /**
     * 指派的用户集合
     */
    @NotNull(message = "指派的用户不能为空")
    private AssignUserCommand assignUser;

    /**
     * 报警抄送通知
     */
    private List<MessageNotifyParam> ccMessageNotifies;
}
