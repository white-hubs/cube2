package com.guhecloud.cube.srv.gen.cabbage.server.vo.dto;

import lombok.Data;

/**
 * 大白菜商品品类表
 */
@Data
public class DbcGoodsCategoryDTO {
    private Long id;
    /**
     * //市场序号
     */
    private Integer marketCode;
    /**
     * //市场名称
     */
    private String marketName;
    /**
     * 品类序号
     */
    private String oid;
    /**
     * 品类编号
     */
    private String code;
    /**
     * 品类名称
     */
    private String name;
    /**
     * 上级序号
     */
    private String parent;
    /**
     * 上级名称
     */
    private String parentName;
    /**
     * 等级
     */
    private String lev;
}
