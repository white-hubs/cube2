package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker;

import com.guhecloud.cube.srv.gen.maintain.client.HiddenDangerClient;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/8/1 13:49
 * @description
 */
public class HiddenDangerWorker extends HealthyWorker {

    public HiddenDangerWorker(Long deviceId, Long healthValueId, Integer tenantId, Requester requester, Principal principal) {
        super(deviceId, healthValueId, tenantId, requester, principal);
    }

    @Override
    protected Integer run() {
        SessionContextHolder.setRequester(requester);
        SecurityContextHolder.setPrincipal(principal);
        SessionContextHolder.requestTenant(tenantId);
        HiddenDangerClient dangerClient = ApplicationContextHolder.getBean(HiddenDangerClient.class);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginOfMonth = now.plusMonths(-1);

        Integer hiddenDangerNumberOfMonth = dangerClient.isHiddenDangerValid(
                deviceId.toString(),
                new Period(beginOfMonth, now));
        return hiddenDangerNumberOfMonth * 3;
    }

    @Override
    protected void remind() {

    }
}
