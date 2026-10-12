package com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.mapper.InvestmentPlanMapper;
import com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.param.InvestmentPlanQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.po.InvestmentPlanPO;
import com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.result.InvestmentPlanResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.service.InvestmentPlanService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 招商计划实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvestmentPlanServiceImpl extends ServiceImpl<InvestmentPlanMapper, InvestmentPlanPO> implements InvestmentPlanService {
    private final InvestmentPlanMapper investmentPlanMapper;

    /**
     * 分页列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @Override
    public Pagination<InvestmentPlanResult> getPage(InvestmentPlanQueryParam param) {
        Page<InvestmentPlanPO> page = investmentPlanMapper.selectPage(PagingUtils.convert(param), Wrappers.<InvestmentPlanPO>lambdaQuery()
                                                                              .eq(StringUtils.isNotBlank(param.getPlanMonth()), InvestmentPlanPO::getPlanMonth, param.getPlanMonth())
                                                                              .eq(StringUtils.isNotBlank(param.getTargetType()), InvestmentPlanPO::getTargetType, param.getTargetType())
                                                                              .orderByDesc(InvestmentPlanPO::getPlanMonth)
                                                                     );

        return PagingUtils.convert(page, InvestmentPlanResult.class);
    }
}
