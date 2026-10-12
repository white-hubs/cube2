package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.EmployeeBalanceDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.query.CollectionQuery;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayBalanceListMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayBalanceMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayTypeMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayBalanceListPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayTypePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.tool.excel.DefaultHorizontalCellStyleStrategy;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.vo.BalanceExportVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.AttendanceGroupQueryService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BalanceExportController {
    @Autowired
    private HttpServletResponse response;
    private final HolidayTypeMapper holidayTypeMapper;
    private final HolidayBalanceListMapper holidayBalanceListMapper;
    private final HolidayBalanceMapper holidayBalanceMapper;
    private final OrganizationClient organizationClient;

    private final AttendanceGroupQueryService attendanceGroupQueryService;

    @GetMapping("/holiday/balance/export")
    public void export(CollectionQuery collectionQuery) throws IOException {
        List<EmployeeBalanceDTO> result = holidayBalanceMapper.list(collectionQuery, SessionContextHolder.getRequester().getTenant());
        List<BalanceExportVO> data = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(result)) {
            Map<Long, List<HolidayBalanceListPO>> holidayUserMap = holidayBalanceListMapper
                    .selectList(Wrappers.<HolidayBalanceListPO>lambdaQuery().eq(HolidayBalanceListPO::getShow, true))
                    .stream().collect(Collectors.groupingBy(HolidayBalanceListPO::getHolidayTypeId));
            List<HolidayTypePO> holidayTypePOS = holidayTypeMapper.selectList(Wrappers.<HolidayTypePO>emptyWrapper());
            Map<Holiday.HolidayType, Long> holidayTypeMap = holidayTypePOS
                    .stream().collect(Collectors.toMap(HolidayTypePO::getType, HolidayTypePO::getId));
            Map<Holiday.HolidayType, Boolean> isLImitQuotaMap = holidayTypePOS.stream()
                    .collect(Collectors.toMap(HolidayTypePO::getType, HolidayTypePO::getIsNeedLimitQuota));

            Map<Long, String> organMap = organizationClient
                    .getOrganizationsByIds(result.stream().map(EmployeeBalanceDTO::getOrganizationId).collect(Collectors.toList()))
                    .stream().collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName));

            Map<Long, String> attendanceGroupMap = attendanceGroupQueryService
                    .listByIds(result.stream().map(EmployeeBalanceDTO::getAttendanceGroupId)
                                       .collect(Collectors.toList())).stream()
                    .collect(Collectors.toMap(AttendanceGroupPO::getId, AttendanceGroupPO::getName));
            result.forEach(employeeBalanceDTO -> {
                // 先检查是否配置
                if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.ANNUAL_LEAVE)))) {
                    boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.ANNUAL_LEAVE))
                            .stream().map(HolidayBalanceListPO::getUserId).toList()
                            .contains(employeeBalanceDTO.getUserId());
                    if (!contains) {
                        employeeBalanceDTO.setAnnualLeave("NOT_CONFIGURED");
                    }
                }

                if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.BEREAVEMENT_LEAVE)))) {
                    boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.BEREAVEMENT_LEAVE))
                            .stream().map(HolidayBalanceListPO::getUserId).toList()
                            .contains(employeeBalanceDTO.getUserId());
                    if (!contains) {
                        employeeBalanceDTO.setBereavementLeave("NOT_CONFIGURED");
                    }
                }

                if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.COMPASSIONATE_LEAVE)))) {
                    boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.COMPASSIONATE_LEAVE)).stream()
                            .map(HolidayBalanceListPO::getUserId).toList().contains(employeeBalanceDTO.getUserId());
                    if (!contains) {
                        employeeBalanceDTO.setCompassionateLeave("NOT_CONFIGURED");
                    }
                }

                if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.COMPENSATORY_LEAVE)))) {
                    boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.COMPENSATORY_LEAVE)).stream()
                            .map(HolidayBalanceListPO::getUserId).toList().contains(employeeBalanceDTO.getUserId());
                    if (!contains) {
                        employeeBalanceDTO.setCompensatoryLeave("NOT_CONFIGURED");
                    }
                }

                if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.MARRIAGE_LEAVE)))) {
                    boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.MARRIAGE_LEAVE)).stream()
                            .map(HolidayBalanceListPO::getUserId).toList().contains(employeeBalanceDTO.getUserId());
                    if (!contains) {
                        employeeBalanceDTO.setMarriageLeave("NOT_CONFIGURED");
                    }
                }

                if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.MATERNITY_LEAVE)))) {
                    boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.MATERNITY_LEAVE)).stream()
                            .map(HolidayBalanceListPO::getUserId).toList().contains(employeeBalanceDTO.getUserId());
                    if (!contains) {
                        employeeBalanceDTO.setMaternityLeave("NOT_CONFIGURED");
                    }
                }

                if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.PATERNITY_LEAVE)))) {
                    boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.PATERNITY_LEAVE)).stream()
                            .map(HolidayBalanceListPO::getUserId).toList().contains(employeeBalanceDTO.getUserId());
                    if (!contains) {
                        employeeBalanceDTO.setPaternityLeave("NOT_CONFIGURED");
                    }
                }

                if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.SICK_LEAVE)))) {
                    boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.SICK_LEAVE)).stream()
                            .map(HolidayBalanceListPO::getUserId).toList().contains(employeeBalanceDTO.getUserId());
                    if (!contains) {
                        employeeBalanceDTO.setSickLeave("NOT_CONFIGURED");
                    }
                }

                if (!employeeBalanceDTO.getSickLeave().equals("NOT_CONFIGURED") && isLImitQuotaMap.get(Holiday.HolidayType.SICK_LEAVE)) {
                    employeeBalanceDTO.setSickLeave("NO_LIMITED");
                } else if (!employeeBalanceDTO.getSickLeave().equals("NOT_CONFIGURED") &&
                           Objects.nonNull(employeeBalanceDTO.getSickLeave())) {
                    employeeBalanceDTO.setSickLeave(String.valueOf(Float.parseFloat(employeeBalanceDTO.getSickLeave()) / 8.0f));
                }
                if (!employeeBalanceDTO.getPaternityLeave().equals("NOT_CONFIGURED") &&
                    isLImitQuotaMap.get(Holiday.HolidayType.PATERNITY_LEAVE)) {
                    employeeBalanceDTO.setPaternityLeave("NO_LIMITED");
                } else if (!employeeBalanceDTO.getPaternityLeave().equals("NOT_CONFIGURED") &&
                           Objects.nonNull(employeeBalanceDTO.getPaternityLeave())) {
                    employeeBalanceDTO.setPaternityLeave(String.valueOf(Float.parseFloat(employeeBalanceDTO.getPaternityLeave()) / 8.0f));
                }
                if (!employeeBalanceDTO.getMarriageLeave().equals("NOT_CONFIGURED") &&
                    isLImitQuotaMap.get(Holiday.HolidayType.MARRIAGE_LEAVE)) {
                    employeeBalanceDTO.setMarriageLeave("NO_LIMITED");
                } else if (!employeeBalanceDTO.getMarriageLeave().equals("NOT_CONFIGURED") &&
                           Objects.nonNull(employeeBalanceDTO.getMarriageLeave())) {
                    employeeBalanceDTO.setMarriageLeave(String.valueOf(Float.parseFloat(employeeBalanceDTO.getMarriageLeave()) / 8.0f));
                }
                if (!employeeBalanceDTO.getMaternityLeave().equals("NOT_CONFIGURED") &&
                    isLImitQuotaMap.get(Holiday.HolidayType.MATERNITY_LEAVE)) {
                    employeeBalanceDTO.setMaternityLeave("NO_LIMITED");
                } else if (!employeeBalanceDTO.getMaternityLeave().equals("NOT_CONFIGURED") &&
                           Objects.nonNull(employeeBalanceDTO.getMaternityLeave())) {
                    employeeBalanceDTO.setMaternityLeave(String.valueOf(Float.parseFloat(employeeBalanceDTO.getMaternityLeave()) / 8.0f));
                }
                if (!employeeBalanceDTO.getCompensatoryLeave().equals("NOT_CONFIGURED") &&
                    isLImitQuotaMap.get(Holiday.HolidayType.COMPENSATORY_LEAVE)) {
                    employeeBalanceDTO.setCompensatoryLeave("NO_LIMITED");
                } else if (!employeeBalanceDTO.getCompensatoryLeave().equals("NOT_CONFIGURED") &&
                           Objects.nonNull(employeeBalanceDTO.getCompensatoryLeave())) {
                    employeeBalanceDTO.setCompensatoryLeave(String.valueOf(Float.parseFloat(employeeBalanceDTO.getCompensatoryLeave())));
                }
                if (!employeeBalanceDTO.getCompassionateLeave().equals("NOT_CONFIGURED") &&
                    isLImitQuotaMap.get(Holiday.HolidayType.COMPASSIONATE_LEAVE)) {
                    employeeBalanceDTO.setCompassionateLeave("NO_LIMITED");
                } else if (!employeeBalanceDTO.getCompassionateLeave().equals("NOT_CONFIGURED") &&
                           Objects.nonNull(employeeBalanceDTO.getCompassionateLeave())) {
                    employeeBalanceDTO.setCompassionateLeave(String.valueOf(
                            Float.parseFloat(employeeBalanceDTO.getCompassionateLeave()) / 8.0f));
                }
                if (!employeeBalanceDTO.getBereavementLeave().equals("NOT_CONFIGURED") &&
                    isLImitQuotaMap.get(Holiday.HolidayType.BEREAVEMENT_LEAVE)) {
                    employeeBalanceDTO.setBereavementLeave("NO_LIMITED");
                } else if (!employeeBalanceDTO.getBereavementLeave().equals("NOT_CONFIGURED") &&
                           Objects.nonNull(employeeBalanceDTO.getBereavementLeave())) {
                    employeeBalanceDTO.setBereavementLeave(String.valueOf(
                            Float.parseFloat(employeeBalanceDTO.getBereavementLeave()) / 8.0f));
                }
                if (!employeeBalanceDTO.getAnnualLeave().equals("NOT_CONFIGURED") &&
                    isLImitQuotaMap.get(Holiday.HolidayType.ANNUAL_LEAVE)) {
                    employeeBalanceDTO.setAnnualLeave("NO_LIMITED");
                } else if (!employeeBalanceDTO.getAnnualLeave().equals("NOT_CONFIGURED") &&
                           Objects.nonNull(employeeBalanceDTO.getAnnualLeave())) {
                    employeeBalanceDTO.setAnnualLeave(String.valueOf(Float.parseFloat(employeeBalanceDTO.getAnnualLeave()) / 8.0f));
                }

                // 获取考勤组名称和部门名称
                BalanceExportVO exportVO = BalanceExportVO.builder()
                        .timeOfInduction(employeeBalanceDTO.getTimeOfInduction())
                        .annualLeave(employeeBalanceDTO.getAnnualLeave())
                        .bereavementLeave(employeeBalanceDTO.getBereavementLeave())
                        .compassionateLeave(employeeBalanceDTO.getCompassionateLeave())
                        .compensatoryLeave(employeeBalanceDTO.getCompensatoryLeave())
                        .employeeName(employeeBalanceDTO.getEmployeeName())
                        .marriageLeave(employeeBalanceDTO.getMarriageLeave())
                        .maternityLeave(employeeBalanceDTO.getMaternityLeave())
                        .paternityLeave(employeeBalanceDTO.getPaternityLeave())
                        .sickLeave(employeeBalanceDTO.getSickLeave())
                        .deptNam(organMap.get(employeeBalanceDTO.getOrganizationId()))
                        .build();
                data.add(exportVO);
            });
        }
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        String excelName = URLEncoder.encode("假期余额明细", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
        EasyExcel.write(response.getOutputStream(), BalanceExportVO.class)
                .registerWriteHandler(DefaultHorizontalCellStyleStrategy.getStyleStrategy()).sheet().doWrite(data);
    }
}
