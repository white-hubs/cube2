package com.guhecloud.cube.base.alarm.server.flow.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStateEventEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmStateEventOperatePO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmStateEventOperateMapper;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmStateEventOperateService extends ServiceImpl<AlarmStateEventOperateMapper, AlarmStateEventOperatePO> {

    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull Long alarmRecordId, @NonNull AlarmStatusEnum status, @NonNull AlarmStateEventEnum event,
                     Object param
                    ) {
        AlarmStateEventOperatePO alarmStateEventOperatePO = AlarmStateEventOperatePO.builder()
                .alarmRecordId(alarmRecordId)
                .alarmStatus(status)
                .alarmStatusEvent(event)
                .operationParam(Objects.nonNull(param)
                                ? JsonUtils.serialize(param)
                                : null)
                .build();
        this.baseMapper.insert(alarmStateEventOperatePO);
        return alarmStateEventOperatePO.getId();
    }

    public List<AlarmStateEventOperatePO> listByAlarmRecordId(@NonNull Long alarmRecordId) {
        return this.lambdaQuery()
                .eq(AlarmStateEventOperatePO::getAlarmRecordId, alarmRecordId)
                .list();
    }
}
