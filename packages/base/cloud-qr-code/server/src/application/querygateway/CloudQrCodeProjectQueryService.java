package com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway;

import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.query.CloudQrCodeProjectQuery;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.CloudQrCodeProject;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.CloudQrCodeProjectRepository;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 云二维码项目查询服务
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeProjectQueryService {

    private final CloudQrCodeProjectRepository repository;

    public Pagination<CloudQrCodeProject> queryProjects(CloudQrCodeProjectQuery query) {
        return repository.pageQuery(PagingUtils.convert(query), query.getKeyword());
    }

    public List<CloudQrCodeProject> queryProjectsList(CloudQrCodeProjectQuery query) {
        return repository.listQuery(query.getKeyword());
    }

    public CloudQrCodeProject queryById(Long id) {
        return repository.findById(id);
    }
}
