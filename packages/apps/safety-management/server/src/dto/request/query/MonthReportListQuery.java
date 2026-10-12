package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 月报查询
 * @date 2023/9/12 9:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MonthReportListQuery extends Paging {
    /**
     * 月报标题
     */
    private String title;
}
