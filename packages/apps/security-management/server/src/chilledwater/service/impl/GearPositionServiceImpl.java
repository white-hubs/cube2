package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePropWriteCmd;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterAttrCtlStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.GearPositionMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.DevicePosition;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.GearPosition;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.DeviceAssociationPositionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.DevicePositionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.DevicePositionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.GearPositionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.OperateLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DevicePositionVo;
import com.guhecloud.cube.srv.gen.stall.client.GenStallClient;
import com.guhecloud.cube.srv.gen.stall.vo.GenStallVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GearPositionServiceImpl extends ServiceImpl<GearPositionMapper, GearPosition> implements GearPositionService {
    private final GearPositionMapper gearPositionMapper;

    private final DevicePositionService devicePositionService;

    private final DeviceCenterAttrCtlStdClient deviceCenterAttrCtlClient;

    private final GenStallClient genStallClient;

    private final OperateLogService operateLogService;

    @Override
    public Pagination<DevicePositionVo> devicePositions(DevicePositionParam pram) {
        Page<DevicePositionVo> devicePosition = gearPositionMapper.getDevicePosition(PagingUtils.convert(pram), pram.getKeyword());
        return PagingUtils.convert(devicePosition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deviceAssociationAssociationPositions(List<DeviceAssociationPositionParam> pram) {
        GenStallVo stallById = genStallClient.getByIdInner(pram.get(0).getGearPositionId());
        String areaName = "";
        String areaIds = "";
        if (CollectionUtils.isNotEmpty(stallById.getAreaIds())) {
            areaIds = stallById.getAreaIds().stream().collect(Collectors.joining("-"));
            areaName = stallById.getAreaNames().stream().collect(Collectors.joining("/"));
        }
        for (DeviceAssociationPositionParam devicePosition : pram) {
            DevicePosition one = devicePositionService.getOne(Wrappers.<DevicePosition>lambdaQuery()
                                                                      .eq(DevicePosition::getColdWaterDevice, devicePosition.getColdWaterDevice())
                                                                      .eq(DevicePosition::getGearPositionId, devicePosition.getGearPositionId()));
            if (Objects.nonNull(one)) {
                controlDevice(devicePosition.getColdWaterDevice(), devicePosition.getEmplaceTemperature());
                one.setGearPositionArea(areaName);
                one.setGearPositionAreaId(areaIds);
                one.setGearPositionCode(stallById.getCode());
                one.setGearPositionName(stallById.getName());
                one.setUnitPrice(devicePosition.getUnitPrice());
                one.setUnitTraffic(devicePosition.getUnitTraffic());
                one.setEmplaceTemperature(devicePosition.getEmplaceTemperature());
                devicePositionService.update(one, Wrappers.<DevicePosition>lambdaUpdate()
                        .eq(DevicePosition::getColdWaterDevice, devicePosition.getColdWaterDevice())
                        .eq(DevicePosition::getGearPositionId, devicePosition.getGearPositionId()));
            } else {
                DevicePosition copying = ObjectUtils.copying(devicePosition, DevicePosition.class);
                controlDevice(devicePosition.getColdWaterDevice(), devicePosition.getEmplaceTemperature());
                copying.setGearPositionArea(areaName);
                copying.setGearPositionAreaId(areaIds);
                copying.setGearPositionCode(stallById.getCode());
                copying.setGearPositionName(stallById.getName());
                devicePositionService.save(copying);
            }

        }

    }

    @Override
    public void cancellationDevice(Long gearPositionId, Long deviceId) {
        List<DevicePosition> list = devicePositionService.list(Wrappers.<DevicePosition>lambdaQuery()
                                                                       .eq(DevicePosition::getGearPositionId, gearPositionId)
                                                                       .eq(DevicePosition::getColdWaterDevice, deviceId));
        DatabaseContextHolder.softDelete(DevicePosition.class, list);
        operateLogService.saveBusinessLog("取消设备关联");
    }

    @Override
    public List<Long> getGearPositionIds() {
        List<GearPosition> gearPositions = gearPositionMapper.selectList(null);
        List<Long> collect = gearPositions.stream().map(GearPosition::getGearPositionId).distinct().collect(Collectors.toList());
        return collect;
    }

    private void controlDevice(Long deviceId, String emplaceTemperature) {
        Set<DevicePropWriteCmd.PropWriteValue> keyValues = new HashSet<>();
        DevicePropWriteCmd controlAttributeCommand = new DevicePropWriteCmd();
        DevicePropWriteCmd.PropWriteValue keyValue = new DevicePropWriteCmd.PropWriteValue();
        keyValue.setCode("ti_bubbles_lower");
        keyValue.setExpectedValue(emplaceTemperature);
        keyValues.add(keyValue);
        controlAttributeCommand.setPropWriteValues(keyValues);
        controlAttributeCommand.setDeviceId(deviceId);
        try {
            deviceCenterAttrCtlClient.controlAttribute(controlAttributeCommand);
            operateLogService.saveBusinessLog("修改设备配置钛泡低温保护设置：" + emplaceTemperature);
        } catch (Exception e) {
            throw new ResourceNotFoundException("该类型设备无冷水设备属性");
        }
    }

}
