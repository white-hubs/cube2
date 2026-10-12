package com.guhecloud.cube.customized.guhecloudwebsite.converts;

import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSitePage;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSitePageParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSitePageVO;

/**
 * 页面管理对象转换
 */
public final class SysSitePageConverts {

    private SysSitePageConverts() {
    }

    public static SysSitePage toEntity(SysSitePageParam param) {
        SysSitePage entity = new SysSitePage();
        entity.setId(param.getId());
        entity.setPageName(param.getPageName());
        entity.setPageTitle(param.getPageTitle());
        entity.setPageRoute(param.getPageRoute());
        entity.setLayoutId(param.getLayoutId());
        entity.setMetaNavStyle(param.getMetaNavStyle());
        entity.setMetaTitle(param.getMetaTitle());
        entity.setMetaDescription(param.getMetaDescription());
        entity.setMetaKeywords(param.getMetaKeywords());
        entity.setComponents(param.getComponents());
        entity.setDeleted(Boolean.FALSE);
        entity.setComponents(param.getComponents());
        entity.setStatus(param.getStatus());
        return entity;
    }

    public static SysSitePageVO toPageVO(SysSitePage entity) {
        SysSitePageVO vo = new SysSitePageVO();
        vo.setId(entity.getId());
        vo.setPageName(entity.getPageName());
        vo.setPageTitle(entity.getPageTitle());
        vo.setPageRoute(entity.getPageRoute());
        vo.setLayoutId(entity.getLayoutId());
        vo.setMetaNavStyle(entity.getMetaNavStyle());
        vo.setMetaTitle(entity.getMetaTitle());
        vo.setMetaDescription(entity.getMetaDescription());
        vo.setMetaKeywords(entity.getMetaKeywords());
        vo.setUpdateTime(entity.getUpdateTime());
        vo.setCreateTime(entity.getCreateTime());
        vo.setDeleted(entity.getDeleted());
        vo.setStatus(entity.getStatus());
        return vo;
    }

    public static SysSitePageVO toVO(SysSitePage entity) {
        SysSitePageVO vo = new SysSitePageVO();
        vo.setId(entity.getId());
        vo.setPageName(entity.getPageName());
        vo.setPageTitle(entity.getPageTitle());
        vo.setPageRoute(entity.getPageRoute());
        vo.setLayoutId(entity.getLayoutId());
        vo.setMetaNavStyle(entity.getMetaNavStyle());
        vo.setMetaTitle(entity.getMetaTitle());
        vo.setMetaDescription(entity.getMetaDescription());
        vo.setMetaKeywords(entity.getMetaKeywords());
        vo.setComponents(entity.getComponents());
        vo.setUpdateTime(entity.getUpdateTime());
        vo.setCreateTime(entity.getCreateTime());
        vo.setDeleted(entity.getDeleted());
        vo.setComponents(entity.getComponents());
        vo.setStatus(entity.getStatus());
        return vo;
    }

}
