package com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class AlarmReasonConfigQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;
}
