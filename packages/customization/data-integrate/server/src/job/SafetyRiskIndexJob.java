package com.guhecloud.cube.base.dataintegrate.server.job;

import com.guhecloud.cube.base.dataintegrate.server.processer.SafetyRiskIndexSyncProcesser;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyRiskIndexJob {

    private final SafetyRiskIndexSyncProcesser safetyRiskIndexSyncProcesser;

    @JobAnnotation(name = "SafetyRiskIndexSyncJobHandler", cron = "0 30 0/1 ? * 2", desc = "总部安全风险得分数据采集")
    public void execute(String param) {
        safetyRiskIndexSyncProcesser.marketDataSync();
    }
}
