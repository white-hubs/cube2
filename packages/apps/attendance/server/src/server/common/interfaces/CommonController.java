package com.guhecloud.cube.srv.gen.attendance.management.server.common.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.common.ApprovalBizEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.CommonEnumVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.FormFieldVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.interfaces.vo.AttendanceStatVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.CommonQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.query.AttendanceStatQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.tool.FormNumberCreater;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.util.CustBeanUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用接口 接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommonController {
    private final CommonQueryService commonQueryService;
    private final FormNumberCreater formNumberCreater;

    /**
     * 获取审批业务类型
     *
     * @return
     */
    @GetMapping("common/approval/biz/type")
    public List<CommonEnumVO> getApprovalBizType() {
        return Arrays.stream(ApprovalBizEnum.values()).map(e -> CommonEnumVO.builder()
                        .code(e.name()).name(e.getDesc()).build())
                .collect(Collectors.toList());
    }

    /**
     * 获取业务字段
     *
     * @return
     */
    @GetMapping("common/approval/biz/form/field")
    public List<FormFieldVO> getFormField(ApprovalBizEnum type) {
        if (type == null) {
            return Collections.emptyList();
        }
        return type.getClazz() != null ? CustBeanUtil.getFormField(type.getClazz()) : Collections.emptyList();
    }

    @GetMapping("/common/approval/biz/form/no")
    public String getFormNo(@RequestParam("prefix") String prefix) {
        return formNumberCreater.create(prefix);
    }

    /**
     * 考勤汇总统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/common/attendance/stat")
    public Pagination<AttendanceStatVO> getAttendanceStat(AttendanceStatQueryParam param) {
        this.initArrangeWorkQuery(param);
        return commonQueryService.getAttendanceStat(param);
    }

    /**
     * 初始化排班查询时间
     *
     * @param param
     */
    private void initArrangeWorkQuery(AttendanceStatQueryParam param) {
        if (StringUtils.isNotEmpty(param.getMonth())) {
            LocalDateTime beginOfMonth = DateUtils.beginOfMonth(DateUtils.parse(param.getMonth(), "yyyy-MM"));
            LocalDateTime endOfMonth = DateUtils.endOfMonth(DateUtils.parse(param.getMonth(), "yyyy-MM"));
            param.setPeriod(Period.of(beginOfMonth, endOfMonth));
        } else if (param.getPeriod() == null) {
            param.setPeriod(Period.of(DateUtils.beginOfMonth(LocalDateTime.now()), DateUtils.endOfMonth(LocalDateTime.now())));
        }
    }

    /**
     * 考勤统计 导出
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/common/attendance/stat/export")
    public void exportAttendanceStat(@RequestBody AttendanceStatQueryParam param,
                                     HttpServletRequest request,
                                     HttpServletResponse response
                                    ) {
        this.initArrangeWorkQuery(param);
        commonQueryService.exportAttendanceStat(param, request, response);
    }

    /**
     * 员工的考勤数据（考勤数据控件）
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/common/attendance/staff/stat")
    public Map<String, String> getStaffAttendanceStat(AttendanceStatQueryParam param) {
        try {
            return commonQueryService.getStaffAttendanceStat(param);
        } catch (Exception e) {
            log.error("获取员工考勤数据失败,查询参数{}", param, e);
            Map<String, String> map = new HashMap<>();
            map.put("name", "暂无数据");
            return map;
        }
    }

}
