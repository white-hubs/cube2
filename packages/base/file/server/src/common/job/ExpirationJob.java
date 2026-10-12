package com.guhecloud.cube.base.file.server.common.job;

import com.guhecloud.cube.base.file.server.service.FileExpirationPolicyService;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpirationJob {
    private final FileExpirationPolicyService fileExpirationPolicyService;

    @JobAnnotation(name = "fileExpirationPolicy", cron = "0 0 1 * * ?",
                   desc = "文件服务-过期清理"
    )
    public void FileExpirationPolicy(String param) {
        log.info("文件过期策略开始执行");
        fileExpirationPolicyService.scheduledDelete();
        log.info("文件过期策略执行结束");
    }
}
