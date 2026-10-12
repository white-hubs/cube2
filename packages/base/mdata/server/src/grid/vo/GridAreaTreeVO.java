package com.guhecloud.cube.biz.mdata.server.grid.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/14 16:36
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GridAreaTreeVO extends Tree.Simple<Long, GridAreaTreeVO> {
    /**
     * 区域id
     */
    private Long id;
    /**
     * 区域名称
     */
    private String name;
}
