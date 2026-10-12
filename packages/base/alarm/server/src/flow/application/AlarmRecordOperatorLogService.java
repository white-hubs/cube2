package com.guhecloud.cube.base.alarm.server.flow.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmOperatorTypeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordOperatorLogPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordOperatorLogMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordOperatorLogService extends ServiceImpl<AlarmRecordOperatorLogMapper, AlarmRecordOperatorLogPO> {

    public Long save(@NonNull Long alarmRecordId, @NonNull AlarmOperatorTypeEnum operatorType, String operatorBy,
                     String operatorByName, @NonNull LocalDateTime operatorTime, @NonNull String operatorContent, String remark
                    ) {
        AlarmRecordOperatorLogPO alarmRecordOperatorLogPO = new AlarmRecordOperatorLogPO();
        alarmRecordOperatorLogPO.setAlarmRecordId(alarmRecordId);
        alarmRecordOperatorLogPO.setOperatorBy(operatorBy);
        alarmRecordOperatorLogPO.setOperatorByName(operatorByName);
        alarmRecordOperatorLogPO.setOperatorTime(operatorTime);
        alarmRecordOperatorLogPO.setOperatorType(operatorType);
        alarmRecordOperatorLogPO.setOperatorContent(operatorContent);
        alarmRecordOperatorLogPO.setRemark(remark);
        this.baseMapper.insert(alarmRecordOperatorLogPO);
        return alarmRecordOperatorLogPO.getId();
    }

    public List<AlarmRecordOperatorLogPO> listAlarmRecordOperatorLogByRecordId(@NonNull Long alarmRecordId) {
        return this.lambdaQuery().eq(AlarmRecordOperatorLogPO::getAlarmRecordId, alarmRecordId)
                .orderByDesc(AlarmRecordOperatorLogPO::getOperatorTime).list();
    }
}
