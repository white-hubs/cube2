package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.ArrangeWorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.command.SubmitArrangeAndDutyWorkCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.WorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkTodayStatVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.CurrentClassesVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.ArrangeWorkQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.query.ArrangeWorkQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity.LeaveFormDetail;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArrangeWorkController {

    private final ArrangeWorkQueryService arrangeWorkQueryService;
    private final ArrangeWorkCommandService arrangeWorkCommandService;
    private final WorkCommandService workCommandService;

    /**
     * 查询用户当前时间的班次
     *
     * @param
     *
     * @return
     */
    @GetMapping("/arrange-work/staff/current-date")
    public CurrentClassesVO getUserClassesCurrentDate() {
        return arrangeWorkQueryService.getUserClassesCurrentDate(null);
    }

    @GetMapping("/arrange-work/list")
    public List<ArrangeWorkVO> list(ArrangeWorkQueryParam param) {
        return arrangeWorkQueryService.getList(param);
    }

    @GetMapping("/arrange-work/page")
    public Pagination<ArrangeWorkVO> getPage(ArrangeWorkQueryParam param) {
        return arrangeWorkQueryService.getPageV2(param);
    }

    @GetMapping("/arrange-work/not-classes-time")
    public Float getStaffNotClassesTime(ArrangeWorkQueryParam param) {
        return arrangeWorkQueryService.getStaffNotClassesTime(param.getStaffId(), param.getPeriod());
    }

    @GetMapping("/arrange-work/not-classes-day")
    public Float getStaffNotClassesDay(ArrangeWorkQueryParam param, LeaveFormDetail.LeaveTimeUnit unit) {
        return arrangeWorkQueryService.getStaffNotClassesDay(param.getStaffId(), param.getPeriod(), unit);
    }

    /**
     * 排班模板导出
     *
     * @param param
     * @param request
     * @param response
     */
    @PostMapping("/arrange-work/template/export")
    public void exportTemplate(@RequestBody ArrangeWorkQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        arrangeWorkQueryService.exportTemplate(param, request, response);
    }

    /**
     * 排班模板导入
     *
     * @param month 月份 格式：yyyy-MM
     * @param file  文件
     */
    @PostMapping("/arrange-work/template/import")
    public void importTemplate(@RequestParam("month") String month, @RequestParam("file") MultipartFile file) {
        workCommandService.importTemplate(month, file, AttendanceGroupTypeEnum.ARRANGE.name());
    }

    /**
     * 排班数据导出
     *
     * @param param
     * @param request
     * @param response
     */
    @PostMapping("/arrange-work/export")
    public void getPage(@RequestBody ArrangeWorkQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        arrangeWorkQueryService.exportArrangeWork(param, request, response);
    }

    /**
     * 考勤结果统计
     * 班次信息 叠加班次的状态
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/arrange-work/page/extend")
    public Pagination<ArrangeWorkVO> getPageExtend(ArrangeWorkQueryParam param) {
        this.initArrangeWorkQuery(param);
        if (param.getPeriod().getBegin().isAfter(LocalDateTime.now())) {
            return Pagination.empty();
        }

        return arrangeWorkQueryService.getPageExtendV2(param);
    }

    /**
     * 初始化排班查询时间
     *
     * @param param
     */
    private void initArrangeWorkQuery(ArrangeWorkQueryParam param) {
        if (StringUtils.isNotEmpty(param.getMonth())) {
            LocalDateTime beginOfMonth = DateUtils.beginOfMonth(DateUtils.parse(param.getMonth(), "yyyy-MM"));
            LocalDateTime endOfMonth = DateUtils.endOfMonth(DateUtils.parse(param.getMonth(), "yyyy-MM"));
            param.setPeriod(Period.of(beginOfMonth, endOfMonth));
        } else if (param.getPeriod() == null) {
            param.setPeriod(Period.of(DateUtils.beginOfMonth(LocalDateTime.now()), DateUtils.endOfMonth(LocalDateTime.now())));
        }
    }

    /**
     * 考勤结果统计 导出
     *
     * @param param
     * @param request
     * @param response
     */
    @PostMapping("/arrange-work/extend/export")
    public void getPageExtend(@RequestBody ArrangeWorkQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        this.initArrangeWorkQuery(param);
        Assert.isNotAcceptable(param.getPeriod().getBegin().isAfter(LocalDateTime.now()), "暂无考勤数据，无法导出");
        arrangeWorkQueryService.exportArrangeWorkExtend(param, request, response);
    }

    /**
     * 当前用户班次信息 叠加考勤记录
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/arrange-work/list/extend")
    public List<ArrangeWorkVO.DateOfClasses> getCurrentUserList(ArrangeWorkQueryParam param) {
        return arrangeWorkQueryService.getCurrentUserList(param);
    }

    /**
     * 查询今日当班人员（可视化使用）
     *
     * @return
     */
    @GetMapping("/arrange-work/today/stat")
    public ArrangeWorkTodayStatVO getToadyStaff() {
        return arrangeWorkQueryService.getToadyStaff();
    }

    /**
     * 筛选当班人员，参数传人员ID数组，筛选出当前时间有班次的人员
     *
     * @param userIds 需要筛选的人员ID数组
     *
     * @return
     */
    @PostMapping("/arrange-work/user/filter")
    public List<Long> filterUserInClasses(@RequestBody List<Long> userIds) {
        return arrangeWorkQueryService.filterUserInClasses(userIds);
    }

    @PostMapping("/arrange-work")
    public void manualArrange(@Valid @RequestBody SubmitArrangeAndDutyWorkCommand submitArrangeAndDutyWorkCommand) {
        arrangeWorkCommandService.manualArrange(submitArrangeAndDutyWorkCommand);
    }

}
