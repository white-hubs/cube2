package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj;

import com.guhecloud.apps.linkage.metadata.enumeration.MessageTargetNotifyEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageTargetNotify {

    /**
     * 消息触达对象类型
     */
    private MessageTargetNotifyEnum messageTargetNotify;

    /**
     * 触发对象配置
     */
    private Object targetConfig;
}
