package com.guhecloud.cube.biz.mdata.client.dto;

import lombok.Data;

/**
 * 区域关联bim返回参数
 *
 * @author lanchun
 * @date 2021/11/29 10:33
 */
@Data
public class AreaBimListDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 上级主键
     */
    private Long parentId;

    /**
     * 树层级
     */
    private Integer level;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域编号
     */
    private String code;

    /**
     * bim楼层名称
     */
    private String bimLevelName;

    /**
     * 导航配置
     */
    private String navigationConfig;
}
