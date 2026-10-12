package com.guhecloud.cube.iot.north.bridge.consumer.cfg;

import lombok.Data;

@Data
public class SubscribeConfig {

    protected int taskQueueSize = 1000; // 任务队列大小, 默认 1000
}
