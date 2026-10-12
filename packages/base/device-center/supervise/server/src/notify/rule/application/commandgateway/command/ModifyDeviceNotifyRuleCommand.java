package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ModifyDeviceNotifyRuleCommand {

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 开关
     */
    private Boolean enabled;

    /**
     * 规则
     */
    private List<ModifyDeviceNotifyRuleConditionCommand> ruleConditions;

    /**
     * 通知配置
     */
    @NotNull(message = "通知配置不能为空")
    @Size(min = 1, message = "通知配置数量不能小于1")
    @Valid
    private List<ModifyMessageNotifyCommand> messageNotifies;
}
