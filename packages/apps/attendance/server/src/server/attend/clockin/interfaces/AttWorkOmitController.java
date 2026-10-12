package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway.AttWorkOmitApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.WorkOmitParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.WorkOmitDetailsResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.WorkOmitResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 补卡台账
 *
 * @Author: lijie
 * @CreateTime: 2023-11-13  10:17
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttWorkOmitController {
    private final AttWorkOmitApplication workOmitApplication;

    /**
     * 补卡统计
     */
    @GetMapping("/getWorkOmitByPage")
    public Pagination<WorkOmitResult> getWorkOmitByPage(WorkOmitParam param) {
        return workOmitApplication.getWorkOmitByPage(param);
    }

    /**
     * 补卡明细
     */
    @GetMapping("/getWorkOmitDetails/page")
    public Pagination<WorkOmitDetailsResult> getWorkOmitDetailsByPage(WorkOmitParam param) {
        return workOmitApplication.getWorkOmitDetailsByPage(param);
    }

    @GetMapping("/getWorkOmitByPage/excel")
    public void getWorkOmitExcel(HttpServletRequest request, HttpServletResponse response, WorkOmitParam param) {
        workOmitApplication.getWorkOmitExcel(request, response, param);
    }

    @GetMapping("/getWorkOmitDetails/excel")
    public void getWorkOmitDetailsExcel(HttpServletRequest request, HttpServletResponse response, WorkOmitParam param) {
        workOmitApplication.getWorkOmitDetailsExcel(request, response, param);
    }
}
