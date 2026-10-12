package com.guhecloud.cube.srv.gen.attendance.management.server.attend.beforestat.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.beforestat.application.querygateway.AttBeforeStatQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.param.AttSummaryStatPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result.AttSummaryStatPageResult;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 预统计
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttBeforeStatController {
    private final AttBeforeStatQueryService attBeforeStatQueryService;

    /**
     * 预统计分页列表
     *
     * @param param 条件对象
     *
     * @return 分页对象
     */
    @GetMapping("/att/beforeStat/page")
    @DataPermissionSwitch(dataModel = "attendance:attGroupStaff")
    public AttSummaryStatPageResult getBeforeStatPage(AttSummaryStatPageParam param) {
        return attBeforeStatQueryService.getBeforeStatPage(param);
    }

    /**
     * 预统计导出
     *
     * @param param    条件
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/att/beforeStat/export")
    @DataPermissionSwitch(dataModel = "attendance:attGroupStaff")
    public void beforeStatExport(AttSummaryStatPageParam param, HttpServletRequest request, HttpServletResponse response) {
        attBeforeStatQueryService.beforeStatExport(param, request, response);
    }
}
