package com.guhecloud.cube.srv.gen.energymanagement.server.converters;

import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AreaProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AreaProjectTreeVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class AreaProjectConverterForAreaProjectTreeVO {
    private AreaProjectConverterForAreaProjectTreeVO() {

    }

    public static AreaProjectTreeVO toAreaProjectTreeVO(AreaProject areaProject) {
        var areaProjectTreeVO = new AreaProjectTreeVO();
        areaProjectTreeVO.setId(areaProject.getId());
        areaProjectTreeVO.setParentId(areaProject.getParentId());
        areaProjectTreeVO.setLevel(areaProject.getLevel());
        areaProjectTreeVO.setAreaName(areaProject.getAreaName());
        areaProjectTreeVO.setInstrumentType(areaProject.getInstrumentType());
        areaProjectTreeVO.setSequence(areaProject.getSequence());
        areaProjectTreeVO.setRemark(areaProject.getRemark());
        return areaProjectTreeVO;
    }

    public static List<AreaProjectTreeVO> toAreaProjectTreeVOS(List<AreaProject> areaProjects) {
        if (CollectionUtils.isEmpty(areaProjects)) {
            return CollectionUtils.emptyList();
        }
        return areaProjects.stream().map(AreaProjectConverterForAreaProjectTreeVO::toAreaProjectTreeVO).toList();
    }
}
