package com.guhecloud.cube.base.alarm.server.flow.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordOperatorLogPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordOperatorLogMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordOperatorLogQueryService {

    private final AlarmRecordOperatorLogMapper alarmRecordOperatorLogMapper;

    public List<AlarmRecordOperatorLogPO> listAlarmRecordOperatorLogByRecordId(@NonNull Long alarmRecordId) {
        return this.alarmRecordOperatorLogMapper.selectList(Wrappers.<AlarmRecordOperatorLogPO>lambdaQuery()
                                                                    .eq(AlarmRecordOperatorLogPO::getAlarmRecordId, alarmRecordId)
                                                                    .orderByDesc(AlarmRecordOperatorLogPO::getOperatorTime));
    }
}
