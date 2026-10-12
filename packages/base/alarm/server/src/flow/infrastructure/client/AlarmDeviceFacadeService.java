package com.guhecloud.cube.base.alarm.server.flow.infrastructure.client;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmSnapshot;
import com.guhecloud.cube.iot.north.bridge.client.IotCommandClient;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.IotProductMethodClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.CommandResponseDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.MethodInvokeRequestDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.CommandResetEnum;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.CommandRestParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class AlarmDeviceFacadeService {

    private final IotDeviceClient iotDeviceClient;

    private final IotCommandClient iotCommandClient;

    private final IotProductMethodClient iotProductMethodClient;

    private final static String ALARM_SNAPSHOT_ATT_DEVICE_ID = "deviceId";
    private final static String ALARM_SNAPSHOT_ATT_EVENT_CODE = "eventCode";
    private final static String DEVICE_MUTE_METHOD_CODE = "mute";

    public AlarmDeviceFacadeService(IotDeviceClient iotDeviceClient, IotCommandClient iotCommandClient,
                                    IotProductMethodClient iotProductMethodClient
                                   ) {
        this.iotDeviceClient = iotDeviceClient;
        this.iotCommandClient = iotCommandClient;
        this.iotProductMethodClient = iotProductMethodClient;
    }

    public void sendDeviceStatusReset(AlarmRecord alarmRecord) {
        if (Objects.isNull(alarmRecord)) {
            log.error("alarmRecord is null");
            return;
        }
        AlarmSnapshot alarmSnapshot = alarmRecord.getAlarmSnapshot();
        if (Objects.isNull(alarmSnapshot)) {
            log.error("alarmSnapshot is null");
            return;
        }
        String alarmSnapshotStr = alarmSnapshot.getAlarmSnapshot();
        try {
            Map<String, Object> alarmSnapshotMap = JsonUtils.deserialize(alarmSnapshotStr, new TypeReference<Map<String, Object>>() {});
            if (Objects.isNull(alarmSnapshotMap)) {
                log.error("alarmSnapshotMap is null");
                return;
            }
            Object deviceId = alarmSnapshotMap.get(ALARM_SNAPSHOT_ATT_DEVICE_ID);
            Object eventCode = alarmSnapshotMap.get(ALARM_SNAPSHOT_ATT_EVENT_CODE);
            if (Objects.isNull(deviceId) || Objects.isNull(eventCode)) {
                log.error("deviceId or eventCode is null");
                return;
            }
            //根据报警事件信息发送设备状态重置
            CommandRestParam commandRestParam = new CommandRestParam();
            commandRestParam.setDeviceId(deviceId.toString());
            commandRestParam.setCommandSeq(String.valueOf(IdWorker.getId()));
            commandRestParam.setCommandResetEnum(CommandResetEnum.RESET_DEVICE_EVENT);
            commandRestParam.setResetTarget(eventCode.toString());
            CommandResponseDTO commandResponseDTO = iotCommandClient.projectCommandResetIssue(commandRestParam);
            log.info("设备状态重置结果:{}", commandResponseDTO);
        } catch (Exception e) {
            log.error("设备状态重置失败,错误原因：{}", e.getMessage());
        }
    }

    public void commandDeviceMuteStatus(AlarmRecord alarmRecord) {
        try {
            var param = new DeviceDetailParam();
            param.setWithPropertySpec(true);
            param.setDeviceId(alarmRecord.getAlarmTarget().getAlarmTargetKey().toString());
            var deviceDetail = iotDeviceClient.detail(param);
            if (Objects.isNull(deviceDetail)) {
                log.error("iot deviceDetail is null id:{}", alarmRecord.getAlarmTarget().getAlarmTargetKey().toString());
                return;
            }
            var productMethods = iotProductMethodClient.listProductMethods(Long.valueOf(deviceDetail.getProductId()));
            if (CollectionUtils.isEmpty(productMethods)) {
                log.error("iot productMethods is empty deviceId:{} prodId:{}", param.getDeviceId(), deviceDetail.getProductId());
                return;
            }
            if (productMethods.stream().noneMatch(method -> DEVICE_MUTE_METHOD_CODE.equals(method.getMethodCode()))) {
                log.error("iot productMethods not contain mute method deviceId:{} prodId:{}", param.getDeviceId(), deviceDetail.getProductId());
                return;
            }
            var requestDTO = new MethodInvokeRequestDTO();
            requestDTO.setDeviceId(param.getDeviceId());
            requestDTO.setMethodCode(DEVICE_MUTE_METHOD_CODE);
            iotProductMethodClient.invokeMethod(requestDTO);
        } catch (Exception e) {
            log.error("设备消音属性写值失败,错误原因：{}", e.getMessage());
        }
    }
}
