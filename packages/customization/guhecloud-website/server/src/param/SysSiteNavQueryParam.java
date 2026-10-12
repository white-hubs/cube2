package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

/**
 * 站点导航查询参数
 */
@Data
public class SysSiteNavQueryParam {

    /** 导航名称（模糊） */
    private String navName;

    /** 上级导航ID */
    private Long parentId;

    /** 状态 1启用 0禁用 */
    private Integer status;

}
