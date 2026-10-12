package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingStatisticsQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingVehicleFlowDetailExportVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingVehicleFlowRankExportVO;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 车辆通行统计导出服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingAccessRecordExportService {

    private final ParkingAccessRecordService parkingAccessRecordService;

    public void exportParkingVehicleFlowRank(ParkingStatisticsQueryParam param, HttpServletResponse response) {
        var rows = parkingAccessRecordService.listParkingVehicleFlowRank(param).stream()
                .map(row -> new ParkingVehicleFlowRankExportVO(row.getName(),
                                                               row.getRemarks(),
                                                               row.getTotalVehicleNumber(),
                                                               row.getAvgVehicleNumber(),
                                                               row.getPeakVehicleNumber(),
                                                               row.getPeakPeriod()))
                .collect(Collectors.toList());
        Assert.isNotFound(CollectionUtils.isEmpty(rows), "暂无可导出的停车场车辆排行数据");
        writeExcel(response, "停车场车辆排行", ParkingVehicleFlowRankExportVO.class, rows);
    }

    public void exportParkingVehicleFlowDetail(ParkingStatisticsQueryParam param, HttpServletResponse response) {
        var rows = parkingAccessRecordService.listVehicleFlowDetail(param).stream()
                .map(row -> new ParkingVehicleFlowDetailExportVO(row.getName(),
                                                                 row.getTotalVehicleNumber(),
                                                                 row.getReportTime()))
                .collect(Collectors.toList());
        Assert.isNotFound(CollectionUtils.isEmpty(rows), "暂无可导出的车辆统计明细数据");
        writeExcel(response, "车辆统计明细", ParkingVehicleFlowDetailExportVO.class, rows);
    }

    private <T> void writeExcel(HttpServletResponse response, String fileName, Class<T> head, List<T> rows) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            var excelName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), head)
                    .sheet(fileName)
                    .doWrite(rows);
        } catch (IOException e) {
            log.error("{}导出失败", fileName, e);
            throw new ServiceException("文件下载失败");
        }
    }
}
