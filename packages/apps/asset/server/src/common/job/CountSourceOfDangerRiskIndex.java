package com.guhecloud.cube.srv.gen.property.management.server.common.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.impl.SourceOfDangerCalculateScore;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.dao.RiskSourceLedgerMapper;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceLedger;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 统计危险源建档风险指数
 * @date 2023/11/20 9:33
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CountSourceOfDangerRiskIndex {

    private final SourceOfDangerCalculateScore sourceOfDangerCalculateScore;
    private final RiskSourceLedgerMapper riskSourceLedgerMapper;

    @JobAnnotation(name = "countSourceOfDangerRiskIndex", cron = "0 10 0 ? * 2", desc = "市场风险源建档风险指数计算")
    public void countSourceOfDangerRiskIndex(String param) {
        log.info("执行危险源建档风险指数计算任务:{}", "");
        try {
            var list = riskSourceLedgerMapper.selectList(new LambdaQueryWrapper<RiskSourceLedger>()
                                                                 .groupBy(RiskSourceLedger::getTenant));
            list.forEach(tenant -> {
                SessionContextHolder.requestTenant(tenant.getTenant());
                //隐患数量风险指数计算
                sourceOfDangerCalculateScore.calculateRiskScore();
            });
        } catch (Exception e) {
            log.error("危险源建档风险指数计算任务执行异常:", e);
            SessionContextHolder.clearSessionContext();
        }
    }
}
