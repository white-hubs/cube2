package com.guhecloud.cube.srv.gen.property.management.server.common.service.impl;

import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.OrganizationClientService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrganizationClientServiceImpl implements OrganizationClientService {
    private final OrganizationClient organizationClient;

    @Override
    public String getOrganizationsById(Long id) {
        if (ObjectUtils.isNotEmpty(id)) {
            OrganizationDTO dto = organizationClient.getOrganizationsById(id);
            if (ObjectUtils.isNotEmpty(dto)) {
                return dto.getName();
            }
        }
        return "";
    }

    @Override
    public String getOrganizationsById(String id) {
        if (ObjectUtils.isNotEmpty(id)) {
            OrganizationDTO dto = organizationClient.getOrganizationsById(Long.parseLong(id));
            if (ObjectUtils.isNotEmpty(dto)) {
                return dto.getName();
            }
        }
        return "";
    }
}
