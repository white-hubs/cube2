package com.guhecloud.cube.biz.metric.server.score.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SafetyReportQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;
}
