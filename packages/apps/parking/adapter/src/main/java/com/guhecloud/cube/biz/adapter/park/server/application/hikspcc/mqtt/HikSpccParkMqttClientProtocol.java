package com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.mqtt;

import com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.model.HikSpccParkConfig;
import com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.service.HikSpccParkingService;
import com.guhecloud.cube.biz.adapter.park.server.factory.ParkMqttClientFactory;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Component
public class HikSpccParkMqttClientProtocol implements ParkMqttClientFactory {

    private final HikSpccParkingService hikParkingService;
    private final SubsystemRedisUtils subsystemRedisUtils;
    private Map<Integer, HikSpccParkMqttHolder> mqttHolderMap = new HashMap<>();

    /**
     * 获取停车场配置信息
     */
    private HikSpccParkConfig getParkingInfo() {
        SubsystemAdapterConfig config = getBasicConfig();
        return JsonUtils.deserialize(config.getManufacturerJson(), HikSpccParkConfig.class);
    }

    /**
     * 获取基础配置
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key =
                SubsystemTypeConstants.PARKING_SERVER + ":" + "hikspcc" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    @Override
    public Boolean connect(SubsystemAdapterConfig config) {
        try {
            Integer tenant = config.getTenant();
            HikSpccParkMqttHolder mqttHolder;
            if (MapUtils.isNotEmpty(mqttHolderMap) && mqttHolderMap.containsKey(tenant)) {
                mqttHolder = this.mqttHolderMap.get(tenant);
                mqttHolder.stop();
                this.mqttHolderMap.remove(tenant);
            }
            mqttHolder = new HikSpccParkMqttHolder(getParkingInfo(), (topic, mqttMessage) -> {
                try {
                    // 处理接收的消息体
                    if (!HikSpccParkMqttDataHandler.checkTopic(topic)) {
                        log.warn("海康监管综合管控平台 停车场无效主题[{}]", topic);
                        return;
                    }
                    String jsonStr = HikSpccParkMqttDataHandler.messageResolve(mqttMessage);
                    // 处理数据
                    hikParkingService.reportVehicleBottomMonitoringRecord(tenant, jsonStr);
                } catch (Throwable e) {
                    log.error(String.format("topic[%s]接收数据处理异常！！！", topic), e);
                }
            });
            this.mqttHolderMap.put(tenant, mqttHolder);
            mqttHolder.start();

            return true;
        } catch (Exception e) {
            log.error("mqtt服务连接失败！！！", e);
            return false;
        }
    }

    @Override
    public void disconnect() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        if (this.mqttHolderMap.containsKey(tenant)) {
            HikSpccParkMqttHolder mqttHolder = this.mqttHolderMap.get(tenant);
            mqttHolder.stop();
            this.mqttHolderMap.remove(tenant);
        }
    }
}
