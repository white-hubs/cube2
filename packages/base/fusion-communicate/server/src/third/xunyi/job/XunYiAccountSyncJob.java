package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.job;

import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.IProviderConfig;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.constants.FusionCommunicateConfigConstant;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.XunYiFusionCommunicateAccountProvider;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class XunYiAccountSyncJob {

    private final XunYiFusionCommunicateAccountProvider xunYiFusionCommunicateProvider;

    private final IProviderConfig providerConfig;

    public XunYiAccountSyncJob(XunYiFusionCommunicateAccountProvider xunYiFusionCommunicateProvider,
                               IProviderConfig providerConfig
                              ) {
        this.xunYiFusionCommunicateProvider = xunYiFusionCommunicateProvider;
        this.providerConfig = providerConfig;
    }

    @JobAnnotation(name = "fusionCommunicateSyncXunYiAccount", cron = "0 0/5 * * * ?",
                   desc = "融合通讯-定时同步讯一账号信息"
    )
    public void xunYiAccountSyncJob(String param) {
        var allTenantProviderConfig = this.providerConfig
                .getAllTenantProviderConfig(FusionCommunicateConfigConstant.CONFIG_KEY);
        for (var config : allTenantProviderConfig) {
            SessionContextHolder.requestTenant(config.getTenant());
            if (!xunYiFusionCommunicateProvider.getProviderCode().equals(config.getProviderCode())) {
                return;
            }
            this.xunYiFusionCommunicateProvider.syncThirdFusionCommunicateAccounts();
        }
        SessionContextHolder.clearSessionContext();
    }
}
