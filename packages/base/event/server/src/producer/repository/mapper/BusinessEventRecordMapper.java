package com.guhecloud.cube.base.event.server.producer.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.event.server.producer.repository.entity.BusinessEventRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BusinessEventRecordMapper extends BaseMapper<BusinessEventRecord> {
}
