package com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzy
 * @date 2022/5/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TechnicalNoticeCollectionQuery extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 时间
     */
    private Period period;

}
