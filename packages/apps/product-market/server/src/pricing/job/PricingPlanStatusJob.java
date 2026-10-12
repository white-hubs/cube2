package com.guhecloud.cube.srv.gen.product.market.server.pricing.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingPlanMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingPlan;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.PlanStatusEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 检查采价计划结束状态
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PricingPlanStatusJob {
    private final GoodsPricingPlanMapper goodsPricingPlanMapper;

    /**
     * 检查采价计划状态
     *
     * @param param 无
     *
     * @return 结果
     */
    @JobAnnotation(name = "PricingPlanStatusJobHandler", cron = "0 0/30 * * * ?", desc = "商品采价-采价计划状态更新")
    public void execute(String param) {
        //查询没有结束的所有计划
        var goodsPricingPlans = goodsPricingPlanMapper.selectList(Wrappers.<GoodsPricingPlan>lambdaQuery()
                                                                          .ne(GoodsPricingPlan::getStatus, PlanStatusEnum.OVER.name()));
        if (CollectionUtils.isNotEmpty(goodsPricingPlans)) {
            var currentTime = LocalDateTime.now();
            goodsPricingPlans.forEach(p -> {
                SessionContextHolder.requestTenant(p.getTenant());
                if (p.getEndTime() != null && p.getEndTime().isBefore(currentTime)) {
                    p.setStatus(PlanStatusEnum.OVER.name());
                    goodsPricingPlanMapper.updateById(p);
                }
                SessionContextHolder.requestTenant(null);
            });
        }
    }
}
