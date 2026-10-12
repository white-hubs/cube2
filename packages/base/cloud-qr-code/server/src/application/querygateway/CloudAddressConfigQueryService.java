package com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway;

import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.AddressConfig;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.CloudAddressConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 云地址配置QueryService
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudAddressConfigQueryService {

    private final CloudAddressConfigRepository cloudAddressConfigRepository;

    /**
     * 查询云地址配置
     *
     * @return 云地址配置实体，如果不存在则返回null
     */
    public AddressConfig queryAddressConfig() {
        return cloudAddressConfigRepository.find();
    }
}
