package com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.job;

import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.IProviderConfig;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.constants.FusionCommunicateConfigConstant;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FusionCommunicateConfigRefresh {

    private final IProviderConfig providerConfig;

    private final TenantClient tenantClient;

    public FusionCommunicateConfigRefresh(IProviderConfig providerConfig,
                                          TenantClient tenantClient
                                         ) {
        this.providerConfig = providerConfig;
        this.tenantClient = tenantClient;
    }

    @JobAnnotation(name = "fusionCommunicateRefreshConfig", cron = "0 0/10 * * * ?",
                   desc = "融合通讯-定时刷新通讯厂家配置"
    )
    public void refreshConfig(String param) {
        List<TenantDTO> tenantDTOS = this.tenantClient.listTenants("");
        for (TenantDTO tenant : tenantDTOS) {
            SessionContextHolder.requestTenant(tenant.getCode());
            this.providerConfig.refreshProviderConfig(FusionCommunicateConfigConstant.CONFIG_KEY);
        }
        SessionContextHolder.clearSessionContext();
    }
}
