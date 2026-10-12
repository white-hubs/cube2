package com.guhecloud.cube.biz.mdata.server.goods.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GoodsCategorySyncDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 商品分类编号
     */
    private String code;

    /**
     * 商品分类名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 上级主键
     */
    private Long parentId;

    /**
     * 树层级
     */
    private Integer level;

    /**
     * 序列号
     */
    private String sequence;
    private Integer tenant;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private Boolean deleted;
}
