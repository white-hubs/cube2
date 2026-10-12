package com.guhecloud.cube.base.event.server.consumer.repostiory.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.event.server.consumer.repostiory.entity.EventConsumerRecord;
import com.guhecloud.cube.base.event.server.consumer.repostiory.mapper.EventConsumerRecordMapper;
import com.guhecloud.cube.base.event.server.consumer.repostiory.service.EventConsumerRecordService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventConsumerRecordServiceImpl extends ServiceImpl<EventConsumerRecordMapper, EventConsumerRecord> implements EventConsumerRecordService {

    @Override
    public Long getLastEventId() {
        Page<EventConsumerRecord> consumerRecordPage = this.baseMapper.selectPage(new Page<>(1, 1), Wrappers
                                                                                          .<EventConsumerRecord>lambdaQuery()
                                                                                          .orderByDesc(EventConsumerRecord::getEventId)
                                                                                 );
        if (CollectionUtils.isEmpty(consumerRecordPage.getRecords())) {
            return null;
        }
        return consumerRecordPage.getRecords().get(0).getEventId();
    }
}
