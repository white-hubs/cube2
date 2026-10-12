package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.job;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.EmployeeHolidayCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.GrantHolidayCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.FixedHolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.TimeOfInductionYearHolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.WorkingYearHolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayTypeMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayUserRangMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayUserRangPO;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.StaffQuery;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 处理假期定时发放
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HolidayGrantTask {

    private final HolidayTypeMapper holidayTypeMapper;
    private final HolidayUserRangMapper holidayUserRangMapper;
    private final StaffClient staffClient;
    private final EmployeeHolidayCommandService employeeHolidayCommandService;

    /**
     * 自动发放假期
     *
     * @param param
     *
     * @return
     */
    public void execute(String param) {
        // 获取未自动发放的假期规则
        var holidayTypeGrantConfigDTOS = holidayTypeMapper.selectListIgnoreTenant();
        // 过滤可以发放的假期 -- 满足发放规则
        holidayTypeGrantConfigDTOS.forEach(holidayTypeGrantConfigDTO -> {
            SessionContextHolder.requestTenant(holidayTypeGrantConfigDTO.getTenant());
            SecurityContextHolder.setPrincipal(Principal.builder().account("system").type(Principal.Type.CLIENT).build());
            var grantTime = HolidayGrantRule.GrantTime.valueOf(holidayTypeGrantConfigDTO.getGrantTime());

            if (grantTime == HolidayGrantRule.GrantTime.JANUARY_FIRST_OF_ANNUALLY &&
                DateUtils.format(LocalDateTime.now(), "MM-dd").equals("01-01")) {
                var grantQuotas = JSON.parseArray(holidayTypeGrantConfigDTO.getGrantQuota(), HolidayGrantQuota.class);
                // 固定额度
                var fixed = grantQuotas.stream()
                        .filter(holidayGrantQuota -> holidayGrantQuota instanceof FixedHolidayGrantQuota).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(fixed)) {
                    var holidayGrantQuota = fixed.getFirst();
                    // 根据假期获取对应的员工--获取此假期的适用范围
                    var holidayUserRanges = holidayUserRangMapper.selectList(
                            Wrappers.<HolidayUserRangPO>lambdaQuery()
                                    .eq(HolidayUserRangPO::getHolidayTypeId, holidayTypeGrantConfigDTO.getHolidayTypeId())
                                                                            );
                    Set<Long> employeeIds;
                    if (CollectionUtils.isNotEmpty(holidayUserRanges)) {
                        employeeIds = holidayUserRanges.stream().map(HolidayUserRangPO::getUserId).collect(Collectors.toSet());
                    } else {
                        // 所有人适用 --- 查询此租户的所有员工
                        var employeeList = staffClient.getList(new StaffQuery());
                        employeeIds = employeeList.stream().map(StaffDetailDTO::getId).collect(Collectors.toSet());
                    }

                    if (CollectionUtils.isNotEmpty(employeeIds)) {
                        employeeIds.forEach(employeeId -> {
                            // 执行发放
                            var grantHolidayCommand = new GrantHolidayCommand(
                                    employeeId,
                                    // todo 获取计算时间
                                    holidayGrantQuota.getQuota(),
                                    holidayTypeGrantConfigDTO.getHolidayType()
                            );
                            employeeHolidayCommandService.grantHoliday(grantHolidayCommand);
                        });
                    }
                }

                // 按社会工龄
                var workingYear = grantQuotas.stream()
                        .filter(holidayGrantQuota -> holidayGrantQuota instanceof WorkingYearHolidayGrantQuota)
                        .toList();
                if (CollectionUtils.isNotEmpty(workingYear)) {
                    workingYear.forEach(holidayGrantQuota -> {
                        // 获取社会工龄满足的员工
                        var workingYearHolidayGrantQuota = (WorkingYearHolidayGrantQuota) holidayGrantQuota;
                        var employeeIds = staffClient.getListWithWorkingYear(workingYearHolidayGrantQuota.getThreshold());
                        if (CollectionUtils.isNotEmpty(employeeIds)) {
                            employeeIds.forEach(employeeId -> {
                                // 执行发放
                                var grantHolidayCommand = new GrantHolidayCommand(
                                        employeeId,
                                        // todo 获取计算时间
                                        holidayGrantQuota.getQuota(),
                                        holidayTypeGrantConfigDTO.getHolidayType()
                                );
                                employeeHolidayCommandService.grantHoliday(grantHolidayCommand);
                            });
                        }
                    });
                }

                // 按公司司龄
                var timeOfInduction = grantQuotas.stream()
                        .filter(holidayGrantQuota -> holidayGrantQuota instanceof TimeOfInductionYearHolidayGrantQuota)
                        .toList();
                timeOfInduction.forEach(holidayGrantQuota -> {
                    // 获取公司司龄满足的员工
                    var timeOfInductionYearHolidayGrantQuota = (TimeOfInductionYearHolidayGrantQuota) holidayGrantQuota;
                    var employeeIds = staffClient.getListTimeOfInduction(timeOfInductionYearHolidayGrantQuota.getThreshold());
                    if (CollectionUtils.isNotEmpty(employeeIds)) {
                        employeeIds.forEach(employeeId -> {
                            // 执行发放
                            var grantHolidayCommand = new GrantHolidayCommand(
                                    employeeId,
                                    // todo 获取计算时间
                                    holidayGrantQuota.getQuota(),
                                    holidayTypeGrantConfigDTO.getHolidayType()
                            );
                            employeeHolidayCommandService.grantHoliday(grantHolidayCommand);
                        });
                    }
                });
            }
        });
    }
}
