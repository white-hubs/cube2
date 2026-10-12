package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.mq.listener;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.entity.PersonRollCallEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.mq.message.PersonRollCallInfoMessage;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.service.PersonRollCallService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 人员点到明细
 *
 * @author xiaofeng
 * @date 2024/11/05 22:22
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "person_roll_call_record", topic = PersonRollCallInfoMessage.TOPIC)
public class PersonRollCallInfoListener implements RocketMQListener<PersonRollCallInfoMessage> {

    private final PersonRollCallService personRollCallService;

    @Override
    public void onMessage(PersonRollCallInfoMessage message) {
        SessionContextHolder.requestTenant(message.getPersonRollCallRecords().getFirst().getTenant());
        List<PersonRollCallEntity> personRollCallEntities = ObjectUtils.copying(message.getPersonRollCallRecords(), PersonRollCallEntity.class);
        personRollCallEntities.forEach(personRollCallEntity -> {
            personRollCallEntity.setId(IdWorker.getId());
            personRollCallEntity.setThirdRecordId(personRollCallEntity.getRecordCode());
            personRollCallEntity.setDeleted(Boolean.FALSE);
            EntityAuditor.populateInnerAudit(personRollCallEntity);
        });
        personRollCallService.insertOrUpdateMeetingApplyPerson(personRollCallEntities);
    }
}

