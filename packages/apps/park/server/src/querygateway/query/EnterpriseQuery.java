package com.guhecloud.cube.srv.gen.park.management.server.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class EnterpriseQuery extends Paging {

    private String keyword;

    /**
     * 类型
     */
    private String type;

    /**
     * 行业类型ID
     */
    private Long industryTypeId;

    /**
     * 规模
     */
    private String scale;

    private Period period;

}
