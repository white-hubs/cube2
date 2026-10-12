package com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter;

import com.guhecloud.apps.linkage.metadata.dto.LinkageActionDTO;
import com.guhecloud.cube.base.alarm.client.dto.SatAlarmLevelDTO;
import com.guhecloud.cube.biz.linkage.client.param.RuleModifyParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo.EnergyAlarmStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo.EnergyAlarmVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;

import java.math.BigDecimal;
import java.util.List;

public interface AdapterAlarmService {
    /**
     * 报警推送
     */
    void pushAlarm(MeterDevice device, BigDecimal lastDeviceValue, BigDecimal readeValue);

    List<SatAlarmLevelDTO> listAlarmLevels();

    Long save(RuleModifyParam ruleModifyParam);

    void update(Long id, RuleModifyParam param);

    void updateEnabled(Long id, Boolean enabled);

    void remove(Long id);

    List<LinkageActionDTO> getLinkageActions(Long ruleId);

    List<EnergyAlarmVO> getRecentAlarms();

    EnergyAlarmStatVO energyAlarmStatForHandle();

    List<EnergyAlarmStatVO> energyAlarmStatForLevel();
}
