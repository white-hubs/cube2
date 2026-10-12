package com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.message;

import lombok.Data;

/**
 * 联动消息通知对象消息
 *
 * @author lanchun
 * @date 2022/11/1 9:42
 */
@Data
public class LinkageMessageNotifyTargetMessage {

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;
}
