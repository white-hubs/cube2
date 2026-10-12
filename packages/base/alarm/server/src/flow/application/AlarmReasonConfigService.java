package com.guhecloud.cube.base.alarm.server.flow.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmReasonConfigPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmReasonConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlarmReasonConfigService extends ServiceImpl<AlarmReasonConfigMapper, AlarmReasonConfigPO> {
}
