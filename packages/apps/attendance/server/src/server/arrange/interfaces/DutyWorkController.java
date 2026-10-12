package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.DutyWorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.command.SubmitArrangeAndDutyWorkCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.WorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.DutyWorkAppVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.DutyWorkVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.DutyWorkQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.query.DutyWorkQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DutyWorkController {

    private final DutyWorkQueryService dutyWorkQueryService;
    private final DutyWorkCommandService dutyWorkCommandService;
    private final WorkCommandService workCommandService;

    @GetMapping("/duty-work/list")
    public List<DutyWorkAppVO> list(DutyWorkQueryParam param) {
        return dutyWorkQueryService.getList(param);
    }

    @GetMapping("/duty-work/page")
    public Pagination<DutyWorkVO> getPage(DutyWorkQueryParam param) {
        return dutyWorkQueryService.getPage(param);
    }

    @GetMapping("/duty-work/classes")
    public List<Classes> getDutyClasses(@RequestParam("dutyWorkClassesIds") String dutyWorkClassesIds) {
        return dutyWorkQueryService.getDutyClasses(dutyWorkClassesIds);
    }

    /**
     * 导出值班数据
     *
     * @param param
     * @param request
     * @param response
     */
    @PostMapping("/duty-work/export")
    public void getPage(@RequestBody DutyWorkQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        dutyWorkQueryService.exportDuty(param, request, response);
    }

    /**
     * 值班模板导出
     *
     * @param param
     * @param request
     * @param response
     */
    @PostMapping("/duty-work/template/export")
    public void exportTemplate(@RequestBody DutyWorkQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        dutyWorkQueryService.exportTemplate(param, request, response);
    }

    /**
     * 值班模板导入
     *
     * @param month 月份 格式：yyyy-MM
     * @param file  文件
     */
    @PostMapping("/duty-work/template/import")
    public void importTemplate(@RequestParam("month") String month, @RequestParam("file") MultipartFile file) {
        workCommandService.importTemplate(month, file, AttendanceGroupTypeEnum.DUTY.name());
    }

    @PostMapping("/duty-work")
    public void saveOrUpdate(@Valid @RequestBody SubmitArrangeAndDutyWorkCommand submitArrangeAndDutyWorkCommand) {
        dutyWorkCommandService.manualArrange(submitArrangeAndDutyWorkCommand);
    }

}
