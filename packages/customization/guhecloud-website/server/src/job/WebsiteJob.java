package com.guhecloud.cube.customized.guhecloudwebsite.job;

import com.guhecloud.cube.customized.guhecloudwebsite.service.BuildRecordService;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebsiteJob {
    private final BuildRecordService buildRecordService;

    @JobAnnotation(name = "WebsiteJob", cron = "0 * * * * ?",
                   desc = "网站管理-更新构建状态"
    )
    public void WebsiteJob(String param) {
        log.info("网站管理-更新构建状态开始执行");
        buildRecordService.pollBuildTasks();
        buildRecordService.pollDeployTasks();
        log.info("网站管理-更新构建状态执行结束");
    }
}
