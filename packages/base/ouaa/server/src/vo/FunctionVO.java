package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

/**
 * 功能返回参数
 *
 * @author lanchun
 * @date 2021/12/24 10:12
 */
@Data
public class FunctionVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 功能编号
     */
    private String code;

    /**
     * 功能名称
     */
    private String name;

    /**
     * 菜单主键
     */
    private Long menuId;

    /**
     * 功能备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
