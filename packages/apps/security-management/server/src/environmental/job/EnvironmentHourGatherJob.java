package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.service.EnvironmentHourStatService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 环境监测小时采集
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnvironmentHourGatherJob {
    private final EnvironmentHourStatService environmentHourStatService;
    private final SecurityCommonMapper securityCommonMapper;

    /**
     * 小时采集
     */
    @JobAnnotation(name = "environmentGatherInto", cron = "0 0 0 * * ?", desc = "环境检测-空气质量小时统计")
    public void environmentGatherInto(String param) {
        SecurityContextHolder.clearSecurityContext();
        SessionContextHolder.clearSessionContext();
        var tenantList = securityCommonMapper.selectList(Wrappers.<SecurityDevice>lambdaQuery()
                                                                 .select(SecurityDevice::getTenant)
                                                                 .groupBy(SecurityDevice::getTenant)
                                                        );
        if (CollectionUtils.isEmpty(tenantList)) {
            log.warn("小时采集设备表为空！");
            return;
        }
        SecurityContextHolder.setPrincipal(Principal.builder().id(1L).account("system").name("job").admin(true).type(Principal.Type.CLIENT).build());
        var nowTime = LocalDateTime.now();
        try {
            for (SecurityDevice securityDevice : tenantList) {
                SessionContextHolder.requestTenant(securityDevice.getTenant());
                environmentHourStatService.gatherInto(nowTime);
            }

        } catch (Exception e) {
            log.error("小时采集出错：", e);
        }
    }
}
