package com.guhecloud.cube.base.alarm.server.flow.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmLevelPlayConfigPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmLevelPlayConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlarmLevelPlayConfigService extends ServiceImpl<AlarmLevelPlayConfigMapper, AlarmLevelPlayConfigPO> {
}
