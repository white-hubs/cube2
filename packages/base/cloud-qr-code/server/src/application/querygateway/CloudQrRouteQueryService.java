package com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway;

import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.QrRoute;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.CloudQrCodeRouteRepository;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 云二维码路由查询服务
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrRouteQueryService {

    private final CloudQrCodeRouteRepository cloudQrCodeRouteRepository;

    /**
     * 根据短码查询路由配置
     *
     * @param shortCode 短码
     *
     * @return 路由配置
     */
    public QrRoute queryRouteByShortCode(String shortCode) {
        QrRoute qrRoute = cloudQrCodeRouteRepository.findByShortCode(shortCode);
        Assert.isNotFound(Objects.isNull(qrRoute), "二维码路由不存在");
        return qrRoute;
    }
}
