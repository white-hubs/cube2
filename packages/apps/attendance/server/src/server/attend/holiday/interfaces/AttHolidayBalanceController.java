package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.commandgateway.AttHolidayBalanceCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.querygateway.AttHolidayBalanceQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param.AttHolidayBalancePageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param.AttHolidayBalanceParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param.AttHolidayBalanceRecordPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param.AttStaffTotalBalanceParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayBalanceImportResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayBalancePageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayBalanceRecordPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttStaffTotalBalanceResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;

/**
 * 假期余额
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttHolidayBalanceController {

    private final AttHolidayBalanceQueryService attHolidayBalanceQueryService;
    private final AttHolidayBalanceCommandService attHolidayBalanceCommandService;

    /**
     * 假期余额分页列表
     *
     * @param param 条件入参
     *
     * @return 分页列表
     */
    @GetMapping("/att/Holiday/balance/page")
    public AttHolidayBalancePageResult pageHolidayBalance(AttHolidayBalancePageParam param) {
        return attHolidayBalanceQueryService.getHolidayBalancePage(param);
    }

    /**
     * 编辑假期余额
     *
     * @param param 编辑内容
     */
    @PutMapping("/att/Holiday/balance/edit")
    public void editHolidayBalance(@RequestBody @Valid AttHolidayBalanceParam param) {
        attHolidayBalanceCommandService.editHolidayBalance(param);
    }

    /**
     * 假期余额记录分页列表
     *
     * @param param 条件入参
     *
     * @return 列表
     */
    @GetMapping("/att/Holiday/balance/record/page")
    public Pagination<AttHolidayBalanceRecordPageResult> pageHolidayBalanceRecord(AttHolidayBalanceRecordPageParam param) {

        return attHolidayBalanceQueryService.pageHolidayBalanceRecord(param);
    }

    /**
     * 获取人员假期总余额
     *
     * @return 余额（小时
     */
    @GetMapping("/att/Holiday/balance/total")
    public AttStaffTotalBalanceResult getStaffTotalBalance(AttStaffTotalBalanceParam param) {
        return attHolidayBalanceQueryService.getStaffTotalBalance(param);
    }

    /**
     * 导出余额
     *
     * @param param
     * @param request
     * @param response
     */
    @GetMapping("/att/Holiday/balance/export")
    public void exportHolidayBalance(AttHolidayBalancePageParam param, HttpServletRequest request, HttpServletResponse response) {
        attHolidayBalanceQueryService.exportHolidayBalance(param, request, response);
    }

    /**
     * 假期余额初始化导入
     *
     * @param file 文件
     * @param init true表示重置(false或null表示增量)
     */
    @PostMapping("/att/Holiday/balance/import")
    public AttHolidayBalanceImportResult importTemplateDuty(@RequestParam("file") MultipartFile file, @RequestParam("init") Boolean init,
                                                            @RequestParam("ruleNames") String ruleNames
                                                           ) {
        return attHolidayBalanceCommandService.readInitBalance(file, ruleNames, init);
    }

    /**
     * 操作记录作废
     *
     * @param id              记录id
     * @param effectiveRemark 作废理由
     */
    @GetMapping("/att/holiday/balance/record/effective")
    public void balanceRecordCancel(Long id, String effectiveRemark) {
        attHolidayBalanceCommandService.balanceRecordCancel(id, effectiveRemark);
    }

    /**
     * 下载假期余额模板
     *
     * @param param    条件入参
     * @param request  请求
     * @param response 返回
     */
    @PostMapping("/att/holiday/balance/template/download")
    public void downloadTemplate(@RequestBody AttHolidayBalancePageParam param, HttpServletRequest request, HttpServletResponse response) {
        attHolidayBalanceQueryService.downloadTemplate(param, request, response);
    }

}
