package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.sync;

import com.guhecloud.cube.base.cloud.qr.code.sdk.CloudQrCodeClient;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchCreateQrCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchDeleteByCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchUpdateQrCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.CreateQrCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.QrCodeDTO;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.UpdateQrCodeRequest;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.factory.CloudQrCodeClientFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 云二维码同步处理器
 * 职责：处理QrcodePO的CRUD操作同步到云二维码服务
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeSync {

    private final CloudQrCodeClientFactory clientFactory;

    /**
     * 同步创建二维码
     *
     * @param request 二维码PO
     *
     * @return 云端二维码
     */
    public QrCodeDTO syncCreate(CreateQrCodeRequest request) {
        return executeSync("创建", () -> {
            var client = getClient();
            var result = client.createQrCode(request);
            log.info("成功同步创建云二维码， cloudCode={}",
                     result.getCode());
            return result;
        }, request.getCode());
    }

    /**
     * 同步批量创建二维码
     *
     * @param requests 二维码PO列表
     *
     * @return 云端二维码列表
     */
    public List<QrCodeDTO> syncBatchCreate(List<CreateQrCodeRequest> requests) {
        return executeSync("读取", () -> {
            var client = getClient();
            var request = new BatchCreateQrCodeRequest();
            request.setItems(requests);
            var result = client.batchCreateQrCodes(request);
            log.info("成功同步读取云二维码");
            return result;
        }, null);
    }

    /**
     * 同步更新二维码
     *
     * @param request 更新二维码请求
     */
    public QrCodeDTO syncUpdate(UpdateQrCodeRequest request) {
        return executeSync("更新", () -> {
            var client = getClient();
            var qrCodeDTO = client.updateQrCodeByCode(request);
            log.info("成功同步更新云二维码， cloudCode={}",
                     qrCodeDTO.getCode());
            return qrCodeDTO;
        }, request.getCode());
    }

    /**
     * 同步批量更新二维码
     *
     * @param requests 更新二维码请求列表
     */
    public List<QrCodeDTO> syncBatchUpdate(List<UpdateQrCodeRequest> requests) {
        return executeSync("更新", () -> {
            var client = getClient();
            var request = new BatchUpdateQrCodeRequest();
            request.setItems(requests);
            var result = client.batchUpdateQrCodes(request);
            log.info("成功同步批量更新云二维码");
            return result;
        }, null);
    }

    /**
     * 同步删除二维码
     *
     * @param request 删除二维码请求
     */
    public void syncDelete(BatchDeleteByCodeRequest request) {
        executeSync("删除", () -> {
            var client = getClient();
            client.batchDeleteQrCodesByCode(request);
            log.info("成功同步删除云二维码");
            return null;
        }, null);
    }

    /**
     * 执行同步操作（统一处理客户端检查和异常）
     *
     * @param operation 操作类型（用于日志）
     * @param action    具体的同步操作
     * @param code      二维码code（用于错误日志）
     *
     * @return 操作结果，失败返回null
     */
    private <T> T executeSync(String operation, SyncAction<T> action, String code) {
        if (!clientFactory.isEnabled()) {
            log.debug("当前租户未启用云二维码，跳过同步{}", operation);
            return null;
        }
        try {
            return action.execute();
        } catch (Exception e) {
            log.error("同步{}云二维码失败，code={}", operation, code, e);
            return null;
        }
    }

    /**
     * 获取云二维码客户端（带校验）
     *
     * @return CloudQrCodeClient实例
     *
     * @throws IllegalStateException 客户端创建失败时抛出
     */
    private CloudQrCodeClient getClient() {
        var client = clientFactory.createClient();
        if (client == null) {
            throw new IllegalStateException("创建云二维码客户端失败");
        }
        return client;
    }

    /**
     * 同步操作函数式接口
     */
    @FunctionalInterface
    private interface SyncAction<T> {
        T execute() throws Exception;
    }
}

