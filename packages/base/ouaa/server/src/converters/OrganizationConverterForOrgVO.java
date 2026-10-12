package com.guhecloud.cube.biz.ouaa.server.converters;

import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.vo.OrganizationVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class OrganizationConverterForOrgVO {
    private OrganizationConverterForOrgVO() {}

    public static OrganizationVO toOrganizationVO(Organization organization) {
        var organizationVO = new OrganizationVO();
        organizationVO.setId(organization.getId());
        organizationVO.setName(organization.getName());
        organizationVO.setRemark(organization.getRemark());
        organizationVO.setMembers(organizationVO.getMembers());
        organizationVO.setEnabled(organization.getEnabled());
        organizationVO.setSequence(organization.getSequence());
        organizationVO.setLevel(organizationVO.getLevel());
        organizationVO.setParentId(organization.getParentId());
        return organizationVO;
    }

    public static List<OrganizationVO> toOrganizationVOS(List<Organization> organizations) {
        if (CollectionUtils.isEmpty(organizations)) {
            return CollectionUtils.emptyList();
        }
        return organizations.stream().map(OrganizationConverterForOrgVO::toOrganizationVO).toList();
    }
}
