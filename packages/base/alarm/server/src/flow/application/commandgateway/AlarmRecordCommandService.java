package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.IncreaseAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.RemoveAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.SettingAlarmBusinessSnapshotCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.UrgingAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.IncreaseAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.InitCreateAssignAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.RemoveAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.UrgingAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.factory.AlarmRecordFactory;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.AlarmRecordRepository;
import com.guhecloud.cube.base.alarm.server.sat.param.InitAssignAlarmRecordCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 报警记录命令操作
 *
 * @author lanchun
 * @date 2023/2/28 15:48
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordCommandService {

    private final AlarmRecordRepository alarmRecordRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 报警记录新增
     *
     * @param increaseAlarmRecordCommand
     */
    public void increaseAlarmRecord(IncreaseAlarmRecordCommand increaseAlarmRecordCommand) {
        //构造报警记录对象
        AlarmRecord alarmRecord = AlarmRecordFactory.newAlarmRecord(increaseAlarmRecordCommand);
        //持久化
        this.alarmRecordRepository.save(alarmRecord);
        //发送报警记录创建事件(记录流程日志与待办消息发送)
        this.applicationEventPublisher.publishEvent(new IncreaseAlarmRecordEvent(this, alarmRecord));
    }

    /**
     * 初始化指派报警记录
     *
     * @param initAssignAlarmRecordCommand
     */
    public void initAssignAlarmRecord(InitAssignAlarmRecordCommand initAssignAlarmRecordCommand) {
        AlarmRecord alarmRecord = alarmRecordRepository.detail(initAssignAlarmRecordCommand.getId());
        alarmRecord.settingChannelTypes(initAssignAlarmRecordCommand.getChannelTypes());
        this.alarmRecordRepository.saveAlarmNotifyChannelTypes(alarmRecord);
        this.applicationEventPublisher.publishEvent(new InitCreateAssignAlarmRecordEvent(this, alarmRecord, initAssignAlarmRecordCommand.getHandlerUsers(), initAssignAlarmRecordCommand.getChannelTypes()));
    }

    /**
     * 设置报警业务快照信息
     *
     * @param command
     */
    public void settingAlarmBusinessSnapshot(SettingAlarmBusinessSnapshotCommand command) {
        this.alarmRecordRepository.settingAlarmRecordBusinessSnapshot(command.getId(), command.getBusinessSnapshot());
    }

    /**
     * 报警催办
     *
     * @param urgingAlarmRecordCommand
     */
    public void urgingAlarmRecord(UrgingAlarmRecordCommand urgingAlarmRecordCommand) {
        for (Long id : urgingAlarmRecordCommand.getIds()) {
            AlarmRecord alarmRecord = this.alarmRecordRepository.detail(id);
            if (Objects.nonNull(alarmRecord)) {
                //发送报警催办事件(记录流程日志与消息发送)
                this.applicationEventPublisher.publishEvent(new UrgingAlarmRecordEvent(this, alarmRecord));
            }
        }
    }

    /**
     * 报警记录删除
     *
     * @param command 命令
     */
    public void removeAlarmRecord(RemoveAlarmRecordCommand command) {
        List<AlarmRecordPO> removeAlarmRecords = this.alarmRecordRepository.remove(command.getIds());
        this.applicationEventPublisher.publishEvent(new RemoveAlarmRecordEvent(this, removeAlarmRecords));
    }
}
