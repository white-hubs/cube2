package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-10-30  16:46
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttClockInRecordslogResult {
    /**
     * 审批信息
     */
    private List<AttApproveLogPO> applyDetails;
    /**
     * 班次信息
     */
    private List<ClockInInfoClassesResult> classesDetails;
}
