package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

import java.util.List;

/**
 * 产品（系统）信息
 *
 * @author Woody
 * @date 2021/7/14
 */
@Data
public class ProductVO {
    /**
     * 产品主键
     */
    private Long id;

    /**
     * 产品编号
     */
    private String code;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品域名
     */
    private String domain;

    /**
     * 产品备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    private List<ApplicationVO> applicationVOS;

}
