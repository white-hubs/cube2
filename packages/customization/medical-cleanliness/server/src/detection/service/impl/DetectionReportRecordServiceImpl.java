package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.ReportRecord;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.repo.mapper.DetectionReportRecordMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionReportRecordService;
import org.springframework.stereotype.Service;

@Service
public class DetectionReportRecordServiceImpl extends ServiceImpl<DetectionReportRecordMapper, ReportRecord> implements IDetectionReportRecordService {
    @Override
    public String getRecordSourceListById(long recordId) {

        return this.getBaseMapper().getRecordSourceList(recordId);
    }
}