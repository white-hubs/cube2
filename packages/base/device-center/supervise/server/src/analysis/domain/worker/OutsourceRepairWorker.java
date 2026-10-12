package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker;

import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.dto.DeviceLifeCycleDTO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service.DeviceLifeCycleService;
import com.guhecloud.cube.srv.gen.maintain.client.OutsourceRepairCheckClient;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/8/1 13:50
 * @description
 */
public class OutsourceRepairWorker extends HealthyWorker {

    public OutsourceRepairWorker(Long deviceId, Long healthValueId, Integer tenantId, Requester requester, Principal principal) {
        super(deviceId, healthValueId, tenantId, requester, principal);
    }

    @Override
    protected Integer run() {
        SessionContextHolder.setRequester(requester);
        SecurityContextHolder.setPrincipal(principal);
        SessionContextHolder.requestTenant(tenantId);
        var score = 0;
        DeviceLifeCycleService deviceLifeCycleService = ApplicationContextHolder.getBean(DeviceLifeCycleService.class);
        DeviceLifeCycleDTO deviceLifeCycleDTO = deviceLifeCycleService.get(this.deviceId);

        var planScrappedTime = deviceLifeCycleDTO.getStopTime();
        var createTime = deviceLifeCycleDTO.getCreateTime();

        OutsourceRepairCheckClient checkClient = ApplicationContextHolder.getBean(OutsourceRepairCheckClient.class);
        Period period = new Period(createTime, planScrappedTime);
        Integer countOfLifeCycle = checkClient.getRepairNumberByDeviceId(deviceId, period);

        var date = LocalDateTime.now();
        var preDate = date.minusDays(30);
        Integer count = checkClient.getRepairNumberByDeviceId(deviceId, new Period(preDate, date));

        if (countOfLifeCycle > 0) {
            score = 5;
        }

        score += count * 10;
        return score;
    }

    @Override
    protected void remind() {

    }
}
