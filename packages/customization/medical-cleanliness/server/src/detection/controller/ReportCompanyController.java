package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionReportCompanyService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.ReportCompanyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 检测记录公司
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportCompanyController {

    private final IDetectionReportCompanyService service;

    @GetMapping("/report/company")
    public ReportCompanyVO getReportCompany(@RequestParam("reportId") Long reportId) {
        return service.getReportCompany(reportId);
    }
}
