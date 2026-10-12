package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.ScenePointDeviceBaseVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectScenePointDeviceService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectScenePointDeviceMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectScenePointDevicePO;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 现场点关联设备
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectScenePointDeviceServiceImpl extends ServiceImpl<InspectScenePointDeviceMapper, InspectScenePointDevicePO>
        implements InspectScenePointDeviceService {
    private final DeviceCenterStdClient deviceCenterStdClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceScenePointDevices(Long scenePointId, List<Long> deviceIdList) {
        if (scenePointId == null) {
            return;
        }
        this.remove(Wrappers.<InspectScenePointDevicePO>lambdaQuery().eq(InspectScenePointDevicePO::getScenePointId, scenePointId));
        if (CollectionUtils.isEmpty(deviceIdList)) {
            return;
        }
        List<Long> distinctIds = deviceIdList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (distinctIds.isEmpty()) {
            return;
        }
        List<InspectScenePointDevicePO> batch = new ArrayList<>(distinctIds.size());
        for (Long deviceId : distinctIds) {
            InspectScenePointDevicePO row = new InspectScenePointDevicePO();
            row.setId(IdWorker.getId());
            row.setScenePointId(scenePointId);
            row.setDeviceId(deviceId);
            batch.add(row);
        }
        this.saveBatch(batch);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByScenePointIds(Collection<Long> scenePointIds) {
        if (CollectionUtils.isEmpty(scenePointIds)) {
            return;
        }
        this.remove(Wrappers.<InspectScenePointDevicePO>lambdaQuery().in(InspectScenePointDevicePO::getScenePointId, scenePointIds));
    }

    @Override
    public Map<Long, List<Long>> mapDeviceIdsByScenePointIds(Collection<Long> scenePointIds) {
        if (CollectionUtils.isEmpty(scenePointIds)) {
            return Collections.emptyMap();
        }
        List<InspectScenePointDevicePO> rows = this.lambdaQuery()
                .select(InspectScenePointDevicePO::getScenePointId, InspectScenePointDevicePO::getDeviceId)
                .in(InspectScenePointDevicePO::getScenePointId, scenePointIds)
                .list();
        if (CollectionUtils.isEmpty(rows)) {
            return Collections.emptyMap();
        }
        Map<Long, List<Long>> map = new HashMap<>();
        for (InspectScenePointDevicePO row : rows) {
            map.computeIfAbsent(row.getScenePointId(), k -> new ArrayList<>()).add(row.getDeviceId());
        }
        return map;
    }

    @Override
    public Map<Long, List<ScenePointDeviceBaseVO>> mapDevicesByScenePointIds(Collection<Long> scenePointIds) {
        if (CollectionUtils.isEmpty(scenePointIds)) {
            return Collections.emptyMap();
        }
        List<InspectScenePointDevicePO> rows = this.lambdaQuery()
                .select(InspectScenePointDevicePO::getScenePointId, InspectScenePointDevicePO::getDeviceId)
                .in(InspectScenePointDevicePO::getScenePointId, scenePointIds)
                .list();
        if (CollectionUtils.isEmpty(rows)) {
            return Collections.emptyMap();
        }
        DevicePostByIdSetParam param = new DevicePostByIdSetParam();
        param.setDeviceIdSet(rows.stream()
                                  .map(InspectScenePointDevicePO::getDeviceId)
                                  .filter(Objects::nonNull)
                                  .collect(Collectors.toSet()));
        List<DeviceBaseArchivesInfoDTO> devices = deviceCenterStdClient.postListByDeviceId(param);
        Map<Long, String> deviceNameMap = Collections.emptyMap();
        if (CollectionUtils.isNotEmpty(devices)) {
            deviceNameMap = devices.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(DeviceBaseArchivesInfoDTO::getId,
                            DeviceBaseArchivesInfoDTO::getName, (oldVal, newVal) -> newVal));
        }
        Map<Long, List<ScenePointDeviceBaseVO>> map = new HashMap<>();
        for (InspectScenePointDevicePO row : rows) {
            if (row.getDeviceId() == null) {
                continue;
            }
            ScenePointDeviceBaseVO baseVO = new ScenePointDeviceBaseVO(row.getDeviceId(), deviceNameMap.get(row.getDeviceId()));
            map.computeIfAbsent(row.getScenePointId(), k -> new ArrayList<>()).add(baseVO);
        }
        return map;
    }
}
