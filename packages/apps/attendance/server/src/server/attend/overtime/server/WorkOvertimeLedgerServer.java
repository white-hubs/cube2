package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.entity.WorkOvertimeLedger;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeLedgerQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeSituationParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeLedgerDetailPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeLedgerPageResult;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-14  18:24
 * @Description:
 * @Version: 1.0
 */

public interface WorkOvertimeLedgerServer {
    /**
     * 查询加班台账
     *
     * @author: lijie
     * @date: 2023/12/15 10:15
     * @param: Pagination<WorkOvertimeLedgerPageResult>
     * @return: WorkOvertimeLedgerQueryParam
     **/
    @GetMapping("/getWorkOvertimeLedgerPage")
    Pagination<WorkOvertimeLedgerPageResult> getWorkOvertimeLedgerPage(WorkOvertimeLedgerQueryParam param);

    /**
     * 加班台账导出
     */
    @GetMapping("/getWorkOvertimeLedgerExcel")
    void getWorkOvertimeLedgerExcel(HttpServletRequest request, HttpServletResponse response, WorkOvertimeLedgerQueryParam param);

    /**
     * 查询加班台账详情
     *
     * @author: lijie
     * @date: 2023/12/15 10:15
     * @param: Pagination<WorkOvertimeLedgerPageResult>
     * @return: WorkOvertimeLedgerQueryParam
     **/
    @GetMapping("/getWorkOvertimeLedgerDetailsPage")
    Pagination<WorkOvertimeLedgerDetailPageResult> getWorkOvertimeLedgerDetailsPage(WorkOvertimeLedgerQueryParam param);

    /**
     * 加班台账详情导出
     */
    @GetMapping("/getWorkOvertimeLedgerDetailsExcel")
    void getWorkOvertimeLedgerDetailsExcel(HttpServletRequest request, HttpServletResponse response, WorkOvertimeLedgerQueryParam param);

    /**
     * 新增加班台账
     */
    Boolean addWorkOvertime(WorkOvertimeSituationParam command, List<AttApproveLogPO> attApproveLog);

    WorkOvertimeLedger getWorkOvertimeLedger(LocalDateTime startTime, LocalDateTime endTime, WorkOvertimeSituationParam command,
                                             AttApproveLogPO attApproveLog, String compensate
                                            );
}
