package com.guhecloud.cube.base.alarm.server.flow.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordFollowUpPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordFollowUpMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlarmRecordFollowUpService extends ServiceImpl<AlarmRecordFollowUpMapper, AlarmRecordFollowUpPO> {
}
