package com.guhecloud.cube.base.alarm.server.flow.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordTargetPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmNotifyUser;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordTargetMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordTargetService extends ServiceImpl<AlarmRecordTargetMapper, AlarmRecordTargetPO> {

    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(@NonNull Long alarmRecordId, @NonNull LocalDateTime sendTime, @NonNull List<AlarmNotifyUser> alarmNotifyUsers) {
        List<AlarmRecordTargetPO> alarmRecordTargetPOS = new ArrayList<>();
        alarmNotifyUsers.stream().forEach(param -> {
            AlarmRecordTargetPO alarmRecordTargetPO = new AlarmRecordTargetPO();
            alarmRecordTargetPO.setUserId(param.getId());
            alarmRecordTargetPO.setUserName(param.getUserName());
            alarmRecordTargetPO.setAlarmRecordId(alarmRecordId);
            alarmRecordTargetPO.setSendTime(sendTime);
            alarmRecordTargetPOS.add(alarmRecordTargetPO);
        });
        this.saveBatch(alarmRecordTargetPOS);
    }

    public List<AlarmRecordTargetPO> listByAlarmRecordId(@NonNull Long alarmRecordId) {
        return this.lambdaQuery().eq(AlarmRecordTargetPO::getAlarmRecordId, alarmRecordId)
                .orderByDesc(AlarmRecordTargetPO::getSendTime).list();
    }
}
