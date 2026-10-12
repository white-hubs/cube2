package com.guhecloud.cube.srv.gen.attendance.management.server.attend.beforestat.application.querygateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAwayBusinessFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttLeavePostFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAwayBusinessFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttLeavePostFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.beforestat.application.dto.AttNodeChangeClassDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.beforestat.application.dto.AttRepairSimpleInfoDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttApproveLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInLedgerDetailsMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInLedgerDetailsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttClassesTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttHolidayGrantWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.repository.AttHolidayRuleRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRuleListResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result.WorkOvertimeLedgerPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.WorkOvertimeLedgerAdvanceServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.application.dto.AttClockDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.application.querygateway.AttSummaryStatQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.param.AttSummaryStatPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result.AttSummaryStatPageDataResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result.AttSummaryStatPageLeaveResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result.AttSummaryStatPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttLegalHolidayUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.util.ExcelUtils;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
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
 * 预统计服务(汇总统计)
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttBeforeStatQueryService {
    private final AttGroupStaffMapper attGroupStaffMapper;
    private final AttSchedulingMapper attSchedulingMapper;
    private final AttHolidayRuleRepository attHolidayRuleRepository;
    private final AttAskLeaveFromMapper attAskLeaveFromMapper;
    private final AttLeavePostFromMapper attLeavePostFromMapper;
    private final AttApproveLogMapper attApproveLogMapper;
    private final AttAwayBusinessFromMapper attAwayBusinessFromMapper;
    private final WorkOvertimeLedgerAdvanceServer workOvertimeLedgerAdvanceServer;
    private final AttSummaryStatQueryService attSummaryStatQueryService;
    private final AttClockInLedgerDetailsMapper attClockInLedgerDetailsMapper;

    /**
     * 预统计分页列表
     *
     * @param param 条件对象
     *
     * @return 分页对象
     */
    public AttSummaryStatPageResult getBeforeStatPage(AttSummaryStatPageParam param) {
        // 判断入参的时间段
        if (param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null) {
            param.setPeriod(new Period(DateUtils.beginOfMonth(LocalDateTime.now()), DateUtils.parse(
                    DateUtils.toTimestamp(DateUtils.beginOfMonth(LocalDateTime.now())) - 1000)));
        }

        AttSummaryStatPageResult result = new AttSummaryStatPageResult();
        /* 获取人员 */
        Page<AttGroupStaffPO> page = attGroupStaffMapper.selectPage(PagingUtils.convert(param), Wrappers.<AttGroupStaffPO>lambdaQuery()
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
        result.setRecords(this.getPageDataList(param, page.getRecords(), holidayRulePOMap));
        return result;
    }

    /**
     * 预统计导出
     *
     * @param param    条件
     * @param request  请求
     * @param response 响应
     */
    public void beforeStatExport(AttSummaryStatPageParam param, HttpServletRequest request, HttpServletResponse response) {
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

        List<AttSummaryStatPageDataResult> dataList = this.getPageDataList(param, groupStaffPOList, holidayRulePOMap);

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
        List<Map<String, String>> excelDataList = attSummaryStatQueryService.getExcelData(dataList, holidayTitleList);

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
        String fileName = "beforeStat";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    private List<AttSummaryStatPageDataResult> getPageDataList(AttSummaryStatPageParam param, List<AttGroupStaffPO> groupStaffPOList,
                                                               Map<Long, AttHolidayRulePO> holidayRulePOMap
                                                              ) {
        List<AttSummaryStatPageDataResult> resultList = new ArrayList<>();
        Set<Long> staffIdSet = new HashSet<>();
        if (groupStaffPOList == null) {
            groupStaffPOList = new ArrayList<>();
        }
        for (AttGroupStaffPO groupStaffPO : groupStaffPOList) {
            staffIdSet.add(groupStaffPO.getStaffId());
        }
        if (staffIdSet.isEmpty()) {
            return resultList;
        }
        // 法定出勤天数
        int legalWorkDay = this.getLegalWorkDay(param.getPeriod());
        // 排班数据
        Map<Long, List<AttSchedulingPO>> staffSchedulingMap = this.getSchedulingMap(param, staffIdSet);
        /* 获取请假单信息↑ */
        Map<Long, List<AttAskLeaveFromPO>> askLeaveFromMap = new HashMap<>();
        List<AttAskLeaveFromPO> askLeaveFromPOList = attAskLeaveFromMapper.selectList(Wrappers.<AttAskLeaveFromPO>lambdaQuery()
                                                                                              .le(AttAskLeaveFromPO::getLeaveStartTime, param
                                                                                                      .getPeriod().getEnd())
                                                                                              .ge(AttAskLeaveFromPO::getLeaveEndTime, param
                                                                                                      .getPeriod().getBegin())
                                                                                              .and(i -> i
                                                                                                      .eq(AttAskLeaveFromPO::getStatus, FlowResult.PASS.name())
                                                                                                      .or()
                                                                                                      .eq(AttAskLeaveFromPO::getStatus, FlowResult.FINISHED.name()))
                                                                                              .in(AttAskLeaveFromPO::getApplicantId, staffIdSet)
                                                                                     );
        if (askLeaveFromPOList != null && !askLeaveFromPOList.isEmpty()) {
            askLeaveFromMap = askLeaveFromPOList.stream().collect(Collectors.groupingBy(AttAskLeaveFromPO::getApplicantId));
        }
        /* 获取离岗单 */
        Map<Long, List<AttLeavePostFromPO>> leavePostFromMap = new HashMap<>();
        List<AttLeavePostFromPO> leavePostFromPOList = attLeavePostFromMapper.selectList(Wrappers.<AttLeavePostFromPO>lambdaQuery()
                                                                                                 .le(AttLeavePostFromPO::getPostStartTime, param
                                                                                                         .getPeriod().getEnd())
                                                                                                 .ge(AttLeavePostFromPO::getPostEndTime, param
                                                                                                         .getPeriod().getBegin())
                                                                                                 .and(i -> i
                                                                                                         .eq(AttLeavePostFromPO::getStatus, FlowResult.FINISHED.name())
                                                                                                         .or()
                                                                                                         .eq(AttLeavePostFromPO::getStatus, FlowResult.PASS.name()))
                                                                                                 .in(AttLeavePostFromPO::getApplicantId, staffIdSet)
                                                                                        );
        if (leavePostFromPOList != null) {
            leavePostFromMap = leavePostFromPOList.stream().collect(Collectors.groupingBy(AttLeavePostFromPO::getApplicantId));
        }
        /* 获取出差单 */
        Map<Long, List<AttAwayBusinessFromPO>> awayBusinessFromMap = new HashMap<>();
        List<AttAwayBusinessFromPO> awayBusinessFromPOList = attAwayBusinessFromMapper.selectList(Wrappers
                                                                                                          .<AttAwayBusinessFromPO>lambdaQuery()
                                                                                                          .le(AttAwayBusinessFromPO::getAwayStartTime, param
                                                                                                                  .getPeriod().getEnd())
                                                                                                          .ge(AttAwayBusinessFromPO::getAwayEndTime, param
                                                                                                                  .getPeriod().getBegin())
                                                                                                          .and(i -> i
                                                                                                                  .eq(AttAwayBusinessFromPO::getStatus, FlowResult.FINISHED.name())
                                                                                                                  .or()
                                                                                                                  .eq(AttAwayBusinessFromPO::getStatus, FlowResult.PASS.name()))
                                                                                                          .in(AttAwayBusinessFromPO::getApplicantId, staffIdSet)
                                                                                                 );
        if (awayBusinessFromPOList != null) {
            awayBusinessFromMap = awayBusinessFromPOList.stream().collect(Collectors.groupingBy(AttAwayBusinessFromPO::getApplicantId));
        }
        /* 获取补卡信息 */
        Map<Long, List<AttRepairSimpleInfoDTO>> repairCardMap = new HashMap<>();
        List<AttRepairSimpleInfoDTO> repairCardList = attApproveLogMapper.getRepairSimpleInfo(Wrappers.<AttApproveLogPO>lambdaQuery()
                                                                                                      .le(AttApproveLogPO::getStartTime, param
                                                                                                              .getPeriod().getEnd())
                                                                                                      .ge(AttApproveLogPO::getStartTime, param
                                                                                                              .getPeriod().getBegin())
                                                                                                      .eq(AttApproveLogPO::getBusinessType, ClassesStatusTriggerEnum.REPAIR_CLOCK.name())
                                                                                                      .in(AttApproveLogPO::getStatus, FlowResult.PASS.name(), FlowResult.FINISHED.name())
                                                                                             );
        if (repairCardList != null && !repairCardList.isEmpty()) {
            repairCardMap = repairCardList.stream().collect(Collectors.groupingBy(AttRepairSimpleInfoDTO::getUserId));
        }
        /* 获取加班信息 */
        Map<Long, List<WorkOvertimeLedgerPageResult>> workOverTimeMap = new HashMap<>();
        List<WorkOvertimeLedgerPageResult> workOverTimeList = workOvertimeLedgerAdvanceServer.getWorkOvertimeLedgerByUserIds(staffIdSet, param.getPeriod());
        if (workOverTimeList != null && !workOverTimeList.isEmpty()) {
            workOverTimeMap = workOverTimeList.stream().filter(result -> result != null && result.getUserId() != null)
                    .collect(Collectors.groupingBy(WorkOvertimeLedgerPageResult::getUserId));
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

        Map<Long, List<AttAskLeaveFromPO>> finalAskLeaveFromMap = askLeaveFromMap;
        Map<Long, List<AttLeavePostFromPO>> finalLeavePostFromMap = leavePostFromMap;
        Map<Long, List<AttAwayBusinessFromPO>> finalAwayBusinessFromMap = awayBusinessFromMap;
        Map<Long, List<AttRepairSimpleInfoDTO>> finalRepairCardMap = repairCardMap;
        Map<Long, List<WorkOvertimeLedgerPageResult>> finalWorkOverTimeMap = workOverTimeMap;
        Map<Long, List<AttClockInLedgerDetailsPO>> finalClockLedgerMap = clockLedgerMap;
        resultList = groupStaffPOList.stream().map(po -> {
            Long staffId = po.getStaffId();
            AttSummaryStatPageDataResult data = new AttSummaryStatPageDataResult();
            data.setStaffId(staffId);
            data.setStaffName(po.getName());
            data.setDeptId(po.getDeptId());
            data.setDeptName(po.getDeptName());
            data.setLegalWorkDays((float) legalWorkDay);
            // 排班
            this.extArrange(data, staffId, staffSchedulingMap);
            /* 请假 */
            this.extLeaveList(data, staffId, param.getPeriod(), finalAskLeaveFromMap, holidayRulePOMap);
            /* 离岗 */
            this.extPostList(data, staffId, param.getPeriod(), finalLeavePostFromMap);
            /* 出差 */
            this.extAwayList(data, staffId, param.getPeriod(), finalAwayBusinessFromMap);
            /* 设置补卡数据 */
            List<AttRepairSimpleInfoDTO> staffRepairCardList = finalRepairCardMap.get(staffId);
            if (staffRepairCardList != null && !staffRepairCardList.isEmpty()) {
                data.setRepairClockNum(staffRepairCardList.size());
            } else {
                data.setRepairClockNum(AttConstants.zeroInt);
            }
            /* 设置加班数据 */
            List<WorkOvertimeLedgerPageResult> staffWorkOverTimeList = finalWorkOverTimeMap.get(staffId);
            WorkOvertimeLedgerPageResult staffWorkOverTime = new WorkOvertimeLedgerPageResult();
            if (staffWorkOverTimeList != null && !staffWorkOverTimeList.isEmpty()) {
                staffWorkOverTime = staffWorkOverTimeList.getFirst();
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
            /* 设置打卡数据 */
            AttClockDTO clockDTO = this.extClockData(finalClockLedgerMap.get(staffId), finalAskLeaveFromMap.get(staffId), finalLeavePostFromMap.get(staffId), finalAwayBusinessFromMap.get(staffId), finalRepairCardMap.get(staffId));
            data.setNoClockNum(clockDTO.getNoClockNum());
            data.setNoClockStartNum(clockDTO.getNoClockStartNum());
            data.setNoClockEndNum(clockDTO.getNoClockEndNum());
            data.setSkipWorkDay(clockDTO.getSkipWorkDay());
            data.setLateNum(clockDTO.getLateNum());
            data.setEarlyNum(clockDTO.getEarlyNum());
            data.setLateTotalTime(clockDTO.getLateTotalTime());
            data.setEarlyTotalTime(clockDTO.getEarlyTotalTime());
            /*
            List<AttSummaryStatPageDataResult> staffClockList = clockMap.get(staffId);
            AttSummaryStatPageDataResult staffClock = new AttSummaryStatPageDataResult();
            if(staffClockList!=null&&!staffClockList.isEmpty()){
                staffClock = staffClockList.get(AttConstants.zeroInt);
            }
            data.setNoClockStartNum(staffClock.getNoClockStartNum()==null?AttConstants.zeroInt:staffClock.getNoClockStartNum());
            data.setNoClockEndNum(staffClock.getNoClockEndNum()==null?AttConstants.zeroInt:staffClock.getNoClockEndNum());
            data.setNoClockNum(staffClock.getNoClockNum()==null?AttConstants.zeroInt:staffClock.getNoClockNum());
            data.setLateNum(staffClock.getLateNum()==null?AttConstants.zeroInt:staffClock.getLateNum());
            data.setEarlyNum(staffClock.getEarlyNum()==null?AttConstants.zeroInt:staffClock.getEarlyNum());
            data.setSkipWorkDay(staffClock.getSkipWorkDay()==null?AttConstants.zeroInt:staffClock.getSkipWorkDay());
            data.setLateTotalTime(staffClock.getLateTotalTime()==null?AttConstants.zeroInt:staffClock.getLateTotalTime());
            data.setEarlyTotalTime(staffClock.getEarlyTotalTime()==null?AttConstants.zeroInt:staffClock.getEarlyTotalTime());*/
            /* 设置实际出勤天数 */
            data.setRealWorkDays(Math.max(
                    data.getLegalWorkDays() - data.getSkipWorkDay() - data.getDeductionLeaveDay(), AttConstants.zeroFloat));
            return data;
        }).collect(Collectors.toList());

        return resultList;
    }

    // 法定出勤天数
    private int getLegalWorkDay(Period period) {
        /* 获取法定出勤天数 */
        int legalWorkDay = AttConstants.zeroInt;
        LocalDateTime printDateTime = DateUtils.beginOfMonth(period.getBegin());
        do {
            legalWorkDay += AttLegalHolidayUtils.getMonthLegalWorkDay(printDateTime.getYear(), printDateTime.getMonthValue())
                    .getWorkList().size();
            printDateTime = DateUtils.offsetMonth(printDateTime, AttConstants.oneInt);
        } while (period.getEnd().isAfter(printDateTime));
        return legalWorkDay;
    }

    // 排班数据
    private Map<Long, List<AttSchedulingPO>> getSchedulingMap(AttSummaryStatPageParam param, Set<Long> staffIdSet) {
        Map<Long, List<AttSchedulingPO>> staffSchedulingMap = new HashMap<>();
        List<AttSchedulingPO> schedulingPOList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                        .between(AttSchedulingPO::getClassesRealStartTime, param
                                                                                                .getPeriod().getBegin(), param.getPeriod()
                                                                                                         .getEnd())
                                                                                        .in(AttSchedulingPO::getStaffId, staffIdSet)
                                                                                        .orderByAsc(AttSchedulingPO::getSchedulingDate)
                                                                               );
        if (schedulingPOList == null || schedulingPOList.isEmpty()) {
            return staffSchedulingMap;
        }
        staffSchedulingMap = schedulingPOList.stream().collect(Collectors.groupingBy(AttSchedulingPO::getStaffId));
        // 进行中的换班
        List<AttNodeChangeClassDTO> changeClassDTOList = new ArrayList<>();
        List<AttApproveLogPO> changeClassLogList = attApproveLogMapper.selectList(Wrappers.<AttApproveLogPO>lambdaQuery()
                                                                                          .eq(AttApproveLogPO::getStatus, FlowResult.PASS.name())
                                                                                          .eq(AttApproveLogPO::getBusinessType, ClassesStatusTriggerEnum.CHANGE_CLASSES.name())
                                                                                          .orderByAsc(AttApproveLogPO::getCreateTime)
                                                                                 );
        if (changeClassLogList != null) {
            for (AttApproveLogPO approveLogPO : changeClassLogList) {
                AttNodeChangeClassDTO dto = new AttNodeChangeClassDTO();
                String proportion = approveLogPO.getProportion();
                if (StringUtils.isNotBlank(proportion)) {
                    dto = JsonUtils.deserialize(proportion, AttNodeChangeClassDTO.class);
                }
                changeClassDTOList.add(dto);
            }
        }

        for (AttNodeChangeClassDTO changeDto : changeClassDTOList) {
            if (changeDto.getFromId().equals(changeDto.getToId())) {
                List<AttSchedulingPO> staffSchedulingList = staffSchedulingMap.get(changeDto.getFromId());
                if (staffSchedulingList != null) {
                    for (AttSchedulingPO attSchedulingPO : staffSchedulingList) {
                        if (attSchedulingPO.getSchedulingDate() == null || changeDto.getFromDate() == null) {
                            continue;
                        }
                        long classId = attSchedulingPO.getClassesId() == null ? AttConstants.zeroLong : attSchedulingPO.getClassesId();
                        // 如果是要换日期的班，就设置成被换的班次类型
                        if (DateUtils.isSameDay(changeDto.getFromDate(), attSchedulingPO.getSchedulingDate()) &&
                            changeDto.getFromClassId().equals(classId)) {
                            attSchedulingPO.setClassesType(changeDto.getToClassType());
                        }
                        // 如果是被换日期的班，就设置成要换的班次类型
                        if (DateUtils.isSameDay(changeDto.getToDate(), attSchedulingPO.getSchedulingDate()) &&
                            changeDto.getToClassId().equals(classId)) {
                            attSchedulingPO.setClassesType(changeDto.getFromClassType());
                        }
                    }
                }
            } else {
                List<AttSchedulingPO> staffFromSchedulingList = staffSchedulingMap.get(changeDto.getFromId());
                if (staffFromSchedulingList != null) {
                    int loopSize = staffFromSchedulingList.size();
                    for (int i = AttConstants.zeroInt; i < loopSize; i++) {
                        AttSchedulingPO attSchedulingPO = staffFromSchedulingList.get(i);
                        if (attSchedulingPO.getSchedulingDate() == null || changeDto.getFromDate() == null) {
                            continue;
                        }
                        if (DateUtils.isSameDay(attSchedulingPO.getSchedulingDate(), changeDto.getFromDate())) {
                            attSchedulingPO.setClassesType("");
                        }
                        if (DateUtils.isSameDay(attSchedulingPO.getSchedulingDate(), changeDto.getToDate())) {
                            staffFromSchedulingList.add(AttSchedulingPO.builder().classesType(changeDto.getToClassType()).build());
                        }
                    }
                }
                List<AttSchedulingPO> staffToSchedulingList = staffSchedulingMap.get(changeDto.getToId());
                if (staffToSchedulingList != null && !staffToSchedulingList.isEmpty()) {
                    int loopSize = staffToSchedulingList.size();
                    for (int i = 0; i < loopSize; i++) {
                        AttSchedulingPO attSchedulingPO = staffToSchedulingList.get(i);
                        if (attSchedulingPO.getSchedulingDate() == null || changeDto.getFromDate() == null) {
                            continue;
                        }
                        if (DateUtils.isSameDay(attSchedulingPO.getSchedulingDate(), changeDto.getToDate())) {
                            attSchedulingPO.setClassesType("");
                        }
                        if (DateUtils.isSameDay(attSchedulingPO.getSchedulingDate(), changeDto.getFromDate())) {
                            staffToSchedulingList.add(AttSchedulingPO.builder().classesType(changeDto.getFromClassType()).build());
                        }
                    }
                }
            }

        }
        return staffSchedulingMap;
    }

    // 排班
    private void extArrange(AttSummaryStatPageDataResult data, Long staffId, Map<Long, List<AttSchedulingPO>> staffSchedulingMap) {
        List<AttSchedulingPO> staffSchedulingPOS = staffSchedulingMap.get(staffId);
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
    }

    // 请假
    private void extLeaveList(AttSummaryStatPageDataResult source, Long staffId, Period period,
                              Map<Long, List<AttAskLeaveFromPO>> askLeaveFromMap, Map<Long, AttHolidayRulePO> holidayRulePOMap
                             ) {
        List<AttSummaryStatPageLeaveResult> staffLeaveList = new ArrayList<>();
        List<AttAskLeaveFromPO> staffLeaveFromList = askLeaveFromMap.get(staffId);
        // 计入缺勤天数
        float deductionLeaveDay = AttConstants.zeroFloat;
        if (staffLeaveFromList != null) {
            Map<Long, List<AttAskLeaveFromPO>> holidayLeaveMap = staffLeaveFromList.stream()
                    .collect(Collectors.groupingBy(AttAskLeaveFromPO::getHolidayId));
            for (Map.Entry<Long, List<AttAskLeaveFromPO>> longListEntry : holidayLeaveMap.entrySet()) {
                Long holidayId = longListEntry.getKey();
                List<AttAskLeaveFromPO> leaveFromList = longListEntry.getValue();
                AttSummaryStatPageLeaveResult leaveResult = new AttSummaryStatPageLeaveResult();
                leaveResult.setStaffId(staffId);
                leaveResult.setHolidayRuleId(holidayId);
                // 规则
                AttHolidayRulePO rulePO = holidayRulePOMap.get(holidayId);
                String unit = AttConstants.DAYS;
                if (rulePO != null) {
                    leaveResult.setHolidayRuleName(rulePO.getName());
                    if (AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(rulePO.getGrantWay())) {
                        unit = AttConstants.HOUR;
                    }
                }
                leaveResult.setUnit(unit);
                // 请假数据
                float totalLeave = AttConstants.zeroFloat;
                for (AttAskLeaveFromPO leaveFromPO : leaveFromList) {
                    int dayOfHour;
                    if (leaveFromPO.getHolidayDayOfHour() != null) {
                        dayOfHour = leaveFromPO.getHolidayDayOfHour();
                    } else {
                        dayOfHour = AttConstants.dayOfHour;
                    }
                    Long applyNumber = leaveFromPO.getLeaveNumber();
                    boolean beforeBool = leaveFromPO.getLeaveStartTime().isBefore(period.getBegin());
                    boolean afterBool = leaveFromPO.getLeaveEndTime().isAfter(period.getEnd());
                    if (beforeBool || afterBool) {
                        // 相差天数
                        long betweenDay =
                                DateUtils.between(leaveFromPO.getLeaveStartTime(), leaveFromPO.getLeaveEndTime(), ChronoUnit.DAYS, true) +
                                AttConstants.oneLong;
                        // 每天的时间
                        long numberDay = applyNumber / betweenDay;
                        // 开始时间在范围外的天数
                        if (beforeBool) {
                            long startOut = DateUtils.between(leaveFromPO.getLeaveStartTime(), period.getBegin(), ChronoUnit.DAYS, true);
                            betweenDay -= Math.abs(startOut);
                        }
                        if (afterBool) {
                            long endOut = DateUtils.between(leaveFromPO.getLeaveEndTime(), period.getEnd(), ChronoUnit.DAYS, true);
                            betweenDay -= Math.abs(endOut);
                        }
                        applyNumber = numberDay * betweenDay;
                    }

                    String leaveString = AttHolidayDomain.longToUnit(applyNumber, unit, dayOfHour, 4);
                    float leaveNum = Float.parseFloat(leaveString);
                    totalLeave += leaveNum;
                    // 计入缺勤
                    if (Boolean.TRUE.equals(leaveFromPO.getIntoAbsence())) {
                        String dayString = AttHolidayDomain.longToUnit(applyNumber, AttConstants.DAYS, dayOfHour, 2);
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

    // 离岗数据
    private void extPostList(AttSummaryStatPageDataResult data, Long staffId, Period period,
                             Map<Long, List<AttLeavePostFromPO>> postFromMap
                            ) {
        List<AttLeavePostFromPO> staffPostFromPOList = postFromMap.get(staffId);
        if (staffPostFromPOList != null) {
            float totalPost = AttConstants.zeroFloat;
            for (AttLeavePostFromPO postFromPO : staffPostFromPOList) {
                Long applyNumber = postFromPO.getPostNumber();
                boolean beforeBool = postFromPO.getPostStartTime().isBefore(period.getBegin());
                boolean afterBool = postFromPO.getPostEndTime().isAfter(period.getEnd());
                if (beforeBool || afterBool) {
                    // 相差天数
                    long betweenDay =
                            DateUtils.between(postFromPO.getPostStartTime(), postFromPO.getPostEndTime(), ChronoUnit.DAYS, true) +
                            AttConstants.oneLong;
                    // 每天的时间
                    long numberDay = applyNumber / betweenDay;
                    // 开始时间在范围外的天数
                    if (beforeBool) {
                        long startOut = DateUtils.between(postFromPO.getPostStartTime(), period.getBegin(), ChronoUnit.DAYS, true);
                        betweenDay -= Math.abs(startOut);
                    }
                    if (afterBool) {
                        long endOut = DateUtils.between(postFromPO.getPostEndTime(), period.getEnd(), ChronoUnit.DAYS, true);
                        betweenDay -= Math.abs(endOut);
                    }
                    applyNumber = numberDay * betweenDay;
                }
                String dayString = AttHolidayDomain.longToUnit(applyNumber, AttConstants.DAYS, AttConstants.dayOfHour, 4);
                float num = Float.parseFloat(dayString);
                totalPost += num;
            }
            data.setLeavePostDuration(Float.parseFloat(String.format("%.1f", totalPost)));
        }
    }

    // 出差
    private void extAwayList(AttSummaryStatPageDataResult data, Long staffId, Period period,
                             Map<Long, List<AttAwayBusinessFromPO>> awayFromMap
                            ) {
        List<AttAwayBusinessFromPO> awayFromPOList = awayFromMap.get(staffId);
        if (awayFromPOList != null) {
            float totalAway = AttConstants.zeroFloat;
            for (AttAwayBusinessFromPO awayFromPO : awayFromPOList) {
                Long applyNumber = awayFromPO.getAwayNumber();
                boolean beforeBool = awayFromPO.getAwayStartTime().isBefore(period.getBegin());
                boolean afterBool = awayFromPO.getAwayEndTime().isAfter(period.getEnd());
                if (beforeBool || afterBool) {
                    // 相差天数
                    long betweenDay =
                            DateUtils.between(awayFromPO.getAwayStartTime(), awayFromPO.getAwayEndTime(), ChronoUnit.DAYS, true) +
                            AttConstants.oneLong;
                    // 每天的时间
                    long numberDay = applyNumber / betweenDay;
                    // 开始时间在范围外的天数
                    if (beforeBool) {
                        long startOut = DateUtils.between(awayFromPO.getAwayStartTime(), period.getBegin(), ChronoUnit.DAYS, true);
                        betweenDay -= Math.abs(startOut);
                    }
                    if (afterBool) {
                        long endOut = DateUtils.between(awayFromPO.getAwayEndTime(), period.getEnd(), ChronoUnit.DAYS, true);
                        betweenDay -= Math.abs(endOut);
                    }
                    applyNumber = numberDay * betweenDay;
                }
                String dayString = AttHolidayDomain.longToUnit(applyNumber, AttConstants.DAYS, AttConstants.dayOfHour, 4);
                float num = Float.parseFloat(dayString);
                totalAway += num;
            }
            data.setAwayBusinessDuration(Float.parseFloat(String.format("%.1f", totalAway)));
        }
    }

    private AttClockDTO extClockData(List<AttClockInLedgerDetailsPO> clockLedgerList, List<AttAskLeaveFromPO> askLeaveFromList,
                                     List<AttLeavePostFromPO> leavePostFromList, List<AttAwayBusinessFromPO> awawFromList,
                                     List<AttRepairSimpleInfoDTO> repairLogList
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
        if (awawFromList == null) {
            awawFromList = new ArrayList<>();
        }
        if (repairLogList == null) {
            repairLogList = new ArrayList<>();
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
            // 补卡
            boolean repairStartBool = false;
            boolean repairEndBool = false;
            for (AttRepairSimpleInfoDTO repairDTO : repairLogList) {
                if (repairDTO.getRepairTime() == null || clockLedger.getStartClockInTime() == null ||
                    clockLedger.getEndClockInTime() == null) {
                    continue;
                }
                if (clockLedger.getStartClockInTime().isEqual(repairDTO.getRepairTime())) {
                    repairStartBool = true;
                }
                if (clockLedger.getEndClockInTime().isEqual(repairDTO.getRepairTime())) {
                    repairEndBool = true;
                }
            }
            if (ClockInEnum.NEGLECT_WORK.name().equals(clockLedger.getState())) { // 矿工
                if (clockLedger.getEndClockInTime() == null || clockLedger.getStartClockInTime() == null) {
                    break;
                }

                if (repairStartBool && !repairEndBool) {
                    noClockNum++;
                    noClockEndNum++;
                }
                if (!repairStartBool && repairEndBool) {
                    noClockNum++;
                    noClockStartNum++;
                }

                if (repairStartBool || repairEndBool) {
                    break;
                }

                /* 请假离岗出差校验 */
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
                if (!applyStartBool || !applyEndBool) {
                    for (AttAwayBusinessFromPO awayBusinessFromPO : awawFromList) {
                        if (clockLedger.getStartClockInTime().isBefore(awayBusinessFromPO.getAwayStartTime()) ||
                            clockLedger.getStartClockInTime().isAfter(awayBusinessFromPO.getAwayEndTime())) {
                        } else {
                            applyStartBool = true;
                        }
                        if (clockLedger.getEndClockInTime().isBefore(awayBusinessFromPO.getAwayStartTime()) ||
                            clockLedger.getEndClockInTime().isAfter(awayBusinessFromPO.getAwayEndTime())) {
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
                if (ClockInEnum.LACK_CHECKED.name().equals(clockLedger.getStartWorkState()) && !repairStartBool) { // 上班缺卡
                    boolean applyStartBool = false;
                    // 请假
                    for (AttAskLeaveFromPO attAskLeaveFromPO : askLeaveFromList) {
                        if (clockLedger.getStartClockInTime().isBefore(attAskLeaveFromPO.getLeaveStartTime()) ||
                            clockLedger.getStartClockInTime().isAfter(attAskLeaveFromPO.getLeaveEndTime())) {
                        } else {
                            applyStartBool = true;
                            break;
                        }
                    }
                    if (applyStartBool) {
                        break;
                    }
                    // 离岗
                    for (AttLeavePostFromPO attLeavePostFromPO : leavePostFromList) {
                        if (clockLedger.getStartClockInTime().isBefore(attLeavePostFromPO.getPostStartTime()) ||
                            clockLedger.getStartClockInTime().isAfter(attLeavePostFromPO.getPostEndTime())) {
                        } else {
                            applyStartBool = true;
                            break;
                        }
                    }
                    if (applyStartBool) {
                        break;
                    }
                    // 出差
                    for (AttAwayBusinessFromPO awayBusinessFromPO : awawFromList) {
                        if (clockLedger.getStartClockInTime().isBefore(awayBusinessFromPO.getAwayStartTime()) ||
                            clockLedger.getStartClockInTime().isAfter(awayBusinessFromPO.getAwayEndTime())) {
                        } else {
                            applyStartBool = true;
                            break;
                        }
                    }
                    if (applyStartBool) {
                        break;
                    }

                    noClockNum++;
                    noClockStartNum++;
                } else if (ClockInEnum.LATE_CHECKED.name().equals(clockLedger.getStartWorkState()) && !repairStartBool) { // 迟到
                    lateNum++;
                    // 计算迟到分钟数
                    if (clockLedger.getStartClockInTime() != null && clockLedger.getActualStartClockInTime() != null) {
                        long minute = DateUtils.between(clockLedger.getStartClockInTime(), clockLedger.getActualStartClockInTime(), ChronoUnit.MINUTES, true);
                        lateTotalTime += minute;
                    }
                }

                if (ClockInEnum.LACK_CHECKED.name().equals(clockLedger.getEndWorkState()) && !repairEndBool) { // 下班缺卡
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
                } else if (ClockInEnum.EARLY_CHECKED.name().equals(clockLedger.getEndWorkState()) && !repairEndBool) { // 早退
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
