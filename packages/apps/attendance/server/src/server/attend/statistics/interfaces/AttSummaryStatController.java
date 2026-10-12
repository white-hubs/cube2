package com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.application.querygateway.AttSummaryStatQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.param.AttSummaryStatPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result.AttAppStaffSummaryStatResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result.AttSummaryStaffStatResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result.AttSummaryStatPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttPeriodTypeUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;

/**
 * 汇总统计入口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttSummaryStatController {
    private final AttSummaryStatQueryService attSummaryStatQueryService;

    /**
     * 汇总统计分页列表
     *
     * @param param 条件入参
     *
     * @return 分页列表对象
     */
    @GetMapping("/att/summary/page")
    @DataPermissionSwitch(dataModel = "attendance:attGroupStaff")
    public AttSummaryStatPageResult summaryStatPage(@Valid AttSummaryStatPageParam param) {

        return attSummaryStatQueryService.summaryStatPage(param);
    }

    /**
     * app首页统计数据
     *
     * @param param 条件
     *
     * @return 结果
     */
    @GetMapping("/att/summary/appHomeStat")
    public AttSummaryStaffStatResult appHomeStat(AttSummaryStatPageParam param) {
        param.setPeriod(AttPeriodTypeUtils.getPeriodByPeriodType(param.getPeriodType()));
        return attSummaryStatQueryService.appHomeStat(param);
    }

    /**
     * 导出汇总统计
     *
     * @param param    条件
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/att/summary/export")
    @DataPermissionSwitch(dataModel = "attendance:attGroupStaff")
    public void summaryStatExport(AttSummaryStatPageParam param, HttpServletRequest request, HttpServletResponse response) {
        attSummaryStatQueryService.summaryStatExport(param, request, response);
    }

    /**
     * app人员汇总统计
     *
     * @param param 条件
     *
     * @return 统计
     */
    @GetMapping("/att/app/staffSummary")
    public AttAppStaffSummaryStatResult appStaffSummaryStat(AttSummaryStatPageParam param) {
        return attSummaryStatQueryService.staffSummaryStat(param);
    }
}
