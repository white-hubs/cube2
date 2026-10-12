package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.level.IncreaseAlarmLevelPlayConfigCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmFileSceneCodeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmLevelPlayConfigPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmLevelPlayConfigMapper;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmLevelPlayConfigCommandService {

    private final AlarmLevelPlayConfigMapper alarmLevelPlayConfigMapper;

    private final FileRefClient fileRefClient;

    @Transactional
    public void increaseAlarmLevelPlayConfig(IncreaseAlarmLevelPlayConfigCommand command) {
        AlarmLevelPlayConfigPO alarmLevelPlayConfigPO = this.alarmLevelPlayConfigMapper.selectOne(Wrappers
                                                                                                          .<AlarmLevelPlayConfigPO>lambdaQuery()
                                                                                                          .eq(AlarmLevelPlayConfigPO::getAlarmLevelCode, command.getAlarmLevelCode())
                                                                                                 );
        if (Objects.nonNull(alarmLevelPlayConfigPO)) {
            alarmLevelPlayConfigPO.setLooped(command.getLooped());
            alarmLevelPlayConfigPO.setPlayNum(command.getPlayNum());
            alarmLevelPlayConfigMapper.updateById(alarmLevelPlayConfigPO);
        } else {
            alarmLevelPlayConfigPO = new AlarmLevelPlayConfigPO();
            alarmLevelPlayConfigPO.setAlarmLevelCode(command.getAlarmLevelCode());
            alarmLevelPlayConfigPO.setLooped(command.getLooped());
            alarmLevelPlayConfigPO.setPlayNum(command.getPlayNum());
            alarmLevelPlayConfigMapper.insert(alarmLevelPlayConfigPO);
        }
        this.fileRefClient.removeByBusinessKeyAndScene(alarmLevelPlayConfigPO.getId(), AlarmFileSceneCodeEnum.ALARM_LEVEL_PLAY_CONFIG.getCode());
        if (Objects.nonNull(command.getFile())) {
            this.fileRefClient.refInduceSingle(AlarmFileSceneCodeEnum.ALARM_LEVEL_PLAY_CONFIG.getCode(),
                                               alarmLevelPlayConfigPO.getId(), command.getFile(), null);
        }
    }

    @Transactional
    public void deleteAlarmLevelPlayConfig(AlarmLevelPlayConfigPO alarmLevelPlayConfigPO) {
        DatabaseContextHolder.softDelete(AlarmLevelPlayConfigPO.class, alarmLevelPlayConfigPO);
    }
}
