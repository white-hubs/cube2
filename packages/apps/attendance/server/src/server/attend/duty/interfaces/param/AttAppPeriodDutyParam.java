package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class AttAppPeriodDutyParam {
    private String month;
    private Period period;
}
