package com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.query.TrafficDetectionAreaQueryParam;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.query.TrafficDetectionAreaStatQueryParam;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.converters.TrafficDetectionAreaConverter;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.TrafficDetectionAreaExportVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionAreaPersonFlowRankExportVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionAreaPersonTrendExportVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionPersonFlowDetailExportVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 人流区域列表导出
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrafficDetectionAreaExportService {

    private final TrafficDetectionAreaQueryService trafficDetectionAreaQueryService;

    private final TrafficDetectionAreaPersonFlowService trafficDetectionAreaPersonFlowService;

    private final TrafficDetectionAreaStatService trafficDetectionAreaStatService;

    public void exportTrafficDetectionAreas(TrafficDetectionAreaQueryParam param, HttpServletResponse response) {
        var trafficDetectionAreas = trafficDetectionAreaQueryService.getTrafficDetectionAreas(param);
        Assert.isNotFound(CollectionUtils.isEmpty(trafficDetectionAreas), "暂无可导出的人流区域数据");
        var areaIdToPersonFlow = trafficDetectionAreaPersonFlowService.getAreaIdToPersonFlowMap(trafficDetectionAreas);
        var exportRows = TrafficDetectionAreaConverter.toTrafficDetectionAreaExportVOList(trafficDetectionAreas, areaIdToPersonFlow);
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String excelName = URLEncoder.encode("人流区域统计", StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), TrafficDetectionAreaExportVO.class)
                    .sheet("人流区域统计")
                    .doWrite(exportRows);
        } catch (IOException e) {
            log.error("人流区域导出失败", e);
            throw new ServiceException("文件下载失败");
        }
    }

    /**
     * 各区域人流量趋势导出（一行一个区域的一个时间点）
     */
    public void exportTrafficDetectionAreaPersonTrend(TrafficDetectionAreaStatQueryParam param, HttpServletResponse response) {
        var areaTrendList = trafficDetectionAreaStatService.getTrafficDetectionAreaPersonTrend(param);
        Assert.isNotFound(CollectionUtils.isEmpty(areaTrendList), "暂无可导出的区域人流量趋势数据");
        List<TrafficDetectionAreaPersonTrendExportVO> rows = new ArrayList<>(areaTrendList.size());
        for (var row : areaTrendList) {
            rows.add(new TrafficDetectionAreaPersonTrendExportVO(
                    row.getName(),
                    row.getRemarks(),
                    row.getPeriod(),
                    row.getTotalPersonNumber()));
        }
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String excelName = URLEncoder.encode("区域人流量趋势", StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), TrafficDetectionAreaPersonTrendExportVO.class)
                    .sheet("区域人流量趋势")
                    .doWrite(rows);
        } catch (IOException e) {
            log.error("区域人流量趋势导出失败", e);
            throw new ServiceException("文件下载失败");
        }
    }

    /**
     * 区域人流排行导出
     */
    public void exportTrafficDetectionAreaPersonFlowRank(TrafficDetectionAreaStatQueryParam param, HttpServletResponse response) {
        var areaRankList = trafficDetectionAreaStatService.listAreaPersonFlowRank(param);
        Assert.isNotFound(CollectionUtils.isEmpty(areaRankList), "暂无可导出的区域人流排行数据");
        List<TrafficDetectionAreaPersonFlowRankExportVO> rows = new ArrayList<>(areaRankList.size());
        for (var row : areaRankList) {
            rows.add(new TrafficDetectionAreaPersonFlowRankExportVO(
                    row.getName(),
                    row.getRemarks(),
                    row.getTotalPersonNumber(),
                    row.getAvgPersonNumber(),
                    row.getPeakPersonNumber(),
                    row.getPeakPeriod()));
        }
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String excelName = URLEncoder.encode("区域人流排行", StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), TrafficDetectionAreaPersonFlowRankExportVO.class)
                    .sheet("区域人流排行")
                    .doWrite(rows);
        } catch (IOException e) {
            log.error("区域人流排行导出失败", e);
            throw new ServiceException("文件下载失败");
        }
    }

    /**
     * 人流详情列表导出
     */
    public void exportTrafficDetectionPersonFlowDetail(TrafficDetectionAreaStatQueryParam param, HttpServletResponse response) {
        var detailList = trafficDetectionAreaStatService.listPersonFlowDetail(param);
        Assert.isNotFound(CollectionUtils.isEmpty(detailList), "暂无可导出的人流详情数据");
        List<TrafficDetectionPersonFlowDetailExportVO> rows = new ArrayList<>(detailList.size());
        for (var row : detailList) {
            rows.add(new TrafficDetectionPersonFlowDetailExportVO(
                    row.getName(),
                    row.getRemarks(),
                    row.getTotalPersonNumber(),
                    row.getReportTime()));
        }
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String excelName = URLEncoder.encode("人流详情列表", StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), TrafficDetectionPersonFlowDetailExportVO.class)
                    .sheet("人流详情列表")
                    .doWrite(rows);
        } catch (IOException e) {
            log.error("人流详情列表导出失败", e);
            throw new ServiceException("文件下载失败");
        }
    }
}
