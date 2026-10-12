package com.guhecloud.cube.base.alarm.client.param;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 报警指派动作
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitAssignAlarmRecordParam {

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

    /**
     * 报警抄送通知
     */
    private List<String> channelTypes;
}
