package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker;

import com.guhecloud.cube.base.alarm.client.AlarmStatisticsClient;
import com.guhecloud.cube.base.alarm.client.param.AlarmBusinessKeyStatisticsQueryParam;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.dto.DeviceLifeCycleDTO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service.DeviceLifeCycleService;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.dao.DeviceHealthProblemMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po.DeviceHealthProblemPO;
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
 * @date 2022/8/1 13:44
 * @description
 */
@Slf4j
public class AlarmWorker extends HealthyWorker {

    private Integer actualNumber = 0;

    public AlarmWorker(Long deviceId, Long healthValueId, Integer tenantId, Requester requester, Principal principal) {
        super(deviceId, healthValueId, tenantId, requester, principal);
    }

    @Override
    protected Integer run() {
        SessionContextHolder.setRequester(requester);
        SecurityContextHolder.setPrincipal(principal);
        SessionContextHolder.requestTenant(tenantId);
        Integer result = 0;

        DeviceLifeCycleService deviceLifeCycleService = ApplicationContextHolder.getBean(DeviceLifeCycleService.class);
        DeviceLifeCycleDTO deviceLifeCycleDTO = deviceLifeCycleService.get(this.deviceId);

        AlarmStatisticsClient statisticsClient = ApplicationContextHolder.getBean(AlarmStatisticsClient.class);
        AlarmBusinessKeyStatisticsQueryParam statisticsQueryParamOfLife = new AlarmBusinessKeyStatisticsQueryParam();
        statisticsQueryParamOfLife.setBusinessKey(deviceId.toString());
        statisticsQueryParamOfLife.setPeriod(new Period(deviceLifeCycleDTO.getCreateTime(), deviceLifeCycleDTO.getStopTime()));
        Long actualTotalOfLife = statisticsClient.getBusinessKeyActualTotal(statisticsQueryParamOfLife);
        if (actualTotalOfLife > 0) {
            result = 3;
        }

        AlarmBusinessKeyStatisticsQueryParam statisticsQueryParamOfMonth = new AlarmBusinessKeyStatisticsQueryParam();
        statisticsQueryParamOfMonth.setBusinessKey(deviceId.toString());
        var now = LocalDateTime.now();
        LocalDateTime preMonth = now.plusMonths(-1);

        statisticsQueryParamOfMonth.setPeriod(
                new Period(preMonth, now)
                                             );
        Long actualTotalOfMonth = statisticsClient.getBusinessKeyActualTotal(statisticsQueryParamOfMonth);

        if (actualTotalOfMonth < 3) {
            result += Integer.parseInt(actualTotalOfMonth * 3 + "");
        }

        if (actualTotalOfMonth >= 3 && actualTotalOfMonth <= 5) {
            result += Integer.parseInt(actualTotalOfMonth * 5 + "");
        }

        if (actualTotalOfMonth > 5) {
            result += 30;
        }

        this.actualNumber = Integer.valueOf(actualTotalOfMonth.toString());

        return result;
    }

    @Override
    protected void remind() {

        if (this.actualNumber > 0) {
            DeviceHealthProblemPO deviceHealthProblemPO = new DeviceHealthProblemPO();
            deviceHealthProblemPO.setDeviceHealthValueId(healthValueId);
            deviceHealthProblemPO.setDesc("设备近一个月发生过故障报警事件且超过" + actualNumber + "次");

            DeviceHealthProblemMapper problemMapper = ApplicationContextHolder.getBean(DeviceHealthProblemMapper.class);
            problemMapper.insert(deviceHealthProblemPO);
        }
    }
}
