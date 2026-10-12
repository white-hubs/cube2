package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker;

import com.guhecloud.cube.biz.daily.task.client.DailyTargetStatClient;
import com.guhecloud.cube.biz.daily.task.commons.vo.statistics.TargetDailyStatVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/8/1 13:48
 * @description 巡检
 */
public class InspectionWorker extends HealthyWorker {

    private static final String INSPECTION_TASK_CODE = "DAILY_INSPECTION";

    public InspectionWorker(Long deviceId, Long healthValueId, Integer tenantId, Requester requester, Principal principal) {
        super(deviceId, healthValueId, tenantId, requester, principal);
    }

    @Override
    protected Integer run() {
        SessionContextHolder.setRequester(requester);
        SecurityContextHolder.setPrincipal(principal);
        SessionContextHolder.requestTenant(tenantId);
        long result = 0;
        DailyTargetStatClient dailyTargetStatClient = ApplicationContextHolder.getBean(DailyTargetStatClient.class);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginOfMonth = now.plusMonths(-1);
        TargetDailyStatVO healthStatByTarget = dailyTargetStatClient.getTargetDailyStat(this.deviceId, INSPECTION_TASK_CODE, new Period(beginOfMonth, now));

        if (healthStatByTarget.getTaskNum() <= 0) {
            result += 3;
        }
        if (healthStatByTarget.getAbnormalNum() > 0) {
            result += healthStatByTarget.getAbnormalNum() * 5;
        }

        TargetDailyStatVO statByTarget = dailyTargetStatClient.getTargetDailyStat(this.deviceId, INSPECTION_TASK_CODE, null);
        if (statByTarget.getPlanNum() <= 0) {
            result += 3;
        }

        return Integer.parseInt(String.valueOf(result));
    }

    @Override
    protected void remind() {

    }
}
