package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.job;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.service.H5sVideoService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.context.SubsystemContextHolder;
import com.guhecloud.subsystem.config.listener.web.model.SubsystemRequester;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class H5sKeepAliveJob {

    private final SubsystemRedisUtils subsystemRedisUtils;

    private final H5sVideoService h5sVideoService;

    @JobAnnotation(name = "videoH5sKeepAliveSession", cron = "0 0/4 * * * ?", desc = "视频平台-h5s-定时保活session")
    public void h5sKeepAliveSession(String param) {
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.VIDEO_SERVER);
        subEndpointRefreshConfigs.forEach(streamMediaConfig -> {
            if ("h5s".equals(streamMediaConfig.getManufacturer())) {
                SubsystemContextHolder.setRequester(SubsystemRequester.builder()
                                                            .subsystemCode(SubsystemTypeConstants.VIDEO_SERVER)
                                                            .manufacturer("h5s")
                                                            .build());
                SessionContextHolder.requestTenant(streamMediaConfig.getTenant());
                //session保活
                h5sVideoService.keepAliveSession();
            }
        });
        SubsystemContextHolder.clearSessionContext();
        SessionContextHolder.clearSessionContext();
    }
}
