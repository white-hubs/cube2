package com.guhecloud.cube.customized.guhecloudwebsite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 站点导航实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysSiteNav extends BasicEnhancedTenantEntity<Long> {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 导航名称 */
    private String navName;

    /** 上级导航ID */
    private Long parentId;

    /** 导航图标 */
    private String navIcon;

    /** 导航类型 1页面 2外链 */
    private Integer navType;

    /** 导航地址 */
    private String navUrl;

    /** 排序 */
    private Integer sort;

    /** 打开方式 1当前 2新窗口 */
    private Integer openType;

    /** 状态 1启用 0禁用 */
    private Integer status;

    /** 导航页面名称 */
    private String navPageName;

}
