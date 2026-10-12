package com.guhecloud.cube.biz.subsystem.broadcast.server.schedule.infrastruceture.rocketmq.message;

import lombok.Data;

/**
 * 广播播放参数
 *
 * @author lanchun
 * @date 2023/3/23 16:07
 */
@Data
public class BroadcastPlayMessage {

    public static final String TOPIC = "BROADCAST_PLAY_TEXT";

    /**
     * 计划主键
     */
    private Long planId;

    /**
     * 播放参数
     */
    private String playParam;

    /**
     * 租户编号
     */
    private Integer tenant;
}
