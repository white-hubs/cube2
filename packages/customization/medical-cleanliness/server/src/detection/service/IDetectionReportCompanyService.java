package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.ReportCompany;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.ReportCompanyVO;

public interface IDetectionReportCompanyService extends IService<ReportCompany> {
    ReportCompanyVO getReportCompany(Long reportId);
}
