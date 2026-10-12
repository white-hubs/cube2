package com.guhecloud.cube.biz.ouaa.server.converters;

import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class OrganizationConverterForUserVOOrg {
    private OrganizationConverterForUserVOOrg() {}

    public static UserVO.Organization toUserVOOrganization(Organization organization) {
        var userVOOrganization = new UserVO.Organization();
        userVOOrganization.setId(organization.getId());
        userVOOrganization.setName(organization.getName());
        return userVOOrganization;
    }

    public static List<UserVO.Organization> toUserVOOrganizations(List<Organization> organizations) {
        if (CollectionUtils.isEmpty(organizations)) {
            return CollectionUtils.emptyList();
        }
        return organizations.stream().map(OrganizationConverterForUserVOOrg::toUserVOOrganization).toList();
    }
}
