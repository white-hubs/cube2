package com.guhecloud.cube.base.alarm.server.sat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmTypeTerminal;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmTypeTerminalModifyParam;

import java.util.List;

public interface SatAlarmTypeTerminalService extends IService<SatAlarmTypeTerminal> {

    void saveBatch(Long alarmTypeId, List<SatAlarmTypeTerminalModifyParam> terminals);

    List<SatAlarmTypeTerminal> listAlarmTypeTerminalsByAlarmTypeId(Long alarmTypeId);

    void removeByAlarmTypeId(Long alarmTypeId);
}
