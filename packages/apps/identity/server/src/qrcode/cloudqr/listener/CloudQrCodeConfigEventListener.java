package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.listener;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.CloudQrCodeConfigEnabledEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.sync.CloudQrCodeTypeSync;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 云二维码配置事件监听器
 * 职责：监听配置启用事件，触发类型数据同步
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeConfigEventListener {

    private final CloudQrCodeTypeSync cloudQrCodeTypeSync;

    /**
     * 处理配置启用事件：同步类型数据到云端
     */
    @EventListener
    public void handleConfigEnabledEvent(CloudQrCodeConfigEnabledEvent event) {
        log.info("监听到云二维码配置启用事件，租户={}", event.getConfig().getTenant());

        try {
            cloudQrCodeTypeSync.syncAllTenantTypes(event.getConfig());
            log.info("云二维码类型数据同步成功，租户={}", event.getConfig().getTenant());
        } catch (Exception e) {
            log.error("云二维码类型数据同步失败，租户={}", event.getConfig().getTenant(), e);
            // 不抛出异常，避免影响配置保存
        }
    }
}
