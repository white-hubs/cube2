package com.guhecloud.cube.biz.mdata.server.video.job;

import com.guhecloud.cube.biz.mdata.server.config.service.StreamMediaConfigService;
import com.guhecloud.cube.biz.mdata.server.video.service.VideoPtzService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeepAliveJob {

    private final VideoPtzService videoPtzService;

    private final StreamMediaConfigService streamMediaConfigService;

    @JobAnnotation(name = "mdataH5sKeepAliveSession", cron = "0 0/4 * * * ?",
                   desc = "主数据-定时保活h5s认证信息"
    )
    public void keepAliveSession(String param) {
        SessionContextHolder.requestTenant(null);
        var streamMediaConfigs = streamMediaConfigService.list();
        streamMediaConfigs.forEach(streamMediaConfig -> {
            SessionContextHolder.requestTenant(streamMediaConfig.getTenant());
            //session保活
            videoPtzService.keepAliveSession();
        });
    }
}
