package com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.job;

import com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.TencentCommunicationAccountProvider;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TencentAccountDisableJob {

    private final TencentCommunicationAccountProvider tencentCommunicationAccountProvider;

    public TencentAccountDisableJob(TencentCommunicationAccountProvider tencentCommunicationAccountProvider) {
        this.tencentCommunicationAccountProvider = tencentCommunicationAccountProvider;
    }

    @JobAnnotation(name = "fusionCommunicateDiableTencentAccount", cron = "0 0/2 * * * ?",
                   desc = "融合通讯-定时释放腾讯未使用的账号"
    )
    public void disableAccount(String param) {
        this.tencentCommunicationAccountProvider.disableFusionAccount(2);
    }
}
