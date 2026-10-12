package com.guhecloud.cube.base.alarm.server.flow.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordHandlerUserPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordHandlerUserMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordHandlerUserQueryService {

    private final AlarmRecordHandlerUserMapper alarmRecordHandlerUserMapper;

    /**
     * 获取处理人列表
     *
     * @param alarmRecordId
     *
     * @return
     */
    public List<AlarmRecordHandlerUserPO> getUsersByAlarmRecordId(@NonNull Long alarmRecordId) {
        return this.alarmRecordHandlerUserMapper.selectList(Wrappers.<AlarmRecordHandlerUserPO>lambdaQuery()
                                                                    .eq(AlarmRecordHandlerUserPO::getAlarmRecordId, alarmRecordId));
    }

    /**
     * 获取处理人列表
     *
     * @param alarmRecordIds
     *
     * @return
     */
    public List<AlarmRecordHandlerUserPO> getUsersByAlarmRecordIds(@NonNull Collection<Long> alarmRecordIds) {
        return CollectionUtils.isEmpty(alarmRecordIds) ? Collections.emptyList()
                                                       : this.alarmRecordHandlerUserMapper.selectList(Wrappers
                                                                                                              .<AlarmRecordHandlerUserPO>lambdaQuery()
                                                                                                              .in(AlarmRecordHandlerUserPO::getAlarmRecordId, alarmRecordIds));
    }

    /**
     * 获取处理人列表
     *
     * @param alarmRecordId
     * @param userId
     *
     * @return
     */
    public Optional<AlarmRecordHandlerUserPO> getUserByAlarmRecordId(@NonNull Long alarmRecordId, @NonNull Long userId) {
        return Optional.ofNullable(this.alarmRecordHandlerUserMapper.selectOne(Wrappers.<AlarmRecordHandlerUserPO>lambdaQuery()
                                                                                       .eq(AlarmRecordHandlerUserPO::getAlarmRecordId, alarmRecordId)
                                                                                       .eq(AlarmRecordHandlerUserPO::getUserId, userId)));
    }

    /**
     * 获取处理人
     *
     * @param alarmRecordId
     * @param userId
     *
     * @return
     */
    public AlarmRecordHandlerUserPO getUser(@NonNull Long alarmRecordId, @NonNull Long userId) {
        return this.alarmRecordHandlerUserMapper.selectOne(Wrappers.<AlarmRecordHandlerUserPO>lambdaQuery()
                                                                   .eq(AlarmRecordHandlerUserPO::getAlarmRecordId, alarmRecordId)
                                                                   .eq(AlarmRecordHandlerUserPO::getUserId, userId));
    }
}
