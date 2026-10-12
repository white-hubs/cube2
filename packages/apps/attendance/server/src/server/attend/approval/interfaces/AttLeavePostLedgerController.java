package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttLeavePostLedgerQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param.AttLeavePostLedgerPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result.AttLeavePostLedgerPageResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 离岗台账控制层
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttLeavePostLedgerController {
    private final AttLeavePostLedgerQueryService attLeavePostLedgerQueryService;

    /**
     * 离岗台账分页列表
     *
     * @param param 条件入参
     *
     * @return 分页列表对象
     */
    @GetMapping("/att/leavePost/ledger/page")
    public Pagination<AttLeavePostLedgerPageResult> leavePostLedgerPage(AttLeavePostLedgerPageParam param) {
        return attLeavePostLedgerQueryService.leavePostLedgerPage(param);
    }

    /**
     * 离岗台账导出
     *
     * @param param    条件
     * @param request  请求
     * @param response 返回
     */
    @GetMapping("/att/leavePost/ledger/export")
    public void leavePostLedgerExport(AttLeavePostLedgerPageParam param, HttpServletRequest request, HttpServletResponse response) {
        attLeavePostLedgerQueryService.leavePostLedgerExport(param, request, response);
    }

}
