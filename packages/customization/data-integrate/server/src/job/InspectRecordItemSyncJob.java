package com.guhecloud.cube.base.dataintegrate.server.job;

import com.guhecloud.cube.base.dataintegrate.server.processer.InspectRecordItemSyncProcesses;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 巡检记录项采集
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordItemSyncJob {
    private final InspectRecordItemSyncProcesses inspectRecordItemSyncProcesses;

    /**
     * 巡检记录项采集
     */
    @JobAnnotation(name = "InspectRecordItemSyncJobHandler", cron = "0 0/1 * * * ?", desc = "总部巡检记录项数据采集")
    public void execute(String param) {
        inspectRecordItemSyncProcesses.marketDataSync();
    }
}
