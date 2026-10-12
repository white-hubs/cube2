package com.guhecloud.cube.base.alarm.server.state.operate;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.AssignAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.HandlerAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.SuspendAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.TransferAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;

/**
 * 报警操作接口
 *
 * @author lanchun
 * @date 2023/2/20 14:57
 */
public interface AlarmOperateService {

    /**
     * 报警确认
     *
     * @param alarmRecord
     *
     * @return
     */
    void confirmedAlarmRecord(AlarmRecord alarmRecord);

    /**
     * 报警移交
     *
     * @param alarmRecord
     * @param param
     *
     * @return
     */
    void transferAlarmRecord(AlarmRecord alarmRecord, TransferAlarmRecordCommand param);

    /**
     * 报警指派
     *
     * @param alarmRecord
     * @param param
     */
    void assignAlarmRecord(AlarmRecord alarmRecord, AssignAlarmRecordCommand param);

    /**
     * 报警挂起
     *
     * @param alarmRecord
     * @param param
     */
    void suspendAlarmRecord(AlarmRecord alarmRecord, SuspendAlarmRecordCommand param);

    /**
     * 报警处理
     *
     * @param alarmRecord
     * @param param
     */
    void handlerAlarmRecord(AlarmRecord alarmRecord, HandlerAlarmRecordCommand param);

}
