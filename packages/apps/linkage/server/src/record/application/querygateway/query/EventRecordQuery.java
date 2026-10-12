package com.guhecloud.cube.biz.linkage.server.record.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventRecordQuery extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 报警级别
     */
    private String level;

    /**
     * 时间范围
     */
    private Period period;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 二次确认结果
     */
    private String assistConfirmResult;
}
