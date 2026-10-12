package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.controller;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param.WorkOvertimeLedgerQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeLedgerDetailPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeLedgerPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.WorkOvertimeLedgerServer;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-14  18:22
 * @Description: 加班台账
 * @Version: 1.0
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkOvertimeLedgerController {
    private final WorkOvertimeLedgerServer workOvertimeLedgerServer;

    /**
     * 查询加班台账
     *
     * @author: lijie
     * @date: 2023/12/15 10:15
     * @param: Pagination<WorkOvertimeLedgerPageResult>
     * @return: WorkOvertimeLedgerQueryParam
     **/
    @GetMapping("/getWorkOvertimeLedgerPage")
    public Pagination<WorkOvertimeLedgerPageResult> getWorkOvertimeLedgerPage(WorkOvertimeLedgerQueryParam param) {
        return workOvertimeLedgerServer.getWorkOvertimeLedgerPage(param);
    }

    /**
     * 加班台账导出
     */
    @GetMapping("/getWorkOvertimeLedgerExcel")
    public void getWorkOvertimeLedgerExcel(HttpServletRequest request, HttpServletResponse response, WorkOvertimeLedgerQueryParam param) {
        workOvertimeLedgerServer.getWorkOvertimeLedgerExcel(request, response, param);
    }

    /**
     * 查询加班台账详情
     *
     * @author: lijie
     * @date: 2023/12/15 10:15
     * @param: Pagination<WorkOvertimeLedgerPageResult>
     * @return: WorkOvertimeLedgerQueryParam
     **/
    @GetMapping("/getWorkOvertimeLedgerDetailsPage")
    public Pagination<WorkOvertimeLedgerDetailPageResult> getWorkOvertimeLedgerDetailsPage(WorkOvertimeLedgerQueryParam param) {
        Assert.isParameterError(StringUtils.isBlank(param.getWorkType()), "加班类型不能为空");
        Assert.isParameterError(Objects.isNull(param.getPeriod()), "加班时间不能为空");
        Assert.isParameterError(Objects.isNull(param.getUserId()), "用户id不能为空");
        return workOvertimeLedgerServer.getWorkOvertimeLedgerDetailsPage(param);
    }

    /**
     * 加班台账详情导出
     */
    @GetMapping("/getWorkOvertimeLedgerDetailsExcel")
    public void getWorkOvertimeLedgerDetailsExcel(HttpServletRequest request, HttpServletResponse response,
                                                  WorkOvertimeLedgerQueryParam param
                                                 ) {
        workOvertimeLedgerServer.getWorkOvertimeLedgerDetailsExcel(request, response, param);
    }

}
