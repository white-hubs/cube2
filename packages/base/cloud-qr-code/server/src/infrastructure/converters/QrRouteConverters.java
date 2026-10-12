package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters;

import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.ModifyQrRouteCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.QrRoute;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.po.CloudQrRoutePO;
import com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo.CloudQrRouteVO;

/**
 * 二维码路由转换工具类
 */
public final class QrRouteConverters {

    private QrRouteConverters() {
    }

    public static QrRoute commandToEntity(ModifyQrRouteCommand command) {
        if (command == null) {
            return null;
        }
        var entity = new QrRoute();
        entity.setTargetUrl(command.getTargetUrl());
        entity.setTargetParams(command.getTargetParams());
        return entity;
    }

    public static CloudQrRoutePO entityToPO(QrRoute entity, Long qrId, String qrShortCode) {
        if (entity == null) {
            return null;
        }
        var po = new CloudQrRoutePO();
        po.setQrCodeId(qrId);
        po.setShortCode(qrShortCode);
        po.setTargetUrl(entity.getTargetUrl());
        po.setTargetParams(entity.getTargetParams());
        return po;
    }

    public static QrRoute poToEntity(CloudQrRoutePO po) {
        if (po == null) {
            return null;
        }
        var entity = new QrRoute();
        entity.setTargetUrl(po.getTargetUrl());
        entity.setTargetParams(po.getTargetParams());
        return entity;
    }

    public static CloudQrRouteVO entityToVO(QrRoute entity) {
        if (entity == null) {
            return null;
        }
        var vo = new CloudQrRouteVO();
        vo.setTargetUrl(entity.getTargetUrl());
        vo.setTargetParams(entity.getTargetParams());
        return vo;
    }
}
