package com.guhecloud.cube.biz.mdata.server.area.converters;

import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaListVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class AreaConverterForAreaListVO {
    private AreaConverterForAreaListVO() {

    }

    public static AreaListVO toAreaListVO(Area area) {
        var areaListVO = new AreaListVO();
        areaListVO.setId(area.getId());
        areaListVO.setCode(area.getCode());
        areaListVO.setName(area.getName());
        areaListVO.setParentId(area.getParentId());
        areaListVO.setLevel(area.getLevel());
        areaListVO.setSequence(area.getSequence());
        areaListVO.setBuiltUpArea(area.getBuiltUpArea());
        areaListVO.setAliasName(area.getAliasName());
        areaListVO.setSort(area.getSort());
        areaListVO.setAreaManagerId(area.getAreaManagerId());
        areaListVO.setAreaDepartmentId(area.getAreaDepartmentId());
        areaListVO.setVisualizationAreaType(area.getVisualizationAreaType());
        areaListVO.setStatusId(area.getStatusId());
        areaListVO.setFeatureIds(area.getFeatureIds());
        areaListVO.setTagIds(area.getTagIds());
        return areaListVO;
    }

    public static List<AreaListVO> toAreaListVOS(List<Area> areas) {
        if (CollectionUtils.isEmpty(areas)) {
            return CollectionUtils.emptyList();
        }
        return areas.stream().map(AreaConverterForAreaListVO::toAreaListVO).toList();
    }
}
