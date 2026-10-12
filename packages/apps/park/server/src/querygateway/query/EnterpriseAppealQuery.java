package com.guhecloud.cube.srv.gen.park.management.server.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class EnterpriseAppealQuery extends Paging {

    private String keyword;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 诉求类型ID
     */
    private Long appealTypeId;

    private Period period;

}
