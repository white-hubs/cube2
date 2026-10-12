package com.guhecloud.cube.biz.smartcard.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.smartcard.server.entity.VisitingRecord;

public interface VisitingRecordService extends IService<VisitingRecord> {
    void insertOrUpdate(VisitingRecord record);
}
