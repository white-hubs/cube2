package com.guhecloud.cube.biz.daily.task.job;

import com.guhecloud.cube.biz.daily.task.domain.DailyGenerateTaskService;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 计划生成任务job
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyPlanGenerateTaskJob {
    private final TenantClient tenantClient;
    private final DailyGenerateTaskService dailyGenerateTaskService;

    /**
     * 计划生成任务扫描
     * 每分钟执行一次
     */
    @JobAnnotation(name = "dailyPlanGenerateTask", cron = "0 */2 * * * ?", desc = "计划生成任务扫描")
    public void execute(String param) {
        var now = LocalDateTime.now();
        var tenantList = tenantClient.listTenants(null);
        if (CollectionUtils.isEmpty(tenantList)) {
            return;
        }
        tenantList.forEach(tenant -> {
            SessionContextHolder.requestTenant(tenant.getCode());
            SecurityContextHolder.setPrincipal(Principal.builder().id(1L).name("system").account("system").type(Principal.Type.CLIENT).build());
            dailyGenerateTaskService.batchGenerateTaskByPlan(now);
        });
    }
}
