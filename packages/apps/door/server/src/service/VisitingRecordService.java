package com.guhecloud.cube.biz.door.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.door.server.entity.VisitingRecord;

public interface VisitingRecordService extends IService<VisitingRecord> {
    void insertOrUpdate(VisitingRecord record);
}
