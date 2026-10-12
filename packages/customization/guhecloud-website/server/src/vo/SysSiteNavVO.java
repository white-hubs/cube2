package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import lombok.Data;

/**
 * 站点导航VO
 */
@Data
public class SysSiteNavVO {

    private Long id;
    private String navName;
    private Long parentId;
    private String navIcon;
    private Integer navType;
    private String navUrl;
    private Integer sort;
    private Integer openType;
    private Integer status;
    private String navPageName;

}
