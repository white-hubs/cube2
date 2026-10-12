package com.guhecloud.cube.srv.gen.disinfection.server.member.listener;

import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.cube.iot.north.bridge.consumer.util.CallbackUtil;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberLog;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.MemberLogTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupDeviceService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberLogService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationStartedListener implements ApplicationListener<ApplicationReadyEvent> {
    private final SubscribeContainer subscribeContainer;
    private final DisinfectionMemberLogService memberLogService;
    private final IotDeviceClient deviceClient;
    private final DisinfectionMemberGroupDeviceService memberGroupDeviceService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Spring 已经启动完成");
        subscribeContainer.registerDeviceStatusMsgHandler(msg -> {
            if (Objects.isNull(msg)) {
                return;
            }
            var iotDeviceId = Long.parseLong(msg.getDeviceId());
            //查询设备关联的租户
            var tenants = memberGroupDeviceService.getTenantsByDeviceId(iotDeviceId);
            if (CollectionUtils.isEmpty(tenants)) {
                return;
            }
            var deviceParam = new DeviceDetailParam();
            deviceParam.setDeviceId(String.valueOf(iotDeviceId));
            var device = deviceClient.detail(deviceParam);
            var communicateStateVal = CallbackUtil.communicateIsNormal(msg) ? 1 : 0;
            tenants.forEach(tenant -> {
                SessionContextHolder.requestTenant(tenant);
                try {
                    //查询设备上次更新记录
                    var lastRecord = memberLogService.lambdaQuery().eq(MemberLog::getDeviceId, iotDeviceId)
                            .orderBy(true, false, MemberLog::getCreateTime).last("limit 1").one();
                    var statusDesc = communicateStateVal == 1 ? "上线" : "离线";
                    if (Objects.isNull(lastRecord)) {
                        saveLog(iotDeviceId, statusDesc, device);
                        return;
                    } else {
                        //如果不为空则判断设备状态是否一致
                        var detail = lastRecord.getDetail();
                        if (detail.equals(statusDesc)) {
                            //如果一致则不处理
                            return;
                        }
                    }
                    //如果不一致则插入最近设备状态记录
                    saveLog(iotDeviceId, statusDesc, device);
                } catch (Exception e) {
                    log.error("查询设备详情失败", e);
                } finally {
                    SessionContextHolder.clearSessionContext();
                }
            });

        });
    }

    private void saveLog(long iotDeviceId, String statusDesc, DeviceDetailDTO device) {
        //如果为空则记录设备状态
        var memberLog = MemberLog.builder()
                .deviceId(iotDeviceId)
                .type(MemberLogTypeEnum.DEVICE_ALARM_LOG)
                .detail(statusDesc)
                .title(Objects.nonNull(device) ? device.getDeviceName() : "设备")
                .build();
        memberLogService.save(memberLog);
    }
}
