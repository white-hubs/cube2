package com.guhecloud.cube.biz.mdata.server.grid.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 批量查询区域网格配置参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GridAreaConfigQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 网格区域id列表（可选，如果不传则查询所有已绑定网格的区域）
     */
    private List<Long> gridAreaIds;
}

