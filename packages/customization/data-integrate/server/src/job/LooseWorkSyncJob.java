package com.guhecloud.cube.base.dataintegrate.server.job;

import com.guhecloud.cube.base.dataintegrate.server.processer.LooseWorkSyncProcesser;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LooseWorkSyncJob {
    private final LooseWorkSyncProcesser syncProcesser;

    @JobAnnotation(name = "LooseWorkSyncJobHandler", cron = "0 0/1 * * * ?", desc = "总部小散工程数据采集")
    public void execute(String param) {
        syncProcesser.marketDataSync();
    }
}
