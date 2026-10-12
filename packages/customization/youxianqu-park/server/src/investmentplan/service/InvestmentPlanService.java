package com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.service;

import com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.param.InvestmentPlanQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.result.InvestmentPlanResult;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 招商计划服务
 */
public interface InvestmentPlanService {
    /**
     * 分页列表
     *
     * @param param 条件
     * @return 分页
     */
    Pagination<InvestmentPlanResult> getPage(InvestmentPlanQueryParam param);
}
