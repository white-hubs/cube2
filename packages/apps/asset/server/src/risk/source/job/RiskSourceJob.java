package com.guhecloud.cube.srv.gen.property.management.server.risk.source.job;

import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.RiskSourceLedgerService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Author: lijie
 * @CreateTime: 2024-02-01  15:45
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskSourceJob {
    private final RiskSourceLedgerService riskSourceLedgerService;

    @JobAnnotation(name = "riskSourceExpireMessage", cron = "0 0 0,8 * * ?", desc = "风险源-到期消息提醒")
    public void PropertyOverdue(String param) {
        log.info("风险源定时任务开始执行");
        SessionContextHolder.clearSessionContext();
        riskSourceLedgerService.updateStatus();
        riskSourceLedgerService.overageJob();
        log.info("风险源定时任务执行结束");
        SessionContextHolder.clearSessionContext();
    }

    /**
     * @param param
     *
     * @return
     */
    public ReturnT propertyCopyRiskSource(String param) {
        log.info("风险源同步资产数据任务开始执行");
        SessionContextHolder.clearSessionContext();
        var typeNames = Arrays.asList(param.split(","));
        try {
            SessionContextHolder.requestTenant(Integer.parseInt(typeNames.getFirst()));
            riskSourceLedgerService.propertyCopyRiskSource(typeNames);
        } catch (Exception e) {
            log.error("风险源同步资产数据异常", e);
        }
        log.info("风险源同步资产数据任务执行结束");
        SessionContextHolder.clearSessionContext();
        return ReturnT.SUCCESS;
    }

}
