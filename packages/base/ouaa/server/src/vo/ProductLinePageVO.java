package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

/**
 * 产品线分页对象
 *
 * @author lanchun
 * @date 2021/10/13 14:01
 */
@Data
public class ProductLinePageVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 产品线名称
     */
    private String name;

    /**
     * 产品线备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
