package com.guhecloud.cube.srv.gen.park.management.server.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class RoomRentRecordQuery extends Paging {

    private String keyword;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 签订时间
     */
    private String signTimePeriod;

    /**
     * 结束时间
     */
    private String endTimePeriod;
}
