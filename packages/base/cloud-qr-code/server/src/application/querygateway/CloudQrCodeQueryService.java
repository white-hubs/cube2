package com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway;

import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.query.CloudQrCodeQuery;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.QrCode;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.CloudQrCodeRepository;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.CloudQrCodeRouteRepository;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class CloudQrCodeQueryService {

    private final CloudQrCodeRepository cloudQrCodeRepository;

    private final CloudQrCodeRouteRepository cloudQrCodeRouteRepository;

    public CloudQrCodeQueryService(CloudQrCodeRepository cloudQrCodeRepository, CloudQrCodeRouteRepository cloudQrCodeRouteRepository) {
        this.cloudQrCodeRepository = cloudQrCodeRepository;
        this.cloudQrCodeRouteRepository = cloudQrCodeRouteRepository;
    }

    public Pagination<QrCode> queryCloudQrCodes(CloudQrCodeQuery query) {
        return cloudQrCodeRepository.pageQuery(PagingUtils.convert(query), query.getKeyword(), query.getGrantClientId());
    }

    public QrCode queryCloudQrCodeById(Long id) {
        var qrCode = cloudQrCodeRepository.findById(id);
        if (Objects.isNull(qrCode)) {
            return null;
        }
        var qrRoute = cloudQrCodeRouteRepository.findByQrCodeId(qrCode.getId());
        qrCode.setQrRoute(qrRoute);
        return qrCode;
    }

    /**
     * 通过code查询二维码
     *
     * @param code 二维码code
     *
     * @return 二维码实体，不存在返回null
     */
    public QrCode queryCloudQrCodeByCode(String code) {
        var qrCode = cloudQrCodeRepository.findByGrantClientIdAndCode(
                SecurityContextHolder.getPrincipalProperty(Principal::getAccount),
                code);
        if (Objects.isNull(qrCode)) {
            return null;
        }
        var qrRoute = cloudQrCodeRouteRepository.findByQrCodeId(qrCode.getId());
        qrCode.setQrRoute(qrRoute);
        return qrCode;
    }

    /**
     * 通过codes批量查询二维码
     *
     * @param codes 二维码code列表
     *
     * @return 二维码实体
     */
    public List<QrCode> queryCloudQrCodeByCodes(Collection<String> codes) {
        return cloudQrCodeRepository.findByGrantClientIdAndCodes(SecurityContextHolder.getPrincipalProperty(Principal::getAccount),
                                                                 codes);
    }

    /**
     * 通过短码查询二维码
     *
     * @param shortCode 短码
     *
     * @return 二维码实体
     */
    public QrCode getByShortCode(String shortCode) {
        var qrCode = cloudQrCodeRepository.findByShortCode(shortCode);
        if (Objects.isNull(qrCode)) {
            return null;
        }
        var qrRoute = cloudQrCodeRouteRepository.findByQrCodeId(qrCode.getId());
        qrCode.setQrRoute(qrRoute);
        return qrCode;
    }
}
