package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.job;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service.IllegalParkingJobService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.dao.IllegalParkingConfigMapper;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/20 15:21
 * @Description:
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IllegalParkingJob {
    private final IllegalParkingJobService illegalParkingJobService;
    private final IllegalParkingConfigMapper illegalParkingConfigMapper;

    @JobAnnotation(name = "cleanIllegalParkingBlackList", cron = "0 0/30 * * * ?", desc = "停车管理-定时清理黑名单")
    public void cleanIllegalParkingBlackList(String param) {
        log.info("定时清理黑名单");
        List<Integer> tenantIds = illegalParkingConfigMapper.getAllTenant();
        if (CollectionUtils.isEmpty(tenantIds)) {
            return;
        }
        tenantIds.forEach(t -> {
            try {
                SessionContextHolder.requestTenant(t);
                illegalParkingJobService.cleanIllegalParkingBlackList();
            } catch (Exception exception) {
                log.error("{}租户清理失败", t, exception);
            }
        });

    }
}
