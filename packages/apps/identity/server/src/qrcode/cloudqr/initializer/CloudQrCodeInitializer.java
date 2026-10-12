package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.initializer;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.service.CloudQrCodeConfigService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.sync.CloudQrCodeTypeSync;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 云二维码初始化器
 * 职责：应用启动时初始化所有租户的二维码类型数据到云二维码服务
 *
 * @author system
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EventBusListen(order = 3)
public class CloudQrCodeInitializer {

    private final CloudQrCodeConfigService configService;
    private final CloudQrCodeTypeSync cloudQrCodeTypeSync;

    /**
     * 应用启动完成后初始化云二维码类型数据
     */
    @Subscribe
    @AllowConcurrentEvents
    public void initializeCloudQrCodeTypes(SysReadEvent event) {
        log.info("========== 开始初始化云二维码类型数据 ==========");

        try {
            // 获取所有启用云二维码的租户配置
            var enabledConfigs = configService.getAllEnabledConfigs();

            if (CollectionUtils.isEmpty(enabledConfigs)) {
                log.info("未找到启用云二维码的租户配置，跳过初始化");
                return;
            }

            log.info("找到{}个启用云二维码的租户，开始同步数据", enabledConfigs.size());

            // 循环处理每个租户
            var successCount = 0;
            var failCount = 0;

            for (var config : enabledConfigs) {
                try {
                    cloudQrCodeTypeSync.syncAllTenantTypes(config);
                    successCount++;
                } catch (Exception e) {
                    log.error("租户{}初始化失败", config.getTenant(), e);
                    failCount++;
                }
            }

            log.info("========== 云二维码类型数据初始化完成，成功:{}，失败:{} ==========",
                     successCount, failCount);

        } catch (Exception e) {
            log.error("云二维码类型数据初始化失败", e);
        }
    }
}
