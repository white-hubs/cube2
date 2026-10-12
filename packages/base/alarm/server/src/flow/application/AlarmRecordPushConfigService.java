package com.guhecloud.cube.base.alarm.server.flow.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPushConfigPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordPushConfigMapper;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class AlarmRecordPushConfigService extends ServiceImpl<AlarmRecordPushConfigMapper, AlarmRecordPushConfigPO> {

    public AlarmRecordPushConfigPO getAlarmRecordPushConfigPO(Long alarmRecordId) {
        List<AlarmRecordPushConfigPO> alarmRecordPushConfigPOS = this.baseMapper.selectList(Wrappers.<AlarmRecordPushConfigPO>lambdaQuery()
                                                                                                    .eq(AlarmRecordPushConfigPO::getAlarmRecordId, alarmRecordId)
                                                                                           );
        if (CollectionUtils.isEmpty(alarmRecordPushConfigPOS)) {
            return null;
        }
        return alarmRecordPushConfigPOS.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByAlarmRecordId(Long alarmRecordId) {
        List<AlarmRecordPushConfigPO> alarmRecordPushConfigPOS = this.baseMapper.selectList(Wrappers.<AlarmRecordPushConfigPO>lambdaQuery()
                                                                                                    .eq(AlarmRecordPushConfigPO::getAlarmRecordId, alarmRecordId)
                                                                                           );
        if (!CollectionUtils.isEmpty(alarmRecordPushConfigPOS)) {
            DatabaseContextHolder.softDelete(this, alarmRecordPushConfigPOS);
        }
    }
}
