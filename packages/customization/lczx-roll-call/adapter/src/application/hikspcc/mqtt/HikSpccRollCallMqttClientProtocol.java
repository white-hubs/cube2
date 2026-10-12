package com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.mqtt;

import com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.model.HikSpccRollCallConfig;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.vo.PersonRollCallInfoMsg;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.factory.RollCallMqttClientFactory;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.rocketmq.message.PersonRollCallInfoMessage;
import com.guhecloud.cube.biz.adapter.person.rollcall.server.rocketmq.sender.PersonRollCallInfoSender;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class HikSpccRollCallMqttClientProtocol implements RollCallMqttClientFactory {

    private final SubsystemRedisUtils subsystemRedisUtils;
    private final PersonRollCallInfoSender sender;
    private final Map<Integer, HikSpccRollMqttHolder> mqttHolderMap = new HashMap<>();

    /**
     * 获取停车场配置信息
     */
    private HikSpccRollCallConfig getRollCallConfig() {
        SubsystemAdapterConfig config = getBasicConfig();
        return JsonUtils.deserialize(config.getManufacturerJson(), HikSpccRollCallConfig.class);
    }

    /**
     * 获取基础配置
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key =
                SubsystemTypeConstants.PERSON_ROLLCALL_SERVER + ":" + "hikspcc" + ":" +
                SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    @Override
    public Boolean connect(SubsystemAdapterConfig config) {
        try {
            Integer tenant = config.getTenant();
            HikSpccRollMqttHolder mqttHolder;
            if (!MapUtils.isEmpty(mqttHolderMap) && mqttHolderMap.containsKey(tenant)) {
                mqttHolder = this.mqttHolderMap.get(tenant);
                mqttHolder.stop();
                mqttHolder.resetMqttHolder(getRollCallConfig());
                this.mqttHolderMap.remove(tenant);
            }
            mqttHolder = new HikSpccRollMqttHolder(getRollCallConfig(), (topic, message) -> {
                try {
                    // 处理接收的消息体
                    if (!HikSpccRollCallMqttDataHandler.checkTopic(topic)) {
                        log.warn("海康监管综合管控平台 人员清点无效主题[{}]", topic);
                        return;
                    }
                    String jsonStr = HikSpccRollCallMqttDataHandler.messageResolve(message);
                    // 处理数据
                    PersonRollCallInfoMsg process = JsonUtils.deserialize(jsonStr, PersonRollCallInfoMsg.class);
                    if (ObjectUtils.isNotEmpty(process)
                        && ObjectUtils.isNotEmpty(process.getParams())
                        && !process.getParams().getEvents().isEmpty()) {
                        List<PersonRollCallInfoMsg.RollCallEvent> events = process.getParams().getEvents();
                        sender.sendPersonRollCallInfoMessages(tenant, convertToPersonRollCallInfoMessage(events, tenant));
                    }
                } catch (Throwable e) {
                    log.error(String.format("topic[%s]接收数据处理异常！！！", topic), e);
                }
            });
            this.mqttHolderMap.put(tenant, mqttHolder);
            mqttHolder.start();
            return true;
        } catch (Exception e) {
            log.error("海康监管综合管控平台人员清点mqtt服务连接失败！！！", e);
            return false;
        }
    }

    @Override
    public void disconnect() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        if (this.mqttHolderMap.containsKey(tenant)) {
            HikSpccRollMqttHolder mqttHolder = this.mqttHolderMap.get(tenant);
            mqttHolder.stop();
            this.mqttHolderMap.remove(tenant);
        }
    }

    private List<PersonRollCallInfoMessage.PersonRollCallRecord> convertToPersonRollCallInfoMessage(
            List<PersonRollCallInfoMsg.RollCallEvent> events,
            Integer tenant
                                                                                                   ) {
        List<PersonRollCallInfoMessage.PersonRollCallRecord> messages = new ArrayList<>();
        for (PersonRollCallInfoMsg.RollCallEvent event : events) {
            if (event.getData() != null && event.getData().getText() != null) {
                for (PersonRollCallInfoMsg.RollCallText text : event.getData().getText()) {
                    PersonRollCallInfoMessage.PersonRollCallRecord messageObj = new PersonRollCallInfoMessage.PersonRollCallRecord();
                    messageObj.setTenant(tenant);
                    messageObj.setRecordCode(text.getRecordCode());
                    messageObj.setPersonName(text.getName());
                    messageObj.setIndexCode(text.getIndexCode());
                    messageObj.setFacePhoto(text.getFacePhoto());
                    messageObj.setCapturePhoto(text.getCapturePhoto());
                    messageObj.setDeviceCode(text.getChannelCode());
                    messageObj.setDeviceName(text.getChannelName());
                    messageObj.setTaskType(text.getTaskType());
                    messageObj.setSimilarity(text.getSimilarity());
                    messageObj.setStatus(text.getStatus());
                    messageObj.setRemark(text.getRemark());
                    messageObj.setRollcallTime(convertToCallStartTime(text.getRollcallTime()));
                    messageObj.setAreaIndexCode(text.getAreaIndexCode());
                    messageObj.setDepartmentIndexCode(text.getDepartmentIndexCode());
                    messageObj.setDepartmentName(text.getDepartmentName());
                    messageObj.setAreaName(text.getAreaName());
                    messages.add(messageObj);
                }
            }
        }
        return messages;
    }

    private LocalDateTime convertToCallStartTime(String time) {
        if (time != null) {
            try {
                return DateUtils.parse(time);
            } catch (Exception e) {
                log.error("string_format_date error", e);
            }
        }
        return null;
    }
}
