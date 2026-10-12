package com.guhecloud.cube.biz.ouaa.server.dto;

import lombok.Data;

/**
 * 岗位信息
 *
 * @author Woody
 * @date 2021/7/16
 */
@Data
public class PositionDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 岗位名称
     */
    private String name;

    /**
     * 岗位备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
