package com.guhecloud.cube.srv.gen.library.management.server.vo.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 操作员工作量
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TidyUpRecordPageParam extends Paging {
    private static final long serialVersionUID = 3783853708946359553L;
    // 操作员
    private String name;
    // 编目日期段
    private Period period;
    // 图书类型
    private String bookType;
}
