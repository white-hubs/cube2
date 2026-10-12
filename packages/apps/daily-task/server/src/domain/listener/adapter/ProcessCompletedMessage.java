package com.guhecloud.cube.biz.daily.task.domain.listener.adapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessCompletedMessage {
    public static final String TOPIC = "FLOW_COMPLETED";

    private String processInstanceId;

    private FlowResult result;
    private BusinessId businessId;

    private String data;

    private LocalDateTime startTime;

    private LocalDateTime completedTime;

    private Integer tenant;

    private Long startUserId;
    private String mobile;
    private String sourceProcessId;
    private Map<String, Object> properties;
}