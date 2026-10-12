package com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.controller;

import com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.param.InvestmentPlanQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.result.InvestmentPlanResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.service.InvestmentPlanService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 招商计划接口
 *
 * @author lanchun
 * @date 2024/8/28 15:22
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvestmentPlanController {
    private final InvestmentPlanService investmentPlanService;

    /**
     * 分页列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @GetMapping("/investmentPlan/page")
    public Pagination<InvestmentPlanResult> getPage(InvestmentPlanQueryParam param) {
        return investmentPlanService.getPage(param);
    }
}
