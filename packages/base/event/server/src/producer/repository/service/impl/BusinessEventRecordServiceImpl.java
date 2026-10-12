package com.guhecloud.cube.base.event.server.producer.repository.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.event.server.producer.repository.entity.BusinessEventRecord;
import com.guhecloud.cube.base.event.server.producer.repository.mapper.BusinessEventRecordMapper;
import com.guhecloud.cube.base.event.server.producer.repository.service.BusinessEventRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BusinessEventRecordServiceImpl extends ServiceImpl<BusinessEventRecordMapper, BusinessEventRecord> implements BusinessEventRecordService {

    @Override
    public List<BusinessEventRecord> getBusinessEventRecords(Integer maxSize, Long lastEventId) {
        Page<BusinessEventRecord> recordPage = this.baseMapper.selectPage(new Page<>(1, maxSize), Wrappers
                                                                                  .<BusinessEventRecord>lambdaQuery()
                                                                                  .gt(Objects.nonNull(lastEventId), BusinessEventRecord::getId, lastEventId)
                                                                                  .orderByDesc(BusinessEventRecord::getId)
                                                                         );
        return recordPage.getRecords();
    }
}
