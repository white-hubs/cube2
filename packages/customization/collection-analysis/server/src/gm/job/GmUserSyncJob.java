package com.guhecloud.cube.biz.collection.analysis.gm.job;

import com.guhecloud.cube.biz.collection.analysis.gm.service.GmUserSyncService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 光明人员每日同步任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GmUserSyncJob {
    private final GmUserSyncService gmUserSyncService;

    @JobAnnotation(name = "gmUserSync", cron = "0 0 2 * * ?", desc = "光明人员每日同步", param = "68547128")
    public void sync(String tenantParam) {
        if (StringUtils.isBlank(tenantParam)) {
            log.warn("光明人员同步未指定租户，跳过");
            return;
        }
        try {
            Integer tenant = Integer.parseInt(tenantParam);
            SessionContextHolder.requestTenant(tenant);
            gmUserSyncService.sync();
        } finally {
            SessionContextHolder.clearSessionContext();
        }
        log.info("光明人员同步完成");
    }
}
