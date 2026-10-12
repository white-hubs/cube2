package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeSituationParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeLedgerPageResult;
import com.guhecloud.platform.base.model.Period;

import java.util.List;
import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-12  10:47
 * @Description:
 * @Version: 1.0
 */
public interface WorkOvertimeLedgerAdvanceServer {
    /**
     * 修改加班预统计台账
     */
    Boolean updateWorkOvertime(String processInstanceId, String status);

    /**
     * 新增加班预统计台账
     */
    Boolean addWorkOvertime(WorkOvertimeSituationParam command, List<AttApproveLogPO> attApproveLog);

    List<WorkOvertimeLedgerPageResult> getWorkOvertimeLedgerByUserIds(Set<Long> userIds, Period period);
}
