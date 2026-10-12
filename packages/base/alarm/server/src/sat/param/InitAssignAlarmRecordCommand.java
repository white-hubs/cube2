package com.guhecloud.cube.base.alarm.server.sat.param;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.IncreaseAlarmRecordTargetCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 报警指派
 *
 * @author lanchun
 * @date 2022/12/27 9:39
 */
@Data
public class InitAssignAlarmRecordCommand {

    /**
     * 报警主键
     */
    private Long id;

    /**
     * 指派的用户集合
     */
    @Valid
    @NotNull(message = "指派的用户不能为空")
    @Size(min = 1, message = "指派的用户不能小于1")
    private List<SatAlarmMsgTargetSendParam> assignUsers;

    private List<IncreaseAlarmRecordTargetCommand> handlerUsers;

    /**
     * 报警抄送通知
     */
    private List<String> channelTypes;
}
