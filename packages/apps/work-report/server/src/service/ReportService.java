package com.guhecloud.cube.srv.gen.reportmanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.Report;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportBatchDeleteParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportQueryProcessParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.StatisticsParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportStatistics;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportStatisticsVo;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportVo;
import com.guhecloud.platform.base.model.Pagination;
import instance.event.ProcessStartedMessage;

public interface ReportService extends IService<Report> {

    void addReport(ReportAddOrUpdateParam param);

    void cancelReport(Long id);

    Pagination<ProcessInstanceDTO> getReportPage(ReportQueryProcessParam param);

    ReportStatisticsVo getReportStatisticsInfo(StatisticsParam param);

    ReportVo getReportDetails(Long id);

    Pagination<ReportVo> getReportPending(ReportQueryParam param);

    Pagination<ReportVo> getReportInitiate(ReportQueryParam param);

    /**
     * 同步流引擎数据
     *
     * @param message
     */
    ReportAddOrUpdateParam synFlowData(ProcessStartedMessage message);

    /**
     * 查询工作汇报统计
     *
     * @param param
     *
     * @return
     */
    Pagination<ReportStatistics> reportStatistics(ReportQueryParam param);

    void reportStatisticsExport(ReportQueryParam param);

    /**
     * 工作汇报批量删除
     *
     * @param param
     */
    void reportBatchDelete(ReportBatchDeleteParam param);
}
