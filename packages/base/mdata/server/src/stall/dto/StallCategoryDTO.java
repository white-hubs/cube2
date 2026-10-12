package com.guhecloud.cube.biz.mdata.server.stall.dto;

import lombok.Data;

/**
 * 档位类型
 *
 * @author lzy
 * @date 2021/11/8
 */
@Data
public class StallCategoryDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 档位分类名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
