package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttGroupStaffQueryParam extends Paging {
    private Long deptId;
    private Long groupId;
    private String staffName;
    private List<Long> groupIdList;
}
