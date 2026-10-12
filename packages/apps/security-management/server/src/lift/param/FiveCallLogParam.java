package com.guhecloud.cube.srv.gen.securitymanagement.server.lift.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 五方通话记录条件入参对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FiveCallLogParam extends Paging {
    /**
     * 来电时间段
     */
    private Period period;
}
