package com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 联动消息通知
 *
 * @author lanchun
 * @date 2022/11/1 9:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkageMessageNotifyMessage {

    public static final String TOPIC = "LINKAGE_MESSAGE_NOTIFY";

    private String businessKey;

    private Set<LinkageMessageNotifyTargetMessage> messageNotifyTargetMessages;
}
