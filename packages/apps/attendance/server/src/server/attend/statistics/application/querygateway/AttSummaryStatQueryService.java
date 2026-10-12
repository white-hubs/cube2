package com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.application.querygateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAwayBusinessLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttLeavePostFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttLeavePostLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAwayBusinessLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttLeavePostFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttLeavePostLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result.AttAskLeaveLedgerPageDateResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result.AttAwayBusinessLedgerPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result.AttLeavePostLedgerPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttApproveLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInLedgerDetailsMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInLedgerDetailsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.WorkOmitResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttClassesTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttHolidayGrantWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.repository.AttHolidayRuleRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRuleListResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.dao.WorkOvertimeLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.entity.WorkOvertimeLedger;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeLedgerPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.application.dto.AttClockDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.param.AttSummaryStatPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result.AttAppStaffSummaryStatResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result.AttSummaryStaffStatResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result.AttSummaryStatPageDataResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result.AttSummaryStatPageLeaveResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result.AttSummaryStatPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttLegalHolidayUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.util.ExcelUtils;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 汇总统计查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttSummaryStatQueryService {
    private final AttAskLeaveLedgerMapper attAskLeaveLedgerMapper;
    private final AttGroupStaffMapper attGroupStaffMapper;
    private final AttSchedulingMapper attSchedulingMapper;
    private final AttLeavePostLedgerMapper attLeavePostLedgerMapper;
    private final WorkOvertimeLedgerMapper workOvertimeLedgerMapper;
    private final AttHolidayRuleRepository attHolidayRuleRepository;
    private final AttClockInLedgerDetailsMapper attClockInLedgerDetailsMapper;
    private final AttAskLeaveFromMapper attAskLeaveFromMapper;
    private final AttLeavePostFromMapper attLeavePostFromMapper;
    private final AttApproveLogMapper attApproveLogMapper;
    private final AttAwayBusinessLedgerMapper attAwayBusinessLedgerMapper;
    private final AttHolidayRuleMapper attHolidayRuleMapper;

    /**
     * 汇总统计分页列表
     *
     * @param param 条件入参
     *
     * @return 分页列表对象
     */
    public AttSummaryStatPageResult summaryStatPage(AttSummaryStatPageParam param) {
        // 判断入参的时间段
        if (param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null) {
            param.setPeriod(new Period(DateUtils.beginOfMonth(LocalDateTime.now()), DateUtils.parse(
                    DateUtils.toTimestamp(DateUtils.beginOfMonth(LocalDateTime.now())) - 1000)));
        }

        AttSummaryStatPageResult result = new AttSummaryStatPageResult();
        /* 获取人员 */
        Page<AttGroupStaffPO> page = attGroupStaffMapper.selectPage(PagingUtils.convert(param), Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                            .eq(param.getStaffId() != null, AttGroupStaffPO::getStaffId, param.getStaffId())
                                                                            .eq(param.getDeptId() != null, AttGroupStaffPO::getDeptId, param.getDeptId())
                                                                            .eq(param.getGroupId() != null, AttGroupStaffPO::getGroupId, param.getGroupId())
                                                                            .like(StringUtils.isNotBlank(param.getStaffName()), AttGroupStaffPO::getName, param.getStaffName())
                                                                            .orderByDesc(AttGroupStaffPO::getName)
                                                                   );
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setRecords(new ArrayList<>());

        Set<Long> staffIdSet = new HashSet<>();
        if (page.getRecords() != null) {
            for (AttGroupStaffPO groupStaffPO : page.getRecords()) {
                staffIdSet.add(groupStaffPO.getStaffId());
            }
        }
        if (staffIdSet.isEmpty()) {
            return result;
        }

        /* 获取假期表头 */
        Map<Long, AttHolidayRulePO> holidayRulePOMap = new HashMap<>();
        List<AttHolidayRuleListResult> holidayTitleList = new ArrayList<>();
        attHolidayRuleRepository.setHolidayTableTitle(holidayTitleList, holidayRulePOMap);
        result.setTableTitle(holidayTitleList);
        // 获取扩展数据
        result.setRecords(this.getDataList(param, page.getRecords(), holidayRulePOMap));
        return result;
    }

    /**
     * app首页统计数据
     *
     * @param param 条件
     *
     * @return 结果
     */
    public AttSummaryStaffStatResult appHomeStat(AttSummaryStatPageParam param) {
        if (param.getStaffId() == null) {
            param.setStaffId(SecurityContextHolder.getPrincipal().getId());
        }
        AttSummaryStaffStatResult result = new AttSummaryStaffStatResult();
        AttAppStaffSummaryStatResult summary = this.staffSummaryStat(param);
        BeanUtils.copyProperties(summary, result);
        Map<Long, AttHolidayRulePO> holidayRulePOMap = new HashMap<>();
        List<AttHolidayRulePO> holidayRulePOList = attHolidayRuleMapper.selectList(Wrappers.emptyWrapper());
        if (holidayRulePOList != null) {
            holidayRulePOMap = holidayRulePOList.stream().collect(Collectors.toMap(AttHolidayRulePO::getId, r -> r));
        }
        float askLeaveDuration = AttConstants.zeroFloat;
        for (AttSummaryStatPageLeaveResult leaveResult : summary.getStatLeaveValueList()) {
            if (AttConstants.DAYS.equals(leaveResult.getUnit())) {
                askLeaveDuration += Float.parseFloat(leaveResult.getNumber());
            } else {
                AttHolidayRulePO po = holidayRulePOMap.get(leaveResult.getHolidayRuleId());
                int dayOfHour = AttConstants.dayOfHour;
                if (po != null) {
                    dayOfHour = po.getDayOfHour();
                }
                Long num = AttHolidayDomain.unitAndNumToLong(Float.parseFloat(leaveResult.getNumber()), leaveResult.getUnit(), dayOfHour);
                askLeaveDuration += Float.parseFloat(AttHolidayDomain.longToUnit(num, AttConstants.DAYS, dayOfHour, false));
            }
        }
        result.setAskLeaveDuration(Float.parseFloat(String.format("%." + AttConstants.oneInt + "f", askLeaveDuration)));
        return result;
    }

    /**
     * app人员汇总统计
     *
     * @param param 条件
     *
     * @return 统计
     */
    public AttAppStaffSummaryStatResult staffSummaryStat(AttSummaryStatPageParam param) {
        if (param == null) {
            param = new AttSummaryStatPageParam();
        }
        if (param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null) {
            param.setPeriod(new Period(DateUtils.beginOfMonth(LocalDateTime.now()), DateUtils.parse(
                    DateUtils.toTimestamp(DateUtils.endOfMonth(LocalDateTime.now())) - 1000)));
        }
        if (param.getStaffId() == null) {
            Long staffId = SecurityContextHolder.getPrincipal().getId();
            Assert.isUnauthenticated(staffId == null, "登录信息过期！");
            param.setStaffId(staffId);
        }

        List<AttHolidayRulePO> holidayRulePOList = attHolidayRuleMapper.selectList(Wrappers.<AttHolidayRulePO>lambdaQuery()
                                                                                           .orderByAsc(AttHolidayRulePO::getCreateTime));
        if (holidayRulePOList == null) {
            holidayRulePOList = new ArrayList<>();
        }
        return this.getAppDataList(param, param.getStaffId(), holidayRulePOList);
    }

    /**
     * 导出汇总统计
     *
     * @param param    条件
     * @param request  请求
     * @param response 响应
     */
    public void summaryStatExport(AttSummaryStatPageParam param, HttpServletRequest request, HttpServletResponse response) {
        // 判断入参的时间段
        if (param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null) {
            param.setPeriod(new Period(DateUtils.beginOfMonth(LocalDateTime.now()), DateUtils.parse(
                    DateUtils.toTimestamp(DateUtils.beginOfMonth(LocalDateTime.now())) - 1000)));
        }

        /* 获取人员 */
        List<AttGroupStaffPO> groupStaffPOList = attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                                        .eq(param.getDeptId() !=
                                                                                            null, AttGroupStaffPO::getDeptId, param.getDeptId())
                                                                                        .eq(param.getGroupId() !=
                                                                                            null, AttGroupStaffPO::getGroupId, param.getGroupId())
                                                                                        .like(StringUtils.isNotBlank(param.getStaffName()), AttGroupStaffPO::getName, param.getStaffName())
                                                                                        .orderByDesc(AttGroupStaffPO::getName)
                                                                               );

        Set<Long> staffIdSet = new HashSet<>();
        if (groupStaffPOList != null) {
            for (AttGroupStaffPO groupStaffPO : groupStaffPOList) {
                staffIdSet.add(groupStaffPO.getStaffId());
            }
        }
        if (staffIdSet.isEmpty()) {
            throw new ServiceException("暂无考勤组人员！");
        }

        /* 获取假期表头 */
        Map<Long, AttHolidayRulePO> holidayRulePOMap = new HashMap<>();
        List<AttHolidayRuleListResult> holidayTitleList = new ArrayList<>();
        attHolidayRuleRepository.setHolidayTableTitle(holidayTitleList, holidayRulePOMap);

        List<AttSummaryStatPageDataResult> dataList = this.getDataList(param, groupStaffPOList, holidayRulePOMap);

        // 处理表头
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("staffName", "员工姓名");
        headers.put("deptName", "部门");
        headers.put("legalWorkDays", "应出勤天数");
        headers.put("realWorkDays", "实际出勤天数");
        headers.put("sumShiftNum", "合计班次");
        headers.put("morningNum", "白班");
        headers.put("middleNum", "中班");
        headers.put("nightNum", "夜班");
        headers.put("leavePostDuration", "离岗(天)");
        headers.put("awayBusinessDuration", "出差(天)");
        headers.put("skipWorkDay", "旷工(矿工)");
        headers.put("noClockNum", "缺卡次数");
        headers.put("repairClockNum", "补卡次数");
        headers.put("lateNum", "迟到次数");
        headers.put("lateTotalTime", "迟到时长(分钟)");
        headers.put("earlyNum", "早退次数");
        headers.put("earlyTotalTime", "早退时长(分钟)");
        headers.put("overtimeWorkDuration", "工作日加班(小时)");
        headers.put("overtimeRestDuration", "休息日加班(小时)");
        headers.put("overtimeFestDuration", "节假日加班(小时)");
        headers.put("overtimeWorkCostDuration", "工作日计加班费的时长(小时)");
        headers.put("overtimeRestCostDuration", "休息日计加班费的时长(小时)");
        headers.put("overtimeFestCostDuration", "节假日计加班费的时长(小时)");
        if (!holidayTitleList.isEmpty()) {
            for (AttHolidayRuleListResult ruleResult : holidayTitleList) {
                headers.put(String.valueOf(ruleResult.getId()), ruleResult.getName() + "(" + ruleResult.getUnit() + ")");
            }
        }

        // 处理excel数据
        List<Map<String, String>> excelDataList = this.getExcelData(dataList, holidayTitleList);

        /* 导出 */
        ExcelWriter writer = ExcelUtil.getWriter(true);
        /* 设置样式 */

        // 设置表头背景色
        CellStyle headCellStyle = writer.getHeadCellStyle();
        StyleUtil.setColor(headCellStyle, IndexedColors.AQUA, FillPatternType.SOLID_FOREGROUND);
        //设置单元格自动换行
        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setWrapText(true);
        // 设置列宽
        writer.setColumnWidth(-1, 25);
        writer.setRowHeight(-1, 25);

        /* 设置数据 */
        writer.setHeaderAlias(headers);
        writer.write(excelDataList, true);
        String fileName = "summaryStat";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    private List<AttSummaryStatPageDataResult> getDataList(AttSummaryStatPageParam param, List<AttGroupStaffPO> groupStaffPOList,
                                                           Map<Long, AttHolidayRulePO> holidayRulePOMap
                                                          ) {
        List<AttSummaryStatPageDataResult> resultList = new ArrayList<>();
        Set<Long> staffIdSet = new HashSet<>();
        if (groupStaffPOList != null) {
            for (AttGroupStaffPO groupStaffPO : groupStaffPOList) {
                staffIdSet.add(groupStaffPO.getStaffId());
            }
        }
        if (staffIdSet.isEmpty()) {
            return resultList;
        }

        /* 获取法定出勤天数 */
        int legalWorkDay = AttConstants.zeroInt;
        LocalDateTime printDateTime = DateUtils.beginOfMonth(param.getPeriod().getBegin());
        do {
            legalWorkDay += AttLegalHolidayUtils.getMonthLegalWorkDay(printDateTime.getYear(), printDateTime.getMonthValue())
                    .getWorkList().size();
            printDateTime = DateUtils.offsetMonth(printDateTime, AttConstants.oneInt);
        } while (param.getPeriod().getEnd().isAfter(printDateTime));

        /* 获取排班数据 */
        Map<Long, List<AttSchedulingPO>> staffSchedulingMap = new HashMap<>();
        List<AttSchedulingPO> schedulingPOList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                        .between(AttSchedulingPO::getClassesRealStartTime, param
                                                                                                .getPeriod().getBegin(), param.getPeriod()
                                                                                                         .getEnd())
                                                                                        .in(AttSchedulingPO::getStaffId, staffIdSet)
                                                                               );
        if (schedulingPOList != null && !schedulingPOList.isEmpty()) {
            staffSchedulingMap = schedulingPOList.stream().collect(Collectors.groupingBy(AttSchedulingPO::getStaffId));
        }

        /* 获取打卡台账信息(异常的台账信息) */
        Map<Long, List<AttClockInLedgerDetailsPO>> clockLedgerMap = new HashMap<>();
        List<AttClockInLedgerDetailsPO> clockLedgerList = attClockInLedgerDetailsMapper.selectList(Wrappers
                                                                                                           .<AttClockInLedgerDetailsPO>lambdaQuery()
                                                                                                           .between(AttClockInLedgerDetailsPO::getLedgerBelongDate, param
                                                                                                                   .getPeriod()
                                                                                                                   .getBegin(), param
                                                                                                                            .getPeriod()
                                                                                                                            .getEnd())
                                                                                                           .and(i -> i
                                                                                                                   .eq(AttClockInLedgerDetailsPO::getState, ClockInEnum.ABNORMAL_WORK.name())
                                                                                                                   .or()
                                                                                                                   .eq(AttClockInLedgerDetailsPO::getState, ClockInEnum.NEGLECT_WORK.name()))
                                                                                                           .in(AttClockInLedgerDetailsPO::getUserId, staffIdSet)
                                                                                                  );
        if (clockLedgerList != null && !clockLedgerList.isEmpty()) {
            clockLedgerMap = clockLedgerList.stream().collect(Collectors.groupingBy(AttClockInLedgerDetailsPO::getUserId));
        }

        /* 获取补卡信息 */
        Map<Long, List<WorkOmitResult>> repairCardMap = new HashMap<>();
        List<WorkOmitResult> repairCardList = attApproveLogMapper.getRepairCardCountStatList(Wrappers.<AttApproveLogPO>lambdaQuery()
                                                                                                     .le(AttApproveLogPO::getStartTime, param
                                                                                                             .getPeriod().getEnd())
                                                                                                     .ge(AttApproveLogPO::getEndTime, param
                                                                                                             .getPeriod().getBegin())
                                                                                                     .eq(AttApproveLogPO::getBusinessType, ClassesStatusTriggerEnum.REPAIR_CLOCK.name())
                                                                                                     .eq(AttApproveLogPO::getStatus, FlowResult.FINISHED.name())
                                                                                                     .in(AttApproveLogPO::getUserId, staffIdSet)
                                                                                            );
        if (repairCardList != null && !repairCardList.isEmpty()) {
            repairCardMap = repairCardList.stream().collect(Collectors.groupingBy(WorkOmitResult::getUserId));
        }

        /* 获取请假单信息↑ */
        Map<Long, List<AttAskLeaveFromPO>> askLeaveFromMap = new HashMap<>();
        List<AttAskLeaveFromPO> askLeaveFromPOList = attAskLeaveFromMapper.selectList(Wrappers.<AttAskLeaveFromPO>lambdaQuery()
                                                                                              .le(AttAskLeaveFromPO::getLeaveStartTime, param
                                                                                                      .getPeriod().getEnd())
                                                                                              .ge(AttAskLeaveFromPO::getLeaveEndTime, param
                                                                                                      .getPeriod().getBegin())
                                                                                              .eq(AttAskLeaveFromPO::getStatus, FlowResult.FINISHED.name())
                                                                                              .in(AttAskLeaveFromPO::getApplicantId, staffIdSet)
                                                                                     );
        if (askLeaveFromPOList != null && !askLeaveFromPOList.isEmpty()) {
            askLeaveFromMap = askLeaveFromPOList.stream().collect(Collectors.groupingBy(AttAskLeaveFromPO::getApplicantId));
        }
        /* 获取离岗单信息 */
        Map<Long, List<AttLeavePostFromPO>> leavePostFromMap = new HashMap<>();
        List<AttLeavePostFromPO> leavePostFromPOList = attLeavePostFromMapper.selectList(Wrappers.<AttLeavePostFromPO>lambdaQuery()
                                                                                                 .le(AttLeavePostFromPO::getPostStartTime, param
                                                                                                         .getPeriod().getEnd())
                                                                                                 .ge(AttLeavePostFromPO::getPostEndTime, param
                                                                                                         .getPeriod().getBegin())
                                                                                                 .eq(AttLeavePostFromPO::getStatus, FlowResult.FINISHED.name())
                                                                                                 .in(AttLeavePostFromPO::getApplicantId, staffIdSet)
                                                                                        );
        if (leavePostFromPOList != null && !leavePostFromPOList.isEmpty()) {
            leavePostFromMap = leavePostFromPOList.stream().collect(Collectors.groupingBy(AttLeavePostFromPO::getApplicantId));
        }

        /* 获取请假统计 */
        Map<Long, List<AttAskLeaveLedgerPO>> askLeaveDetailMap = new HashMap<>();
        List<AttAskLeaveLedgerPO> detailPOList = attAskLeaveLedgerMapper.selectList(Wrappers.<AttAskLeaveLedgerPO>lambdaQuery()
                                                                                            .ge(AttAskLeaveLedgerPO::getLeaveDate, param
                                                                                                    .getPeriod().getBegin())
                                                                                            .le(AttAskLeaveLedgerPO::getLeaveDate, param
                                                                                                    .getPeriod().getEnd())
                                                                                            .in(AttAskLeaveLedgerPO::getStaffId, staffIdSet)
                                                                                   );
        if (detailPOList != null && !detailPOList.isEmpty()) {
            askLeaveDetailMap = detailPOList.stream().collect(Collectors.groupingBy(AttAskLeaveLedgerPO::getStaffId));
        }


        /* 获取离岗统计 */
        Map<Long, List<AttLeavePostLedgerPageResult>> leavePostMap = new HashMap<>();
        List<AttLeavePostLedgerPageResult> leavePostLedgerPOList = attLeavePostLedgerMapper.selectLedgerList(Wrappers
                                                                                                                     .<AttLeavePostLedgerPO>lambdaQuery()
                                                                                                                     .ge(AttLeavePostLedgerPO::getPostDate, param
                                                                                                                             .getPeriod()
                                                                                                                             .getBegin())
                                                                                                                     .le(AttLeavePostLedgerPO::getPostDate, param
                                                                                                                             .getPeriod()
                                                                                                                             .getEnd())
                                                                                                                     .in(AttLeavePostLedgerPO::getStaffId, staffIdSet)
                                                                                                                     .groupBy(AttLeavePostLedgerPO::getStaffId)
                                                                                                            );
        if (leavePostLedgerPOList != null && !leavePostLedgerPOList.isEmpty()) {
            leavePostMap = leavePostLedgerPOList.stream().collect(Collectors.groupingBy(AttLeavePostLedgerPageResult::getStaffId));
        }

        /* 获取出差统计 */
        Map<Long, AttAwayBusinessLedgerPageResult> awayBusinessMap = new HashMap<>();
        List<AttAwayBusinessLedgerPageResult> awayBusinessLedgerPOList = attAwayBusinessLedgerMapper.selectLedgerList(Wrappers
                                                                                                                              .<AttAwayBusinessLedgerPO>lambdaQuery()
                                                                                                                              .ge(AttAwayBusinessLedgerPO::getAwayDate, param
                                                                                                                                      .getPeriod()
                                                                                                                                      .getBegin())
                                                                                                                              .le(AttAwayBusinessLedgerPO::getAwayDate, param
                                                                                                                                      .getPeriod()
                                                                                                                                      .getEnd())
                                                                                                                              .in(AttAwayBusinessLedgerPO::getStaffId, staffIdSet)
                                                                                                                              .groupBy(AttAwayBusinessLedgerPO::getStaffId)
                                                                                                                     );
        if (awayBusinessLedgerPOList != null && !awayBusinessLedgerPOList.isEmpty()) {
            for (AttAwayBusinessLedgerPageResult attAwayBusinessLedgerPageResult : awayBusinessLedgerPOList) {
                awayBusinessMap.put(attAwayBusinessLedgerPageResult.getStaffId(), attAwayBusinessLedgerPageResult);
            }
        }

        /* 获取加班统计 */
        Map<Long, List<WorkOvertimeLedgerPageResult>> workOverTimeMap = new HashMap<>();
        List<WorkOvertimeLedgerPageResult> workOverTimeList = workOvertimeLedgerMapper.selectLedgerList(Wrappers
                                                                                                                .<WorkOvertimeLedger>lambdaQuery()
                                                                                                                .ge(WorkOvertimeLedger::getWorkDate, param
                                                                                                                        .getPeriod()
                                                                                                                        .getBegin())
                                                                                                                .le(WorkOvertimeLedger::getWorkDate, param
                                                                                                                        .getPeriod()
                                                                                                                        .getEnd())
                                                                                                                .in(WorkOvertimeLedger::getUserId, staffIdSet)
                                                                                                                .groupBy(WorkOvertimeLedger::getUserId)
                                                                                                       );
        if (workOverTimeList != null && !workOverTimeList.isEmpty()) {
            workOverTimeMap = workOverTimeList.stream().collect(Collectors.groupingBy(WorkOvertimeLedgerPageResult::getUserId));
        }

        /* 组装返回对象*/
        int finalLegalWorkDay = legalWorkDay;
        Map<Long, List<AttAskLeaveLedgerPO>> finalAskLeaveDetailMap = askLeaveDetailMap;
        Map<Long, List<AttSchedulingPO>> finalStaffSchedulingMap = staffSchedulingMap;
        Map<Long, List<AttLeavePostLedgerPageResult>> finalLeavePostMap = leavePostMap;
        Map<Long, List<WorkOvertimeLedgerPageResult>> finalWorkOverTimeMap = workOverTimeMap;
        Map<Long, List<AttClockInLedgerDetailsPO>> finalClockLedgerMap = clockLedgerMap;
        Map<Long, List<AttAskLeaveFromPO>> finalAskLeaveFromMap = askLeaveFromMap;
        Map<Long, List<AttLeavePostFromPO>> finalLeavePostFromMap = leavePostFromMap;
        Map<Long, List<WorkOmitResult>> finalRepairCardMap = repairCardMap;
        resultList = groupStaffPOList.stream().map(po -> {
            Long staffId = po.getStaffId();
            AttSummaryStatPageDataResult data = new AttSummaryStatPageDataResult();
            data.setStaffId(staffId);
            data.setStaffName(po.getName());
            data.setDeptId(po.getDeptId());
            data.setDeptName(po.getDeptName());
            // 法定出勤天数
            data.setLegalWorkDays((float) finalLegalWorkDay);
            // 设置排班数据
            List<AttSchedulingPO> staffSchedulingPOS = finalStaffSchedulingMap.get(staffId);
            if (staffSchedulingPOS != null && !staffSchedulingPOS.isEmpty()) {
                Map<String, List<AttSchedulingPO>> staffClassTypeMap = staffSchedulingPOS.stream()
                        .collect(Collectors.groupingBy(AttSchedulingPO::getClassesType));
                List<AttSchedulingPO> morningList = staffClassTypeMap.get(AttClassesTypeEnum.EARLY.name());
                if (morningList != null) {
                    data.setMorningNum(morningList.size());
                }
                List<AttSchedulingPO> middleList = staffClassTypeMap.get(AttClassesTypeEnum.MIDDLE_SHIFT.name());
                if (middleList != null) {
                    data.setMiddleNum(middleList.size());
                }
                List<AttSchedulingPO> nightList = staffClassTypeMap.get(AttClassesTypeEnum.NIGHT_SHIFT.name());
                if (nightList != null) {
                    data.setNightNum(nightList.size());
                }
                data.setSumShiftNum(data.getMorningNum() + data.getMiddleNum() + data.getNightNum());
            }

            /* 设置出差信息 */
            AttAwayBusinessLedgerPageResult staffAwayBusiness = awayBusinessMap.get(staffId);
            if (staffAwayBusiness != null) {
                data.setAwayBusinessDuration(Float.parseFloat(AttHolidayDomain.longToUnit(staffAwayBusiness.getAwayNumber(), AttConstants.DAYS, AttConstants.dayOfHour, false)));
            }

            /* 设置离岗数据 */
            List<AttLeavePostLedgerPageResult> staffLeavePostList = finalLeavePostMap.get(staffId);
            if (staffLeavePostList != null && !staffLeavePostList.isEmpty()) {
                AttLeavePostLedgerPageResult staffLeavePost = staffLeavePostList.get(AttConstants.zeroInt);
                if (staffLeavePost != null && staffLeavePost.getPostNumber() != null) {
                    data.setLeavePostDuration(Float.parseFloat(AttHolidayDomain.longToUnit(staffLeavePost.getPostNumber(), AttConstants.DAYS, AttConstants.dayOfHour, false)));
                }
            }

            /* 设置补卡数据 */
            List<WorkOmitResult> staffRepairCardList = finalRepairCardMap.get(staffId);
            if (staffRepairCardList != null && !staffRepairCardList.isEmpty()) {
                WorkOmitResult staffRepairCard = staffRepairCardList.get(AttConstants.zeroInt);
                if (staffRepairCard != null) {
                    data.setRepairClockNum(staffRepairCard.getNum());
                }
            }

            /* 设置加班数据 */
            List<WorkOvertimeLedgerPageResult> staffWorkOverTimeList = finalWorkOverTimeMap.get(staffId);
            WorkOvertimeLedgerPageResult staffWorkOverTime = new WorkOvertimeLedgerPageResult();
            if (staffWorkOverTimeList != null && !staffWorkOverTimeList.isEmpty()) {
                staffWorkOverTime = staffWorkOverTimeList.get(AttConstants.zeroInt);
            }
            data.setOvertimeWorkDuration(
                    staffWorkOverTime.getWorkDuration() == null ? BigDecimal.ZERO : staffWorkOverTime.getWorkDuration());
            data.setOvertimeWorkCostDuration(
                    staffWorkOverTime.getWorkPayDuration() == null ? BigDecimal.ZERO : staffWorkOverTime.getWorkPayDuration());
            data.setOvertimeRestDuration(
                    staffWorkOverTime.getRestDuration() == null ? BigDecimal.ZERO : staffWorkOverTime.getRestDuration());
            data.setOvertimeRestCostDuration(
                    staffWorkOverTime.getRestPatDuration() == null ? BigDecimal.ZERO : staffWorkOverTime.getRestPatDuration());
            data.setOvertimeFestDuration(
                    staffWorkOverTime.getFestDuration() == null ? BigDecimal.ZERO : staffWorkOverTime.getFestDuration());
            data.setOvertimeFestCostDuration(
                    staffWorkOverTime.getFestOatDuration() == null ? BigDecimal.ZERO : staffWorkOverTime.getFestOatDuration());

            /* 设置请假数据，同时计算计入缺勤的请假天数 */
            this.extLeaveList(data, staffId, finalAskLeaveDetailMap, holidayRulePOMap);

            /* 设置打卡数据 */
            AttClockDTO clockDTO = this.extClockData(finalClockLedgerMap.get(staffId), finalAskLeaveFromMap.get(staffId), finalLeavePostFromMap.get(staffId));
            data.setNoClockNum(clockDTO.getNoClockNum());
            data.setNoClockStartNum(clockDTO.getNoClockStartNum());
            data.setNoClockEndNum(clockDTO.getNoClockEndNum());
            data.setSkipWorkDay(clockDTO.getSkipWorkDay());
            data.setLateNum(clockDTO.getLateNum());
            data.setEarlyNum(clockDTO.getEarlyNum());
            data.setLateTotalTime(clockDTO.getLateTotalTime());
            data.setEarlyTotalTime(clockDTO.getEarlyTotalTime());

            /* 设置实际出勤天数 */
            data.setRealWorkDays(Math.max(
                    data.getLegalWorkDays() - data.getSkipWorkDay() - data.getDeductionLeaveDay(), AttConstants.zeroFloat));
            return data;
        }).collect(Collectors.toList());
        return resultList;
    }

    private AttAppStaffSummaryStatResult getAppDataList(AttSummaryStatPageParam param, Long staffId,
                                                        List<AttHolidayRulePO> holidayRulePOList
                                                       ) {
        AttAppStaffSummaryStatResult result = new AttAppStaffSummaryStatResult();
        if (staffId == null) {
            return result;
        }

        /* 获取法定出勤天数 */
        int legalWorkDay = AttConstants.zeroInt;
        legalWorkDay += AttLegalHolidayUtils.getPeriodLegalWorkDay(param.getPeriod()).getWorkList().size();

        /* 获取打卡信息(异常的台账信息) */
        List<AttClockInLedgerDetailsPO> clockLedgerList = attClockInLedgerDetailsMapper.selectList(Wrappers
                                                                                                           .<AttClockInLedgerDetailsPO>lambdaQuery()
                                                                                                           .eq(AttClockInLedgerDetailsPO::getUserId, staffId)
                                                                                                           .between(AttClockInLedgerDetailsPO::getLedgerBelongDate, param
                                                                                                                   .getPeriod()
                                                                                                                   .getBegin(), param
                                                                                                                            .getPeriod()
                                                                                                                            .getEnd())
                                                                                                           .and(i -> i
                                                                                                                   .eq(AttClockInLedgerDetailsPO::getState, ClockInEnum.ABNORMAL_WORK.name())
                                                                                                                   .or()
                                                                                                                   .eq(AttClockInLedgerDetailsPO::getState, ClockInEnum.NEGLECT_WORK.name()))
                                                                                                  );
        if (clockLedgerList == null) {
            clockLedgerList = new ArrayList<>();
        }

        /* 获取补卡台账信息 */
        WorkOmitResult repairCard = new WorkOmitResult();
        List<WorkOmitResult> repairCardList = attApproveLogMapper.getRepairCardCountStatList(Wrappers.<AttApproveLogPO>lambdaQuery()
                                                                                                     .eq(AttApproveLogPO::getUserId, staffId)
                                                                                                     .le(AttApproveLogPO::getStartTime, param
                                                                                                             .getPeriod().getEnd())
                                                                                                     .ge(AttApproveLogPO::getEndTime, param
                                                                                                             .getPeriod().getBegin())
                                                                                                     .eq(AttApproveLogPO::getBusinessType, ClassesStatusTriggerEnum.REPAIR_CLOCK.name())
                                                                                                     .eq(AttApproveLogPO::getStatus, FlowResult.FINISHED.name())
                                                                                            );
        if (repairCardList != null && !repairCardList.isEmpty()) {
            repairCard = repairCardList.get(AttConstants.zeroInt);
        }

        /* 获取请假单信息↑ */
        List<AttAskLeaveFromPO> askLeaveFromPOList = attAskLeaveFromMapper.selectList(Wrappers.<AttAskLeaveFromPO>lambdaQuery()
                                                                                              .le(AttAskLeaveFromPO::getLeaveStartTime, param
                                                                                                      .getPeriod().getEnd())
                                                                                              .ge(AttAskLeaveFromPO::getLeaveEndTime, param
                                                                                                      .getPeriod().getBegin())
                                                                                              .eq(AttAskLeaveFromPO::getStatus, FlowResult.FINISHED.name())
                                                                                              .eq(AttAskLeaveFromPO::getApplicantId, staffId)
                                                                                     );
        if (askLeaveFromPOList == null) {
            askLeaveFromPOList = new ArrayList<>();
        }

        /* 获取离岗单信息 */
        List<AttLeavePostFromPO> leavePostFromPOList = attLeavePostFromMapper.selectList(Wrappers.<AttLeavePostFromPO>lambdaQuery()
                                                                                                 .le(AttLeavePostFromPO::getPostStartTime, param
                                                                                                         .getPeriod().getEnd())
                                                                                                 .ge(AttLeavePostFromPO::getPostEndTime, param
                                                                                                         .getPeriod().getBegin())
                                                                                                 .eq(AttLeavePostFromPO::getStatus, FlowResult.FINISHED.name())
                                                                                                 .eq(AttLeavePostFromPO::getApplicantId, staffId)
                                                                                        );
        if (leavePostFromPOList == null) {
            leavePostFromPOList = new ArrayList<>();
        }

        /* 获取请假台账 */
        Map<Long, AttAskLeaveLedgerPageDateResult> leaveLedgerMap = new HashMap<>();
        List<AttAskLeaveLedgerPageDateResult> leaveLedgerPOList = attAskLeaveLedgerMapper.selectLedgerList(Wrappers
                                                                                                                   .<AttAskLeaveLedgerPO>lambdaQuery()
                                                                                                                   .ge(AttAskLeaveLedgerPO::getLeaveDate, param
                                                                                                                           .getPeriod()
                                                                                                                           .getBegin())
                                                                                                                   .le(AttAskLeaveLedgerPO::getLeaveDate, param
                                                                                                                           .getPeriod()
                                                                                                                           .getEnd())
                                                                                                                   .eq(AttAskLeaveLedgerPO::getStaffId, staffId)
                                                                                                          );
        if (leaveLedgerPOList != null && !leaveLedgerPOList.isEmpty()) {
            for (AttAskLeaveLedgerPageDateResult attAskLeaveLedgerPageDateResult : leaveLedgerPOList) {
                leaveLedgerMap.put(attAskLeaveLedgerPageDateResult.getHolidayRuleId(), attAskLeaveLedgerPageDateResult);
            }
        }

        /* 获取离岗统计 */
        AttLeavePostLedgerPageResult leavePostLedger = new AttLeavePostLedgerPageResult();
        List<AttLeavePostLedgerPageResult> leavePostLedgerPOList = attLeavePostLedgerMapper.selectLedgerList(Wrappers
                                                                                                                     .<AttLeavePostLedgerPO>lambdaQuery()
                                                                                                                     .eq(AttLeavePostLedgerPO::getStaffId, staffId)
                                                                                                                     .ge(AttLeavePostLedgerPO::getPostDate, param
                                                                                                                             .getPeriod()
                                                                                                                             .getBegin())
                                                                                                                     .le(AttLeavePostLedgerPO::getPostDate, param
                                                                                                                             .getPeriod()
                                                                                                                             .getEnd())
                                                                                                            );
        if (leavePostLedgerPOList != null && !leavePostLedgerPOList.isEmpty()) {
            leavePostLedger = leavePostLedgerPOList.get(AttConstants.zeroInt);
        }

        /* 获取出差统计 */
        AttAwayBusinessLedgerPageResult awayBusinessLedger = new AttAwayBusinessLedgerPageResult();
        List<AttAwayBusinessLedgerPageResult> awayBusinessLedgerPOList = attAwayBusinessLedgerMapper.selectLedgerList(Wrappers
                                                                                                                              .<AttAwayBusinessLedgerPO>lambdaQuery()
                                                                                                                              .eq(AttAwayBusinessLedgerPO::getStaffId, staffId)
                                                                                                                              .ge(AttAwayBusinessLedgerPO::getAwayDate, param
                                                                                                                                      .getPeriod()
                                                                                                                                      .getBegin())
                                                                                                                              .le(AttAwayBusinessLedgerPO::getAwayDate, param
                                                                                                                                      .getPeriod()
                                                                                                                                      .getEnd())
                                                                                                                              .last(" limit 1 ")
                                                                                                                     );
        if (awayBusinessLedgerPOList != null && !awayBusinessLedgerPOList.isEmpty()) {
            awayBusinessLedger = awayBusinessLedgerPOList.get(AttConstants.zeroInt);
        }

        /* 获取加班统计 */
        WorkOvertimeLedgerPageResult workOverTime = new WorkOvertimeLedgerPageResult();
        List<WorkOvertimeLedgerPageResult> workOverTimeList = workOvertimeLedgerMapper.selectLedgerList(Wrappers
                                                                                                                .<WorkOvertimeLedger>lambdaQuery()
                                                                                                                .eq(WorkOvertimeLedger::getUserId, staffId)
                                                                                                                .ge(WorkOvertimeLedger::getWorkDate, param
                                                                                                                        .getPeriod()
                                                                                                                        .getBegin())
                                                                                                                .le(WorkOvertimeLedger::getWorkDate, param
                                                                                                                        .getPeriod()
                                                                                                                        .getEnd())
                                                                                                                .groupBy(WorkOvertimeLedger::getUserId)
                                                                                                       );
        if (workOverTimeList != null && !workOverTimeList.isEmpty()) {
            workOverTime = workOverTimeList.get(AttConstants.zeroInt);
        }

        /* 组装返回对象*/
        result.setStaffId(staffId);
        // 法定出勤天数
        result.setLegalWorkDays((float) legalWorkDay);

        /* 设置出差信息 */
        result.setAwayBusinessDuration(Float.parseFloat(AttHolidayDomain.longToUnit(awayBusinessLedger.getAwayNumber(), AttConstants.DAYS, AttConstants.dayOfHour, false)));

        /* 设置离岗数据 */
        result.setLeavePostDuration(Float.parseFloat(AttHolidayDomain.longToUnit(leavePostLedger.getPostNumber(), AttConstants.DAYS, AttConstants.dayOfHour, false)));

        /* 设置补卡数据 */
        result.setRepairClockNum(repairCard.getNum() == null ? AttConstants.zeroInt : repairCard.getNum());


        /* 设置加班数据 */
        result.setOvertimeWorkDuration(workOverTime.getWorkDuration() == null ? BigDecimal.ZERO : workOverTime.getWorkDuration());
        result.setOvertimeRestDuration(workOverTime.getRestDuration() == null ? BigDecimal.ZERO : workOverTime.getRestDuration());
        result.setOvertimeFestDuration(workOverTime.getFestDuration() == null ? BigDecimal.ZERO : workOverTime.getFestDuration());
        result.setOvertimeDuration(result.getOvertimeWorkDuration().add(result.getOvertimeRestDuration())
                                           .add(result.getOvertimeFestDuration()));

        /* 设置请假数据，同时计算计入缺勤的请假天数 */
        float deductionLeaveDay = AttConstants.zeroFloat;
        List<AttSummaryStatPageLeaveResult> staffLeaveList = new ArrayList<>();
        for (AttHolidayRulePO rulePO : holidayRulePOList) {
            AttSummaryStatPageLeaveResult leaveResult = new AttSummaryStatPageLeaveResult();
            leaveResult.setStaffId(staffId);
            leaveResult.setHolidayRuleId(rulePO.getId());
            leaveResult.setHolidayRuleName(rulePO.getName());
            String unit = AttConstants.DAYS;
            if (AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(rulePO.getGrantWay())) {
                unit = AttConstants.HOUR;
            }
            leaveResult.setUnit(unit);
            String leaveString = AttConstants.zeroString;

            AttAskLeaveLedgerPageDateResult leaveLedger = leaveLedgerMap.get(rulePO.getId());
            if (leaveLedger != null) {
                leaveString = AttHolidayDomain.longToUnit(leaveLedger.getLeaveNumber(), unit, rulePO.getDayOfHour(), false);
                if (Boolean.TRUE.equals(rulePO.getIntoAbsence())) {
                    String dayString = AttHolidayDomain.longToUnit(leaveLedger.getLeaveNumber(), AttConstants.DAYS, rulePO.getDayOfHour(), false);
                    deductionLeaveDay += Float.parseFloat(dayString);
                }
            }
            leaveResult.setNumber(leaveString);
            staffLeaveList.add(leaveResult);
        }
        result.setStatLeaveValueList(staffLeaveList);

        /* 设置打卡数据 */
        AttClockDTO clockDTO = this.extClockData(clockLedgerList, askLeaveFromPOList, leavePostFromPOList);
        result.setNoClockNum(clockDTO.getNoClockNum());
        result.setNoClockStartNum(clockDTO.getNoClockStartNum());
        result.setNoClockEndNum(clockDTO.getNoClockEndNum());
        result.setSkipWorkDay(clockDTO.getSkipWorkDay());
        result.setLateNum(clockDTO.getLateNum());
        result.setEarlyNum(clockDTO.getEarlyNum());

        /* 设置实际出勤天数 */
        result.setRealWorkDays(Math.max(result.getLegalWorkDays() - result.getSkipWorkDay() - deductionLeaveDay, AttConstants.zeroFloat));
        return result;

    }

    public List<Map<String, String>> getExcelData(List<AttSummaryStatPageDataResult> dataList,
                                                  List<AttHolidayRuleListResult> holidayTitleList
                                                 ) {
        if (dataList == null || dataList.isEmpty()) {
            return new ArrayList<>();
        }
        if (holidayTitleList == null) {
            holidayTitleList = new ArrayList<>();
        }
        List<Map<String, String>> resultList = new ArrayList<>();
        for (AttSummaryStatPageDataResult result : dataList) {
            Map<String, String> data = new HashMap<>();
            data.put("staffName", result.getStaffName());
            data.put("deptName", result.getDeptName());
            data.put("legalWorkDays", String.valueOf(result.getLegalWorkDays()));
            data.put("realWorkDays", String.valueOf(result.getRealWorkDays()));
            data.put("sumShiftNum", String.valueOf(result.getSumShiftNum()));
            data.put("morningNum", String.valueOf(result.getMorningNum()));
            data.put("middleNum", String.valueOf(result.getMiddleNum()));
            data.put("nightNum", String.valueOf(result.getNightNum()));
            data.put("leavePostDuration", String.valueOf(result.getLeavePostDuration()));
            data.put("awayBusinessDuration", String.valueOf(result.getAwayBusinessDuration()));
            data.put("skipWorkDay", String.valueOf(result.getSkipWorkDay()));
            data.put("noClockNum", String.valueOf(result.getNoClockNum()));
            data.put("repairClockNum", String.valueOf(result.getRepairClockNum()));
            data.put("lateNum", String.valueOf(result.getLateNum()));
            data.put("lateTotalTime", String.valueOf(result.getLateTotalTime()));
            data.put("earlyNum", String.valueOf(result.getEarlyNum()));
            data.put("earlyTotalTime", String.valueOf(result.getEarlyTotalTime()));
            data.put("overtimeWorkDuration", String.valueOf(result.getOvertimeWorkDuration()));
            data.put("overtimeRestDuration", String.valueOf(result.getOvertimeRestDuration()));
            data.put("overtimeFestDuration", String.valueOf(result.getOvertimeFestDuration()));
            data.put("overtimeWorkCostDuration", String.valueOf(result.getOvertimeWorkCostDuration()));
            data.put("overtimeRestCostDuration", String.valueOf(result.getOvertimeRestCostDuration()));
            data.put("overtimeFestCostDuration", String.valueOf(result.getOvertimeFestCostDuration()));

            Map<Long, AttSummaryStatPageLeaveResult> leaveNumberMap = new HashMap<>();
            List<AttSummaryStatPageLeaveResult> statLeaveValueList = result.getStatLeaveValueList();
            if (statLeaveValueList != null) {
                for (AttSummaryStatPageLeaveResult leaveResult : statLeaveValueList) {
                    leaveNumberMap.put(leaveResult.getHolidayRuleId(), leaveResult);
                }
            }
            for (AttHolidayRuleListResult holidayTitle : holidayTitleList) {
                String value = AttConstants.zeroString;
                AttSummaryStatPageLeaveResult leaveResult = leaveNumberMap.get(holidayTitle.getId());
                if (leaveResult != null) {
                    value = leaveResult.getNumber();
                }
                data.put(String.valueOf(holidayTitle.getId()), value);
            }

            if (statLeaveValueList != null) {

                for (AttSummaryStatPageLeaveResult leaveResult : statLeaveValueList) {
                    data.put(String.valueOf(leaveResult.getHolidayRuleId()), leaveResult.getNumber());
                }
            }

            resultList.add(data);
        }

        return resultList;
    }

    private void extLeaveList(AttSummaryStatPageDataResult source, Long staffId, Map<Long, List<AttAskLeaveLedgerPO>> finalDetailMap,
                              Map<Long, AttHolidayRulePO> holidayRulePOMap
                             ) {
        List<AttSummaryStatPageLeaveResult> staffLeaveList = new ArrayList<>();
        List<AttAskLeaveLedgerPO> staffLeaveLedgerList = finalDetailMap.get(staffId);
        float deductionLeaveDay = AttConstants.zeroFloat;
        if (staffLeaveLedgerList != null && !staffLeaveLedgerList.isEmpty()) {
            Map<Long, List<AttAskLeaveLedgerPO>> staffHolidayMap = staffLeaveLedgerList.stream()
                    .collect(Collectors.groupingBy(AttAskLeaveLedgerPO::getHolidayId));
            for (Map.Entry<Long, List<AttAskLeaveLedgerPO>> longListEntry : staffHolidayMap.entrySet()) {
                List<AttAskLeaveLedgerPO> holidayList = longListEntry.getValue();
                AttSummaryStatPageLeaveResult leaveResult = new AttSummaryStatPageLeaveResult();
                leaveResult.setStaffId(staffId);
                leaveResult.setHolidayRuleId(longListEntry.getKey());
                AttHolidayRulePO rulePO = holidayRulePOMap.get(longListEntry.getKey());
                String unit = AttConstants.DAYS;
                if (rulePO != null) {
                    leaveResult.setHolidayRuleName(rulePO.getName());
                    if (AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(rulePO.getGrantWay())) {
                        unit = AttConstants.HOUR;
                    }
                }
                leaveResult.setUnit(unit);

                float totalLeave = AttConstants.zeroFloat;
                for (AttAskLeaveLedgerPO askLeaveLedgerPO : holidayList) {

                    int dayOfHour = AttConstants.dayOfHour;
                    if (askLeaveLedgerPO.getDayOfHour() != null) {
                        dayOfHour = askLeaveLedgerPO.getDayOfHour();
                    }

                    String leaveString = AttHolidayDomain.longToUnit(askLeaveLedgerPO.getLeaveNumber(), unit, dayOfHour, 4);
                    float leaveNum = Float.parseFloat(leaveString);
                    totalLeave += leaveNum;
                    // 计入缺勤
                    if (Boolean.TRUE.equals(askLeaveLedgerPO.getIntoAbsence())) {
                        String dayString = AttHolidayDomain.longToUnit(askLeaveLedgerPO.getLeaveNumber(), AttConstants.DAYS, dayOfHour, 2);
                        float dayNum = Float.parseFloat(dayString);
                        deductionLeaveDay += dayNum;
                    }
                }
                leaveResult.setNumber(String.format("%.1f", totalLeave));
                staffLeaveList.add(leaveResult);
            }
        }
        source.setDeductionLeaveDay(Float.parseFloat(String.format("%.1f", deductionLeaveDay)));
        source.setStatLeaveValueList(staffLeaveList);
    }

    public AttClockDTO extClockData(List<AttClockInLedgerDetailsPO> clockLedgerList, List<AttAskLeaveFromPO> askLeaveFromList,
                                    List<AttLeavePostFromPO> leavePostFromList
                                   ) {
        AttClockDTO result = new AttClockDTO();
        if (clockLedgerList == null || clockLedgerList.isEmpty()) {
            return result;
        }
        if (askLeaveFromList == null) {
            askLeaveFromList = new ArrayList<>();
        }
        if (leavePostFromList == null) {
            leavePostFromList = new ArrayList<>();
        }

        int noClockStartNum = AttConstants.zeroInt;
        int noClockEndNum = AttConstants.zeroInt;
        // 缺卡
        int noClockNum = AttConstants.zeroInt;
        // 矿工
        float skipWorkDay = AttConstants.zeroFloat;
        // 迟到
        int lateNum = AttConstants.zeroInt;
        // 早退
        int earlyNum = AttConstants.zeroInt;
        // 迟到分钟数
        float lateTotalTime = AttConstants.zeroFloat;
        // 早退分钟数
        float earlyTotalTime = AttConstants.zeroFloat;

        for (AttClockInLedgerDetailsPO clockLedger : clockLedgerList) {
            if (ClockInEnum.NEGLECT_WORK.name().equals(clockLedger.getState())) { // 矿工
                if (clockLedger.getEndClockInTime() == null || clockLedger.getStartClockInTime() == null) {
                    skipWorkDay++;
                    continue;
                }
                boolean applyStartBool = false;
                boolean applyEndBool = false;
                for (AttAskLeaveFromPO attAskLeaveFromPO : askLeaveFromList) {
                    if (clockLedger.getStartClockInTime().isBefore(attAskLeaveFromPO.getLeaveStartTime()) ||
                        clockLedger.getStartClockInTime().isAfter(attAskLeaveFromPO.getLeaveEndTime())) {
                    } else {
                        applyStartBool = true;
                        if (applyEndBool) {
                            break;
                        }
                    }
                    if (clockLedger.getEndClockInTime().isBefore(attAskLeaveFromPO.getLeaveStartTime()) ||
                        clockLedger.getEndClockInTime().isAfter(attAskLeaveFromPO.getLeaveEndTime())) {
                    } else {
                        applyEndBool = true;
                        if (applyStartBool) {
                            break;
                        }
                    }

                }
                if (!applyStartBool || !applyEndBool) {
                    for (AttLeavePostFromPO attLeavePostFromPO : leavePostFromList) {
                        if (clockLedger.getStartClockInTime().isBefore(attLeavePostFromPO.getPostStartTime()) ||
                            clockLedger.getStartClockInTime().isAfter(attLeavePostFromPO.getPostEndTime())) {
                        } else {
                            applyStartBool = true;
                        }
                        if (clockLedger.getEndClockInTime().isBefore(attLeavePostFromPO.getPostStartTime()) ||
                            clockLedger.getEndClockInTime().isAfter(attLeavePostFromPO.getPostEndTime())) {
                        } else {
                            applyEndBool = true;
                        }
                        if (applyStartBool && applyEndBool) {
                            break;
                        }
                    }
                }
                if (!applyStartBool && !applyEndBool) {
                    skipWorkDay++;
                } else if (!applyStartBool || !applyEndBool) {
                    skipWorkDay += 0.5f;
                }
            } else {
                if (ClockInEnum.LACK_CHECKED.name().equals(clockLedger.getStartWorkState())) { // 上班缺卡
                    boolean applyStartBool = false;
                    for (AttAskLeaveFromPO attAskLeaveFromPO : askLeaveFromList) {
                        if (clockLedger.getStartClockInTime().isBefore(attAskLeaveFromPO.getLeaveStartTime()) ||
                            clockLedger.getStartClockInTime().isAfter(attAskLeaveFromPO.getLeaveEndTime())) {
                        } else {
                            applyStartBool = true;
                            break;
                        }
                    }
                    if (!applyStartBool) {
                        noClockNum++;
                        noClockStartNum++;
                    }
                } else if (ClockInEnum.LATE_CHECKED.name().equals(clockLedger.getStartWorkState())) {
                    // 迟到
                    lateNum++;
                    // 计算迟到分钟数
                    if (clockLedger.getStartClockInTime() != null && clockLedger.getActualStartClockInTime() != null) {
                        long minute = DateUtils.between(clockLedger.getStartClockInTime(), clockLedger.getActualStartClockInTime(), ChronoUnit.MINUTES, true);
                        lateTotalTime += minute;
                    }
                }

                if (ClockInEnum.LACK_CHECKED.name().equals(clockLedger.getEndWorkState())) { // 下班缺卡
                    boolean applyEndBool = false;
                    for (AttAskLeaveFromPO attAskLeaveFromPO : askLeaveFromList) {
                        if (clockLedger.getEndClockInTime().isBefore(attAskLeaveFromPO.getLeaveStartTime()) ||
                            clockLedger.getEndClockInTime().isAfter(attAskLeaveFromPO.getLeaveEndTime())) {
                        } else {
                            applyEndBool = true;
                            break;
                        }
                    }
                    if (!applyEndBool) {
                        noClockNum++;
                        noClockEndNum++;
                    }
                } else if (ClockInEnum.EARLY_CHECKED.name().equals(clockLedger.getEndWorkState())) {
                    // 早退
                    earlyNum++;
                    // 计算早退分钟数
                    if (clockLedger.getEndClockInTime() != null && clockLedger.getActualEndClockInTime() != null) {
                        String requireClockInTime = DateUtils.format(clockLedger.getEndClockInTime());
                        requireClockInTime = requireClockInTime.substring(0, requireClockInTime.length() - 2) + "59";
                        long minute = DateUtils.between(DateUtils.parse(requireClockInTime), clockLedger.getActualEndClockInTime(), ChronoUnit.MINUTES, true);
                        if (minute > AttConstants.zeroLong) {
                            earlyTotalTime += minute;
                        }
                    }
                }
            }
        }

        result.setNoClockNum(noClockNum);
        result.setNoClockStartNum(noClockStartNum);
        result.setNoClockEndNum(noClockEndNum);
        result.setSkipWorkDay(skipWorkDay);
        result.setLateNum(lateNum);
        result.setEarlyNum(earlyNum);
        result.setLateTotalTime(lateTotalTime);
        result.setEarlyTotalTime(earlyTotalTime);

        return result;
    }
}
