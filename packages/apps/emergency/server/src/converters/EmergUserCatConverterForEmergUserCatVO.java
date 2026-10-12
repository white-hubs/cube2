package com.guhecloud.cube.biz.emergency.server.converters;

import com.guhecloud.cube.biz.emergency.server.entity.EmergencyUserCategory;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyUserCategoryVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class EmergUserCatConverterForEmergUserCatVO {
    private EmergUserCatConverterForEmergUserCatVO() {

    }

    public static EmergencyUserCategoryVO toEmergencyUserCatVO(EmergencyUserCategory emergencyUserCategory) {
        var emergencyUserCategoryVO = new EmergencyUserCategoryVO();
        emergencyUserCategoryVO.setId(emergencyUserCategory.getId());
        emergencyUserCategoryVO.setName(emergencyUserCategory.getName());
        emergencyUserCategoryVO.setParentId(emergencyUserCategory.getParentId());
        emergencyUserCategoryVO.setLevel(emergencyUserCategory.getLevel());
        emergencyUserCategoryVO.setSequence(emergencyUserCategory.getSequence());
        return emergencyUserCategoryVO;
    }

    public static List<EmergencyUserCategoryVO> toEmergencyUserCatVOS(List<EmergencyUserCategory> emergencyUserCategories) {
        if (CollectionUtils.isEmpty(emergencyUserCategories)) {
            return CollectionUtils.emptyList();
        }
        return emergencyUserCategories.stream().map(EmergUserCatConverterForEmergUserCatVO::toEmergencyUserCatVO).toList();
    }
}
