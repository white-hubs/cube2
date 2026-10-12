package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LineLossAlarmRuleQueryParam extends Paging {
    private String name;
    private Long alarmLevelId;
}
