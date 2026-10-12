package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.application.commandgateway.AttDutyCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.application.querygateway.AttDutyQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param.AttAppPeriodDutyParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param.AttDutyAddStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param.AttDutyHandParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param.AttDutyPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param.AttDutyRemoveStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result.AttAppDutyPeriodResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result.AttDutyPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result.AttDutyStaffIdNameResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 值班入口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttDutyController {

    private final AttDutyQueryService attDutyQueryService;
    private final AttDutyCommandService attDutyCommandService;

    /**
     * 值班列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/att/duty/page")
    public Pagination<AttDutyPageResult> pageDuty(@Valid AttDutyPageParam param) {
        return attDutyQueryService.pageDuty(param);
    }

    /**
     * 手动设置值班
     *
     * @param param 值班
     */
    @PostMapping("/att/duty/hand")
    public void handDuty(@RequestBody @Valid AttDutyHandParam param) {
        this.attDutyCommandService.handDuty(param);
    }

    /**
     * 下载值班模板
     *
     * @param param    条件入参
     * @param request  请求
     * @param response 返回
     */
    @PostMapping("/att/duty/template/download")
    public void downloadTemplate(@RequestBody @Valid AttDutyPageParam param, HttpServletRequest request, HttpServletResponse response) {
        attDutyQueryService.downloadTemplate(param, request, response);
    }

    /**
     * 值班模板导入
     * 导入excel值班
     *
     * @param yearMonth 月份 格式：yyyy-MM
     * @param file      文件
     */
    @PostMapping("/att/duty/template/import")
    public void importTemplateDuty(@RequestParam("yearMonth") String yearMonth, @RequestParam("file") MultipartFile file) {
        attDutyCommandService.importTemplateDuty(yearMonth, file);
    }

    /**
     * 添加员工
     *
     * @param param
     */
    @PostMapping("/att/duty/add/staff")
    public void addStaff(@RequestBody @Valid AttDutyAddStaffParam param) {
        this.attDutyCommandService.addDutyStaff(param);
    }

    /**
     * 移除员工
     *
     * @param param
     */
    @PostMapping("/att/duty/remove/staff")
    public void removeStaff(@RequestBody @Valid AttDutyRemoveStaffParam param) {
        this.attDutyCommandService.removeDutyStaff(param);
    }

    /**
     * 获取某月所有的值班人员
     *
     * @param yearMonth 年月
     *
     * @return 值班人员集合
     */
    @GetMapping("/att/duty/all/staff")
    public List<AttDutyStaffIdNameResult> getDutyAllStaffIdAndName(@RequestParam("yearMonth") String yearMonth) {
        return attDutyQueryService.getDutyAllStaffIdAndName(yearMonth);
    }

    /**
     * 导出排班
     *
     * @param param    条件
     * @param request  请求
     * @param response 返回
     */
    @PostMapping("/att/duty/export")
    public void exportAttDuty(@RequestBody AttDutyPageParam param, HttpServletRequest request, HttpServletResponse response) {
        attDutyQueryService.exportAttDuty(param, request, response);
    }

    /**
     * app获取某月的值班
     *
     * @param param r
     *
     * @return 值班列表
     */
    @GetMapping("/att/app/duty/period")
    public List<AttAppDutyPeriodResult> getAppPeriodDuty(AttAppPeriodDutyParam param) {
        return attDutyQueryService.getAppPeriodDuty(param);
    }
}
