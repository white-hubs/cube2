package com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttStaffSequentPageParam extends Paging {
    private Long deptId;
    private String staffName;

}
