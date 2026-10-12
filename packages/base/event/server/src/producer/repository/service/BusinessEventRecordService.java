package com.guhecloud.cube.base.event.server.producer.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.event.server.producer.repository.entity.BusinessEventRecord;

import java.util.List;

public interface BusinessEventRecordService extends IService<BusinessEventRecord> {

    List<BusinessEventRecord> getBusinessEventRecords(Integer maxSize, Long lastEventId);
}
