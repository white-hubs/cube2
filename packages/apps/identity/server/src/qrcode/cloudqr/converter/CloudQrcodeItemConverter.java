package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.converter;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeBatchCreatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeBatchUpdatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.handler.QrcodeStyleInstHandler;
import com.guhecloud.platform.base.context.ApplicationContextHolder;

import java.util.List;
import java.util.stream.Collectors;

public class CloudQrcodeItemConverter {

    private CloudQrcodeItemConverter() {}

    public static List<QrcodeBatchCreatedEvent.QrcodeCreateItem> buildQrcodeCreateItems(List<QrcodePO> savePos) {
        var qrcodeStyleInstHandler = ApplicationContextHolder.getBean(QrcodeStyleInstHandler.class);
        return savePos.stream()
                .map(po -> new QrcodeBatchCreatedEvent.QrcodeCreateItem(
                        po.getCode(),
                        po.getName(),
                        po.getEntityType(),
                        qrcodeStyleInstHandler.getQrCodeUrl(po.getCode())
                ))
                .collect(Collectors.toList());
    }

    public static List<QrcodeBatchUpdatedEvent.QrcodeUpdateItem> buildQrcodeUpdateItems(List<QrcodePO> updatePos) {
        var qrcodeStyleInstHandler = ApplicationContextHolder.getBean(QrcodeStyleInstHandler.class);
        return updatePos.stream()
                .map(po -> new QrcodeBatchUpdatedEvent.QrcodeUpdateItem(
                        po.getCode(),
                        po.getName(),
                        qrcodeStyleInstHandler.getQrCodeUrl(po.getCode())
                ))
                .collect(Collectors.toList());
    }
}
