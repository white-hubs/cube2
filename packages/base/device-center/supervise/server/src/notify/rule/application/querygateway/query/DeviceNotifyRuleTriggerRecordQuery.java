package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceNotifyRuleTriggerRecordQuery extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 时间范围
     */
    private Period period;
}
