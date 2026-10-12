package com.guhecloud.cube.srv.gen.property.management.server.common.job;

import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyDetailedService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyJob {
    private final PropertyDetailedService propertyDetailedService;

    /**
     * @param param
     */
    public void PropertyOverdue(String param) {
        log.info("资产定时任务开始执行");
        var allTenant = propertyDetailedService.getAllTenant();
        for (Integer ex : allTenant) {
            SessionContextHolder.requestTenant(ex);
            var bool = propertyDetailedService.OverageJob();
        }
        log.info("资产定时任务执行结束");
    }
}
