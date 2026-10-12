package com.guhecloud.cube.base.alarm.server.flow.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordHandlerReasonPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordHandlerReasonMapper;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordHandlerReasonVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AlarmRecordHandlerReasonService extends ServiceImpl<AlarmRecordHandlerReasonMapper, AlarmRecordHandlerReasonPO> {

    public Optional<AlarmRecordHandlerReasonVO> getAlarmRecordHandlerReason(@NonNull Long alarmRecordId) {
        List<AlarmRecordHandlerReasonPO> alarmRecordHandlerReasonPOS = this.baseMapper.selectList(Wrappers
                                                                                                          .<AlarmRecordHandlerReasonPO>lambdaQuery()
                                                                                                          .eq(AlarmRecordHandlerReasonPO::getAlarmRecordId, alarmRecordId)
                                                                                                 );
        if (!CollectionUtils.isNotEmpty(alarmRecordHandlerReasonPOS)) {
            return Optional.empty();
        }
        return Optional.of(ObjectUtils.copying(alarmRecordHandlerReasonPOS.get(0), AlarmRecordHandlerReasonVO.class));
    }
}
