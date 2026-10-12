package com.guhecloud.cube.base.alarm.server.flow.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordHandlerUserPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordHandlerUserMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordHandlerUserService extends ServiceImpl<AlarmRecordHandlerUserMapper, AlarmRecordHandlerUserPO> {

    @Transactional(rollbackFor = Exception.class)
    public void removeByAlarmRecordId(@NonNull Long alarmRecordId, Set<Long> excludeUserIds) {
        this.baseMapper.delete(Wrappers.<AlarmRecordHandlerUserPO>lambdaQuery()
                                       .eq(AlarmRecordHandlerUserPO::getAlarmRecordId, alarmRecordId)
                                       .notIn(!CollectionUtils
                                               .isEmpty(excludeUserIds), AlarmRecordHandlerUserPO::getUserId, excludeUserIds));
    }
}
