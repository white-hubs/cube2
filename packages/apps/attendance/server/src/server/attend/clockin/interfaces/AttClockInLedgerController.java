package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway.AttClockInLedgerApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockInLedgerPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInLedgerResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 打卡台账
 *
 * @Author: lijie
 * @CreateTime: 2023-09-26  14:03
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClockInLedgerController {
    private final AttClockInLedgerApplication attClockInLedger;

    /**
     * 打卡台账列表
     */
    @GetMapping("/att/clockIn/ledgerPage")
    @DataPermissionSwitch(dataModel = "attendance:attClockInLedger")
    public Pagination<AttClockInLedgerResult> getClockInLedger(AttClockInLedgerPageParam param) {
        return attClockInLedger.getClockInLedger(param);
    }

    /**
     * 打卡台账导出
     *
     * @author: lijie
     * @date: 2023/11/14 9:19
     * @param: void
     * @return: 文件流
     **/
    @GetMapping("/ledger/getClockInLedgerExcel")
    @DataPermissionSwitch(dataModel = "attendance:attClockInLedger")
    public void getClockInLedgerExcel(HttpServletRequest request, HttpServletResponse response, AttClockInLedgerPageParam param) {
        attClockInLedger.getClockInLogExcel(request, response, param);
    }

    /**
     * 考勤每日统计列表
     */
    @GetMapping("/daily/ledger/getLedgerDetails")
    @DataPermissionSwitch(dataModel = "attendance:attClockInLedger")
    public Pagination<AttClockInLedgerResult> getLedgerDetails(AttClockInLedgerPageParam param) {
        return attClockInLedger.getClockInLedger(param, true);
    }

    /**
     * 考勤每日统计导出
     *
     * @author: lijie
     * @date: 2024/1/9 13:59
     * @param:
     * @return:
     **/
    @GetMapping("/daily/ledger/getLedgerDetailsExcel")
    @DataPermissionSwitch(dataModel = "attendance:attClockInLedger")
    public void getLedgerDetailsExcel(HttpServletRequest request, HttpServletResponse response, AttClockInLedgerPageParam param) {
        attClockInLedger.getLedgerDetailsExcel(request, response, param);
    }
}
