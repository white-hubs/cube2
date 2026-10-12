package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.reason.ChangeAlarmReasonConfigCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.reason.IncreaseAlarmReasonConfigCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmReasonConfigPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmReasonConfigMapper;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 报警原因配置
 *
 * @author lanchun
 * @date 2024/7/24 13:47
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmReasonConfigCommandService {

    private final AlarmReasonConfigMapper alarmReasonConfigMapper;

    public Long increaseAlarmReasonConfig(@NonNull IncreaseAlarmReasonConfigCommand increaseAlarmReasonConfig) {
        AlarmReasonConfigPO alarmReasonConfigPO = ObjectUtils.copying(increaseAlarmReasonConfig, AlarmReasonConfigPO.class);
        Long id = IdWorker.getId();
        alarmReasonConfigPO.setId(id);
        alarmReasonConfigPO.setCode(String.valueOf(id));
        alarmReasonConfigPO.setInitialized(Boolean.FALSE);
        this.alarmReasonConfigMapper.insert(alarmReasonConfigPO);
        return id;
    }

    @Transactional(rollbackFor = Exception.class)
    public void initAlarmReasonConfig(@NonNull List<IncreaseAlarmReasonConfigCommand> increaseAlarmReasonConfigs) {
        List<AlarmReasonConfigPO> reasonConfigPOS = ObjectUtils.copying(increaseAlarmReasonConfigs, AlarmReasonConfigPO.class);
        reasonConfigPOS.forEach(alarmReasonConfigPO -> {
            Long id = IdWorker.getId();
            alarmReasonConfigPO.setId(id);
            alarmReasonConfigPO.setInitialized(Boolean.TRUE);
            this.alarmReasonConfigMapper.insert(alarmReasonConfigPO);
        });
    }

    public void changeAlarmReasonConfig(@NonNull ChangeAlarmReasonConfigCommand changeAlarmReasonConfig) {
        AlarmReasonConfigPO alarmReasonConfigPO = this.alarmReasonConfigMapper.selectById(changeAlarmReasonConfig.getId());
        if (Objects.nonNull(alarmReasonConfigPO)) {
            alarmReasonConfigPO.setReason(changeAlarmReasonConfig.getReason());
            alarmReasonConfigPO.setRemark(changeAlarmReasonConfig.getRemark());
            this.alarmReasonConfigMapper.updateById(alarmReasonConfigPO);
        }
    }

    public void deleteAlarmReasonConfig(@NonNull Long id) {
        AlarmReasonConfigPO alarmReasonConfigPO = this.alarmReasonConfigMapper.selectById(id);
        if (Objects.isNull(alarmReasonConfigPO)) {
            return;
        }
        DatabaseContextHolder.softDelete(AlarmReasonConfigPO.class, alarmReasonConfigPO);
    }
}
