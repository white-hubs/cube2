package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInLedgerDetailsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInLedgerRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInRecordsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInLedgerDetailsMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInLedgerDetailsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockInLedgerPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInLedgerDetailsExcelResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInLedgerDetailsResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInLedgerResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.KeyValueResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.util.AttExcelUtile;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesRecordMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-26  13:59
 * @Description: 考勤台账
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClockInLedgerApplication {
    private final AttClockInLedgerRepository clockInLedgerRepository;
    private final UserClient userClient;
    private final AttGroupStaffMapper attGroupStaffMapper;
    private final AttGroupMapper attGroupMapper;
    private final AttClockInLedgerMapper clockInLedgerMapper;
    private final AttClockInLedgerDetailsRepository clockInLedgerDetailsRepository;
    private final AttClockInLedgerDetailsMapper clockInLedgerDetailsMapper;
    private final AttClockInRecordsRepository clockInRecordsRepository;
    private final OrganizationClient organizationClient;
    private final AttApproveLogRepository approveLogRepository;
    private final AttClassesRecordMapper attClassesRecordMapper;

    public Pagination<AttClockInLedgerResult> getClockInLedger(AttClockInLedgerPageParam param) {
        return this.getClockInLedger(param, Boolean.FALSE);
    }

    public Pagination<AttClockInLedgerResult> getClockInLedger(AttClockInLedgerPageParam param, Boolean tags) {
        Map<Long, String> orgMap = new HashMap<>();
        Pagination<AttClockInLedgerResult> pagination = clockInLedgerRepository.getClockInLedger(param);
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        pagination.getRecords().forEach(ex -> {
            SessionContextHolder.requestTenant(tenant);
            if (ObjectUtils.isNotEmpty(ex.getOrgId())) {
                String orgName = orgMap.get(ex.getOrgId());
                if (ObjectUtils.isEmpty(orgName)) {
                    OrganizationDTO organization = organizationClient.getOrganizationsById(ex.getOrgId());
                    organization = Optional.ofNullable(organization).orElse(new OrganizationDTO());
                    orgMap.put(ex.getOrgId(), organization.getName());
                    orgName = organization.getName();
                }
                ex.setOrgName(Optional.ofNullable(orgName).orElse(""));
            }
            ex.setDetails(tags ? this.getLedgerDetailsDisplayTags(ex.getId()) : this.getLedgerDetails(ex.getId()));
        });
        return pagination;
    }

    public void getClockInLogExcel(HttpServletRequest request, HttpServletResponse response, AttClockInLedgerPageParam param) {
        List<AttClockInLedgerDetailsExcelResult> data = clockInLedgerMapper.getClockInLogExcel(param);
        Assert.isParameterError(CollectionUtils.isEmpty(data), "暂无数据，无需导出");
        Map<Long, OrganizationDTO> organizationDTOMap = new HashMap<>();
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 每月最多31天
        writer.writeCellValue(0, 0, "姓名");
        writer.writeCellValue(1, 0, "部门");
        writer.writeCellValue(2, 0, "考勤组");
        writer.writeCellValue(3, 0, "打卡日期");
        writer.writeCellValue(4, 0, "上班时间");
        writer.writeCellValue(5, 0, "上班打卡时间");
        writer.writeCellValue(6, 0, "下班时间");
        writer.writeCellValue(7, 0, "下班打卡时间");
        for (int y = 0; y < data.size(); y++) {
            AttClockInLedgerDetailsExcelResult excelResult = data.get(y);
            OrganizationDTO organization = new OrganizationDTO();
            if (ObjectUtils.isNotEmpty(excelResult.getOrgId())) {
                organization = organizationDTOMap.get(excelResult.getOrgId());
                if (ObjectUtils.isEmpty(organization)) {
                    organization = organizationClient.getOrganizationsById(excelResult.getOrgId());
                }
                if (ObjectUtils.isNotEmpty(organization)) {
                    organizationDTOMap.put(excelResult.getOrgId(), organization);
                }
            }
            writer.writeCellValue(0, y + 1, excelResult.getUserName());
            writer.writeCellValue(1, y + 1, organization.getName());
            writer.writeCellValue(2, y + 1, excelResult.getAttGroupName());
            writer.writeCellValue(3, y + 1, DateUtils.formatDate(excelResult.getLedgerBelongDate()));
            String startClockInTime = "";
            if (ObjectUtils.isNotEmpty(excelResult.getStartClockInTime())) {
                startClockInTime = DateUtils.format(excelResult.getStartClockInTime(), "HH:mm");
            }
            writer.writeCellValue(4, y + 1, startClockInTime);
            writer.writeCellValue(5, y + 1, excelResult.getStartWorkTime());
            String endClockInTime = "";
            if (ObjectUtils.isNotEmpty(excelResult.getEndClockInTime())) {
                endClockInTime = DateUtils.format(excelResult.getEndClockInTime(), "HH:mm");
            }
            writer.writeCellValue(6, y + 1, endClockInTime);
            writer.writeCellValue(7, y + 1, excelResult.getEndWorkTime());
        }
        StyleSet ss = writer.getStyleSet();
        ss.setWrapText();
        writer.setStyleSet(ss);
        writer.setOnlyAlias(true);
        writer.setColumnWidth(-1, 25);
        String fileName = "打卡台账";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    public void getLedgerDetailsExcel(HttpServletRequest request, HttpServletResponse response, AttClockInLedgerPageParam param) {
        List<AttClockInLedgerDetailsExcelResult> data = clockInLedgerMapper.getLedgerDetailsExcel(param, DateUtils.formatDate(
                LocalDateTime.now()));
        Assert.isParameterError(CollectionUtils.isEmpty(data), "暂无数据，无需导出");
        Map<Long, OrganizationDTO> organizationDTOMap = new HashMap<>();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        data.parallelStream().forEach(ex -> {
            SessionContextHolder.requestTenant(tenant);
            OrganizationDTO organization = new OrganizationDTO();
            if (ObjectUtils.isNotEmpty(ex.getOrgId())) {
                organization = organizationDTOMap.get(ex.getOrgId());
                if (ObjectUtils.isEmpty(organization)) {
                    organization = organizationClient.getOrganizationsById(ex.getOrgId());
                }
                if (ObjectUtils.isNotEmpty(organization)) {
                    organizationDTOMap.put(ex.getOrgId(), organization);
                }
            }
            ex.setOrgName(organization.getName());
            ex.setStartClockInDate(DateUtils.formatDate(
                    ObjectUtils.isNotEmpty(ex.getStartClockInTime()) ? ex.getStartClockInTime() : ex.getLedgerBelongDate()));
            ex.setEndClockInDate(DateUtils.formatDate(ex.getEndClockInTime()));
            StringBuffer buffer = new StringBuffer();
            if (ClockInEnum.REST.name().equals(ex.getState()) || ClockInEnum.NORMAL.name().equals(ex.getState())) {
                buffer.append(ClockInEnum.getRemarksByName(ex.getState()));
            } else if (ClockInEnum.LATE_CHECKED.name().equals(ex.getStartWorkState())) {
                if (ObjectUtils.isNotEmpty(buffer)) {
                    buffer.append(",");
                }
                if (ObjectUtils.isNotEmpty(ex.getStartWorkOut()) && ex.getStartWorkOut()) {
                    buffer.append("迟到外勤");
                } else {
                    buffer.append(ClockInEnum.LATE_CHECKED.getRemarks());
                }
            } else if (ClockInEnum.LACK_CHECKED.name().equals(ex.getStartWorkState())) {
                List<AttApproveLogPO> approveLogPOList = approveLogRepository.getApproveLog(ex.getUserId(),
                                                                                            ex.getStartClockInTime());
                if (ObjectUtils.isEmpty(approveLogPOList)) {
                    if (ObjectUtils.isNotEmpty(buffer)) {
                        buffer.append(",");
                    }
                    buffer.append("上班缺卡");
                } else {
                    for (AttApproveLogPO approveLog : approveLogPOList) {
                        if (ObjectUtils.isNotEmpty(buffer)) {
                            buffer.append(",");
                        }
                        buffer.append(approveLog.getSceneName());
                    }
                }
            } else if (ObjectUtils.isNotEmpty(ex.getStartWorkOmit()) && ex.getStartWorkOmit()) {
                if (ObjectUtils.isNotEmpty(buffer)) {
                    buffer.append(",");
                }
                buffer.append("上班补卡");
            } else if (ObjectUtils.isNotEmpty(ex.getStartWorkOut()) && ex.getStartWorkOut()) {
                if (ObjectUtils.isNotEmpty(buffer)) {
                    buffer.append(",");
                }
                buffer.append("上班外勤");
            }
            if (ClockInEnum.EARLY_CHECKED.name().equals(ex.getEndWorkState())) {
                if (ObjectUtils.isNotEmpty(buffer)) {
                    buffer.append(",");
                }
                if (ObjectUtils.isNotEmpty(ex.getEndWorkOut()) && ex.getEndWorkOut()) {
                    buffer.append("早退外勤");
                } else {
                    buffer.append(ClockInEnum.EARLY_CHECKED.getRemarks());
                }
            } else if (ClockInEnum.LACK_CHECKED.name().equals(ex.getEndWorkState())) {
                List<AttApproveLogPO> approveLogPOList = approveLogRepository.getApproveLog(ex.getUserId(),
                                                                                            ex.getEndClockInTime());
                if (ObjectUtils.isEmpty(approveLogPOList)) {
                    if (ObjectUtils.isNotEmpty(buffer)) {
                        buffer.append(",");
                    }
                    buffer.append("下班缺卡");
                } else {
                    for (AttApproveLogPO approveLog : approveLogPOList) {
                        if (ObjectUtils.isNotEmpty(buffer)) {
                            buffer.append(",");
                        }
                        buffer.append(approveLog.getSceneName());
                    }
                }
            } else if (ObjectUtils.isNotEmpty(ex.getEndWorkOmit()) && ex.getEndWorkOmit()) {
                if (ObjectUtils.isNotEmpty(buffer)) {
                    buffer.append(",");
                }
                buffer.append("下班补卡");
            } else if (ObjectUtils.isNotEmpty(ex.getEndWorkOut()) && ex.getEndWorkOut()) {
                if (ObjectUtils.isNotEmpty(buffer)) {
                    buffer.append(",");
                }
                buffer.append("下班外勤");
            }
            ex.setResult(
                    "上班缺卡,下班缺卡".contentEquals(buffer) ? ClockInEnum.NEGLECT_WORK.getRemarks() : buffer.toString());
        });
        String title = "考勤每日统计";
        Map<String, List<?>> map = new HashMap<>();
        map.put(title, data);
        try {
            AttExcelUtile.uploadExcelList(response, map, "考勤每日统计.xlsx");
        } catch (IOException e) {
            log.error("考勤每日统计导出异常：", e);
        }

    }

    /**
     * 生成台账数据
     */
    public void addClockInLedger(Integer offsetMonth) {
        this.addClockInLedger(offsetMonth, null);
    }

    public void addClockInLedger(List<AttGroupStaffParam> staffParams) {
        this.addClockInLedger(0, ObjectUtils.copying(staffParams, AttGroupStaffPO.class));
    }

    public synchronized void addClockInLedger(Integer offsetMonth, List<AttGroupStaffPO> staffParams) {
        String month = DateUtils.format(DateUtils.offsetMonth(LocalDateTime.now(), offsetMonth), "yyyy-MM");
        log.info("att:开始生成{}月的台账,偏移量{}", month, offsetMonth);
        List<AttGroupStaffPO> staffList = ObjectUtils.isNotEmpty(staffParams)
                                          ? staffParams
                                          : attGroupStaffMapper.selectList(new LambdaQueryWrapper<AttGroupStaffPO>());
        for (AttGroupStaffPO staff : staffList) {
            try {
                SessionContextHolder.requestTenant(staff.getTenant());
                AttClockInLedgerPO clockInLedger = clockInLedgerMapper.selectOne(new LambdaQueryWrapper<AttClockInLedgerPO>()
                                                                                         .eq(AttClockInLedgerPO::getUserId,
                                                                                             staff.getStaffId())
                                                                                         .eq(AttClockInLedgerPO::getLedgerDate, month)
                                                                                         .last("limit 1")
                                                                                );
                if (ObjectUtils.isEmpty(clockInLedger)) {
                    log.info("att:开始生成{}{}{}月的台账", staff.getStaffId(), staff.getName(), month);
                    AttClockInLedgerPO clock = clockInLedgerMapper.selectOne(new LambdaQueryWrapper<AttClockInLedgerPO>()
                                                                                     .eq(AttClockInLedgerPO::getUserId,
                                                                                         staff.getStaffId())
                                                                                     .eq(AttClockInLedgerPO::getLedgerDate,
                                                                                         month));
                    if (ObjectUtils.isEmpty(clock)) {
                        AttClockInLedgerPO attClockInLedger = new AttClockInLedgerPO();
                        attClockInLedger.setUserId(staff.getStaffId());
                        attClockInLedger.setUserName(staff.getName());
                        attClockInLedger.setLedgerDate(month);
                        UserDTO user = userClient.getUser(staff.getStaffId());
                        attClockInLedger.setOrgId(
                                Optional.ofNullable(user.getOrganization()).orElse(new UserDTO.Organization()).getId());
                        Boolean bool = clockInLedgerMapper.insert(attClockInLedger) > 0;
                        log.info("att:生成{}{}{}月的台账结果{}", staff.getStaffId(), staff.getName(), month, bool);
                        if (bool) {
                            clockInLedgerDetailsRepository.addLedgerDetails(attClockInLedger, month);
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("att:开始生成{}{}{}月的台账异常", staff.getStaffId(), staff.getName(), month, e);
            }
        }
        log.info("att:生成{}月的考勤结束", month);
    }

    public void updateLedgerDetails(AttClockInRecordsPO attClockInRecords) {
        AttClockInLedgerDetailsPO details = this.addLedger(attClockInRecords);
        if (ObjectUtils.isNotEmpty(details)) {
            if (ObjectUtils.isEmpty(details.getClassesId())) {
                // 绑定台账的班次
                details.setClassesId(attClockInRecords.getClassesId());
                details.setClassesVersion(attClockInRecords.getClassesVersion());
                AttClassesRecordPO classesRecord = attClassesRecordMapper.selectOne(new LambdaQueryWrapper<AttClassesRecordPO>()
                                                                                            .eq(AttClassesRecordPO::getClassesId,
                                                                                                attClockInRecords.getClassesId())
                                                                                            .eq(ObjectUtils.isNotEmpty(
                                                                                                        attClockInRecords.getClassesVersion()),
                                                                                                AttClassesRecordPO::getVersion,
                                                                                                attClockInRecords.getClassesVersion())
                                                                                            .orderByDesc(
                                                                                                    AttClassesRecordPO::getVersion)
                                                                                            .last("limit 1"));
                if (ObjectUtils.isNotEmpty(classesRecord)) {
                    details.setClassesDetails(classesRecord.getNickName() + "(" + classesRecord.getStartWorkTime() + "-" +
                                              classesRecord.getEndWorkTime() + ")");
                }
                clockInLedgerDetailsMapper.updateById(details);
            }
            if (ClockInEnum.START.name().equals(attClockInRecords.getClockType())) {
                details.setStartWorkTime(ObjectUtils.isNotEmpty(attClockInRecords.getActualClockInTime()) ?
                                         DateUtils.format(attClockInRecords.getActualClockInTime(), "HH:mm") : null);
                details.setActualStartClockInTime(
                        ObjectUtils.isNotEmpty(attClockInRecords.getActualClockInTime()) ? attClockInRecords.getActualClockInTime()
                                                                                         : null);
                details.setStartWorkOmit(attClockInRecords.getWorkOmit());
                String startWorkDetails = ClockInEnum.LATE_CHECKED.name().equals(attClockInRecords.getState())
                                          ? "迟到" + attClockInRecords.getDeletionDuration() + "分钟"
                                          : ClockInEnum.LACK_CHECKED.name().equals(attClockInRecords.getState())
                                            ? "上班缺卡" : null;
                details.setStartWorkDetails(startWorkDetails);
                details.setStartWorkState(attClockInRecords.getState());
                details.setStartWorkOut(attClockInRecords.getOutClock());
                details.setStartClockInTime(attClockInRecords.getRequireClockInTime());
                AttClockInRecordsPO endRecords = clockInRecordsRepository.getOne(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                                         .eq(AttClockInRecordsPO::getUuid,
                                                                                             attClockInRecords.getUuid())
                                                                                         .eq(AttClockInRecordsPO::getClockType,
                                                                                             ClockInEnum.END.name()));
                if (ObjectUtils.isNotEmpty(endRecords)) {
                    details.setEndClockInTime(endRecords.getRequireClockInTime());
                }
            } else {
                if (ObjectUtils.isEmpty(details.getStartWorkState())) {
                    AttClockInRecordsPO startRecords = clockInRecordsRepository.getOne(
                            new LambdaQueryWrapper<AttClockInRecordsPO>()
                                    .eq(AttClockInRecordsPO::getUuid, attClockInRecords.getUuid())
                                    .eq(AttClockInRecordsPO::getClockType, ClockInEnum.START.name()));
                    details.setStartWorkState(startRecords.getState());
                    details.setStartWorkOmit(Boolean.FALSE);
                    details.setStartWorkTime(ObjectUtils.isNotEmpty(startRecords.getActualClockInTime()) ?
                                             DateUtils.format(startRecords.getActualClockInTime(), "HH:mm") : null);
                    details.setActualStartClockInTime(ObjectUtils.isNotEmpty(attClockInRecords.getActualClockInTime())
                                                      ? attClockInRecords.getActualClockInTime() : null);
                    String startWorkDetails = ClockInEnum.LATE_CHECKED.name().equals(startRecords.getState())
                                              ? "迟到" + startRecords.getDeletionDuration() + "分钟"
                                              : ClockInEnum.LACK_CHECKED.name().equals(startRecords.getState())
                                                ? "上班缺卡" : null;
                    details.setStartWorkDetails(startWorkDetails);
                    details.setStartWorkOut(startRecords.getOutClock());
                    details.setStartClockInTime(attClockInRecords.getRequireClockInTime());
                }
                String endWorkDetails = ClockInEnum.EARLY_CHECKED.name().equals(attClockInRecords.getState())
                                        ? "早退" + attClockInRecords.getDeletionDuration() + "分钟"
                                        : ClockInEnum.LACK_CHECKED.name().equals(attClockInRecords.getState())
                                          ? "下班缺卡" : null;
                details.setEndWorkDetails(endWorkDetails);
                details.setEndWorkState(attClockInRecords.getState());
                details.setEndWorkTime(ObjectUtils.isNotEmpty(attClockInRecords.getActualClockInTime()) ?
                                       DateUtils.format(attClockInRecords.getActualClockInTime(), "HH:mm") : null);
                details.setActualEndClockInTime(
                        ObjectUtils.isNotEmpty(attClockInRecords.getActualClockInTime()) ? attClockInRecords.getActualClockInTime()
                                                                                         : null);
                details.setEndWorkOmit(attClockInRecords.getWorkOmit());
                details.setEndWorkOut(attClockInRecords.getOutClock());
            }
            // 上班缺卡+下班缺卡=旷工
            // 上班正常+下班正常=正常
            // 上班正常+下班未打卡=正常
            // 其他情况都为异常
            if (ClockInEnum.LACK_CHECKED.name().equals(details.getStartWorkState()) &&
                ClockInEnum.LACK_CHECKED.name().equals(details.getEndWorkState())) {
                details.setState(ClockInEnum.NEGLECT_WORK.name());
            } else if (ClockInEnum.NORMAL.name().equals(details.getStartWorkState()) &&
                       ClockInEnum.NORMAL.name().equals(details.getEndWorkState())) {
                details.setState(ClockInEnum.NORMAL.name());
            } else if (ClockInEnum.NORMAL.name().equals(details.getStartWorkState()) &&
                       ObjectUtils.isEmpty(details.getEndWorkState())) {
                details.setState(ClockInEnum.NORMAL.name());
            } else {
                details.setState(ClockInEnum.ABNORMAL_WORK.name());
            }
            clockInLedgerDetailsMapper.updateById(details);
        }
    }

    private List<AttClockInLedgerDetailsResult> getLedgerDetails(Long ledgerId) {
        List<AttClockInLedgerDetailsResult> resultList = clockInLedgerRepository.getClockInLedgerDetails(ledgerId);
        resultList.forEach(obj -> {
            if (ObjectUtils.isEmpty(obj.getDetails()) || ObjectUtils.isEmpty(obj.getDetails().getFirst().getState())) {
                // 置空后前端不需要展示
                obj.setDetails(null);
            } else {
                obj.getDetails().forEach(ex -> {
                    List<KeyValueResult> keyValueResultList = new ArrayList<>();
                    if (ClockInEnum.LATE_CHECKED.name().equals(ex.getStartWorkState()) ||
                        ClockInEnum.LACK_CHECKED.name().equals(ex.getStartWorkState())) {
                        KeyValueResult keyValueResult = new KeyValueResult(ex.getState(), ex.getStartWorkDetails());
                        keyValueResultList.add(keyValueResult);
                    }
                    if (ClockInEnum.EARLY_CHECKED.name().equals(ex.getEndWorkState()) ||
                        ClockInEnum.LACK_CHECKED.name().equals(ex.getEndWorkState())) {
                        KeyValueResult keyValueResult = new KeyValueResult(ex.getState(), ex.getEndWorkDetails());
                        keyValueResultList.add(keyValueResult);
                    }
                    ex.setStateDetails(keyValueResultList);
                });
            }
        });
        return resultList;
    }

    private List<AttClockInLedgerDetailsResult> getLedgerDetailsDisplayTags(Long ledgerId) {
        List<AttClockInLedgerDetailsResult> resultList = clockInLedgerRepository.getClockInLedgerDetails(ledgerId);
        resultList.forEach(obj -> {
            if (ObjectUtils.isEmpty(obj.getDetails()) || ObjectUtils.isEmpty(obj.getDetails().getFirst().getState())) {
                // 置空后前端不需要展示
                obj.setDetails(null);
            } else {
                obj.getDetails().forEach(ex -> {
                    List<KeyValueResult> keyValueResultList = new ArrayList<>();
                    if (ClockInEnum.LATE_CHECKED.name().equals(ex.getStartWorkState())) {
                        KeyValueResult keyValueResult = new KeyValueResult(ex.getStartWorkState(), ex.getStartWorkDetails());
                        if (ObjectUtils.isNotEmpty(ex.getStartWorkOut()) && ex.getStartWorkOut()) {
                            keyValueResult.setValue(ex.getStartWorkDetails() + "外勤");
                        }
                        keyValueResultList.add(keyValueResult);
                    } else if (ClockInEnum.LACK_CHECKED.name().equals(ex.getStartWorkState())) {
                        List<AttApproveLogPO> approveLogPOList = approveLogRepository.getApproveLog(ex.getUserId(),
                                                                                                    ex.getStartClockInTime());
                        if (ObjectUtils.isEmpty(approveLogPOList)) {
                            KeyValueResult keyValueResult = new KeyValueResult(ClockInEnum.LACK_CHECKED.name(), "上班缺卡");
                            keyValueResultList.add(keyValueResult);
                        } else {
                            for (AttApproveLogPO approveLog : approveLogPOList) {
                                KeyValueResult keyValueResult = new KeyValueResult(approveLog.getBusinessType(),
                                                                                   approveLog.getSceneName());
                                keyValueResultList.add(keyValueResult);
                                ex.setState(ClockInEnum.ABNORMAL_WORK.name());
                            }
                        }
                    } else if (ObjectUtils.isNotEmpty(ex.getStartWorkOmit()) && ex.getStartWorkOmit()) {
                        ex.setState(ClassesStatusTriggerEnum.REPAIR_CLOCK.name());
                        KeyValueResult keyValueResult = new KeyValueResult(ex.getStartWorkState(), "上班补卡");
                        keyValueResultList.add(keyValueResult);
                    } else if (ObjectUtils.isNotEmpty(ex.getStartWorkOut()) && ex.getStartWorkOut()) {
                        ex.setState(ClockInEnum.WORK_OUT.name());
                        KeyValueResult keyValueResult = new KeyValueResult(ex.getStartWorkState(), "上班外勤");
                        keyValueResultList.add(keyValueResult);
                    }
                    if (ClockInEnum.EARLY_CHECKED.name().equals(ex.getEndWorkState())) {
                        KeyValueResult keyValueResult = new KeyValueResult(ex.getEndWorkState(), ex.getEndWorkDetails());
                        if (ObjectUtils.isNotEmpty(ex.getEndWorkOut()) && ex.getEndWorkOut()) {
                            keyValueResult.setValue(ex.getEndWorkDetails() + "外勤");
                        }
                        keyValueResultList.add(keyValueResult);
                        ex.setState(ClockInEnum.ABNORMAL_WORK.name());
                    } else if (ClockInEnum.LACK_CHECKED.name().equals(ex.getEndWorkState())) {
                        List<AttApproveLogPO> approveLogPOList = approveLogRepository.getApproveLog(ex.getUserId(),
                                                                                                    ex.getEndClockInTime());
                        if (ObjectUtils.isEmpty(approveLogPOList)) {
                            KeyValueResult keyValueResult = new KeyValueResult(ClockInEnum.LACK_CHECKED.name(), "下班缺卡");
                            keyValueResultList.add(keyValueResult);
                        } else {
                            for (AttApproveLogPO approveLog : approveLogPOList) {
                                KeyValueResult keyValueResult = new KeyValueResult(approveLog.getBusinessType(),
                                                                                   approveLog.getSceneName());
                                keyValueResultList.add(keyValueResult);
                                ex.setState(ClockInEnum.ABNORMAL_WORK.name());
                            }
                        }
                    } else if (ObjectUtils.isNotEmpty(ex.getEndWorkOmit()) && ex.getEndWorkOmit()) {
                        ex.setState(ClassesStatusTriggerEnum.REPAIR_CLOCK.name());
                        KeyValueResult keyValueResult = new KeyValueResult(ex.getEndWorkState(), "下班补卡");
                        keyValueResultList.add(keyValueResult);
                    } else if (ObjectUtils.isNotEmpty(ex.getEndWorkOut()) && ex.getEndWorkOut()) {
                        ex.setState(ClockInEnum.WORK_OUT.name());
                        KeyValueResult keyValueResult = new KeyValueResult(ex.getEndWorkState(), "下班外勤");
                        keyValueResultList.add(keyValueResult);
                    }
                    ex.setStateDetails(keyValueResultList);
                });
            }
        });
        return resultList;
    }

    /**
     * 初始化台账
     */
    public AttClockInLedgerDetailsPO addLedger(AttClockInRecordsPO attClockInRecords) {
        // 查询的台账
        AttClockInLedgerPO ledger = clockInLedgerRepository.getClockInLedger(SecurityContextHolder.getPrincipal().getId(),
                                                                             DateUtils.format(attClockInRecords.getClockInDate(),
                                                                                              "yyyy-MM"));
        // 查询打卡关联的台账
        AttClockInLedgerDetailsPO details = clockInLedgerDetailsMapper.selectOne(
                new LambdaQueryWrapper<AttClockInLedgerDetailsPO>()
                        .eq(AttClockInLedgerDetailsPO::getClockInUuid, attClockInRecords.getUuid())
                        .last("limit 1"));
        if (ObjectUtils.isEmpty(details)) {
            // 打卡没有关联台账查询初始化的台账
            details = clockInLedgerDetailsMapper.selectOne(new LambdaQueryWrapper<AttClockInLedgerDetailsPO>()
                                                                   .eq(AttClockInLedgerDetailsPO::getIedgerId, ledger.getId())
                                                                   .eq(AttClockInLedgerDetailsPO::getToday,
                                                                       attClockInRecords.getClockInDate().getDayOfMonth())
                                                                   .isNull(AttClockInLedgerDetailsPO::getClockInUuid)
                                                                   .last("limit 1"));
            if (ObjectUtils.isNotEmpty(details)) {
                details.setClockInUuid(attClockInRecords.getUuid());
                clockInLedgerDetailsMapper.updateById(details);
            }
        }
        // 该班次没有台账，新增台账记录
        if (ObjectUtils.isEmpty(details)) {
            clockInLedgerDetailsRepository.addLedgerDetails(ledger.getId(), attClockInRecords);
            this.updateLedgerDetails(attClockInRecords);
        }
        return details;
    }
}
