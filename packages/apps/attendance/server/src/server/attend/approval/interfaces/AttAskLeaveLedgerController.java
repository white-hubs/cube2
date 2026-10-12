package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttAskLeaveLedgerQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param.AttAskLeaveLedgerPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result.AttAskLeaveLedgerPageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 请假台账
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAskLeaveLedgerController {

    private final AttAskLeaveLedgerQueryService attAskLeaveLedgerQueryService;

    /**
     * 请假台账分页列表
     *
     * @param param 条件入参
     *
     * @return 分页列表对象
     */
    @GetMapping("/att/askLeave/ledger/page")
    public AttAskLeaveLedgerPageResult askLeaveLedgerPage(AttAskLeaveLedgerPageParam param) {

        return attAskLeaveLedgerQueryService.askLeaveLedgerPage(param);
    }

    /**
     * 导出请假台账
     *
     * @param param    条件对象
     * @param request  请求
     * @param response 响应
     */
    @PostMapping("/att/askLeave/ledger/export")
    public void exportLeaveLedger(@RequestBody AttAskLeaveLedgerPageParam param, HttpServletRequest request, HttpServletResponse response) {
        attAskLeaveLedgerQueryService.exportLeaveLedger(param, request, response);
    }

}
