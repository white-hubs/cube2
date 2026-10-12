package com.guhecloud.cube.base.message.sdk.api.model;

import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageNotifyTarget {

    /**
     * 消息通知对象类型
     */
    private MessageNotifyTargetEnum notifyTargetType;

    /**
     * 对象类型主键集合
     */
    private Set<Long> ids;
}
