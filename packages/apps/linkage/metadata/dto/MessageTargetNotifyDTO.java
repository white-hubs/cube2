package com.guhecloud.apps.linkage.metadata.dto;

import com.guhecloud.apps.linkage.metadata.enumeration.MessageTargetNotifyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 通知配置
 *
 * @author lanchun
 * @date 2022/6/21 17:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageTargetNotifyDTO {

    /**
     * 消息触达对象类型
     */
    private MessageTargetNotifyEnum messageTargetNotify;

    /**
     * 触发对象配置
     */
    private Object targetConfig;
}
