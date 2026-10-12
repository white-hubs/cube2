package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.guhecloud.cube.base.alarm.client.dto.AlarmDeviceStatusStatisticsPageDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.AlarmDeviceStatusParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ColdWaterAlarmService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.AlarmDeviceStatusStatisticsExportVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ColdWaterAlarmController {

    private final ColdWaterAlarmService coldWaterAlarmService;

    /**
     * 报警设备状态分组统计分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/cold-water/device-status-page")
    Pagination<AlarmDeviceStatusStatisticsPageDTO> getDeviceAlarmStatusTotalPage(AlarmDeviceStatusParam param) {
        return coldWaterAlarmService.getDeviceAlarmStatusTotalPage(param);
    }

    /**
     * 报警设备状态分组统计导表
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/cold-water/device-status/list/export")
    public void exportAlarmStatusTotalList(@RequestBody AlarmDeviceStatusParam param) throws IOException {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String excelName = URLEncoder.encode("报警统计", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
        List<AlarmDeviceStatusStatisticsPageDTO> deviceAlarmStatusTotalExcel = coldWaterAlarmService.getDeviceAlarmStatusTotalExcel(param);
        List<AlarmDeviceStatusStatisticsExportVO> alarmEventStatusStatisticsExportVOS = ObjectUtils.copying(deviceAlarmStatusTotalExcel, AlarmDeviceStatusStatisticsExportVO.class);
        EasyExcel.write(response.getOutputStream(), AlarmDeviceStatusStatisticsExportVO.class).sheet("报警统计")
                .doWrite(alarmEventStatusStatisticsExportVOS);
    }

}
