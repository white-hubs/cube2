package com.guhecloud.cube.biz.door.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.door.server.dao.VisitingRecordMapper;
import com.guhecloud.cube.biz.door.server.entity.VisitingRecord;
import com.guhecloud.cube.biz.door.server.service.VisitingRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitingRecordServiceImpl extends ServiceImpl<VisitingRecordMapper, VisitingRecord> implements VisitingRecordService {
    @Override
    public void insertOrUpdate(VisitingRecord record) {
        try {
            VisitingRecord temp = this.baseMapper.selectOne(Wrappers.<VisitingRecord>lambdaQuery()
                                                                    .eq(VisitingRecord::getReceptionistName, record.getReceptionistName())
                                                                    .eq(VisitingRecord::getVisitorName, record.getVisitorName())
                                                                    .eq(VisitingRecord::getVisitStartTime, record.getVisitStartTime())
                                                                    .eq(VisitingRecord::getVisitEndTime, record.getVisitEndTime()));
            if (temp != null) {
                temp.setUpdateTime(record.getUpdateTime());
                this.baseMapper.updateById(temp);
            } else {
                this.baseMapper.insert(record);
            }
        } catch (Exception e) {
            log.error("门禁系统访客记录同步异常：", e);
        }
    }
}
