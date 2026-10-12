package com.guhecloud.cube.biz.ouaa.server.dto;

import lombok.Data;

@Data
public class ApplicationDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 应用组主键
     */
    private Long applicationGroupId;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用图标
     */
    private String icon;

    /**
     * 应用备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
