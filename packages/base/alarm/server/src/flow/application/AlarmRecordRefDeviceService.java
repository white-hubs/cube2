package com.guhecloud.cube.base.alarm.server.flow.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmRecordRefDeviceQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordRefDevicePO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordRefDeviceMapper;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceSortFieldEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.SortEnum;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceSortParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterAssociatedStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlarmRecordRefDeviceService extends ServiceImpl<AlarmRecordRefDeviceMapper, AlarmRecordRefDevicePO> {

    private final DeviceCenterAssociatedStdClient deviceCenterAssociatedStdClient;

    private final DeviceCenterStdClient deviceCenterStdClient;

    public AlarmRecordRefDeviceService(DeviceCenterAssociatedStdClient deviceCenterAssociatedStdClient,
                                       DeviceCenterStdClient deviceCenterStdClient
                                      ) {
        this.deviceCenterAssociatedStdClient = deviceCenterAssociatedStdClient;
        this.deviceCenterStdClient = deviceCenterStdClient;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAlarmRefDevice(Long alarmRecordId, Long alarmDeviceId) {
        Set<Long> refDeviceIds = getRefDeviceIds(alarmDeviceId);
        List<AlarmRecordRefDevicePO> alarmRecordRefDevicePOS = refDeviceIds.stream().map(deviceId ->
                                                                                                 AlarmRecordRefDevicePO.builder()
                                                                                                         .alarmRecordId(alarmRecordId)
                                                                                                         .deviceId(deviceId)
                                                                                                         .build()
                                                                                        ).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(alarmRecordRefDevicePOS)) {
            this.saveBatch(alarmRecordRefDevicePOS);
        }
    }

    public Pagination<DevicePageInfoVO> getAlarmRefDevices(AlarmRecordRefDeviceQueryParam queryParam) {
        List<AlarmRecordRefDevicePO> alarmRecordRefDevicePOS = this.baseMapper.selectList(Wrappers.<AlarmRecordRefDevicePO>lambdaQuery()
                                                                                                  .eq(AlarmRecordRefDevicePO::getAlarmRecordId, queryParam.getAlarmRecordId()));
        if (CollectionUtils.isEmpty(alarmRecordRefDevicePOS)) {
            return Pagination.empty();
        }
        List<Long> refDeviceIds = alarmRecordRefDevicePOS.stream().map(AlarmRecordRefDevicePO::getDeviceId).collect(Collectors.toList());
        DeviceSortParam deviceSortParam = new DeviceSortParam();
        deviceSortParam.setSortField(DeviceSortFieldEnum.ONLINE_STATUS);
        deviceSortParam.setSort(SortEnum.DESC);
        DeviceFilterPageParam param = new DeviceFilterPageParam();
        param.setDeviceSort(deviceSortParam);
        param.setPageNumber(queryParam.getPageNumber());
        param.setPageSize(queryParam.getPageSize());
        param.setCategoryCode(queryParam.getCategoryCode());
        param.setProductId(queryParam.getProductId());
        param.setMatchedDeviceIds(refDeviceIds);
        return deviceCenterStdClient.deviceInfoPage(param);
    }

    private Set<Long> getRefDeviceIds(Long deviceId) {
        try {
            Set<Long> refDeviceIds = new HashSet<>();
            Set<Long> associatedDeviceIds = deviceCenterAssociatedStdClient.getAssociatedDeviceIds(deviceId);
            Set<Long> associatedSpatialCoordinatesDeviceIds = deviceCenterAssociatedStdClient.getAssociatedSpatialCoordinatesDevices(deviceId);
            if (CollectionUtils.isNotEmpty(associatedDeviceIds)) {
                refDeviceIds.addAll(associatedDeviceIds);
            }
            if (CollectionUtils.isNotEmpty(associatedSpatialCoordinatesDeviceIds)) {
                refDeviceIds.addAll(associatedSpatialCoordinatesDeviceIds);
            }
            return refDeviceIds;
        } catch (Exception e) {
            log.error("获取报警设备关联的设备列表失败,deviceId:{}", deviceId, e);
        }
        return Collections.emptySet();
    }
}
