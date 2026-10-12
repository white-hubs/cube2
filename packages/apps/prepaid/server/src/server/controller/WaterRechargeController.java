package com.guhecloud.cube.srv.prepaid.server.controller;

import com.guhecloud.cube.srv.prepaid.server.param.WaterRechargePageParam;
import com.guhecloud.cube.srv.prepaid.server.result.WaterRechargePageResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 水表充值记录
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WaterRechargeController {

    /**
     * 水表充值记录分页列表
     *
     * @param param 条件入参
     *
     * @return 分页列表
     */
    @GetMapping("/waterRecharge/page")
    public Pagination<WaterRechargePageResult> getWaterRechargePage(WaterRechargePageParam param) {
        return Pagination.empty();
    }
}
