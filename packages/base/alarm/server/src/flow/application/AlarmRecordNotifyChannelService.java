package com.guhecloud.cube.base.alarm.server.flow.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordNotifyChannelPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordNotifyChannelMapper;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class AlarmRecordNotifyChannelService extends ServiceImpl<AlarmRecordNotifyChannelMapper, AlarmRecordNotifyChannelPO> {

    public AlarmRecordNotifyChannelPO getByAlarmRecordId(Long alarmRecordId) {
        List<AlarmRecordNotifyChannelPO> alarmRecordNotifyChannelPOS = this.baseMapper
                .selectList(Wrappers
                                    .<AlarmRecordNotifyChannelPO>lambdaQuery()
                                    .eq(AlarmRecordNotifyChannelPO::getAlarmRecordId, alarmRecordId)
                           );
        if (CollectionUtils.isEmpty(alarmRecordNotifyChannelPOS)) {
            return null;
        }
        return alarmRecordNotifyChannelPOS.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByAlarmRecordId(Long alarmRecordId) {
        List<AlarmRecordNotifyChannelPO> alarmRecordNotifyChannelPOS = this.baseMapper
                .selectList(Wrappers
                                    .<AlarmRecordNotifyChannelPO>lambdaQuery()
                                    .eq(AlarmRecordNotifyChannelPO::getAlarmRecordId, alarmRecordId)
                           );
        if (CollectionUtils.isEmpty(alarmRecordNotifyChannelPOS)) {
            DatabaseContextHolder.softDelete(this, alarmRecordNotifyChannelPOS);
        }
    }
}
