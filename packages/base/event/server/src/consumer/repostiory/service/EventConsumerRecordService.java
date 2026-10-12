package com.guhecloud.cube.base.event.server.consumer.repostiory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.event.server.consumer.repostiory.entity.EventConsumerRecord;

public interface EventConsumerRecordService extends IService<EventConsumerRecord> {

    Long getLastEventId();
}
