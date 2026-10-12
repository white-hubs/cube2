package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter.AdapterAlarmService;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo.EnergyAlarmStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo.EnergyAlarmVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lrj
 * @Description 能耗告警
 * @date 2022-07-07 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyAlarmController {
    private final AdapterAlarmService energyAlarmService;

    /**
     * 功能描述： 获取最新的告警
     */
    @GetMapping(value = "/energy-alarm/list")
    public List<EnergyAlarmVO> getList() {
        return energyAlarmService.getRecentAlarms();
    }

    @GetMapping(value = "/energyAlarm/handle/stat")
    public EnergyAlarmStatVO energyAlarmStatForHandle() {
        return energyAlarmService.energyAlarmStatForHandle();
    }

    @GetMapping(value = "/energyAlarm/level/stat")
    public List<EnergyAlarmStatVO> energyAlarmStatForLevel() {
        return energyAlarmService.energyAlarmStatForLevel();
    }

}