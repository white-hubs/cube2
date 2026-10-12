package com.guhecloud.cube.srv.gen.adapter.access.control.server.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.DaHuaApi;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.config.DaHuaConfig;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.context.SubsystemContextHolder;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.listener.web.model.SubsystemRequester;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DaHuaJob {
    private final SubsystemRedisUtils subsystemRedisUtils;
    private final DaHuaApi daHuaApi;

    /**
     * @Description 每小时一次 刷新大华token
     * @Author xiaofeng
     */
    @JobAnnotation(name = "refreshTokenHandler", cron = "0 0 0/1 * * ?", desc = "刷新大华token")
    public ReturnT<String> refreshTokenHandler(String param) {
        var subEndpointRefreshConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.DOOR_SERVER);
        subEndpointRefreshConfigs.forEach(config -> {
            if ("daHua".equals(config.getManufacturer())) {
                SubsystemContextHolder.setRequester(SubsystemRequester.builder()
                                                            .subsystemCode(SubsystemTypeConstants.DOOR_SERVER)
                                                            .manufacturer("daHua")
                                                            .build());
                SessionContextHolder.requestTenant(config.getTenant());
                var subAdapterRefreshConfig = this.getBasicConfig();
                //刷新配置
                try {
                    daHuaApi.refreshToken(this.getAccessConfig(subAdapterRefreshConfig));
                } catch (JsonProcessingException e) {
                    log.error("获取大华token或刷新大华token失败，tenant:{}", config.getTenant(), e);
                }
            }
        });
        return ReturnT.SUCCESS;
    }

    /**
     * 获取门禁基础配置
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key = SubsystemTypeConstants.DOOR_SERVER + ":" + "daHua" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    /**
     * 获取门禁授权配置
     */
    private DaHuaConfig getAccessConfig(SubsystemAdapterConfig config) {
        DaHuaConfig daHuaConfig = JsonUtils.deserialize(config.getManufacturerJson(), DaHuaConfig.class);
        if (daHuaConfig != null) {
            daHuaConfig.setTenant(config.getTenant());
        }
        return daHuaConfig;
    }
}
