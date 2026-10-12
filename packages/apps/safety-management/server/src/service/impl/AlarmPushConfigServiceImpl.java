package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.AlarmPushConfigMapper;
import com.guhecloud.cube.srv.gen.safety.management.po.AlarmPushConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.AlarmPushConfigService;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AlarmPushConfigServiceImpl extends ServiceImpl<AlarmPushConfigMapper, AlarmPushConfigPO> implements AlarmPushConfigService {

    @Override
    public List<AlarmPushConfigPO> getAlarmPushConfigPOS(@NonNull String alarmTypeCode) {
        return this.lambdaQuery().eq(AlarmPushConfigPO::getAlarmTypeCode, alarmTypeCode)
                .list();
    }

    @Override
    public List<AlarmPushConfigPO> getAlarmTimeoutPushConfig() {
        return this.lambdaQuery().eq(AlarmPushConfigPO::getTimeout, Boolean.TRUE).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull String alarmTypeCode, @NonNull String alarmLevelCode, Boolean timeout) {
        this.remove(alarmTypeCode, alarmLevelCode);
        AlarmPushConfigPO alarmPushConfigPO = AlarmPushConfigPO.builder()
                .alarmLevelCode(alarmLevelCode)
                .alarmTypeCode(alarmTypeCode)
                .timeout(timeout)
                .build();
        this.baseMapper.insert(alarmPushConfigPO);
        return alarmPushConfigPO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull String alarmTypeCode, @NonNull String alarmLevelCode) {
        AlarmPushConfigPO alarmPushConfigPO = this.baseMapper.selectOne(Wrappers.<AlarmPushConfigPO>lambdaQuery()
                                                                                .eq(AlarmPushConfigPO::getAlarmLevelCode, alarmLevelCode)
                                                                                .eq(AlarmPushConfigPO::getAlarmTypeCode, alarmTypeCode));
        if (Objects.nonNull(alarmPushConfigPO)) {
            DatabaseContextHolder.softDelete(this, alarmPushConfigPO);
        }
    }
}
