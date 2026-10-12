package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.job;

import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerNewService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.impl.HiddenDangerNumCalculateScore;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.impl.HiddenDangerResponseCalculateScore;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerNew;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 计算隐患单风险指数
 * @date 2023/11/16 16:28
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CountHiddenRiskIndex {

    private final HiddenDangerNewService hiddenDangerNewService;
    private final HiddenDangerNumCalculateScore hiddenDangerNumCalculateScore;
    private final HiddenDangerResponseCalculateScore hiddenDangerResponseCalculateScore;

    @JobAnnotation(name = "countHiddenRiskIndex", cron = "0 10 0 ? * 2", desc = "隐患排查-隐患风险指数计算")
    public void countHiddenRiskIndex(String param) {
        log.info("执行隐患单风险指数计算任务:{}", "");
        try {
            var list = hiddenDangerNewService.lambdaQuery().select(HiddenDangerNew::getTenant).list();
            var collect = list.stream().map(HiddenDangerNew::getTenant).distinct().toList();
            collect.forEach(tenant -> {
                SessionContextHolder.requestTenant(tenant);
                //隐患数量风险指数计算
                hiddenDangerNumCalculateScore.calculateRiskScore();
                //隐患响应风险指数计算
                hiddenDangerResponseCalculateScore.calculateRiskScore();
            });
        } catch (Exception e) {
            log.error("隐患单风险指数计算任务异常:", e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }
}
