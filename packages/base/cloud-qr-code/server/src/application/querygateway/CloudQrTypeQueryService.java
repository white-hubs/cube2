package com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway;

import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.query.CloudQrTypeBatchQuery;
import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.query.CloudQrTypeQuery;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.valueobject.QrCodeType;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.CloudQrCodeTypeRepository;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CloudQrTypeQueryService {

    private final CloudQrCodeTypeRepository cloudQrCodeTypeRepository;

    public CloudQrTypeQueryService(CloudQrCodeTypeRepository cloudQrCodeTypeRepository) {
        this.cloudQrCodeTypeRepository = cloudQrCodeTypeRepository;
    }

    public Pagination<QrCodeType> queryCloudQrTypes(CloudQrTypeQuery query) {
        return cloudQrCodeTypeRepository.pageQuery(PagingUtils.convert(query), query.getKeyword(), query.getGrantClientId());
    }

    public List<QrCodeType> batchQueryQrCodeTypes(CloudQrTypeBatchQuery query) {
        return cloudQrCodeTypeRepository.batchQuery(query.getCodes());
    }

    public QrCodeType queryCloudQrTypeById(Long id) {
        return cloudQrCodeTypeRepository.findById(id);
    }
}
