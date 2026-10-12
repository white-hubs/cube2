package com.guhecloud.cube.srv.gen.energymanagement.server.common;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.energymanagement.server.converters.AreaProjectConverterForAreaProjectTreeVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AreaProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyGate;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.AreaProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyGateService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AreaProjectTreeVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommonAreaData {

    public static String getAreaData(String instrumentType) {

        AreaProjectService areaProjectService = ApplicationContextHolder.getBean(AreaProjectService.class);
        EnergyGateService energyGateService = ApplicationContextHolder.getBean(EnergyGateService.class);
        List<AreaProject> list = new ArrayList<>();
        if (StringUtils.isNotBlank(instrumentType)) {
            list = areaProjectService.list(Wrappers.<AreaProject>lambdaQuery()
                                                   .eq(StringUtils.isNotBlank(instrumentType), AreaProject::getInstrumentType, instrumentType));
        } else {
            List<EnergyGate> energyGates = energyGateService.list(Wrappers.<EnergyGate>lambdaQuery()
                                                                          .eq(EnergyGate::getOpen, true)
                                                                          .ne(EnergyGate::getEnergyType, InstrumentTypeEnum.ENERGY_METER.getCode()));
            if (ListUtils.nonEmpty(energyGates)) {
                List<String> collect = energyGates.stream().map(EnergyGate::getEnergyType).collect(Collectors.toList());
                list = areaProjectService.list(Wrappers.<AreaProject>lambdaQuery()
                                                       .in(AreaProject::getInstrumentType, collect));
            }

        }
        String dataIds = "and (r.areaId==\"11111\")";
        if (CollectionUtils.isNotEmpty(list)) {
            List<Long> ids = list.stream().flatMap(d -> Arrays.stream(d.getSequence().split("-"))).distinct().map(Long::parseLong)
                    .collect(Collectors.toList());
            List<AreaProject> datas = areaProjectService.list(Wrappers.<AreaProject>lambdaQuery()
                                                                      .in(AreaProject::getId, ids));
            List<AreaProjectTreeVO> areas = AreaProjectConverterForAreaProjectTreeVO.toAreaProjectTreeVOS(datas);
            List<String> areaOrRouteIds = new ArrayList<>();
            List<String> areaIds = getAreaIds(ids, areas, areaOrRouteIds);
            if (ListUtils.nonEmpty(areaIds)) {
                String collect = areaIds.stream().map(d -> "r.areaId==\"" + d + "\"").collect(Collectors.joining(" or "));
                dataIds = "and (" + collect + ")";
            }
        }
        return dataIds;
    }

    public static List<String> getAreaIds(List<Long> ids, List<AreaProjectTreeVO> treeIds, List<String> areaOrRouteIds) {
        for (AreaProjectTreeVO treeId : treeIds) {
            if (ids.contains(treeId.getId())) {
                areaOrRouteIds.add(treeId.getId().toString());
            } else if (ListUtils.nonEmpty(treeId.getChildren())) {
                getAreaIds(ids, treeId.getChildren(), areaOrRouteIds);
            }
        }
        return areaOrRouteIds;
    }
}
