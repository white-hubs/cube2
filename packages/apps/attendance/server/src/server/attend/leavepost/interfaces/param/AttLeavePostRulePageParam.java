package com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 离岗规则条件对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttLeavePostRulePageParam extends Paging {
    private String name;
}
