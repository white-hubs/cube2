package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 台账查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttLedgerPageParam extends Paging {
    private Long id;
    private Long deptId;
    private Long groupId;
    private String staffName;
    private LocalDateTime startDay;
    private LocalDateTime endDay;
}
