package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import lombok.Data;

/**
 * 网站设置VO
 */
@Data
public class SysSiteSettingVO {

    private Long id;
    private String siteLogo;
    private String siteName;
    private String copyright;
    private String icpInfo;
    private String contactPhone;
    private String contactEmail;
    private String officialAccount;
    private String videoAccount;
    private String douyin;
    private String toutiao;
    private String zhihu;
    private String xiaohongshu;
    private String statisticsCode;
    private String ogImage;
    private String favicon;
    private String primaryColor;
    private String defaultTitle;
    private String defaultDescription;
    private String defaultKeywords;
    private String trackingScripts;

    /** 网站地址 */
    private String url;

    /** 导航风格 light/dark */
    private String navStyle;
}
