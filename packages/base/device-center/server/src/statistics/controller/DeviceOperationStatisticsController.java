package com.guhecloud.cube.base.device.center.statistics.controller;

import cn.hutool.poi.excel.ExcelWriter;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceStateStatisticsVO;
import com.guhecloud.cube.base.device.center.statistics.param.DeviceStatisticsQueryParam;
import com.guhecloud.cube.base.device.center.statistics.service.BizDeviceStatisticsService;
import com.guhecloud.cube.base.device.center.statistics.service.DeviceOperationStatisticsService;
import com.guhecloud.cube.base.device.center.statistics.vo.DeviceMaintainStatisticsVO;
import com.guhecloud.cube.base.device.center.statistics.vo.DeviceStatisticsNumVO;
import com.guhecloud.cube.base.device.center.statistics.vo.DeviceStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 设备运行统计
 *
 * @author wql
 * @describe 设备运行统计
 * @date 2024/1/22 10:34
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceOperationStatisticsController {
    private final DeviceOperationStatisticsService deviceStatistics;

    private final BizDeviceStatisticsService bizDeviceStatistics;

    /**
     * 设备运行统计
     *
     * @param query 查询条件
     *
     * @return 统计结果分页信息
     */
    @GetMapping("/device/supervise/statistics")
    public Pagination<DeviceStatisticsVO> deviceStatistics(DeviceStatisticsQueryParam query) {
        return deviceStatistics.deviceStatistics(query);
    }

    /**
     * 设备维护统计
     *
     * @param query 查询条件
     *
     * @return 统计结果分页信息
     */
    @GetMapping("/device/supervise/maintain/statistics")
    public Pagination<DeviceMaintainStatisticsVO> deviceMaintainStatistics(DeviceStatisticsQueryParam query) {
        return deviceStatistics.deviceMaintainStatistics(query);
    }

    /**
     * 设备统计导出
     *
     * @param query 查询条件
     */
    @GetMapping("/device/supervise/statistics/export")
    public void deviceStatisticsExport(@Param("query") DeviceStatisticsQueryParam query, HttpServletResponse response
                                      ) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        String fileName = URLEncoder.encode("设备运行统计" + DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss"), "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        try (ExcelWriter writer = deviceStatistics.deviceStatisticsExport(query)) {
            try (var outputStream = response.getOutputStream()) {
                writer.flush(outputStream, true);
            }
        } catch (IOException e) {
            log.error("设备运行统计导出异常:", e);
        }
    }

    /**
     * 设备维护统计导出
     *
     * @param query 查询条件
     */
    @GetMapping("/device/supervise/maintain/statistics/export")
    public void deviceMaintainStatisticsExport(DeviceStatisticsQueryParam query,
                                               HttpServletResponse response
                                              ) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        String fileName = URLEncoder.encode("设备维护统计" + DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss"), "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        try (ExcelWriter writer = deviceStatistics.deviceMaintainStatisticsExport(query)) {
            try (var outputStream = response.getOutputStream()) {
                writer.flush(outputStream, true);
            }
        } catch (IOException e) {
            log.error("设备维护统计导出异常:", e);
        }
    }

    /**
     * @description: 获取产品品类下的设备数量
     * @author: lijie
     * @date: 2024/4/2 13:51
     * @param:
     * @return:
     **/
    @GetMapping("/getDevicesNumber")
    public List<DeviceStatisticsNumVO> getDevicesNumber(String period) {
        return deviceStatistics.getDevicesNumber(period);
    }

    /**
     * 获取设备状态统计信息
     *
     * @param ids
     *
     * @return
     */
    @PostMapping("/device/state/statistics/by-ids")
    public Map<Long, DeviceStateStatisticsVO> deviceStatistics(@RequestBody Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        var bizDeviceStatisticsMap = bizDeviceStatistics.deviceStatistics(ids);
        Map<Long, DeviceStateStatisticsVO> deviceStateStatisticsVOMap = new HashMap<>();
        bizDeviceStatisticsMap.forEach((deviceId, bizDeviceStatistics) -> {
            var deviceStateStatisticsVO = DeviceStateStatisticsVO.builder()
                    .id(deviceId)
                    .deviceState(bizDeviceStatistics.getDeviceState())
                    .reportTime(bizDeviceStatistics.getReportTime())
                    .onlineTime(bizDeviceStatistics.getOnlineTime())
                    .onlineNum(bizDeviceStatistics.getOnlineNum())
                    .offlineTime(bizDeviceStatistics.getOfflineTime())
                    .offlineNum(bizDeviceStatistics.getOfflineNum())
                    .build();
            deviceStateStatisticsVOMap.put(deviceId, deviceStateStatisticsVO);
        });
        return deviceStateStatisticsVOMap;
    }
}
