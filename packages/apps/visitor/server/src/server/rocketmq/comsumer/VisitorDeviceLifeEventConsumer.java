package com.guhecloud.cube.srv.gen.visitor.management.server.rocketmq.comsumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.alarm.client.AlarmRecordClient;
import com.guhecloud.cube.base.alarm.client.param.SettingAlarmBusinessSnapshotParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.BlacklistMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.BlacklistPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.rocketmq.message.DeviceLifeEventMessage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 设备生命事件监听
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = DeviceLifeEventMessage.TOPIC + "_" + "visitor",
                         topic = DeviceLifeEventMessage.TOPIC
)
public class VisitorDeviceLifeEventConsumer implements RocketMQListener<DeviceLifeEventMessage> {
    /**
     * 访客服务code
     */
    private static final String APPLICATION_CODE = "VISITOR_MANAGEMENT";
    /**
     * 访客黑名单报警code
     */
    private static final String PERSONNEL_ALARM = "personnelAlarm";

    private final BlacklistMapper blacklistMapper;
    private final AlarmRecordClient alarmRecordClient;

    @Override
    public void onMessage(DeviceLifeEventMessage message) {
        if (APPLICATION_CODE.equals(message.getApplicationCode())) {
            String alarmSnapshot = message.getAlarmSnapshot();
            if (StringUtils.isBlank(alarmSnapshot)) {
                return;
            }
            EventTriggerDTO triggerDTO = JSON.parseObject(alarmSnapshot, EventTriggerDTO.class);
            if (PERSONNEL_ALARM.equals(triggerDTO.getEventCode())) {
                Map<String, Object> relatedProp = triggerDTO.getRelatedProp();
                Object intellindustObj = relatedProp.get("intellindust");
                if (intellindustObj == null) {
                    log.error("访客黑名单报警无英特灵达信息！");
                    return;
                }
                JSONObject intellindust = (JSONObject) intellindustObj;
                Object personIdObj = intellindust.get("personCode");
                if (personIdObj == null) {
                    log.error("访客黑名单报警无人员code！");
                    return;
                }
                Integer personCode = Integer.parseInt(String.valueOf(personIdObj));
                BlacklistPO blacklistPO = blacklistMapper.selectOne(Wrappers.<BlacklistPO>lambdaQuery()
                                                                            .eq(BlacklistPO::getThirdUserCode, personCode)
                                                                            .last(" limit 1 ")
                                                                   );
                if (blacklistPO == null) {
                    log.error("访客黑名单报警未查询到黑名单人员！" + personCode);
                    return;
                }
                SettingAlarmBusinessSnapshotParam param = new SettingAlarmBusinessSnapshotParam();
                param.setId(message.getEventId());
                param.setBusinessSnapshot(JSON.toJSONString(blacklistPO));
                alarmRecordClient.setAlarmBusinessSnapshot(param);
            }
        }
    }

    @Data
    static class EventTriggerDTO {
        private String deviceId;
        private String eventCode;
        private String instanceId;
        private String timestamp;
        private String delayEventTime;
        private Map<String, Object> relatedProp;
        private String notifyStatus;
        private String notifyStatusTs;
    }
}
