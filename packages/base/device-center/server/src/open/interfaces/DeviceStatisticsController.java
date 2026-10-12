package com.guhecloud.cube.base.device.center.open.interfaces;

import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByAreasOrGroupsAndTagParam;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceOfflineCountVO;
import com.guhecloud.cube.base.device.center.open.param.DeviceCenterStatisticsParam;
import com.guhecloud.cube.base.device.center.open.service.DeviceCenterStatisticsService;
import com.guhecloud.cube.base.device.center.open.vo.DeviceStatusVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceStatisticsController {

    private final DeviceCenterStatisticsService deviceStatisticsService;

    /**
     * @Description 设备状态数据数
     * @Author lrj
     */
    @GetMapping("/device/statistics")
    public Map<String, Object> getDeviceStatistics(DeviceCenterStatisticsParam param) {
        return deviceStatisticsService.getDeviceStatistics(param);
    }

    /**
     * @Description 设备状态 在线&离线&环比统计
     * @Author xiaofeng
     * @Returned 设备状态、数量、环比统计
     */
    @GetMapping("/device/visualization/getDeviceStatus")
    public List<Map<String, Object>> getVisualizationDeviceStatus(DeviceCenterStatisticsParam param) {
        return deviceStatisticsService.getVisualizationDeviceStatus(param);
    }

    /**
     * @Description 设备态势
     * @Author lrj
     */
    @GetMapping("/device/statistics/overview")
    public Map<String, Object> getDeviceStatisticsOverview(DeviceCenterStatisticsParam param) {
        return deviceStatisticsService.getDeviceStatisticsOverview(param);
    }

    /**
     * 设备应用数量态势统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/device/application-statistics/overview")
    public Map<String, Object> getApplicationDeviceStatisticsOverview(DeviceCenterStatisticsParam param) {
        return deviceStatisticsService.getApplicationDeviceStatisticsOverview(param);
    }

    /**
     * @Description 监控态势
     * @Author lrj
     */
    @GetMapping("/device/monitoring-situation")
    public Map<String, Object> getMonitoringSituation() {
        return deviceStatisticsService.getMonitoringSituation();
    }

    /**
     * @Description 设备离线情况
     * @Author lrj
     */
    @GetMapping("/application-device-status-num")
    public List<DeviceStatusVo> getApplicationDeviceStatusNum(DeviceCenterStatisticsParam param) {
        return deviceStatisticsService.getApplicationDeviceStatusNum(param);
    }

    /**
     * @Description 分类设备统计离线
     * @Author lrj
     */
    @GetMapping("/classification-offline")
    public List<DeviceOfflineCountVO> getDeviceOffline(DeviceCenterStatisticsParam param) {
        return deviceStatisticsService.getDeviceOffline(param);
    }

    /**
     * 根据区域集合或分组集合获取设备数量
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/devices/count/by-areas-or-groups-and-tag")
    public Integer deviceCountByAreasOrGroupsAndTag(@Valid @RequestBody QueryDeviceByAreasOrGroupsAndTagParam param) {
        return deviceStatisticsService.deviceCountByAreasOrGroupsAndTag(param);
    }
}
