package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.sync;

import com.guhecloud.cube.base.cloud.qr.code.sdk.CloudQrCodeClient;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchCreateQrCodeTypeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchDeleteByCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchQueryByCodeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.BatchUpdateQrCodeTypeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.CreateQrCodeTypeRequest;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.QrCodeTypeDTO;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.UpdateQrCodeTypeRequest;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.converter.CloudQrCodeTypeConverter;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.factory.CloudQrCodeClientFactory;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.po.CloudQrCodeConfigPO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeEntityTypeService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityTypePO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 云二维码类型同步处理器
 * 职责：处理QrcodeEntityTypePO的CRUD操作同步到云二维码服务
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeTypeSync {

    private final CloudQrCodeClientFactory clientFactory;
    private final QrcodeEntityTypeService qrcodeEntityTypeService;

    /**
     * 同步批量创建二维码类型
     *
     * @param requests 二维码实体类型列表
     */
    public void syncBatchCreate(List<CreateQrCodeTypeRequest> requests) {
        executeSync("批量创建", () -> {
            var client = getClient();
            var batchRequest = new BatchCreateQrCodeTypeRequest();
            batchRequest.setItems(requests);
            client.batchCreateQrCodeTypes(batchRequest);
            log.info("成功同步创建云二维码类型，count={}", requests.size());
        }, null);
    }

    /**
     * 同步创建二维码类型
     *
     * @param request 二维码实体类型
     */
    public void syncCreate(CreateQrCodeTypeRequest request) {
        executeSync("创建", () -> {
            CloudQrCodeClient client = getClient();
            client.createQrCodeType(request);
            log.info("成功同步创建云二维码类型，code={}, name={}",
                     request.getCode(), request.getName());
        }, request.getCode());
    }

    /**
     * 同步更新二维码类型
     *
     * @param request 二维码实体类型
     */
    public void syncUpdate(UpdateQrCodeTypeRequest request) {
        executeSync("更新", () -> {
            var client = getClient();
            client.updateQrCodeTypeByCode(request);
            log.info("成功同步更新云二维码类型，code={}, name={}",
                     request.getCode(), request.getName());
        }, request.getCode());
    }

    /**
     * 同步批量更新二维码类型
     *
     * @param requests 更新项列表
     */
    public void syncBatchUpdate(List<UpdateQrCodeTypeRequest> requests) {
        executeSync("批量更新", () -> {
            var client = getClient();
            var batchRequest = new BatchUpdateQrCodeTypeRequest();
            batchRequest.setItems(requests);
            client.batchUpdateQrCodeTypes(batchRequest);
            log.info("成功同步更新云二维码类型，count={}", requests.size());
        }, null);
    }

    /**
     * 同步删除二维码类型
     *
     * @param code 二维码类型code
     */
    public void syncDelete(String code) {
        executeSync("删除", () -> {
            var request = new BatchDeleteByCodeRequest();
            request.setCodes(Collections.singletonList(String.valueOf(code)));
            var client = getClient();
            client.batchDeleteQrCodeTypesByCode(request);
            log.info("成功同步删除云二维码类型，code={}", code);
        }, code);
    }

    /**
     * 批量同步租户的所有二维码类型到云端（启动初始化或配置保存时调用）
     * <p>
     * 该方法会查询本地所有二维码类型，与云端数据对比后：
     * - 云端不存在的，批量创建
     * - 云端已存在的，批量更新
     *
     * @param config 云二维码配置
     */
    public void syncAllTenantTypes(CloudQrCodeConfigPO config) {
        log.info("开始同步租户{}的云二维码类型数据", config.getTenant());

        // 创建租户上下文
        SessionContextHolder.requestTenant(config.getTenant());
        // 查询该租户的所有二维码类型
        var entityTypes = qrcodeEntityTypeService.list();
        try {

            if (CollectionUtils.isEmpty(entityTypes)) {
                log.info("租户{}没有二维码类型数据，跳过", config.getTenant());
                return;
            }

            log.info("租户{}有{}条二维码类型数据，开始同步",
                     config.getTenant(), entityTypes.size());

            // 批量同步到云二维码服务
            syncEntityTypesToCloud(config, entityTypes);

            log.info("租户{}的云二维码类型数据同步成功", config.getTenant());
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }

    /**
     * 批量同步实体类型到云二维码服务（存在则更新，不存在则创建）
     */
    private void syncEntityTypesToCloud(CloudQrCodeConfigPO config,
                                        List<QrcodeEntityTypePO> entityTypes
                                       ) {
        var client = clientFactory.createClient(config);
        if (client == null) {
            log.warn("创建云二维码客户端失败，租户={}", config.getTenant());
            return;
        }

        try {
            // 1. 提取所有code
            var codes = entityTypes.stream()
                    .map(QrcodeEntityTypePO::getEntityType)
                    .toList();

            // 2. 批量查询云端已存在的类型
            var queryRequest = new BatchQueryByCodeRequest();
            queryRequest.setCodes(codes);
            var existingTypes = client.batchQueryQrCodeTypesByCode(queryRequest);

            // 3. 构建存在的code集合
            var existingCodes = existingTypes.stream()
                    .map(QrCodeTypeDTO::getCode)
                    .collect(java.util.stream.Collectors.toSet());

            // 4. 分组：需要创建的 vs 需要更新的
            var toCreate = entityTypes.stream()
                    .filter(po -> !existingCodes.contains(String.valueOf(po.getEntityType())))
                    .toList();

            var toUpdate = entityTypes.stream()
                    .filter(po -> existingCodes.contains(String.valueOf(po.getEntityType())))
                    .toList();

            // 5. 批量创建新类型
            if (!toCreate.isEmpty()) {
                var createRequest = new BatchCreateQrCodeTypeRequest();
                var createItems = CloudQrCodeTypeConverter.convertPOToCreateRequests(toCreate);
                createRequest.setItems(createItems);
                client.batchCreateQrCodeTypes(createRequest);
                log.info("成功创建{}条新二维码类型", createItems.size());
            }

            // 6. 批量更新已存在的类型
            if (!toUpdate.isEmpty()) {
                var updateRequest = new BatchUpdateQrCodeTypeRequest();
                var updateItems = CloudQrCodeTypeConverter.convertPOToUpdateRequests(toUpdate);
                updateRequest.setItems(updateItems);
                client.batchUpdateQrCodeTypes(updateRequest);
                log.info("成功更新{}条已存在的二维码类型", updateItems.size());
            }

            log.info("成功同步{}条二维码类型数据到云端（创建:{}，更新:{})",
                     entityTypes.size(), toCreate.size(), toUpdate.size());

        } catch (Exception e) {
            throw new ServiceException("批量同步二维码类型失败", e);
        }
    }

    /**
     * 逐条同步
     */
    public void syncOneByOne(CloudQrCodeConfigPO config, List<QrcodeEntityTypePO> entityTypes) {
        var client = clientFactory.createClient(config);
        var successCount = 0;
        var failCount = 0;

        for (var entityType : entityTypes) {
            try {
                // 尝试查询是否存在
                var code = entityType.getEntityType();
                var queryRequest = new BatchQueryByCodeRequest();
                queryRequest.setCodes(java.util.Collections.singletonList(code));
                var existingTypes = client.batchQueryQrCodeTypesByCode(queryRequest);

                if (existingTypes.isEmpty()) {
                    // 不存在，创建
                    var request = CloudQrCodeTypeConverter.convertPOToCreateRequest(entityType);
                    client.createQrCodeType(request);
                } else {
                    // 存在，更新
                    var request = CloudQrCodeTypeConverter.convertPOToUpdateRequest(entityType);
                    client.updateQrCodeTypeByCode(request);
                }
                successCount++;
            } catch (Exception e) {
                log.warn("同步单条数据失败，entityType={}", entityType.getEntityType(), e);
                failCount++;
            }
        }

        log.info("逐条同步完成，成功:{}，失败:{}", successCount, failCount);
    }

    /**
     * 执行同步操作（统一处理客户端检查和异常）
     *
     * @param operation 操作类型（用于日志）
     * @param action    具体的同步操作
     * @param code      二维码类型编码（用于错误日志）
     */
    private void executeSync(String operation, SyncAction action, String code) {
        if (!clientFactory.isEnabled()) {
            log.debug("当前租户未启用云二维码，跳过同步{}", operation);
            return;
        }
        try {
            action.execute();
        } catch (Exception e) {
            log.error("同步{}云二维码类型失败，id={}", operation, code, e);
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
    private interface SyncAction {
        void execute() throws Exception;
    }
}
