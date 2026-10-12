package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyGateParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyGateService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyGateVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lrj
 * @Description 支路分项配置
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyGateController {
    private final EnergyGateService energyGateService;

    /**
     * 功能描述： 设置开关
     */
    @PutMapping(value = "/energy-gate/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EnergyGateParam param) {
        energyGateService.updateOpenStatus(id, param.getOpen());
    }

    /**
     * 功能描述： 列表查询
     */
    @GetMapping(value = "/energy-gate/list")
    public List<EnergyGateVO> list() {
        return ObjectUtils.copying(energyGateService.getList(), EnergyGateVO.class);
    }

    /**
     * @Description 入住率
     * @Author lrj
     */
    @PutMapping("/occupancy-rate")
    public void updateOccupancyRate(@RequestParam(value = "occupancyRate") String occupancyRate) {
        energyGateService.updateOccupancyRate(occupancyRate);
    }

    /**
     * @Description 回显入住率
     * @Author lrj
     */
    @GetMapping("/occupancy-rate")
    public String getOccupancyRate() {
        return energyGateService.getOccupancyRate();
    }

}