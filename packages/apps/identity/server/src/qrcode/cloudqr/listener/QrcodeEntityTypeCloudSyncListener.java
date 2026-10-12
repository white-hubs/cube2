package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.listener;

import com.guhecloud.cube.base.cloud.qr.code.sdk.model.CreateQrCodeTypeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.UpdateQrCodeTypeRequest;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeEntityTypeCreatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeEntityTypeDeletedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeEntityTypeUpdatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.sync.CloudQrCodeTypeSync;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 二维码实体类型云同步事件监听器
 * 职责：监听二维码类型变更事件，转换为SDK请求对象并同步到云服务
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeEntityTypeCloudSyncListener {

    private final CloudQrCodeTypeSync cloudQrCodeTypeSync;

    /**
     * 监听二维码类型创建事件
     */
    @EventListener
    public void onQrcodeEntityTypeCreated(QrcodeEntityTypeCreatedEvent event) {
        log.debug("接收到二维码类型创建事件，code={}", event.getCode());
        // 转换为SDK请求对象
        var request = new CreateQrCodeTypeRequest();
        request.setCode(event.getCode());
        request.setName(event.getName());
        // 调用云服务同步
        cloudQrCodeTypeSync.syncCreate(request);
    }

    /**
     * 监听二维码类型更新事件
     */
    @EventListener
    public void onQrcodeEntityTypeUpdated(QrcodeEntityTypeUpdatedEvent event) {
        log.debug("接收到二维码类型更新事件，code={}", event.getCode());
        // 转换为SDK请求对象
        var request = new UpdateQrCodeTypeRequest();
        request.setCode(event.getCode());
        request.setName(event.getName());
        // 调用云服务同步
        cloudQrCodeTypeSync.syncUpdate(request);
    }

    /**
     * 监听二维码类型删除事件
     */
    @EventListener
    public void onQrcodeEntityTypeDeleted(QrcodeEntityTypeDeletedEvent event) {
        log.debug("接收到二维码类型删除事件，code={}", event.getCode());
        // 调用云服务同步删除
        cloudQrCodeTypeSync.syncDelete(event.getCode());
    }
}
