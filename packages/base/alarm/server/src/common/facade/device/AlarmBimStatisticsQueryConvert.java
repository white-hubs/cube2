package com.guhecloud.cube.base.alarm.server.common.facade.device;

import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmTrendStatisticsQueryParam;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceBimSceneStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmBimStatisticsQueryConvert {

    private final DeviceBimSceneStdClient deviceBimSceneStdClient;

    private final DeviceCenterStdClient deviceCenterStdClient;

    private final AreaClient areaClient;

    public AlarmStatisticsQueryParam convertAlarmStatisticsQueryParam(AlarmBimStatisticsQueryParam param) {
        List<Long> deviceIds = getAlarmBimStatisticsQueryParamDeviceIds(param.getDeviceTags(), param.getSceneType(), param.getAreaId());
        AlarmStatisticsQueryParam alarmStatisticsQueryParam = ObjectUtils.copying(param, AlarmStatisticsQueryParam.class);
        alarmStatisticsQueryParam.setDeviceIds(deviceIds);
        return alarmStatisticsQueryParam;
    }

    public AlarmTrendStatisticsQueryParam convertAlarmStatisticsTrendQueryParam(AlarmBimStatisticsQueryParam param) {
        List<Long> deviceIds = getAlarmBimStatisticsQueryParamDeviceIds(param.getDeviceTags(), param.getSceneType(), param.getAreaId());
        AlarmTrendStatisticsQueryParam alarmStatisticsQueryParam = ObjectUtils.copying(param, AlarmTrendStatisticsQueryParam.class);
        alarmStatisticsQueryParam.setDeviceIds(deviceIds);
        return alarmStatisticsQueryParam;
    }

    public List<Long> convertAlarmStatisticsDeviceTag(Set<String> deviceTags, Long areaId) {
        List<DeviceBaseArchivesInfoDTO> devices = this.deviceCenterStdClient.getListByTag(deviceTags);
        if (CollectionUtils.isEmpty(devices)) {
            return Collections.emptyList();
        }
        if (Objects.nonNull(areaId)) {
            Set<Long> areaChildrenIds = this.areaClient.getChildrenIds(areaId);
            areaChildrenIds.add(areaId);
            devices = devices.stream().filter(device -> Objects.nonNull(device.getArea()) &&
                                                        areaChildrenIds.contains(device.getArea().getKey()))
                    .collect(Collectors.toList());
        }
        return devices.stream().map(DeviceBaseArchivesInfoDTO::getId).collect(Collectors.toList());
    }

    public List<Long> convertAlarmStatisticsDeviceTag(String deviceTag, Long areaId) {
        return this.convertAlarmStatisticsDeviceTag(Collections.singleton(deviceTag), areaId);
    }

    public List<Long> getAlarmBimStatisticsQueryParamDeviceIds(Set<String> deviceTags, Long sceneType, Long areaId) {
        //通过标签过滤设备主键
        if (!CollectionUtils.isEmpty(deviceTags)) {
            return convertAlarmStatisticsDeviceTag(deviceTags, areaId);
        } else if (Objects.nonNull(sceneType)) {
            Set<Long> deviceIds = this.deviceBimSceneStdClient.getBimDeviceIds(sceneType, null);
            return deviceIds.stream().collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
