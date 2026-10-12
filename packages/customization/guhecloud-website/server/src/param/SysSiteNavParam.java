package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

/**
 * 站点导航新增/编辑参数
 */
@Data
public class SysSiteNavParam {

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

    /** 导航页面名称 */
    private String navPageName;
    /** 状态 1启用 0禁用 */
    private Integer status;

}
