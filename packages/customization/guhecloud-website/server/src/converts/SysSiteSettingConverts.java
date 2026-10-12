package com.guhecloud.cube.customized.guhecloudwebsite.converts;

import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSiteSetting;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteSettingParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSiteSettingVO;

public final class SysSiteSettingConverts {

    private SysSiteSettingConverts() {
    }

    public static SysSiteSetting toEntity(SysSiteSettingParam param) {
        SysSiteSetting entity = new SysSiteSetting();
        entity.setSiteLogo(param.getSiteLogo());
        entity.setSiteName(param.getSiteName());
        entity.setCopyright(param.getCopyright());
        entity.setIcpInfo(param.getIcpInfo());
        entity.setContactPhone(param.getContactPhone());
        entity.setContactEmail(param.getContactEmail());
        entity.setOfficialAccount(param.getOfficialAccount());
        entity.setVideoAccount(param.getVideoAccount());
        entity.setDouyin(param.getDouyin());
        entity.setToutiao(param.getToutiao());
        entity.setZhihu(param.getZhihu());
        entity.setXiaohongshu(param.getXiaohongshu());
        entity.setStatisticsCode(param.getStatisticsCode());
        entity.setUrl(param.getUrl());
        entity.setNavStyle(param.getNavStyle());
        entity.setOgImage(param.getOgImage());
        entity.setFavicon(param.getFavicon());
        entity.setPrimaryColor(param.getPrimaryColor());
        entity.setDefaultTitle(param.getDefaultTitle());
        entity.setDefaultDescription(param.getDefaultDescription());
        entity.setDefaultKeywords(param.getDefaultKeywords());
        entity.setTrackingScripts(param.getTrackingScripts());
        entity.setDeleted(false);
        return entity;
    }

    public static SysSiteSettingVO toVO(SysSiteSetting entity) {
        SysSiteSettingVO vo = new SysSiteSettingVO();
        vo.setId(entity.getId());
        vo.setSiteLogo(entity.getSiteLogo());
        vo.setSiteName(entity.getSiteName());
        vo.setCopyright(entity.getCopyright());
        vo.setIcpInfo(entity.getIcpInfo());
        vo.setContactPhone(entity.getContactPhone());
        vo.setContactEmail(entity.getContactEmail());
        vo.setOfficialAccount(entity.getOfficialAccount());
        vo.setVideoAccount(entity.getVideoAccount());
        vo.setDouyin(entity.getDouyin());
        vo.setToutiao(entity.getToutiao());
        vo.setZhihu(entity.getZhihu());
        vo.setXiaohongshu(entity.getXiaohongshu());
        vo.setStatisticsCode(entity.getStatisticsCode());
        vo.setUrl(entity.getUrl());
        vo.setNavStyle(entity.getNavStyle());
        vo.setOgImage(entity.getOgImage());
        vo.setFavicon(entity.getFavicon());
        vo.setPrimaryColor(entity.getPrimaryColor());
        vo.setDefaultTitle(entity.getDefaultTitle());
        vo.setDefaultDescription(entity.getDefaultDescription());
        vo.setDefaultKeywords(entity.getDefaultKeywords());
        vo.setTrackingScripts(entity.getTrackingScripts());
        return vo;
    }
}
