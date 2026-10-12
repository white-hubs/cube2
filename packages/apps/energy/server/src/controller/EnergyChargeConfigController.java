package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeConfigParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyChargeConfigService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeConfigVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @author appet
 * @Description 能耗费用配置
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyChargeConfigController {
    private final EnergyChargeConfigService energyChargeConfigService;

    /**
     * 功能描述： 能耗费用配置新增
     */
    @PostMapping(value = "/energy-charge-config")
    public void add(@Valid @RequestBody EnergyChargeConfigParam param) {
        energyChargeConfigService.add(param);
    }

    /**
     * 功能描述： 能耗费用配置查询
     */
    @GetMapping(value = "/energy-charge-config")
    public EnergyChargeConfigVO getByType(String instrumentType, String chargeType) {
        return energyChargeConfigService.getByType(instrumentType, chargeType);
    }

}