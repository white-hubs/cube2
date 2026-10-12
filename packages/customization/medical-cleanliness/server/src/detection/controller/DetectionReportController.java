package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.DetectionReportCommandParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.DetectionReportQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.DetectionReportVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.ReportRecordDetailVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 检测报告
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DetectionReportController {
    private final IDetectionService service;

    /**
     * 检测报告记录分页列表
     *
     * @param param 分页条件
     *
     * @return 报告记录
     */
    @GetMapping("/detection-report/page")
    public Pagination<DetectionReportVO> getReportPage(DetectionReportQueryParam param) {
        return service.getReportPage(param);
    }

    /**
     * 报告详情
     *
     * @param id 主键id
     *
     * @return 详情
     */
    @GetMapping("/detection-report/detail")
    public ReportRecordDetailVO getReportDetail(Long id) {
        return service.getReportDetail(id);
    }

    /**
     * admin端检测报告记录分页列表
     *
     * @param param 分页条件
     *
     * @return 报告记录
     */
    @GetMapping("/detection-report/admin/page")
    public Pagination<DetectionReportVO> getAdminReportPage(DetectionReportQueryParam param) {
        return service.getAdminReportPage(param);
    }

    /**
     * admin端报告详情
     *
     * @param id 主键id
     *
     * @return 详情
     */
    @GetMapping("/detection-report/admin/detail")
    public ReportRecordDetailVO getAdminReportDetail(Long id) {
        return service.getAdminReportDetail(id);
    }

    /**
     * 生成报告
     *
     * @param commandParam 报告入参
     *
     * @return 是否成功
     */
    @PostMapping("/detection-report/generate")
    public ReportRecordDetailVO generateReport(@RequestBody DetectionReportCommandParam commandParam) {
        return service.generateReport(commandParam);
    }

}
