package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/1 18:12
 * @description
 */
@Data
public class HistoricalHealthValueQuery extends Paging {
    private Long deviceId;
    private Period period;
}
