package com.guhecloud.cube.srv.gen.reportmanagement.server.controller;

import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportBatchDeleteParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportQueryProcessParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.StatisticsParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.service.ReportService;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportFieldVo;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportStatistics;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportStatisticsVo;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import util.CustBeanUtil;
import vo.FormFieldVO;

import jakarta.validation.Valid;

import java.util.List;

/**
 * @Description 工作汇报
 * @Date 2023/3/22 15:35
 * @Author tangzhiqiang
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportController {
    private final ReportService reportService;

    /**
     * app-数据看板-汇报统计
     *
     * @param
     */
    @GetMapping("/report-statistics")
    public ReportStatisticsVo getReportStatisticsInfo(StatisticsParam param) {
        return reportService.getReportStatisticsInfo(param);
    }

    /**
     * pc-汇报中心
     *
     * @param
     */
    @GetMapping("/report-page")
    public Pagination<ProcessInstanceDTO> getReportPage(ReportQueryProcessParam param) {
        return reportService.getReportPage(param);
    }

    /**
     * app-待处理列表
     *
     * @param
     */
    @GetMapping("/report-pending")
    public Pagination<ReportVo> getReportPending(ReportQueryParam param) {
        return reportService.getReportPending(param);
    }

    /**
     * app-我发起列表
     *
     * @param
     */
    @GetMapping("/report-initiate")
    public Pagination<ReportVo> getReportInitiate(ReportQueryParam param) {
        return reportService.getReportInitiate(param);
    }

    /**
     * app-发起汇报
     */
    @PostMapping("/report-add")
    public void insertReport(@RequestBody ReportAddOrUpdateParam param) {
        reportService.addReport(param);
    }

    /**
     * app-撤销汇报
     */
    @PutMapping("/report-update/{id}")
    public void updateReport(@PathVariable("id") Long id) {
        reportService.cancelReport(id);
    }

    /**
     * app-汇报详情查询
     */
    @GetMapping("/report-details/{id}")
    public ReportVo getReportDetails(@PathVariable("id") Long id) {
        return reportService.getReportDetails(id);
    }

    /**
     * 获取工作汇报字段
     *
     * @return
     */
    @GetMapping("/report-field")
    public List<FormFieldVO> getFormField() {
        return CustBeanUtil.getFormField(ReportFieldVo.class);
    }

    /**
     * 工作汇报统计
     *
     * @return
     */
    @GetMapping("/report-statistics-pc")
    public Pagination<ReportStatistics> reportStatistics(ReportQueryParam param) {
        return reportService.reportStatistics(param);
    }

    /**
     * 工作汇报统计导出
     *
     * @return
     */
    @PostMapping("/report-statistics-export-pc")
    public void reportStatisticsExport(@RequestBody ReportQueryParam param) {
        reportService.reportStatisticsExport(param);

    }

    /**
     * 工作汇报批量删除
     *
     * @param param
     */
    @PostMapping("/report/batch-deleted")
    public void reportBatchDelete(@Valid @RequestBody ReportBatchDeleteParam param) {
        reportService.reportBatchDelete(param);
    }
}
