package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.rocketmq.message;

import lombok.Data;

import java.util.List;

@Data
public class HiddenDangerPatrolMessage {
    public final static String TOPIC = "HIDDEN_DANGER_PATROL";

    /**
     * 隐患主键
     */
    private List<String> HiddenDangerIds;

}
