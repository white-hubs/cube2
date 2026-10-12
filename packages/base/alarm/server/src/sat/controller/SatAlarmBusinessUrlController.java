package com.guhecloud.cube.base.alarm.server.sat.controller;

import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmBusinessUrlModifyParam;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmBusinessUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 报警业务路径配置
 *
 * @author lanchun
 * @date 2022/8/2 11:31
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatAlarmBusinessUrlController {

    private final SatAlarmBusinessUrlService satAlarmBusinessUrlService;

    /**
     * 新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-alarm-business-url")
    public Long save(@Valid @RequestBody SatAlarmBusinessUrlModifyParam param) {
        return this.satAlarmBusinessUrlService.save(param);
    }

    /**
     * 修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/sat-alarm-business-url/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody SatAlarmBusinessUrlModifyParam param) {
        this.satAlarmBusinessUrlService.update(id, param);
    }
}
