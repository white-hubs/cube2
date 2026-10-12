package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway.command;

import com.guhecloud.apps.linkage.metadata.enumeration.MessageTargetNotifyEnum;
import lombok.Data;

@Data
public class ModifyMessageTargetNotifyCommand {

    /**
     * 消息触达对象类型
     */
    private MessageTargetNotifyEnum messageTargetNotify;

    /**
     * 触发对象配置
     */
    private Object targetConfig;
}
