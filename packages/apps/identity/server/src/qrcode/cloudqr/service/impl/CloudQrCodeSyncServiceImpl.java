package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.cloud.qr.code.sdk.model.QrCodeDTO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.converter.CloudQrcodeItemConverter;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeBatchCreatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.factory.CloudQrCodeClientFactory;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.service.CloudQrCodeSyncService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.QrcodeQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 云二维码同步Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeSyncServiceImpl implements CloudQrCodeSyncService {

    private final QrcodeQueryService qrcodeQueryService;
    private final CloudQrCodeClientFactory cloudQrCodeClientFactory;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void syncToCloud() {
        // 1. 获取本地所有二维码数据
        List<QrcodePO> localQrCodes = qrcodeQueryService.list(Wrappers.emptyWrapper());
        if (CollectionUtils.isEmpty(localQrCodes)) {
            log.info("本地无二维码数据，无需同步");
            return;
        }

        // 2. 按1000条分批处理
        int batchSize = 1000;
        int totalSize = localQrCodes.size();
        int totalNeedCreate = 0;

        for (int i = 0; i < totalSize; i += batchSize) {
            int end = Math.min(i + batchSize, totalSize);
            List<QrcodePO> batchQrCodes = localQrCodes.subList(i, end);

            // 3. 提取当前批次的code集合，批量查询云二维码
            var codes = batchQrCodes.stream()
                    .map(QrcodePO::getCode)
                    .collect(Collectors.toSet());

            var cloudQrCodeClient = cloudQrCodeClientFactory.createClient();
            var existingCloudQrCodes = cloudQrCodeClient.getQrCodeByCodes(codes);
            var existingCodes = existingCloudQrCodes.stream()
                    .map(QrCodeDTO::getCode)
                    .collect(Collectors.toSet());

            // 4. 过滤出当前批次需要新增的二维码（云端不存在的）
            var needCreateQrCodes = batchQrCodes.stream()
                    .filter(qr -> !existingCodes.contains(qr.getCode()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(needCreateQrCodes)) {
                log.info("批次 [{}-{}] 的二维码已在云端存在，无需同步", i, end);
                continue;
            }

            // 5. 批量新增到云端
            var createItems = CloudQrcodeItemConverter.buildQrcodeCreateItems(needCreateQrCodes);
            var qrcodeBatchCreatedEvent = new QrcodeBatchCreatedEvent(this, createItems);
            applicationEventPublisher.publishEvent(qrcodeBatchCreatedEvent);
            totalNeedCreate += qrcodeBatchCreatedEvent.get().size();
            log.info("批次 [{}-{}] 成功同步 {} 个二维码到云端", i, end, qrcodeBatchCreatedEvent.get().size());
        }

        log.info("同步完成，共同步 {} 个二维码到云端", totalNeedCreate);
    }
}
