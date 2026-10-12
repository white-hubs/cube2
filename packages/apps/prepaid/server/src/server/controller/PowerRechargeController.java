package com.guhecloud.cube.srv.prepaid.server.controller;

import com.guhecloud.cube.srv.prepaid.server.param.PowerRechargePageParam;
import com.guhecloud.cube.srv.prepaid.server.result.PowerRechargeResult;
import com.guhecloud.cube.srv.prepaid.server.service.PowerRechargeService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 电表充值记录
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PowerRechargeController {

    private final PowerRechargeService powerRechargeService;

    /**
     * 电表充值记录分页列表
     *
     * @param param 条件入参
     *
     * @return 分页对象
     */
    @GetMapping("/powerRecharge/page")
    public Pagination<PowerRechargeResult> getPowerRechargePage(@Valid PowerRechargePageParam param) {
        return powerRechargeService.getPowerRechargePage(param);
    }
}
