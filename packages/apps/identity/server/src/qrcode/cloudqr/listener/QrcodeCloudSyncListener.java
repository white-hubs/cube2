package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.listener;

import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchDeleteByCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.CreateQrCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.QrRouteDTO;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.UpdateQrCodeRequest;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeBatchCreatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeBatchUpdatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeCreatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeDeletedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeUpdatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.sync.CloudQrCodeSync;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 二维码云同步事件监听器
 * 职责：监听二维码变更事件，转换为SDK请求对象并同步到云服务
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeCloudSyncListener {

    private final CloudQrCodeSync cloudQrCodeSync;

    /**
     * 监听二维码创建事件
     */
    @EventListener
    public void onQrcodeCreated(QrcodeCreatedEvent event) {
        log.debug("接收到二维码创建事件，code={}", event.getCode());
        // 转换为SDK请求对象
        var request = buildCreateRequest(event.getCode(), event.getName(), event.getQrTypeCode(), event.getRouteUrl());
        // 调用云服务同步
        cloudQrCodeSync.syncCreate(request);
    }

    /**
     * 监听二维码更新事件
     */
    @EventListener
    public void onQrcodeUpdated(QrcodeUpdatedEvent event) {
        log.debug("接收到二维码更新事件，code={}", event.getCode());
        // 转换为SDK请求对象
        var request = buildUpdateRequest(event.getCode(), event.getName(), event.getRouteUrl());
        // 调用云服务同步
        cloudQrCodeSync.syncUpdate(request);
    }

    /**
     * 监听二维码删除事件
     */
    @EventListener
    public void onQrcodeDeleted(QrcodeDeletedEvent event) {
        log.debug("接收到二维码删除事件，code={}", event.getCode());
        // 转换为SDK请求对象
        var request = new BatchDeleteByCodeRequest();
        request.setCodes(Collections.singletonList(event.getCode()));
        // 调用云服务同步删除
        cloudQrCodeSync.syncDelete(request);
    }

    /**
     * 监听二维码批量创建事件
     */
    @EventListener
    public void onQrcodeBatchCreated(QrcodeBatchCreatedEvent event) {
        log.debug("接收到二维码批量创建事件，count={}", event.getItems().size());
        // 转换为SDK请求对象列表
        List<CreateQrCodeRequest> requests = event.getItems().stream()
                .map(item -> buildCreateRequest(item.getCode(), item.getName(), item.getQrTypeCode(), item.getRouteUrl()))
                .collect(Collectors.toList());
        // 调用云服务批量同步
        event.accept(cloudQrCodeSync.syncBatchCreate(requests));
    }

    /**
     * 监听二维码批量更新事件
     */
    @EventListener
    public void onQrcodeBatchUpdated(QrcodeBatchUpdatedEvent event) {
        log.debug("接收到二维码批量更新事件，count={}", event.getItems().size());
        // 转换为SDK请求对象列表
        List<UpdateQrCodeRequest> requests = event.getItems().stream()
                .map(item -> buildUpdateRequest(item.getCode(), item.getName(), item.getRouteUrl()))
                .collect(Collectors.toList());
        // 调用云服务批量同步
        event.accept(cloudQrCodeSync.syncBatchUpdate(requests));
    }

    /**
     * 构建创建请求
     */
    private CreateQrCodeRequest buildCreateRequest(String code, String name, String qrTypeCode, String routeUrl) {
        var request = new CreateQrCodeRequest();
        request.setCode(code);
        request.setName(StringUtils.isNotBlank(name) ? name : "二维码-" + code);
        request.setQrTypeCode(qrTypeCode);
        request.setQrContentType("LINK");
        request.setEnabled(true);
        request.setMode("ROUTE");
        // 设置路由
        var route = new QrRouteDTO();
        route.setTargetUrl(routeUrl);
        request.setRoute(route);
        return request;
    }

    /**
     * 构建更新请求
     */
    private UpdateQrCodeRequest buildUpdateRequest(String code, String name, String routeUrl) {
        var request = new UpdateQrCodeRequest();
        request.setCode(code);
        request.setName(StringUtils.isNotBlank(name) ? name : "二维码-" + code);
        request.setQrContentType("LINK");
        request.setEnabled(true);
        request.setMode("ROUTE");
        request.setModeContent(routeUrl);

        // 设置路由
        if (StringUtils.isNotBlank(routeUrl)) {
            var route = new QrRouteDTO();
            route.setTargetUrl(routeUrl);
            request.setRoute(route);
        }

        return request;
    }
}
