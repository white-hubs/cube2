package com.guhecloud.cube.biz.adapter.person.rollcall.server.rocketmq.sender;

import com.guhecloud.cube.biz.adapter.person.rollcall.server.rocketmq.message.PersonRollCallInfoMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonRollCallInfoSender {

    public static final String TOPIC = "PERSON_ROLL_CALL_RECORD";

    /**
     * 发送人员点到信息消息
     *
     * @param messages 要发送的消息列表
     */
    public void sendPersonRollCallInfoMessages(Integer tenant, List<PersonRollCallInfoMessage.PersonRollCallRecord> messages) {
        SessionContextHolder.requestTenant(tenant);
        PersonRollCallInfoMessage message = new PersonRollCallInfoMessage();
        message.setPersonRollCallRecords(messages);
        RocketMQContextHolder.send(TOPIC, message);
        // 打印发送结果
        log.info("Message sent to topic [{}]", TOPIC);
    }
}
