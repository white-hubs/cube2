package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.event.handler;

import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.event.ProviderConfigChangeEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.XunYiFusionCommunicateAccountProvider;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.XunYiApiService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.config.XunYiConfig;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.request.SaveXunYiAccountRequest;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.request.XunYiAccount;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.response.XunYiAllDeviceResponse;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class XunYiProviderConfigChangeEventHandler {

    private final XunYiApiService xunYiApiService;

    private final XunYiFusionCommunicateAccountProvider xunYiFusionCommunicateProvider;

    public XunYiProviderConfigChangeEventHandler(XunYiApiService xunYiApiService,
                                                 XunYiFusionCommunicateAccountProvider xunYiFusionCommunicateProvider
                                                ) {
        this.xunYiApiService = xunYiApiService;
        this.xunYiFusionCommunicateProvider = xunYiFusionCommunicateProvider;
    }

    @EventListener
    public void xunYiProviderConfigChangeEvent(final ProviderConfigChangeEvent event) {
        if (!xunYiFusionCommunicateProvider.getProviderCode().equals(event.getNewProviderConfig().getProviderCode())) {
            return;
        }
        XunYiConfig xunYiConfig = JsonUtils.deserialize(event.getNewProviderConfig().getConfig(), XunYiConfig.class);
        List<XunYiAllDeviceResponse> allDevices = xunYiApiService.getAllDevices();
        allDevices.stream().forEach(device -> {
            XunYiAccount xunYiAccount = ObjectUtils.copying(device, XunYiAccount.class);
            xunYiAccount.setIsRecord(Objects.equals(Boolean.TRUE, xunYiConfig.getIsRecord()) ? 1 : 0);
            xunYiAccount.setIsVideo(Objects.equals(Boolean.TRUE, xunYiConfig.getIsVideo()) ? 1 : 0);
            if (StringUtils.isBlank(device.getSubType())) {
                xunYiAccount.setType(device.getType() + ".0");
            } else {
                xunYiAccount.setType(device.getType() + "." + device.getSubType());
            }
            xunYiApiService.saveEmployeeInfo(new SaveXunYiAccountRequest(xunYiAccount));
        });
    }
}
