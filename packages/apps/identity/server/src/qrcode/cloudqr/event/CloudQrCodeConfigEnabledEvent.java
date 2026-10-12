package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.po.CloudQrCodeConfigPO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 云二维码配置启用事件
 * 当云二维码配置从禁用切换为启用，或首次启用时触发
 */
@Getter
public class CloudQrCodeConfigEnabledEvent extends ApplicationEvent {

    /**
     * 云二维码配置
     */
    private final CloudQrCodeConfigPO config;

    public CloudQrCodeConfigEnabledEvent(Object source, CloudQrCodeConfigPO config) {
        super(source);
        this.config = config;
    }
}
