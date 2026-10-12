package com.guhecloud.cube.base.alarm.client;

import com.guhecloud.cube.base.alarm.client.dto.SatAlarmLevelDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.constraints.Min;

import java.util.Collection;
import java.util.List;

@FeignClient(name = "alarm")
public interface SatAlarmLevelClient {

    @GetMapping("/sat-alarm-level/{id}")
    SatAlarmLevelDTO getAlarmLevel(@PathVariable("id") @Min(value = 1, message = "报警级别主键不能小于1") Long id);

    @GetMapping("/sat-alarm-levels")
    List<SatAlarmLevelDTO> listAlarmLevels();

    /**
     * 报警级别列表(根据主键集合获取)
     *
     * @param ids
     *
     * @return
     */
    @GetMapping("/sat-alarm-levels/ids")
    List<SatAlarmLevelDTO> getByIds(@RequestParam("ids") Collection<Long> ids);
}
