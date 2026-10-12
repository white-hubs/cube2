package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker;

import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.dto.DeviceLifeCycleDTO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service.DeviceLifeCycleService;
import com.guhecloud.cube.srv.gen.maintain.client.FaultClient;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/8/1 13:47
 * @description 故障保修
 */
@Slf4j
public class FaultWorker extends HealthyWorker {

    public FaultWorker(Long deviceId, Long healthValueId, Integer tenantId, Requester requester, Principal principal) {
        super(deviceId, healthValueId, tenantId, requester, principal);
    }

    @Override
    protected Integer run() {
        SessionContextHolder.setRequester(requester);
        SecurityContextHolder.setPrincipal(principal);
        SessionContextHolder.requestTenant(tenantId);
        int result = 0;
        FaultClient faultClient = ApplicationContextHolder.getBean(FaultClient.class);

        DeviceLifeCycleService deviceLifeCycleService = ApplicationContextHolder.getBean(DeviceLifeCycleService.class);
        DeviceLifeCycleDTO deviceLifeCycleDTO = deviceLifeCycleService.get(this.deviceId);

        Integer faultNumberOfLife = faultClient.isFaultValid(
                deviceId.toString(),
                new Period(deviceLifeCycleDTO.getCreateTime(), deviceLifeCycleDTO.getStopTime()));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginOfMonth = now.plusMonths(-1);

        Integer faultNumberOfMonth = faultClient.isFaultValid(
                deviceId.toString(),
                new Period(beginOfMonth, now));

        if (faultNumberOfLife > 0) {
            // 扣3分
            result = 3;
        }
        result += faultNumberOfMonth * 3;

        return result;
    }

    @Override
    protected void remind() {

    }
}
