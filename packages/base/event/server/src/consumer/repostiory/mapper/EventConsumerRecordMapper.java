package com.guhecloud.cube.base.event.server.consumer.repostiory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.event.server.consumer.repostiory.entity.EventConsumerRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EventConsumerRecordMapper extends BaseMapper<EventConsumerRecord> {
}
