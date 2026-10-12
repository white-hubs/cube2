package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取在岗/离岗人员列表入参对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttWorkStaffPageParam extends Paging {
    private Boolean workBool;
}
