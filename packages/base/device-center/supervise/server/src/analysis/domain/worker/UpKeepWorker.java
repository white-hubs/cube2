package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker;

import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.dto.DeviceLifeCycleDTO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service.DeviceLifeCycleService;
import com.guhecloud.cube.srv.gen.maintain.client.UpkeepClient;
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
public class UpKeepWorker extends HealthyWorker {

    public UpKeepWorker(Long deviceId, Long healthValueId, Integer tenantId, Requester requester, Principal principal) {
        super(deviceId, healthValueId, tenantId, requester, principal);
    }

    @Override
    protected Integer run() {
        SessionContextHolder.requestTenant(tenantId);
        SessionContextHolder.setRequester(requester);
        SecurityContextHolder.setPrincipal(principal);
        var result = 0;
        UpkeepClient upkeepClient = ApplicationContextHolder.getBean(UpkeepClient.class);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginOfYear = now.plusYears(-1);
        Integer upkeepNumberOfYear = upkeepClient.isUpkeepValid(deviceId.toString(), new Period(beginOfYear, now));

        if (upkeepNumberOfYear == 0) {
            result = 5;
        }

        DeviceLifeCycleService deviceLifeCycleService = ApplicationContextHolder.getBean(DeviceLifeCycleService.class);
        DeviceLifeCycleDTO deviceLifeCycleDTO = deviceLifeCycleService.get(this.deviceId);

        Integer upkeepNumber = upkeepClient.isUpkeepValid(deviceId.toString(),
                                                          new Period(deviceLifeCycleDTO.getCreateTime(), deviceLifeCycleDTO.getStopTime())
                                                         );

        if (upkeepNumber == 0) {
            result += 3;
        }
        return result;
    }

    @Override
    protected void remind() {

    }
}
