package com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Sets;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.dto.ArrangeWorkDaysDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.dao.ClassesClockMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.dao.dto.ClassesClockStatDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.interfaces.vo.AttendanceStatVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.query.AttendanceStatQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.util.ExcelUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.EmployeeHolidayQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.HolidayConsumptionDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.dao.LeavePostSituationMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.dao.dto.LeavePostDurationStatDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.dao.WorkOvertimeSituationMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.dao.dto.OvertimeDurationGroupByTypeDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.repairclock.dao.RepairClockSituationMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.repairclock.interfaces.vo.RepairClockNumVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.AttendanceGroupQueryService;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommonQueryService {
    private final ArrangeWorkMapper arrangeWorkMapper;
    private final ClassesClockMapper classesClockMapper;
    private final WorkOvertimeSituationMapper workOvertimeSituationMapper;
    private final LeavePostSituationMapper leavePostSituationMapper;
    private final AttendanceGroupMapper attendanceGroupMapper;
    private final AttendanceGroupStaffMapper attendanceGroupStaffMapper;
    private final EmployeeHolidayQueryService employeeHolidayQueryService;
    private final AttendanceGroupQueryService attendanceGroupQueryService;
    private final StaffClient staffClient;
    private final RepairClockSituationMapper repairClockSituationMapper;
    private final OrganizationClient organizationClient;

    public Pagination<AttendanceStatVO> getAttendanceStat(AttendanceStatQueryParam param) {
        //查询考勤人员
        Page<AttendanceGroupStaffPO> page = attendanceGroupStaffMapper.getAttendanceStaff(PagingUtils.convert(param), param);

        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            // 查询部门
            List<Long> deptIds = page.getRecords().stream().map(AttendanceGroupStaffPO::getStaffOrganizationId)
                    .collect(Collectors.toList());
            Map<Long, String> deptDtoMap = organizationClient.getOrganizationsByIds(deptIds).stream()
                    .collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName));
            // 出来人员工号和部门信息
            List<AttendanceStatVO> datas = page.getRecords().stream().map(r -> {
                                                                              AttendanceGroupPO attendanceGroup = attendanceGroupQueryService.getMyAttendanceGroup(r.getStaffId());

                                                                              return AttendanceStatVO.builder()
                                                                                      .userId(r.getStaffId())
                                                                                      .userName(r.getStaffName())
                                                                                      .deptName(deptDtoMap.get(r.getStaffOrganizationId()))
                                                                                      .groupName(attendanceGroup != null ? attendanceGroup.getName() : "")
                                                                                      .build();
                                                                          }
                                                                         ).collect(Collectors.toList());
            //
            this.extendAttendanceDetail(datas, param);
            return PagingUtils.convert(page, datas);
        }
        return Pagination.empty();
    }

    private void extendAttendanceDetail(List<AttendanceStatVO> datas, AttendanceStatQueryParam param) {
        List<Long> userIds = datas.stream().map(AttendanceStatVO::getUserId).collect(Collectors.toList());
        param.setUserIds(userIds);
        param.setTenant(SessionContextHolder.getRequester().getTenant());
        Period oldPeriod = new Period(DateUtils.newLocalDateTime(param.getPeriod().getBegin()), DateUtils.newLocalDateTime(param.getPeriod()
                                                                                                                                   .getEnd()));
        //获取返回结果
        try {
            //排班统计
            List<ArrangeWorkDaysDTO> arrangeWorkDaysDTOS = arrangeWorkMapper.getArrangeWorkDays(param);//arrangeWorkDaysFuture.get();
            // 前一天
            Period befOneDay = new Period(DateUtils.newLocalDateTime(param.getPeriod()
                                                                             .getBegin()), DateUtils.offsetDay(DateUtils.endOfDay(LocalDateTime.now()), -1));
            if (param.getPeriod().getEnd().isAfter(LocalDateTime.now())) {
                // 统计前一天
                param.setPeriod(befOneDay);
            }
            //打卡统计
            List<ClassesClockStatDTO> classesClockStatDTOS = classesClockMapper.getClassesClockStat(param);//classesClockStatFuture.get();
            //加班统计
            List<OvertimeDurationGroupByTypeDTO> overtimeDurationGroupByTypeDTOS = workOvertimeSituationMapper.getOvertimeDurationStatGroupByType(param);//overtimeDurationFuture.get();
            //离岗统计
            List<LeavePostDurationStatDTO> leavePostDurationStatDTOS = leavePostSituationMapper.getLeavePostDurationStat(param);//leavePostDurationFuture.get();
            //假期统计
            List<HolidayConsumptionDTO> holidayConsumptionDTOS = employeeHolidayQueryService.getConsumptionSummaryByStaff(Sets.newHashSet(userIds), param.getPeriod());//holidaySummaryFuture.get();

            datas.forEach(d -> {
                //出勤天数填充
                ArrangeWorkDaysDTO arrangeWorkDaysDTO = arrangeWorkDaysDTOS.stream()
                        .filter(t -> t.getUserId().longValue() == d.getUserId().longValue()).findFirst().orElse(null);
                d.setWorkDays(arrangeWorkDaysDTO != null ? arrangeWorkDaysDTO.getWorkDays() : 0);
                //打卡数据填充
                classesClockStatDTOS.stream()
                        .filter(t -> t.getUserId().longValue() == d.getUserId().longValue()).findFirst()
                        .ifPresent(classesClockStatDTO -> ObjectUtils.copying(classesClockStatDTO, d));
                //加班数据填充
                overtimeDurationGroupByTypeDTOS.stream()
                        .filter(t -> t.getUserId().longValue() == d.getUserId().longValue()).findFirst()
                        .ifPresent(overtimeDurationDTO -> ObjectUtils.copying(overtimeDurationDTO, d));
                //离岗数据填充
                LeavePostDurationStatDTO leavePostDurationStatDTO = leavePostDurationStatDTOS.stream()
                        .filter(t -> t.getUserId().longValue() == d.getUserId().longValue()).findFirst().orElse(null);
                if (leavePostDurationStatDTO != null) {
                    BigDecimal d1 = leavePostDurationStatDTO.getLeavePostDuration()
                            .divide(BigDecimal.valueOf(60), 1, RoundingMode.HALF_DOWN);
                }
                d.setLeavePostDuration(leavePostDurationStatDTO != null
                                       ? leavePostDurationStatDTO.getLeavePostDuration()
                                               .divide(BigDecimal.valueOf(60), 1, RoundingMode.HALF_DOWN).floatValue()
                                       : 0);

                //请假数据填充
                holidayConsumptionDTOS.stream()
                        .filter(t -> t.getStaffId().longValue() == d.getUserId().longValue()).findFirst()
                        .ifPresent(holidayConsumptionDTO -> ObjectUtils.copying(holidayConsumptionDTO, d));
                // 补卡数据填充
                List<RepairClockNumVO> repairClockNumByPeriod = repairClockSituationMapper.getRepairClockNumByPeriod(d.getUserId(), param
                        .getPeriod().getBegin(), param.getPeriod().getEnd());
                d.setRepairClockNum(repairClockNumByPeriod.stream().mapToInt(RepairClockNumVO::getNum).sum());
            });
        } catch (Exception e) {
            log.error("考勤统计异常：", e);
        }
        param.setPeriod(oldPeriod);
    }

    public void exportAttendanceStat(AttendanceStatQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        //查询考勤人员
        List<AttendanceGroupStaffPO> attendanceStaff = attendanceGroupStaffMapper.getAttendanceStaff(param);
        Assert.isNotFound(CollectionUtils.isEmpty(attendanceStaff), "未查询到相关数据，无法导出");
        List<AttendanceStatVO> datas = attendanceStaff.stream().map(r -> {
                                                                        OrganizationDTO organizationDTO = organizationClient.getOrganizationsById(r.getStaffOrganizationId());
                                                                        AttendanceGroupPO attendanceGroup = attendanceGroupMapper.getAttendanceGroupByUserId(r.getStaffId());
                                                                        return AttendanceStatVO.builder()
                                                                                .userId(r.getStaffId())
                                                                                .userName(r.getStaffName())
                                                                                .deptName(organizationDTO != null ? organizationDTO.getName() : "")
                                                                                .groupName(attendanceGroup != null ? attendanceGroup.getName() : "")
                                                                                .build();
                                                                    }
                                                                   ).collect(Collectors.toList());

        this.extendAttendanceDetail(datas, param);

        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("userName", "员工姓名");
        writer.addHeaderAlias("groupName", "考勤组");
        writer.addHeaderAlias("deptName", "部门");
        writer.addHeaderAlias("workDays", "应出勤天数");
        writer.addHeaderAlias("realWorkDays", "实际出勤天数");
        writer.addHeaderAlias("noClockNum", "缺卡次数");
        writer.addHeaderAlias("repairClockNum", "补卡次数");
        writer.addHeaderAlias("lateNum", "迟到次数");
        writer.addHeaderAlias("lateTotalTime", "迟到时长（分钟）");
        writer.addHeaderAlias("earlyNum", "早退次数");
        writer.addHeaderAlias("earlyTotalTime", "早退时长（分钟）");
        writer.addHeaderAlias("skipWorkNum", "旷工次数");
        writer.addHeaderAlias("overtimeWorkDuration", "工作日加班（小时）");
        writer.addHeaderAlias("overtimeRestDuration", "休息日加班（小时）");
        writer.addHeaderAlias("leavePostDuration", "外出时长（小时）");
        writer.addHeaderAlias("overtimeFestDuration", "节假日加班（小时）");
        writer.addHeaderAlias("compassionateLeave", "事假");
        writer.addHeaderAlias("sickLeave", "病假");
        writer.addHeaderAlias("compensatoryLeave", "调休");
        writer.addHeaderAlias("marriageLeave", "婚假");
        writer.addHeaderAlias("bereavementLeave", "丧假");
        writer.addHeaderAlias("maternityLeave", "产假");
        writer.addHeaderAlias("paternityLeave", "陪产假");
        writer.addHeaderAlias("annualLeave", "年假");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 15);

        String fileName = (StringUtils.isNotEmpty(param.getMonth()) ? param.getMonth() + "_" : "") + "考勤汇总统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    public Map<String, String> getStaffAttendanceStat(AttendanceStatQueryParam param) {
        param.setTenant(SessionContextHolder.getRequester().getTenant());
        Map<String, String> map = new HashMap<>();
        switch (param.getType()) {
            case "sickLeave":
                List<HolidayConsumptionDTO> holidayConsumptionDTOS = employeeHolidayQueryService.getConsumptionSummaryByStaff(Sets.newHashSet(param.getUserId()), param.getPeriod());
                if (CollectionUtils.isNotEmpty(holidayConsumptionDTOS)) {
                    map.put("name",
                            holidayConsumptionDTOS.getFirst().getSickLeave() != null ? holidayConsumptionDTOS.get(0).getSickLeave() + "天"
                                                                                     : "0天");
                }
                break;
            case "compassionateLeave":
                List<HolidayConsumptionDTO> holidayConsumptionDTOS2 = employeeHolidayQueryService.getConsumptionSummaryByStaff(Sets.newHashSet(param.getUserId()), param.getPeriod());
                if (CollectionUtils.isNotEmpty(holidayConsumptionDTOS2)) {
                    map.put("name", holidayConsumptionDTOS2.getFirst().getCompassionateLeave() != null ?
                                    holidayConsumptionDTOS2.getFirst().getCompassionateLeave() + "天" : "0天");
                }
                break;
            case "earlyNum":
                List<ClassesClockStatDTO> classesClockStatDTOS = classesClockMapper.getClassesClockStat(param);
                map.put("name",
                        CollectionUtils.isNotEmpty(classesClockStatDTOS) ? classesClockStatDTOS.getFirst().getEarlyNum() + "次" : "0次");
                break;
            case "lateNum":
                List<ClassesClockStatDTO> classesClockStatDTOS2 = classesClockMapper.getClassesClockStat(param);
                map.put("name",
                        CollectionUtils.isNotEmpty(classesClockStatDTOS2) ? classesClockStatDTOS2.getFirst().getLateNum() + "次" : "0次");
                break;
            case "skipWorkNum":
                List<ClassesClockStatDTO> classesClockStatDTOS3 = classesClockMapper.getClassesClockStat(param);
                map.put("name",
                        CollectionUtils.isNotEmpty(classesClockStatDTOS3) ? classesClockStatDTOS3.getFirst().getSkipWorkNum() + "次"
                                                                          : "0次");
                break;
            default:
                map.put("name", "暂无数据");
                break;
        }
        return map;
    }

}
