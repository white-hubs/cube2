package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces;

import com.guhecloud.cube.base.alarm.client.SatAlarmLevelClient;
import com.guhecloud.cube.base.alarm.client.SatAlarmTypeClient;
import com.guhecloud.cube.base.alarm.client.dto.SatAlarmLevelDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoWithDeviceDTO;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBaseInfoPageQry;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.param.ConfigAlarmRuleParam;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.querygateway.query.DeviceInstanceHealthAlarmRulePreviewQuery;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service.HealthAlarmConfigService;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.dao.HealthAlarmRuleMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.DeviceInstanceHealthAlarmRulePreviewVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.HealthAlarmRuleVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/8/5 11:14
 * @description
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HealthAlarmConfigController {

    private final HealthAlarmRuleMapper healthAlarmRuleMapper;
    private final HealthAlarmConfigService healthAlarmConfigService;
    private final SatAlarmTypeClient satAlarmTypeClient;
    private final SatAlarmLevelClient satAlarmLevelClient;
    private final DeviceCenterStdClient deviceCenterStdClient;

    /**
     * 获取设备的健康报警配置
     */
    @GetMapping("/device/instance/{deviceId}/alarm/health/config")
    public HealthAlarmRuleVO getByDeviceInstanceId(@PathVariable("deviceId") Long deviceInstanceId) {
        return healthAlarmConfigService.getByDeviceInstanceId(deviceInstanceId);
    }

    @PutMapping("/device/instance/{deviceInstanceId}/alarm/health/config/enable")
    public void enableAlarmRule(@PathVariable("deviceInstanceId") Long deviceInstanceId, @RequestParam boolean flag) {
        healthAlarmConfigService.enableAlarmRule(deviceInstanceId, flag);
    }

    /**
     * 覆盖更新
     *
     * @param configAlarmRuleParam
     */
    @PostMapping("/device/instance/{deviceId}/alarm/health/config")
    public void configAlarmRule(@PathVariable("deviceId") Long deviceInstanceId, @RequestBody ConfigAlarmRuleParam configAlarmRuleParam) {
        healthAlarmConfigService.configAlarmRule(deviceInstanceId, configAlarmRuleParam);
    }

    /**
     * 查询所有类型
     *
     */

    /**
     * 设备类型，区域，名称筛选
     */
    @GetMapping("/device/instance/alarm/health/config/page")
    public Pagination<DeviceInstanceHealthAlarmRulePreviewVO> page(DeviceInstanceHealthAlarmRulePreviewQuery query) {
        /**
         * 1. 分页获取设备台账
         * 2. 批量获取设备的报警开关、报警级别、报警阈值
         */
        //        Pagination<DeviceAccountPageVO> page = deviceAccountQueryService.getPage(deviceAccountQuery);
        DeviceBaseInfoPageQry qry = new DeviceBaseInfoPageQry();
        qry.setKeyword(query.getKeyword());
        qry.setPageNumber(query.getPageNumber());
        qry.setPageSize(query.getPageSize());

        Pagination<DeviceBaseArchivesInfoDTO> pagination = deviceCenterStdClient.pageOfBaseInfo(qry);
        if (CollectionUtils.isEmpty(pagination.getRecords())) {
            return new Pagination<>();
        }
        List<DeviceInstanceHealthAlarmRulePreviewVO> deviceAccounts = new LinkedList<DeviceInstanceHealthAlarmRulePreviewVO>();

        Map<Long, DeviceProductInfoWithDeviceDTO> productInfoMap = deviceCenterStdClient
                .getProductInfoByDeviceId(pagination.getRecords().stream().map(DeviceBaseArchivesInfoDTO::getId)
                                                  .collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(DeviceProductInfoWithDeviceDTO::getDeviceId, o -> o));

        pagination.getRecords().forEach(deviceBaseArchivesInfoDTO -> {
            DeviceInstanceHealthAlarmRulePreviewVO deviceInstanceHealthAlarmRulePreviewVO = new DeviceInstanceHealthAlarmRulePreviewVO();
            deviceInstanceHealthAlarmRulePreviewVO.setId(deviceBaseArchivesInfoDTO.getId());
            deviceInstanceHealthAlarmRulePreviewVO.setName(deviceBaseArchivesInfoDTO.getName());
            deviceInstanceHealthAlarmRulePreviewVO.setDeviceId(deviceBaseArchivesInfoDTO.getId());
            DeviceProductInfoWithDeviceDTO deviceProductInfoWithDeviceDTO = productInfoMap.get(deviceBaseArchivesInfoDTO.getId());
            if (Objects.nonNull(deviceProductInfoWithDeviceDTO) && Objects.nonNull(deviceProductInfoWithDeviceDTO.getProductInfo())) {
                deviceInstanceHealthAlarmRulePreviewVO.setProductCategoryName(deviceProductInfoWithDeviceDTO.getProductInfo()
                                                                                      .getProductCategory().getName());
            }
            deviceAccounts.add(deviceInstanceHealthAlarmRulePreviewVO);
        });
        if (CollectionUtils.isEmpty(deviceAccounts)) {
            return new Pagination<>();
        }

        Map<Long, DeviceInstanceHealthAlarmRulePreviewVO> map = healthAlarmRuleMapper.selectDeviceHealthAlarmRuleList(
                        deviceAccounts.stream().map(DeviceInstanceHealthAlarmRulePreviewVO::getDeviceId).collect(Collectors.toSet())
                                                                                                                     ).stream()
                .collect(Collectors.toMap(DeviceInstanceHealthAlarmRulePreviewVO::getDeviceId, v -> v));

        deviceAccounts.forEach(deviceAccount -> {
            DeviceInstanceHealthAlarmRulePreviewVO deviceInstanceHealthAlarmRulePreviewVO = map.get(deviceAccount.getId());
            if (Objects.nonNull(deviceInstanceHealthAlarmRulePreviewVO)) {
                deviceAccount.setThresholdValue(deviceInstanceHealthAlarmRulePreviewVO.getThresholdValue());
                deviceAccount.setEnable(deviceInstanceHealthAlarmRulePreviewVO.getEnable());
                deviceAccount.setLevelName(deviceInstanceHealthAlarmRulePreviewVO.getLevelName());
                deviceAccount.setLevelId(deviceInstanceHealthAlarmRulePreviewVO.getLevelId());
                try {
                    SatAlarmLevelDTO alarmLevel = satAlarmLevelClient.getAlarmLevel(deviceInstanceHealthAlarmRulePreviewVO.getLevelId());
                    deviceAccount.setLevelName(alarmLevel.getName());
                } catch (Exception e) {
                    deviceAccount.setLevelName("");
                }
            }
        });
        return new Pagination<>(pagination.getTotal(), pagination.getPages(), deviceAccounts);
    }
}
