package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

/**
 * 应用端分页对象
 *
 * @author lanchun
 * @date 2021/10/13 19:29
 */
@Data
public class TerminalPageVO {

    private Long id;

    /**
     * 产品主键
     */
    private Long productId;

    /**
     * 访问端编号
     */
    private String code;

    /**
     * 访问端名称
     */
    private String name;

    /**
     * 访问端备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
