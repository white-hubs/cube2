package com.guhecloud.cube.biz.emergency.server.time.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteEmergencyJobMessage {

    public static final String TOPIC = "remove_emergency_job";

    /**
     * 任务id集合
     */
    private List<Long> jobIds;
}
