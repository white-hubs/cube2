package com.guhecloud.cube.base.alarm.client;

import com.guhecloud.cube.base.alarm.client.dto.SatAlarmTypeDTO;
import com.guhecloud.cube.base.alarm.client.param.SatAlarmTypeQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.List;

@FeignClient(name = "alarm")
public interface SatAlarmTypeClient {

    @GetMapping("/sat-alarm-type/{id}")
    SatAlarmTypeDTO getAlarmType(@PathVariable("id") @Min(value = 1, message = "报警类型主键不能小于1") Long id);

    @GetMapping("/sat-alarm-type/by-application-code/{applicationCode}")
    SatAlarmTypeDTO getByApplicationCode(@PathVariable("applicationCode") String applicationCode);

    @GetMapping("/sat-alarm-types")
    List<SatAlarmTypeDTO> listAlarmTypes(@Valid SatAlarmTypeQueryParam param);
}
