package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.job;

import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.XunYiFusionCommunicateAccountProvider;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class XunYiAccountDisableJob {

    private final XunYiFusionCommunicateAccountProvider xunYiFusionCommunicateProvider;

    public XunYiAccountDisableJob(XunYiFusionCommunicateAccountProvider xunYiFusionCommunicateProvider) {
        this.xunYiFusionCommunicateProvider = xunYiFusionCommunicateProvider;
    }

    @JobAnnotation(name = "fusionCommunicateDiableXunYiAccount", cron = "0 0/2 * * * ?",
                   desc = "融合通讯-定时释放讯一未使用的账号"
    )
    public void disableAccount(String param) {
        this.xunYiFusionCommunicateProvider.disableFusionAccount(5);
    }
}
