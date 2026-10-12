package com.guhecloud.cube.srv.gen.energymanagement.server.converters;

import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectTreeVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class EnergyProjectConverterForEnergyProjectTreeVO {
    private EnergyProjectConverterForEnergyProjectTreeVO() {

    }

    public static EnergyProjectTreeVO toEnergyProjectTreeVO(EnergyProject energyProject) {
        var energyProjectTreeVO = new EnergyProjectTreeVO();
        energyProjectTreeVO.setId(energyProject.getId());
        energyProjectTreeVO.setParentId(energyProject.getParentId());
        energyProjectTreeVO.setLevel(energyProject.getLevel());
        energyProjectTreeVO.setProjectName(energyProject.getProjectName());
        return energyProjectTreeVO;
    }

    public static List<EnergyProjectTreeVO> toEnergyProjectTreeVOS(List<EnergyProject> energyProjects) {
        if (CollectionUtils.isEmpty(energyProjects)) {
            return CollectionUtils.emptyList();
        }
        return energyProjects.stream().map(EnergyProjectConverterForEnergyProjectTreeVO::toEnergyProjectTreeVO).toList();
    }
}
