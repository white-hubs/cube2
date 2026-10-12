package com.guhecloud.cube.biz.metric.server.metric.library.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MetricLibraryQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;
}
