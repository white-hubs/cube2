package com.guhecloud.cube.customized.guhecloudwebsite.converts;

import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSideIcon;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSideIconParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSideIconVO;

public final class SysSideIconConverts {

    private SysSideIconConverts() {
    }

    public static SysSideIcon toEntity(SysSideIconParam param) {
        SysSideIcon entity = new SysSideIcon();
        entity.setTitle(param.getTitle());
        entity.setIcon(param.getIcon());
        entity.setSummary(param.getSummary());
        entity.setImage(param.getImage());
        entity.setLink(param.getLink());
        entity.setSort(param.getSort());
        entity.setStatus(param.getStatus());
        entity.setIsOpen(param.getIsOpen());
        return entity;
    }

    public static SysSideIconVO toVO(SysSideIcon entity) {
        SysSideIconVO vo = new SysSideIconVO();
        vo.setId(entity.getId());
        vo.setTitle(entity.getTitle());
        vo.setIcon(entity.getIcon());
        vo.setSummary(entity.getSummary());
        vo.setImage(entity.getImage());
        vo.setLink(entity.getLink());
        vo.setSort(entity.getSort());
        vo.setStatus(entity.getStatus());
        vo.setIsOpen(entity.getIsOpen());
        return vo;
    }
}
