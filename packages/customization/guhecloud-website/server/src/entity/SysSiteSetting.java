package com.guhecloud.cube.customized.guhecloudwebsite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网站设置实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysSiteSetting extends BasicEnhancedTenantEntity<Long> {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 网站LOGO */
    private String siteLogo;

    /** 网站名称 */
    private String siteName;

    /** 版权信息 */
    private String copyright;

    /** 备案信息 */
    private String icpInfo;

    /** 联系电话 */
    private String contactPhone;

    /** 联系邮箱 */
    private String contactEmail;

    /** 公众号 */
    private String officialAccount;

    /** 视频号 */
    private String videoAccount;

    /** 抖音 */
    private String douyin;

    /** 头条 */
    private String toutiao;

    /** 知乎 */
    private String zhihu;

    /** 小红书 */
    private String xiaohongshu;

    /** 统计代码 */
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
