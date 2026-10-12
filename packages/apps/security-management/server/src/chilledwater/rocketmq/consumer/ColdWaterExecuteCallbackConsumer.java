package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.rocketmq.consumer;

import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.KeyValueWithTs;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.ClodWaterAttributeMap;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.OperateLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.enumeration.LogTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ClodWaterAttributeMapService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.OperateLogService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ColdWaterExecuteCallbackConsumer {
    @Resource
    private ClodWaterAttributeMapService clodWaterAttributeMapService;
    @Resource
    private OperateLogService operateLogService;

    @Resource
    private SubscribeContainer subscribeContainer;

    @EventListener
    public void onSystemReadyEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.debug("系统准备就绪，订阅设备属性变化");
        List<ClodWaterAttributeMap> list = clodWaterAttributeMapService.list();
        if (CollectionUtils.isNotEmpty(list)) {
            SessionContextHolder.requestTenant(list.getFirst().getTenant());
            Map<String, ClodWaterAttributeMap> collect = list.stream()
                    .collect(Collectors.toMap(ClodWaterAttributeMap::getAttributeCode, s -> s));

            //同步设备状态
            subscribeContainer.registerDevicePropMsgHandler(msg -> {
                log.debug("设备属性变化:====={}", msg);
                if (Objects.isNull(msg)) {
                    return;
                }

                List<KeyValueWithTs> updates = msg.getUpdates();
                if (Objects.isNull(updates) || updates.isEmpty()) {
                    return;
                }

                String iotDeviceId = msg.getDeviceId();
                updates.forEach(s -> {
                    ClodWaterAttributeMap clodWaterAttributeMap = collect.get(s.getKey());
                    if (Objects.nonNull(clodWaterAttributeMap)) {
                        SessionContextHolder.requestTenant(clodWaterAttributeMap.getTenant());
                        OperateLog operateLog = new OperateLog();
                        operateLog.setDeviceId(Long.valueOf(iotDeviceId));
                        operateLog.setLogType(LogTypeEnum.HARDWARE_LOG.name());
                        LocalDateTime date = DateUtils.parse(s.getTs());
                        operateLog.setLogTime(date);
                        String remark = "";
                        if (clodWaterAttributeMap.getValueType().equals("1")) {
                            remark = clodWaterAttributeMap.getTranslateName() + s.getPreValue() + "调成" + s.getValue();
                        } else if (clodWaterAttributeMap.getValueType().equals("0")) {
                            String[] split = clodWaterAttributeMap.getTranslateName().split(",");
                            for (String sp : split) {
                                String[] split1 = sp.split("-");
                                if (split1[0].equals(s.getValue())) {
                                    remark = split1[1];
                                }
                            }
                        }
                        operateLog.setRemark(remark);
                        operateLogService.save(operateLog);
                        log.debug("设备操作记录成功");
                    }

                });

            });
        }

    }
}
