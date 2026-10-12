package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttClockRulesPageParam extends Paging {
    private String name;
}
