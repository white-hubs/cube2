package com.guhecloud.cube.customized.guhecloudwebsite.converts;

import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSiteNav;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteNavParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSiteNavVO;

public final class SysSiteNavConverts {

    private SysSiteNavConverts() {
    }

    public static SysSiteNav toEntity(SysSiteNavParam param) {
        SysSiteNav entity = new SysSiteNav();
        entity.setNavName(param.getNavName());
        entity.setParentId(param.getParentId());
        entity.setNavIcon(param.getNavIcon());
        entity.setNavType(param.getNavType());
        entity.setNavUrl(param.getNavUrl());
        entity.setSort(param.getSort());
        entity.setOpenType(param.getOpenType());
        entity.setNavPageName(param.getNavPageName());
        entity.setStatus(param.getStatus());
        return entity;
    }

    public static SysSiteNavVO toVO(SysSiteNav entity) {
        SysSiteNavVO vo = new SysSiteNavVO();
        vo.setId(entity.getId());
        vo.setNavName(entity.getNavName());
        vo.setParentId(entity.getParentId());
        vo.setNavIcon(entity.getNavIcon());
        vo.setNavType(entity.getNavType());
        vo.setNavUrl(entity.getNavUrl());
        vo.setSort(entity.getSort());
        vo.setOpenType(entity.getOpenType());
        vo.setStatus(entity.getStatus());
        vo.setNavPageName(entity.getNavPageName());
        return vo;
    }
}
