package com.guhecloud.cube.customization.xialing.server.drone.auth;

import com.guhecloud.cube.customization.xialing.server.drone.dto.XianingLoginCredentialDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 无人机三方配置提供者。
 * <p>
 * 统一从子系统 endpoint 配置解析厂商，再读取 adapter 配置中的 manufacturerJson。
 * </p>
 */
@Component
@RequiredArgsConstructor
public class DroneThirdPartyConfigProvider {
    private final SubsystemRedisUtils subsystemRedisUtils;

    public DroneThirdPartyConfig currentConfig() {
        Integer tenant = currentTenant();
        var endpointConfig = subsystemRedisUtils.getSubEndpointRefreshConfig(SubsystemTypeConstants.DRONE_SERVER + ":" + tenant);
        Assert.isParameterError(endpointConfig == null, "无人机子系统 endpoint 配置不存在 tenant=" + tenant);
        String manufacturer = endpointConfig.getManufacturer();
        Assert.isParameterError(!(StringUtils.isNotBlank(manufacturer)), "无人机子系统 endpoint 未配置厂商 tenant=" + tenant);
        manufacturer = manufacturer.trim();
        var adapterConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(SubsystemTypeConstants.DRONE_SERVER + ":" + manufacturer + ":" + tenant);
        return new DroneThirdPartyConfig(parseCredential(adapterConfig), tenant, manufacturer);
    }

    public XianingLoginCredentialDTO currentCredential() {
        return currentConfig().credential();
    }

    private Integer currentTenant() {
        var requester = SessionContextHolder.getRequester(true);
        Assert.isParameterError(requester == null || requester.getTenant() == null, "当前租户为空，无法获取无人机三方配置");
        return requester.getTenant();
    }

    private XianingLoginCredentialDTO parseCredential(SubsystemAdapterConfig adapterConfig) {
        Assert.isParameterError(adapterConfig == null || adapterConfig.getManufacturerJson() == null, "无人机子系统 adapter 配置 manufacturerJson 为空");
        Object manufacturerJson = adapterConfig.getManufacturerJson();
        XianingLoginCredentialDTO credential;
        if (manufacturerJson instanceof String json) {
            Assert.isParameterError(!(StringUtils.isNotBlank(json)), "无人机子系统 adapter 配置 manufacturerJson 为空");
            credential = JsonUtils.deserialize(json, XianingLoginCredentialDTO.class);
        } else {
            credential = JsonUtils.deserialize(JsonUtils.serialize(manufacturerJson), XianingLoginCredentialDTO.class);
        }
        Assert.isParameterError(credential == null, "无人机子系统 adapter 配置 manufacturerJson 解析失败");
        return credential;
    }

    public record DroneThirdPartyConfig(XianingLoginCredentialDTO credential, Integer tenant, String manufacturer) {
    }
}
