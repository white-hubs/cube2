package com.guhecloud.cube.base.alarm.server.state.operate;

import com.guhecloud.cube.base.alarm.server.state.AlarmStateContext;

/**
 * 报警状态抽象
 *
 * @author lanchun
 * @date 2023/2/20 14:58
 */
public abstract class AbstractAlarmStateOperateService implements AlarmOperateService {

    protected AlarmStateContext alarmStateContext;

    public AbstractAlarmStateOperateService(AlarmStateContext alarmStateContext) {
        this.alarmStateContext = alarmStateContext;
    }

}
