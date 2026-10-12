package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

/**
 * 产品线信息
 *
 * @author Woody
 * @date 2021/7/16
 */
@Data
public class ProductLineVO {
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
