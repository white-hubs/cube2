package com.guhecloud.cube.biz.emergency.server.converters;

import com.guhecloud.cube.biz.emergency.server.entity.EmergencyOrganization;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyOrganizationVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class EmergOrgConverterForEmergOrgVO {
    private EmergOrgConverterForEmergOrgVO() {

    }

    public static EmergencyOrganizationVO toEmergencyOrgVO(EmergencyOrganization emergencyOrganization) {
        var emergencyOrganizationVO = new EmergencyOrganizationVO();
        emergencyOrganizationVO.setId(emergencyOrganization.getId());
        emergencyOrganizationVO.setName(emergencyOrganization.getName());
        emergencyOrganizationVO.setParentId(emergencyOrganization.getParentId());
        emergencyOrganizationVO.setLevel(emergencyOrganization.getLevel());
        emergencyOrganizationVO.setSequence(emergencyOrganization.getSequence());
        return emergencyOrganizationVO;
    }

    public static List<EmergencyOrganizationVO> toEmergencyOrgVOS(List<EmergencyOrganization> emergencyOrganizations) {
        if (CollectionUtils.isEmpty(emergencyOrganizations)) {
            return CollectionUtils.emptyList();
        }
        return emergencyOrganizations.stream().map(EmergOrgConverterForEmergOrgVO::toEmergencyOrgVO).toList();
    }
}
