package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.ReportRecord;

public interface IDetectionReportRecordService extends IService<ReportRecord> {
    String getRecordSourceListById(long recordId);
}