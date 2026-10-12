package com.guhecloud.cube.srv.gen.library.management.server.vo.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 编目综合统计
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TidyUpStatisticsPageParam extends Paging {
    private static final long serialVersionUID = 4703465840044999900L;
    // 操作员
    private String operator;
    // 批次
    private String tidyBatch;
    // 编目状态
    private Integer status;
}
