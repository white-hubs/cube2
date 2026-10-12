package com.guhecloud.cube.base.device.center.visualization.controller;

import com.guhecloud.cube.base.device.center.scene.param.DeviceScenePageQueryParam;
import com.guhecloud.cube.base.device.center.visualization.param.DeviceUsefulLifeExpireParam;
import com.guhecloud.cube.base.device.center.visualization.param.VisualizationQueryParam;
import com.guhecloud.cube.base.device.center.visualization.service.DeviceCenterVisualizationService;
import com.guhecloud.cube.base.device.center.visualization.vo.AreaEquipmentStatisticsVo;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceUsefulLifeExpireChainVO;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceUsefulLifeExpireVO;
import com.guhecloud.cube.base.device.center.visualization.vo.OverallEquipmentStatisticsVo;
import com.guhecloud.cube.base.device.center.visualization.vo.StatusEquipmentStatisticsVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceCenterVisualizationController {

    private final DeviceCenterVisualizationService deviceCenterVisualizationService;

    /**
     * 获取过期设备分页列表
     *
     * @param param
     *
     * @return
     */
    @Deprecated
    @GetMapping("/device-bim/device/expired/page")
    public Pagination<DeviceUsefulLifeExpireVO> getExpireDevicePage(@Valid DeviceScenePageQueryParam param) {
        var now = LocalDateTime.now();
        // 1. 查询此系统下的所有设备
        return this.deviceCenterVisualizationService.getUsefulLifeExpiredDevicePage(DeviceUsefulLifeExpireParam.builder()
                                                                                            .pageSize(param.getPageSize())
                                                                                            .pageNumber(param.getPageNumber())
                                                                                            .timeType(param.getType())
                                                                                            .tagCodes(Collections.singleton(param.getSceneType()))
                                                                                            .areaId(param.getAreaId())
                                                                                            .period(Objects.isNull(param.getPeriod())
                                                                                                    ? new Period(DateUtils.beginOfDay(now), now)
                                                                                                    : new Period(param.getPeriod()
                                                                                                                         .getBegin(), LocalDateTime.now()))
                                                                                            .build());
    }

    /**
     * 获取过期设备环比统计
     *
     * @param param
     *
     * @return
     */
    @Deprecated
    @GetMapping("/device-bim/device/expired/chain")
    public DeviceUsefulLifeExpireChainVO getDeviceUsefulLifeExpireChainVO(@Valid DeviceScenePageQueryParam param) {
        // 1. 查询此系统下的所有设备
        var now = LocalDateTime.now();
        return this.deviceCenterVisualizationService.getDeviceUsefulLifeExpireChainVO(DeviceUsefulLifeExpireParam.builder()
                                                                                              .timeType(param.getType())
                                                                                              .tagCodes(Collections.singleton(param.getSceneType()))
                                                                                              .areaId(param.getAreaId())
                                                                                              .period(Objects.isNull(param.getPeriod())
                                                                                                      ? new Period(DateUtils.beginOfDay(now), now)
                                                                                                      : new Period(param.getPeriod()
                                                                                                                           .getBegin(), LocalDateTime.now()))
                                                                                              .build());
    }

    /**
     * @Description 设备总统计
     * @Author lrj
     */
    @PostMapping("/overall-equipment-statistics")
    public OverallEquipmentStatisticsVo getOverallEquipmentStatistics(@RequestBody VisualizationQueryParam param) {
        return deviceCenterVisualizationService.getOverallEquipmentStatistics(param);
    }

    /**
     * @Description 设备状态统计
     * @Author lrj
     */
    @PostMapping("/status-equipment-statistics")
    public StatusEquipmentStatisticsVo getStatusEquipmentStatistics(@RequestBody VisualizationQueryParam param) {
        return deviceCenterVisualizationService.getStatusEquipmentStatistics(param);
    }

    /**
     * @Description 设备离线
     * @Author lrj
     */
    @PostMapping("/offline-equipment-statistics")
    public AreaEquipmentStatisticsVo getAreaEquipmentStatistics(@RequestBody VisualizationQueryParam param) {
        return deviceCenterVisualizationService.getAreaEquipmentStatistics(param);
    }

}
