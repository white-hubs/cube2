package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.BroadcastPlayRecordDeviceService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.aggregates.BroadcastPlayRecord;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastPlayRecordDevicePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastPlayRecordPO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject.DeviceInfo;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.mapper.BroadcastPlayRecordMapper;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastPlayRecordRepository {

    private final BroadcastPlayRecordMapper broadcastPlayRecordMapper;

    private final BroadcastPlayRecordDeviceService broadcastPlayRecordDeviceService;

    @Transactional(rollbackFor = Exception.class)
    public BroadcastPlayRecord save(BroadcastPlayRecord broadcastPlayRecord) {
        BroadcastPlayRecordPO broadcastPlayRecordPO = BroadcastPlayRecordPO.builder()
                .id(broadcastPlayRecord.getId())
                .broadcastTime(DateUtils.now())
                .broadcastTemplateId(broadcastPlayRecord.getBroadcastPlayText().getBroadcastTemplate().getId())
                .broadcastTemplateName(broadcastPlayRecord.getBroadcastPlayText().getBroadcastTemplate().getName())
                .broadcastTemplateContent(broadcastPlayRecord.getBroadcastPlayText().getBroadcastTemplate().getContent())
                .broadcastTemplateContentParam(JsonUtils.serialize(broadcastPlayRecord.getBroadcastPlayText()
                                                                           .getBroadcastPlayVariableParams()))
                .broadcastPlayContent(broadcastPlayRecord.getBroadcastPlayText().getPlayContent())
                .broadcastPlayNum(broadcastPlayRecord.getBroadcastPlayText().getBroadcastPlayNum())
                .broadcastPlayDevices(broadcastPlayRecord.getBroadcastPlayText().getDeviceInfos().stream().map(DeviceInfo::getName)
                                              .collect(Collectors.joining(",")))
                .broadcastPlayPlanId(broadcastPlayRecord.getBroadcastPlayPlanId())
                .operatorBy(broadcastPlayRecord.getOperatorBy())
                .operatorByName(broadcastPlayRecord.getOperatorByName())
                .operatorByDeptId(broadcastPlayRecord.getOperatorByDeptId())
                .operatorByDeptName(broadcastPlayRecord.getOperatorByDeptName())
                .broadcastPlayStatus(broadcastPlayRecord.getBroadcastPlayTextResponse().getBroadcastPlayStatus())
                .broadcastPlayErrorDesc(broadcastPlayRecord.getBroadcastPlayTextResponse().getBroadcastPlayErrorDesc())
                .build();
        this.broadcastPlayRecordMapper.insert(broadcastPlayRecordPO);
        //广播设备列表
        List<BroadcastPlayRecordDevicePO> broadcastPlayRecordDevicePOS = new ArrayList<>(broadcastPlayRecord.getBroadcastPlayText()
                                                                                                 .getDeviceInfos().size());
        broadcastPlayRecord.getBroadcastPlayText().getDeviceInfos().forEach(deviceInfo -> {
            BroadcastPlayRecordDevicePO broadcastPlayRecordDevicePO = new BroadcastPlayRecordDevicePO();
            broadcastPlayRecordDevicePO.setDeviceId(deviceInfo.getId());
            broadcastPlayRecordDevicePO.setDeviceCode(deviceInfo.getCode());
            broadcastPlayRecordDevicePO.setDeviceName(deviceInfo.getName());
            broadcastPlayRecordDevicePO.setBroadcastPlayRecordId(broadcastPlayRecord.getId());
            broadcastPlayRecordDevicePOS.add(broadcastPlayRecordDevicePO);
        });
        this.broadcastPlayRecordDeviceService.saveBatch(broadcastPlayRecordDevicePOS);
        return broadcastPlayRecord;
    }
}
