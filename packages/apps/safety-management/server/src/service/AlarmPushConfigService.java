package com.guhecloud.cube.srv.gen.safety.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.management.po.AlarmPushConfigPO;

import java.util.List;

public interface AlarmPushConfigService extends IService<AlarmPushConfigPO> {

    List<AlarmPushConfigPO> getAlarmPushConfigPOS(String alarmTypeCode);

    List<AlarmPushConfigPO> getAlarmTimeoutPushConfig();

    Long save(String alarmTypeCode, String alarmLevelCode, Boolean timeout);

    void remove(String alarmTypeCode, String alarmLevelCode);
}
