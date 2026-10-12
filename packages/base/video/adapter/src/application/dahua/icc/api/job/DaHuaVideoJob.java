package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.job;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaGetTokenRequest;
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
public class DaHuaVideoJob {

    private final SubsystemRedisUtils subsystemRedisUtils;

    /**
     * 大华token刷新
     *
     * @return
     */
    @JobAnnotation(name = "daHuaRefreshAccessToken", cron = "0 0 0/1 * * ?", desc = "大华监控token刷新")
    public void reFreshAccessToken(String param) {
        try {
            var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.VIDEO_SERVER);
            subEndpointRefreshConfigs.forEach(streamMediaConfig -> {
                if ("daHua".equals(streamMediaConfig.getManufacturer())) {
                    SubsystemContextHolder.setRequester(SubsystemRequester.builder()
                                                                .subsystemCode(SubsystemTypeConstants.VIDEO_SERVER)
                                                                .manufacturer("daHua")
                                                                .build());
                    SessionContextHolder.requestTenant(streamMediaConfig.getTenant());
                    //token刷新
                    var daHuaGetTokenRequest = new DaHuaGetTokenRequest();
                    daHuaGetTokenRequest.refreshToken();
                }
            });
            SubsystemContextHolder.clearSessionContext();
            SessionContextHolder.clearSessionContext();
        } catch (Exception e) {
            log.error("获取大华token或刷新大华token失败");
        }
    }
}
