package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.commandgateway;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.dto.AttClassesVersionDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttClassesTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesRecordMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject.AttSchedulingWaysEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRuleClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttHandSchedulingParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttHandStaffDateParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttHandStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttLegalHolidayUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttPoAttributeSet;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.dto.AttLegalHolidayMapDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.commandgateway.imp.AttendanceLogServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.po.AttendanceOperationLog;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 已发生的班次
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttSchedulingCommandService extends ServiceImpl<AttSchedulingMapper, AttSchedulingPO> {
    private final AttGroupMapper attGroupMapper;
    private final AttSchedulingRuleMapper attSchedulingRuleMapper;
    private final AttSchedulingRuleClassesMapper attSchedulingRuleClassesMapper;
    private final AttClassesMapper attClassesMapper;
    private final AttSchedulingMapper attSchedulingMapper;
    private final AttGroupStaffMapper attGroupStaffMapper;
    private final AttSchedulingQueryService attSchedulingQueryService;
    private final AttClassesRecordMapper attClassesRecordMapper;
    private final UserClient userClient;
    private final AttendanceLogServiceImpl attendanceLogServiceImpl;

    /**
     * 手动排班
     *
     * @param param 排班入参
     */
    public void handScheduling(AttHandSchedulingParam param) {
        Set<Long> ruleIdList = new HashSet<>();
        Set<Long> groupIdList = new HashSet<>();
        // 遍历获取人员 获取使用的规则和考勤组
        for (AttHandStaffParam hsStaff : param.getHsStaffList()) {
            ruleIdList.add(hsStaff.getRuleId());
            groupIdList.add(hsStaff.getGroupId());
        }
        // 所有使用的考勤组
        List<AttGroupPO> groupPOList = attGroupMapper.selectByIds(groupIdList);
        Map<Long, AttGroupPO> groupPOMap = groupPOList.stream().collect(Collectors.toMap(AttGroupPO::getId, v -> v));
        // 所有使用的规则
        List<AttSchedulingRulePO> rulePOList = attSchedulingRuleMapper.selectByIds(ruleIdList);
        Map<Long, AttSchedulingRulePO> rulePOMap = rulePOList.stream().collect(Collectors.toMap(AttSchedulingRulePO::getId, v -> v));

        // 遍历人员进行处理
        for (AttHandStaffParam hsStaff : param.getHsStaffList()) {
            AttGroupPO groupPO = groupPOMap.get(hsStaff.getGroupId());
            AttSchedulingRulePO rulePO = rulePOMap.get(hsStaff.getRuleId());
            if (groupPO == null || rulePO == null) {
                throw new ServiceException(hsStaff.getStaffName() + "不在任何考勤组无法排班");
            }
            if (AttSchedulingWaysEnum.SHIFTS.name().equals(rulePO.getWays())) {
                // 三班倒
                this.shiftsHandSchedulingAdd(hsStaff.getStaffId(), param.getMonth(), hsStaff.getStaffName(), groupPO, rulePO, hsStaff.getHsDateList(), param.getHandRest());
            } else {
                // 行政班
                this.fixedHandSchedulingAdd(hsStaff.getStaffId(), param.getMonth(), hsStaff.getStaffName(), groupPO, rulePO, hsStaff.getHsDateList(), param.getHandRest());
            }
        }
    }

    /**
     * 排班模板导入
     * 导入excel排班
     *
     * @param yearMonth 月份 格式：yyyy-MM
     * @param file      文件
     */
    public void importTemplateScheduling(String yearMonth, MultipartFile file) {
        Assert.isParameterError(StringUtils.isEmpty(yearMonth), "请选择月份");
        Assert.isParameterError(file.isEmpty(), "请上传excel文件");
        Assert.isConflict(
                DateUtils.compare(DateUtils.parseDate(yearMonth + "-01"), DateUtils.beginOfMonth(LocalDateTime.now())) < 0,
                "历史月份不能再进行排班操作");
        List<List<Object>> dataSource;
        try {
            // 校验隐藏内容
            ExcelReader hideReader = ExcelUtil.getReader(file.getInputStream(), 1);
            List<List<Object>> hideSource = hideReader.read(0);
            if (!yearMonth.equals(hideSource.getFirst().get(1))) {
                throw new ServiceException("0");
            }
            if (!"ARRANGE".equals(hideSource.getFirst().get(2))) {
                throw new ServiceException("1");
            }

            /* 读取正文 */
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            dataSource = reader.read(2);
        } catch (Exception e) {
            log.error("上传排班模板解析失败", e);
            if (e.getMessage().equals("0")) {
                throw new ServiceException("请下载对应月份的excel模板");
            } else if (e.getMessage().equals("1")) {
                throw new ServiceException("模板错误，请下载排班模板");
            } else {
                throw new ServiceException("excel文件解析失败,请下载最新的模板再试！");
            }
        }
        if (dataSource != null && !dataSource.isEmpty()) {
            this.excelDataToHandData(yearMonth, dataSource);
        }
    }

    /**
     * 解析excel数据并排班
     *
     * @param yearMonth  月份 格式：yyyy-MM
     * @param dataSource excel数据
     */
    private void excelDataToHandData(String yearMonth, List<List<Object>> dataSource) {
        AttHandSchedulingParam param = new AttHandSchedulingParam();
        try {
            // 起始日期
            LocalDateTime startDate = DateUtils.parseDate(yearMonth + "-01");
            if (yearMonth.equals(DateUtils.format(LocalDateTime.now(), "yyyy-MM"))) {
                startDate = DateUtils.beginOfDay(DateUtils.offsetDay(LocalDateTime.now(), AttConstants.oneInt));
            }

            int d = startDate.getDayOfMonth();
            LocalDateTime endMonthDate = DateUtils.endOfMonth(startDate);
            /* 班次数据准备 */
            Map<String, Long> classesMap = new HashMap<>();
            List<AttClassesPO> classesList = attClassesMapper.selectList(Wrappers.emptyWrapper());
            if (classesList != null) {
                for (AttClassesPO classesPO : classesList) {
                    classesMap.put(classesPO.getNickName(), classesPO.getId());
                }
            }
            /* 获取人员id，规则id，考勤组id */
            Map<String, AttGroupStaffPO> nameStaffMap = new HashMap<>();
            List<AttGroupStaffPO> groupStaffPOList = attGroupStaffMapper.selectList(Wrappers.emptyWrapper());
            if (groupStaffPOList != null && !groupStaffPOList.isEmpty()) {
                groupStaffPOList.forEach(s -> nameStaffMap.put(s.getName(), s));
            }

            /* 遍历组装入参 */

            param.setMonth(yearMonth);
            List<AttHandStaffParam> hsStaffList = new ArrayList<>();
            for (List<Object> staffListObj : dataSource) {

                AttHandStaffParam staffParam = new AttHandStaffParam();
                String staffName = (String) staffListObj.getFirst();
                AttGroupStaffPO groupStaffPO = nameStaffMap.get(staffName);
                staffParam.setGroupId(groupStaffPO.getGroupId());
                staffParam.setRuleId(groupStaffPO.getSchedulingRuleId());
                staffParam.setStaffId(groupStaffPO.getStaffId());
                staffParam.setStaffName(staffName);

                List<AttHandStaffDateParam> hsDateList = new ArrayList<>();
                LocalDateTime printDate = startDate;
                for (int i = d; i < staffListObj.size(); i++) {

                    if (printDate.isAfter(endMonthDate)) {
                        break;
                    }
                    String classStr = (String) staffListObj.get(i);
                    AttHandStaffDateParam dateParam = new AttHandStaffDateParam();
                    dateParam.setDate(DateUtils.formatDate(printDate));
                    List<Long> dayClassList = new ArrayList<>();
                    if (StringUtils.isNotBlank(classStr)) {
                        dayClassList.add(classesMap.get(classStr));
                    }
                    dateParam.setClassIdList(dayClassList);
                    hsDateList.add(dateParam);
                    printDate = DateUtils.offsetDay(printDate, AttConstants.oneInt);
                }
                staffParam.setHsDateList(hsDateList);
                hsStaffList.add(staffParam);
            }
            param.setHsStaffList(hsStaffList);
            param.setHandRest(true);
        } catch (Exception e) {
            log.error("排班导入解析数据失败：", e);
            log.error("排班导入解析数据失败：{}", JsonUtils.serialize(dataSource));
            if (e.getMessage().equals("0")) {
                throw new ServiceException("人员数据不正确，请下载新模板");
            } else {
                throw new ServiceException("excel数据解析失败,下载模板后请勿更改excel格式。");
            }
        }

        this.handScheduling(param);
    }

    /**
     * 插入行政班数据
     *
     * @param yearMonth 年月“2023-09”
     * @param staffId   人员id
     * @param staffName 人员名称
     * @param groupPO   考勤组
     * @param rulePO    规则
     */
    private void fixedHandSchedulingAdd(Long staffId, String yearMonth, String staffName, AttGroupPO groupPO, AttSchedulingRulePO rulePO,
                                        List<AttHandStaffDateParam> hsDateList, Boolean restHand
                                       ) {
        List<AttSchedulingRuleClassesPO> ruleClassesPOList = attSchedulingRuleClassesMapper.selectList(Wrappers
                                                                                                               .<AttSchedulingRuleClassesPO>lambdaQuery()
                                                                                                               .eq(AttSchedulingRuleClassesPO::getRuleId, rulePO.getId()));
        Map<String, List<AttSchedulingRuleClassesPO>> ruleClassesPOMap = ruleClassesPOList.stream()
                .collect(Collectors.groupingBy(AttSchedulingRuleClassesPO::getWeek));
        // 获取行政班的原来班次
        LocalDateTime startTime = DateUtils.parseDate(yearMonth + "-01");
        LocalDateTime endTime = DateUtils.endOfMonth(startTime);
        List<AttSchedulingPO> oldSchedulingPOList = attSchedulingQueryService.getStaffPeriodScheduling(staffId, startTime, endTime, rulePO.getLegalHolidaysRest(), ruleClassesPOList);
        Map<LocalDateTime, List<AttSchedulingPO>> oldDateClassNameMap;
        if (oldSchedulingPOList != null) {
            oldDateClassNameMap = oldSchedulingPOList.stream().collect(Collectors.groupingBy(AttSchedulingPO::getSchedulingDate));
        } else {
            oldDateClassNameMap = new HashMap<>();
        }
        // 组装成有日期的入参+原来的有日期的完整班次 + 需要移除的集合
        SetParamScheduling setParamScheduling = this.setDateParamScheduling(staffId, staffName, yearMonth, hsDateList, oldSchedulingPOList);
        List<AttSchedulingPO> checkPOList = setParamScheduling.getCheckPOList();
        List<AttSchedulingPO> insertPOList = setParamScheduling.getInsertPOList();
        List<LocalDateTime> removeDateList = setParamScheduling.getRemoveDateList();

        // 校验
        attSchedulingQueryService.fixedHandSchedulingCheck(staffName, checkPOList);

        // 删除需要移除的
        attSchedulingMapper.remove(System.currentTimeMillis(), Wrappers.<AttSchedulingPO>lambdaQuery()
                                           .eq(AttSchedulingPO::getStaffId, staffId)
                                           .in(AttSchedulingPO::getSchedulingDate, removeDateList)
                                  );

        Map<Long, Integer> classVersionMap = new HashMap<>();
        if (insertPOList != null) {
            List<Long> insertClassIdList = insertPOList.stream().map(AttSchedulingPO::getClassesId).distinct().collect(Collectors.toList());
            List<AttClassesVersionDTO> classVersionList = attClassesRecordMapper.getClassMaxVersion(Wrappers
                                                                                                            .<AttClassesRecordPO>lambdaQuery()
                                                                                                            .in(AttClassesRecordPO::getClassesId, insertClassIdList)
                                                                                                   );
            if (classVersionList != null && !classVersionList.isEmpty()) {
                classVersionMap = classVersionList.stream()
                        .collect(Collectors.toMap(AttClassesVersionDTO::getId, AttClassesVersionDTO::getVersion));
            }
        }
        // 插入
        List<AttSchedulingPO> insList = new ArrayList<>();
        List<AttendanceOperationLog> logInsertList = new ArrayList<>();
        Principal principal = SecurityContextHolder.getPrincipal();
        if (insertPOList == null) {
            return;
        }
        for (AttSchedulingPO newPo : insertPOList) {
            LocalDateTime oldTime = newPo.getSchedulingDate();
            LocalDateTime nowDateTime = LocalDateTime.of(oldTime.getYear(), oldTime.getMonth(), oldTime.getDayOfMonth(), oldTime.getHour(), oldTime.getMinute(), oldTime.getSecond());

            int dateWeek = nowDateTime.getDayOfWeek().getValue();
            AttLegalHolidayMapDTO holidayMapDTO = AttLegalHolidayUtils.getMonthLegalHolidayMap(nowDateTime.getYear(), nowDateTime.getMonth()
                    .getValue());
            boolean workDateBool = Boolean.TRUE.equals(rulePO.getLegalHolidaysRest()) &&
                                   holidayMapDTO.getWorkDateMap().get(DateUtils.formatDate(nowDateTime)) != null;
            // 置为休息，且规则也是休息并且不是法定上班就不用再插入手动的排班了
            if (!Boolean.TRUE.equals(restHand) && newPo.getClassesId() == null && ruleClassesPOMap.get(String.valueOf(dateWeek)) == null &&
                !workDateBool) {
                continue;
            }

            AttSchedulingPO insertSchedulingPO = AttSchedulingPO.builder().build();
            BeanUtils.copyProperties(newPo, insertSchedulingPO);
            insertSchedulingPO.setSchedulingMonth(newPo.getSchedulingMonth());
            insertSchedulingPO.setSchedulingDate(newPo.getSchedulingDate());
            insertSchedulingPO.setStaffId(staffId);
            insertSchedulingPO.setClassesId(newPo.getClassesId());
            insertSchedulingPO.setClassesVersion(classVersionMap.get(newPo.getClassesId()));
            insertSchedulingPO.setStaffName(staffName);
            insertSchedulingPO.setStaffGroupId(groupPO.getId());
            insertSchedulingPO.setStaffGroupName(groupPO.getName());
            insertSchedulingPO.setStaffGroupRanges(groupPO.getRanges());
            insertSchedulingPO.setSchedulingRuleId(groupPO.getSchedulingRuleId());
            insertSchedulingPO.setClockRuleId(groupPO.getClockRuleId());
            insertSchedulingPO.setSchedulingWays(rulePO.getWays());
            insertSchedulingPO.setHandScheduling(true);
            insertSchedulingPO.setEndTimeIsMorrow(newPo.getEndTimeIsMorrow());
            AttPoAttributeSet.initAddBasePoAttribute(insertSchedulingPO);
            insList.add(insertSchedulingPO);
        }
        if (!insList.isEmpty()) {
            this.saveBatch(insList);
        }

        Map<LocalDateTime, List<AttSchedulingPO>> insertMap = insertPOList.stream()
                .collect(Collectors.groupingBy(AttSchedulingPO::getSchedulingDate));
        insertMap.forEach((date, list) -> {
            // 操作日志
            AttendanceOperationLog operationLog = new AttendanceOperationLog();
            operationLog.setId(IdWorker.getId());
            operationLog.setBusinessType("ATTENDANCE_MANAGER"); // 考勤
            operationLog.setOperationObj("ARRANGE_WORK"); // 排班
            operationLog.setDeleted(false);
            operationLog.setCreateTime(LocalDateTime.now());
            if (principal != null) {
                operationLog.setUserId(principal.getId());
                operationLog.setUserName(principal.getName());
                operationLog.setCreateBy(principal.toString());
            }
            String oldClassName = "";
            List<AttSchedulingPO> oldClassNameList = oldDateClassNameMap.get(date);
            if (oldClassNameList != null) {
                StringBuilder sb = new StringBuilder();
                for (AttSchedulingPO attSchedulingPO : oldClassNameList) {
                    if (StringUtils.isBlank(attSchedulingPO.getClassesName())) {
                        continue;
                    }
                    sb.append(attSchedulingPO.getClassesName()).append(",");
                }
                oldClassName = sb.substring(AttConstants.zeroInt, sb.length() - AttConstants.oneInt);
            }
            String newClassName = "";
            if (list != null) {
                StringBuilder stringBuffer = new StringBuilder();
                for (AttSchedulingPO attSchedulingPO : list) {
                    if (attSchedulingPO.getClassesId() == null) {
                        newClassName = "休息";
                        break;
                    } else {
                        stringBuffer.append(attSchedulingPO.getClassesName()).append(",");
                    }
                }
                if (!stringBuffer.isEmpty()) {
                    newClassName = stringBuffer.substring(AttConstants.zeroInt, stringBuffer.length() - AttConstants.oneInt);
                }
            }

            operationLog.setOperationType("UPDATE");
            operationLog.setOperationContent(
                    "修改:" + staffName + " " + DateUtils.formatDate(date) + "排班,从【" + oldClassName + "】改为【" + newClassName + "】");

            logInsertList.add(operationLog);
        });

        if (!logInsertList.isEmpty()) {
            attendanceLogServiceImpl.saveBatch(logInsertList);
        }
    }

    /**
     * 插入三班倒数据
     *
     * @param yearMonth 年月“2023-09”
     * @param staffId   人员id
     * @param staffName 人员名称
     * @param groupPO   考勤组
     * @param rulePO    规则
     */
    private void shiftsHandSchedulingAdd(Long staffId, String yearMonth, String staffName, AttGroupPO groupPO, AttSchedulingRulePO rulePO,
                                         List<AttHandStaffDateParam> hsDateList, Boolean handRest
                                        ) {
        // 获取原来的班次(只获取当前规则的班，因为之前规则的班不适用于现在的规则
        List<AttSchedulingPO> oldSchedulingPOList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                           .eq(AttSchedulingPO::getStaffId, staffId)
                                                                                           .eq(AttSchedulingPO::getSchedulingRuleId, rulePO.getId())
                                                                                           .eq(AttSchedulingPO::getSchedulingMonth, yearMonth)
                                                                                  );

        Map<LocalDateTime, List<AttSchedulingPO>> oldDateClassNameMap;
        if (oldSchedulingPOList != null) {
            oldDateClassNameMap = oldSchedulingPOList.stream().collect(Collectors.groupingBy(AttSchedulingPO::getSchedulingDate));
        } else {
            oldDateClassNameMap = new HashMap<>();
        }

        // 组装成有日期的入参+原来的有日期的完整班次 + 需要移除的集合
        SetParamScheduling setParamScheduling = this.setDateParamScheduling(staffId, staffName, yearMonth, hsDateList, oldSchedulingPOList);
        List<AttSchedulingPO> checkSchedulingPOList = setParamScheduling.getCheckPOList();
        List<LocalDateTime> removeDateList = setParamScheduling.getRemoveDateList();
        List<AttSchedulingPO> insertPOList = setParamScheduling.getInsertPOList();

        // 三班倒校验
        attSchedulingQueryService.shiftsHandSchedulingCheck(staffName, rulePO, checkSchedulingPOList);

        // 删除需要移除的
        attSchedulingMapper.remove(System.currentTimeMillis(), Wrappers.<AttSchedulingPO>lambdaQuery()
                                           .eq(AttSchedulingPO::getStaffId, staffId)
                                           .in(AttSchedulingPO::getSchedulingDate, removeDateList)
                                  );
        List<AttSchedulingPO> insList = new ArrayList<>();
        Map<Long, Integer> classVersionMap = new HashMap<>();
        if (insertPOList != null) {
            List<Long> insertClassIdList = insertPOList.stream().map(AttSchedulingPO::getClassesId).distinct().collect(Collectors.toList());
            List<AttClassesVersionDTO> classVersionList = attClassesRecordMapper.getClassMaxVersion(Wrappers
                                                                                                            .<AttClassesRecordPO>lambdaQuery()
                                                                                                            .in(AttClassesRecordPO::getClassesId, insertClassIdList)
                                                                                                   );
            if (classVersionList != null && !classVersionList.isEmpty()) {
                classVersionMap = classVersionList.stream()
                        .collect(Collectors.toMap(AttClassesVersionDTO::getId, AttClassesVersionDTO::getVersion));
            }
        }
        List<AttendanceOperationLog> logInsertList = new ArrayList<>();
        Principal principal = SecurityContextHolder.getPrincipal();
        // 插入
        for (AttSchedulingPO oldPO : insertPOList) {
            if (oldPO.getClassesId() == null) {
                continue;
            }
            AttSchedulingPO insertSchedulingPO = AttSchedulingPO.builder().build();
            BeanUtils.copyProperties(oldPO, insertSchedulingPO);
            insertSchedulingPO.setSchedulingMonth(oldPO.getSchedulingMonth());
            insertSchedulingPO.setSchedulingDate(oldPO.getSchedulingDate());
            insertSchedulingPO.setClassesId(oldPO.getClassesId());
            insertSchedulingPO.setClassesVersion(classVersionMap.get(oldPO.getClassesId()));
            insertSchedulingPO.setStaffId(staffId);
            insertSchedulingPO.setStaffName(staffName);
            insertSchedulingPO.setStaffGroupId(groupPO.getId());
            insertSchedulingPO.setStaffGroupName(groupPO.getName());
            insertSchedulingPO.setStaffGroupRanges(groupPO.getRanges());
            insertSchedulingPO.setSchedulingRuleId(rulePO.getId());
            insertSchedulingPO.setClockRuleId(groupPO.getClockRuleId());
            insertSchedulingPO.setSchedulingWays(rulePO.getWays());
            insertSchedulingPO.setHandScheduling(true);
            insertSchedulingPO.setEndTimeIsMorrow(oldPO.getEndTimeIsMorrow());
            AttPoAttributeSet.initAddBasePoAttribute(insertSchedulingPO);
            insList.add(insertSchedulingPO);
        }
        if (!insList.isEmpty()) {
            this.saveBatch(insList);
        }

        Map<LocalDateTime, List<AttSchedulingPO>> insertMap = insertPOList.stream()
                .collect(Collectors.groupingBy(AttSchedulingPO::getSchedulingDate));
        insertMap.forEach((date, list) -> {
            // 操作日志
            AttendanceOperationLog operationLog = new AttendanceOperationLog();
            operationLog.setId(IdWorker.getId());
            operationLog.setBusinessType("ATTENDANCE_MANAGER"); // 考勤
            operationLog.setOperationObj("ARRANGE_WORK"); // 排班
            operationLog.setDeleted(false);
            operationLog.setCreateTime(LocalDateTime.now());
            if (principal != null) {
                operationLog.setUserId(principal.getId());
                operationLog.setUserName(principal.getName());
                operationLog.setCreateBy(principal.toString());
            }
            String oldClassName = "";
            List<AttSchedulingPO> oldClassNameList = oldDateClassNameMap.get(date);
            if (oldClassNameList != null) {
                StringBuilder sb = new StringBuilder();
                for (AttSchedulingPO attSchedulingPO : oldClassNameList) {
                    if (StringUtils.isBlank(attSchedulingPO.getClassesName())) {
                        continue;
                    }
                    sb.append(attSchedulingPO.getClassesName()).append(",");
                }
                oldClassName = sb.substring(AttConstants.zeroInt, sb.length() - AttConstants.oneInt);
            }
            String newClassName = "";
            if (list != null) {
                StringBuilder stringBuffer = new StringBuilder();
                for (AttSchedulingPO attSchedulingPO : list) {
                    if (attSchedulingPO.getClassesId() == null) {
                        newClassName = "休息";
                        break;
                    } else {
                        stringBuffer.append(attSchedulingPO.getClassesName()).append(",");
                    }
                }
                if (!stringBuffer.isEmpty()) {
                    newClassName = stringBuffer.substring(AttConstants.zeroInt, stringBuffer.length() - AttConstants.oneInt);
                }
            }

            operationLog.setOperationType("UPDATE");
            operationLog.setOperationContent(
                    "修改:" + staffName + " " + DateUtils.formatDate(date) + "排班,从【" + oldClassName + "】改为【" + newClassName + "】");

            logInsertList.add(operationLog);
        });

        if (!logInsertList.isEmpty()) {
            attendanceLogServiceImpl.saveBatch(logInsertList);
        }
    }

    /**
     * 将入参和数据库原来的班次做并集合，并且组装成有日期的排班集合对象
     *
     * @param staffId             人员id
     * @param yearMonth           年月“2023-09”
     * @param hsDateList          排班入参
     * @param oldSchedulingPOList 数据库原来的排班(注意:行政班和三班倒数据库原来的班次获取方式不同)
     *
     * @return 有日期的排班
     */
    private SetParamScheduling setDateParamScheduling(Long staffId, String staffName, String yearMonth,
                                                      List<AttHandStaffDateParam> hsDateList, List<AttSchedulingPO> oldSchedulingPOList
                                                     ) {
        SetParamScheduling setParamScheduling = new SetParamScheduling();
        Map<String, List<AttSchedulingPO>> oldSchedulingPOMap = new HashMap<>();
        if (oldSchedulingPOList != null) {
            oldSchedulingPOMap = oldSchedulingPOList.stream()
                    .collect(Collectors.groupingBy(k -> k.getStaffId() + DateUtils.formatDate(k.getSchedulingDate())));
        }

        // 入参的排班
        List<AttSchedulingPO> paramSchedulinPOList = new ArrayList<>();

        StringBuffer errorBuffer = new StringBuffer();
        // 组装入参的排班
        for (AttHandStaffDateParam hsDate : hsDateList) {
            // 手动排班日期的开始时间
            LocalDateTime startHasDay = DateUtils.parse(hsDate.getDate() + " 00:00:00");
            // 只能修改今日之后的排班   （/*&&(oldDayRestBool||paramDayRestBool)*/ 这里判断的是且为休息，表示可以改当天未开始的班次而不是今天不能改）
            if (startHasDay.isBefore(LocalDateTime.now())/*&&(oldDayRestBool||paramDayRestBool)*/) {
                errorBuffer.append(staffName).append(hsDate.getDate()).append("只能修改明天及以后的排班\r\n");
                continue;
            }
            if (hsDate.getClassIdList() == null || hsDate.getClassIdList().isEmpty()) {
                AttSchedulingPO attSchedulingPO = AttSchedulingPO.builder()
                        .staffId(staffId)
                        .schedulingMonth(yearMonth)
                        .schedulingDate(DateUtils.parseDate(hsDate.getDate()))
                        .build();
                paramSchedulinPOList.add(attSchedulingPO);
                continue;
            }
            hsDate.getClassIdList().forEach(classId -> {
                AttSchedulingPO attSchedulingPO = AttSchedulingPO.builder()
                        .staffId(staffId)
                        .schedulingMonth(yearMonth)
                        .schedulingDate(DateUtils.parseDate(hsDate.getDate()))
                        .classesId(classId)
                        .build();
                attSchedulingPO.setStaffId(staffId);
                paramSchedulinPOList.add(attSchedulingPO);
            });
        }
        Map<String, Long> insertMap = new HashMap<>();
        List<LocalDateTime> removeDateList = new ArrayList<>();
        // 遍历入参的排班，如果有就移除原本的排班
        for (AttSchedulingPO paramSchedulingPO : paramSchedulinPOList) {
            String strKey = paramSchedulingPO.getStaffId() + DateUtils.formatDate(paramSchedulingPO.getSchedulingDate());
            oldSchedulingPOMap.remove(strKey);
            removeDateList.add(paramSchedulingPO.getSchedulingDate());
            insertMap.put(paramSchedulingPO.getStaffId() + DateUtils.formatDate(paramSchedulingPO.getSchedulingDate()), 1L);
        }
        List<AttSchedulingPO> insertSchedulingList = new ArrayList<>();
        // 移除后的原本的排班加入参的排班，就是入参后的完整排班
        oldSchedulingPOMap.forEach((k, v) -> paramSchedulinPOList.addAll(new ArrayList<>(v)));
        // 所有的入参班次id集合
        Map<Long, AttClassesPO> classesPOMap = new HashMap<>();
        if (!paramSchedulinPOList.isEmpty()) {
            List<Long> classIdList = paramSchedulinPOList.stream().map(AttSchedulingPO::getClassesId).filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (!classIdList.isEmpty()) {
                List<AttClassesPO> classesPOList = attClassesMapper.selectByIds(classIdList);
                if (classesPOList != null && !classesPOList.isEmpty()) {
                    classesPOMap = classesPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
                }
            }
        }
        // 组装有日期的排班
        List<AttSchedulingPO> checkSchedulingList = new ArrayList<>();
        for (AttSchedulingPO paramPo : paramSchedulinPOList) {
            if (paramPo.getClassesRealStartTime() == null) {
                attSchedulingQueryService.stringClassToScheduling(paramPo, classesPOMap.get(paramPo.getClassesId()));
                if (paramPo.getClassesRealStartTime() != null) {
                    checkSchedulingList.add(paramPo);
                }
                String dateKey = DateUtils.formatDate(paramPo.getSchedulingDate());
                if (insertMap.get(staffId + dateKey) != null) { // 入参的日期需要插入
                    if (!Boolean.TRUE.equals(paramPo.getStarted())) { // 如果是未开始的时间才插入(已经开始的抛出提示
                        insertSchedulingList.add(paramPo);
                    } else if (!errorBuffer.toString().contains(staffName + dateKey)) {
                        errorBuffer.append(staffName).append(dateKey).append("排班已经开始。\r\n");
                    }
                }
            } else {
                checkSchedulingList.add(paramPo);
            }
        }
        setParamScheduling.setCheckPOList(checkSchedulingList);
        setParamScheduling.setRemoveDateList(removeDateList);
        setParamScheduling.setInsertPOList(insertSchedulingList);
        setParamScheduling.setErrorBuffer(errorBuffer);
        if (!errorBuffer.isEmpty()) {
            throw new ServiceException(errorBuffer.toString());
        }
        return setParamScheduling;
    }

    /**
     * 固定当天的手动排班
     *
     * @param nowTime 时间
     *
     * @return 有手动排班的人
     */
    private Map<Long, Boolean> fixedNowHandScheduling(LocalDateTime nowTime, List<Long> classIdList) {
        // 已经有手动排班的人的id的集合
        Map<Long, Boolean> handStaffMap = new HashMap<>();
        String yearMonth = DateUtils.format(nowTime, "yyyy-MM");
        String dayTimeStr = DateUtils.format(nowTime, DateUtils.DEFAULT_DATE_FORMATTER);
        List<AttSchedulingPO> schedulingPOList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                        .eq(AttSchedulingPO::getSchedulingMonth, yearMonth)
                                                                                        .eq(AttSchedulingPO::getSchedulingDate, dayTimeStr)
                                                                                        .in(classIdList != null &&
                                                                                            !classIdList.isEmpty(), AttSchedulingPO::getClassesId, classIdList)
                                                                               );
        if (schedulingPOList == null) {
            return handStaffMap;
        }
        if (classIdList == null || classIdList.isEmpty()) {
            classIdList = schedulingPOList.stream().map(AttSchedulingPO::getClassesId).filter(Objects::nonNull).distinct()
                    .collect(Collectors.toList());
        }

        Map<Long, AttClassesPO> classesPOMap = new HashMap<>();
        Map<Long, Integer> classVersionMap = new HashMap<>();
        if (!classIdList.isEmpty()) {
            List<AttClassesPO> classesPOList = attClassesMapper.selectByIds(classIdList);
            classesPOMap = classesPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
            List<AttClassesVersionDTO> classVersionList = attClassesRecordMapper.getClassMaxVersion(Wrappers
                                                                                                            .<AttClassesRecordPO>lambdaQuery()
                                                                                                            .in(AttClassesRecordPO::getClassesId, classIdList)
                                                                                                   );
            if (classVersionList != null && !classVersionList.isEmpty()) {
                classVersionMap = classVersionList.stream()
                        .collect(Collectors.toMap(AttClassesVersionDTO::getId, AttClassesVersionDTO::getVersion));
            }
        }

        for (AttSchedulingPO po : schedulingPOList) {
            handStaffMap.put(po.getStaffId(), true);
            if (Boolean.TRUE.equals(po.getStarted())) {
                continue;
            }
            if (po.getClassesId() == null) {
                // 班次id为空 直接确定为休息的且已经开始的排班
                attSchedulingMapper.update(null, Wrappers.<AttSchedulingPO>lambdaUpdate()
                                                   .set(AttSchedulingPO::getStarted, true)
                                                   .set(AttSchedulingPO::getClassesType, AttClassesTypeEnum.REST.name())
                                                   .eq(AttSchedulingPO::getId, po.getId())
                                          );
                continue;
            }
            if (po.getClassesRealStartTime() == null) {
                attSchedulingQueryService.stringClassToScheduling(po, classesPOMap.get(po.getClassesId()));
                if (po.getClassesRealStartTime() == null) {
                    attSchedulingMapper.update(null, Wrappers.<AttSchedulingPO>lambdaUpdate()
                                                       .set(AttSchedulingPO::getStarted, true)
                                                       .set(AttSchedulingPO::getClassesType, AttClassesTypeEnum.REST.name())
                                                       .eq(AttSchedulingPO::getId, po.getId())
                                              );
                    continue;
                }
                // 如果班次已经开始就将班次固定下来
                if (!nowTime.isBefore(po.getClassesRealStartTime()) &&
                    !nowTime.isAfter(po.getClassesRealEndTime())) {
                    po.setClassesVersion(classVersionMap.get(po.getClassesId()));
                    attSchedulingMapper.updateById(po);
                }
            }
        }

        return handStaffMap;
    }

    @Data
    private static class SetParamScheduling {
        // 需要删除的日期(有手动排班就要删除原来日期的数据)
        private List<LocalDateTime> removeDateList;
        // 需要校验的集合(校验需要校验一个月的排班)
        private List<AttSchedulingPO> checkPOList;
        // 需要插入的集合(插入只插入传入的排班)
        private List<AttSchedulingPO> insertPOList;
        // 不合格的信息
        private StringBuffer errorBuffer;
    }

    /**
     * 固定排班班次信息
     *
     * @param nowTime  固定的时刻（null 为当前时间
     * @param operator 操作人（当前登录人
     */
    public void fixedNowScheduling(LocalDateTime nowTime, String operator) {
        if (nowTime == null) {
            nowTime = LocalDateTime.now();
        }
        List<AttGroupPO> groupPOList = attGroupMapper.selectList(Wrappers.emptyWrapper());
        if (groupPOList == null || groupPOList.isEmpty()) {
            return;
        }
        LocalDateTime finalNowTime = nowTime;
        groupPOList.forEach(groupPO -> this.fixedNowGroupScheduling(null, groupPO, finalNowTime, operator));
    }

    /**
     * 固定考勤组排班
     *
     * @param groupPO  考勤组
     * @param staffId  人员id（如果人员id为空就固定整个考勤组的
     * @param nowTime  时间
     * @param operator 操作人
     */
    public void fixedNowGroupScheduling(Long staffId, AttGroupPO groupPO, LocalDateTime nowTime, String operator) {
        if (nowTime == null) {
            nowTime = LocalDateTime.now();
        }
        // 1.固定手动排班
        Map<Long, Boolean> handStaffMap = this.fixedNowHandScheduling(staffId, groupPO, nowTime);
        // 2.固定规则排班
        this.fixedNowRuleScheduling(staffId, groupPO, handStaffMap, nowTime, operator, groupPO.getTenant());
    }

    /**
     * 固定当天的手动排班
     *
     * @param groupPO 考勤组
     * @param staffId 人员id（如果人员id为空就固定整个考勤组的
     * @param nowTime 时间
     *
     * @return 有手动排班的人
     */
    private Map<Long, Boolean> fixedNowHandScheduling(Long staffId, AttGroupPO groupPO, LocalDateTime nowTime) {
        Map<Long, Boolean> handStaffMap = new HashMap<>();
        if (groupPO == null) {
            return handStaffMap;
        }
        String yearMonth = DateUtils.format(nowTime, "yyyy-MM");
        String dayTimeStr = DateUtils.format(nowTime, DateUtils.DEFAULT_DATE_FORMATTER);

        Wrapper<AttSchedulingPO> querWrapper = null;
        if (staffId == null) { // staffId为空就固定整个考勤组，不为空就固定某人的
            List<Long> staffIdLlist = new ArrayList<>();
            List<AttGroupStaffPO> groupStaffPOList = attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                                            .eq(AttGroupStaffPO::getGroupId, groupPO.getId())
                                                                                   );
            if (groupStaffPOList != null && !groupStaffPOList.isEmpty()) {
                // 人员id集合
                staffIdLlist = groupStaffPOList.stream().map(AttGroupStaffPO::getStaffId).collect(Collectors.toList());
            }
            if (staffIdLlist.isEmpty()) {
                return handStaffMap;
            }
            querWrapper = Wrappers.<AttSchedulingPO>lambdaQuery()
                    .eq(AttSchedulingPO::getSchedulingMonth, yearMonth)
                    .eq(AttSchedulingPO::getSchedulingDate, dayTimeStr)
                    .in(AttSchedulingPO::getStaffId, staffIdLlist);
        } else {
            querWrapper = Wrappers.<AttSchedulingPO>lambdaQuery()
                    .eq(AttSchedulingPO::getSchedulingMonth, yearMonth)
                    .eq(AttSchedulingPO::getSchedulingDate, dayTimeStr)
                    .eq(AttSchedulingPO::getStaffId, staffId);
        }

        // 手动排班数据
        List<AttSchedulingPO> schedulingPOList = attSchedulingMapper.selectList(querWrapper);
        if (schedulingPOList == null) {
            return handStaffMap;
        }

        Map<Long, Integer> classVersionMap = new HashMap<>();
        Map<Long, AttClassesPO> classesPOMap = new HashMap<>();
        List<AttClassesPO> classesPOList = attClassesMapper.selectList(Wrappers.emptyWrapper());
        if (classesPOList != null && !classesPOList.isEmpty()) {
            List<Long> classIdList = new ArrayList<>();
            classesPOList.forEach(classesPO -> {
                classesPOMap.put(classesPO.getId(), classesPO);
                classIdList.add(classesPO.getId());
            });
            List<AttClassesVersionDTO> classVersionList = attClassesRecordMapper.getClassMaxVersion(Wrappers
                                                                                                            .<AttClassesRecordPO>lambdaQuery()
                                                                                                            .in(AttClassesRecordPO::getClassesId, classIdList)
                                                                                                   );
            if (classVersionList != null && !classVersionList.isEmpty()) {
                classVersionMap = classVersionList.stream()
                        .collect(Collectors.toMap(AttClassesVersionDTO::getId, AttClassesVersionDTO::getVersion));
            }
        }

        for (AttSchedulingPO po : schedulingPOList) {
            handStaffMap.put(po.getStaffId(), true);
            if (Boolean.TRUE.equals(po.getStarted())) {
                continue;
            }
            if (po.getClassesId() == null) {
                // 班次id为空 直接确定为休息的且已经开始的排班
                attSchedulingMapper.update(null, Wrappers.<AttSchedulingPO>lambdaUpdate()
                                                   .set(AttSchedulingPO::getStarted, true)
                                                   .set(AttSchedulingPO::getClassesType, AttClassesTypeEnum.REST.name())
                                                   .eq(AttSchedulingPO::getId, po.getId())
                                          );
                continue;
            }
            if (po.getClassesRealStartTime() == null) {
                attSchedulingQueryService.stringClassToScheduling(po, classesPOMap.get(po.getClassesId()));
                if (po.getClassesRealStartTime() == null) {
                    // 如果处理班次时间后还没有班次时间，说明班次可能被删除了，还是视为休息
                    attSchedulingMapper.update(null, Wrappers.<AttSchedulingPO>lambdaUpdate()
                                                       .set(AttSchedulingPO::getStarted, true)
                                                       .set(AttSchedulingPO::getClassesType, AttClassesTypeEnum.REST.name())
                                                       .eq(AttSchedulingPO::getId, po.getId())
                                              );
                    continue;
                }
                // 如果班次已经开始就将班次固定下来
                po.setClassesVersion(classVersionMap.get(po.getClassesId()));
                po.setStarted(true);
                attSchedulingMapper.updateById(po);
            }
        }

        return handStaffMap;
    }

    /**
     * 固定当天的规则排班
     *
     * @param groupPO      考勤组
     * @param staffId      人员id（如果人员id为空就固定整个考勤组的
     * @param nowTime      时间
     * @param handStaffMap 手动排过班的人(map的key为人员id,value固定为true)
     */
    private void fixedNowRuleScheduling(Long staffId, AttGroupPO groupPO, Map<Long, Boolean> handStaffMap, LocalDateTime nowTime,
                                        String operator, Integer tenant
                                       ) {
        String yearMonth = DateUtils.format(nowTime, "yyyy-MM");
        AttSchedulingRulePO rulePO = null;
        List<AttSchedulingRuleClassesPO> ruleClassesPOList = null;
        List<AttGroupStaffPO> groupStaffPOList = new ArrayList<>();
        if (groupPO != null) {
            rulePO = attSchedulingRuleMapper.selectById(groupPO.getSchedulingRuleId());
            if (staffId != null) {
                UserDTO user = userClient.getUser(staffId);
                AttGroupStaffPO groupStaffPO = AttGroupStaffPO.builder()
                        .staffId(staffId)
                        .groupId(groupPO.getId())
                        .schedulingRuleId(groupPO.getSchedulingRuleId())
                        .clockRuleId(groupPO.getClockRuleId())
                        .name(user.getName())
                        .deptId(user.getOrganization() == null ? null : user.getOrganization().getId())
                        .deptName(user.getOrganization() == null ? null : user.getOrganization().getName())
                        .build();
                groupStaffPOList.add(groupStaffPO);
            } else {
                groupStaffPOList = this.getGroupAllStaff(groupPO);
            }
        }
        if (groupStaffPOList == null || groupStaffPOList.isEmpty()) {
            return;
        }
        if (rulePO != null) {
            int week = nowTime.getDayOfWeek().getValue();
            // 2.规则的班次
            ruleClassesPOList = attSchedulingRuleClassesMapper.selectList(Wrappers.<AttSchedulingRuleClassesPO>lambdaQuery()
                                                                                  .eq(AttSchedulingRuleClassesPO::getRuleId, rulePO.getId())
                                                                                  .and(i -> i.eq(AttSchedulingRuleClassesPO::getWeek, 1)
                                                                                          .or()
                                                                                          .eq(AttSchedulingRuleClassesPO::getWeek, week)
                                                                                      )

                                                                         );
        }

        // 获取法定节假日
        AttLegalHolidayMapDTO legalHolidayMapDTO = AttLegalHolidayUtils.getMonthLegalHolidayMap(nowTime.getYear(), nowTime.getMonthValue());
        boolean workDateBool = legalHolidayMapDTO.getWorkDateMap().get(DateUtils.format(nowTime, DateUtils.DEFAULT_DATE_FORMATTER)) != null;
        boolean restDateBool = legalHolidayMapDTO.getRestDateMap().get(DateUtils.format(nowTime, DateUtils.DEFAULT_DATE_FORMATTER)) != null;

        Map<Long, Integer> classVersionMap = new HashMap<>();
        Map<Long, AttClassesPO> classesPOMap = new HashMap<>();
        List<AttClassesPO> classesPOList = attClassesMapper.selectList(Wrappers.emptyWrapper());
        if (classesPOList != null && !classesPOList.isEmpty()) {
            List<Long> classIdList = new ArrayList<>();
            classesPOList.forEach(classesPO -> {
                classesPOMap.put(classesPO.getId(), classesPO);
                classIdList.add(classesPO.getId());
            });
            List<AttClassesVersionDTO> classVersionList = attClassesRecordMapper.getClassMaxVersion(Wrappers
                                                                                                            .<AttClassesRecordPO>lambdaQuery()
                                                                                                            .in(AttClassesRecordPO::getClassesId, classIdList)
                                                                                                   );
            if (classVersionList != null && !classVersionList.isEmpty()) {
                classVersionMap = classVersionList.stream()
                        .collect(Collectors.toMap(AttClassesVersionDTO::getId, AttClassesVersionDTO::getVersion));
            }
        }
        List<AttSchedulingPO> insList = new ArrayList<>();
        for (AttGroupStaffPO groupStaffPO : groupStaffPOList) {
            // 已经有手动排班了，就不用管规则的了
            if (handStaffMap.get(groupStaffPO.getStaffId()) != null) {
                continue;
            }
            if (groupPO == null || rulePO == null || ruleClassesPOList == null) {
                insList.add(this.insRestScheduling(groupStaffPO.getStaffId(), groupStaffPO.getName(), groupPO, rulePO, yearMonth, nowTime, groupStaffPO.getDeptId(), groupStaffPO.getDeptName(), operator, tenant));
                continue;
            }

            List<AttSchedulingRuleClassesPO> dayWeekRuleClassPOList = new ArrayList<>();
            List<AttSchedulingRuleClassesPO> oneWeekRuleClassPOList = new ArrayList<>();
            for (AttSchedulingRuleClassesPO ruleClassesPO : ruleClassesPOList) {
                if (StringUtils.isBlank(ruleClassesPO.getWeek())) {
                    // 没有week 脏数据直接跳过该数据
                    continue;
                }
                if (ruleClassesPO.getWeek().equals(String.valueOf(nowTime.getDayOfWeek().getValue()))) {
                    dayWeekRuleClassPOList.add(ruleClassesPO);
                } else if (ruleClassesPO.getWeek().equals(String.valueOf(1))) {
                    oneWeekRuleClassPOList.add(ruleClassesPO);
                }
            }
            if (restDateBool) { // 法定休息
                dayWeekRuleClassPOList = new ArrayList<>();
            } else if (dayWeekRuleClassPOList.isEmpty() && workDateBool) { // 规则为休息，但法定工作日
                dayWeekRuleClassPOList = oneWeekRuleClassPOList;
            }

            // 为空就插入休息班次
            if (dayWeekRuleClassPOList.isEmpty()) {
                insList.add(this.insRestScheduling(groupStaffPO.getStaffId(), groupStaffPO.getName(), groupPO, rulePO, yearMonth, nowTime, groupStaffPO.getDeptId(), groupStaffPO.getDeptName(), operator, tenant));
                continue;
            }

            // 遍历当天的班次生成排班
            for (AttSchedulingRuleClassesPO ruleClassesPO : dayWeekRuleClassPOList) {
                AttSchedulingPO po = AttSchedulingPO.builder()
                        .id(IdWorker.getId())
                        .schedulingMonth(yearMonth)
                        .schedulingDate(nowTime)
                        .staffId(groupStaffPO.getStaffId())
                        .staffName(groupStaffPO.getName())
                        .staffDeptName(groupStaffPO.getDeptName())
                        .staffDeptName(groupStaffPO.getDeptName())
                        .staffGroupId(groupStaffPO.getGroupId())
                        .schedulingRuleId(groupStaffPO.getSchedulingRuleId())
                        .clockRuleId(groupStaffPO.getClockRuleId())
                        .build();
                po.setStaffGroupRanges(groupPO.getRanges());
                po.setStaffGroupName(groupPO.getName());
                po.setSchedulingWays(rulePO.getWays());
                po.setDeleted(false);
                po.setTenant(tenant);
                EntityAuditor.populateAudit(po, operator, StringUtils.EMPTY_STRING, null, nowTime);
                attSchedulingQueryService.stringClassToScheduling(po, classesPOMap.get(ruleClassesPO.getId()));
                if (po.getClassesRealStartTime() == null) {
                    insList.add(this.insRestScheduling(groupStaffPO.getStaffId(), groupStaffPO.getName(), groupPO, rulePO, yearMonth, nowTime, groupStaffPO.getDeptId(), groupStaffPO.getDeptName(), operator, tenant));
                    continue;
                }
                po.setClassesVersion(classVersionMap.get(po.getClassesId()));
                po.setStarted(true);
                // 插入已开始的班次
                insList.add(po);
            }
        }
        if (!insList.isEmpty()) {
            this.saveBatch(insList);
        }
    }

    private AttSchedulingPO insRestScheduling(Long staffId, String staffName, AttGroupPO groupPO, AttSchedulingRulePO rulePO,
                                              String yearMonth, LocalDateTime nowTime, Long deptId, String deptName, String operator,
                                              Integer tenant
                                             ) {
        AttSchedulingPO po = AttSchedulingPO.builder()
                .id(IdWorker.getId())
                .schedulingMonth(yearMonth)
                .schedulingDate(nowTime)
                .staffId(staffId)
                .staffName(staffName)
                .classesName("休息")
                .classesNickName("休息")
                .staffDeptId(deptId)
                .staffDeptName(deptName)
                .started(!DateUtils.beginOfDay(nowTime).isAfter(LocalDateTime.now()))
                .build();
        if (groupPO != null) {
            po.setStaffGroupId(groupPO.getId());
            po.setStaffGroupRanges(groupPO.getRanges());
            po.setStaffGroupName(groupPO.getName());
        }
        if (rulePO != null) {
            po.setSchedulingRuleId(rulePO.getId());
            po.setSchedulingWays(rulePO.getWays());
        }
        po.setTenant(tenant);
        EntityAuditor.populateAudit(po, operator, StringUtils.EMPTY_STRING, null, nowTime);
        return po;
    }

    private List<AttGroupStaffPO> getGroupAllStaff(AttGroupPO groupPO) {
        List<AttGroupStaffPO> groupStaffPOList = null;
        if (groupPO == null) {
            return groupStaffPOList;
        }
        if (AttGroupRangesEnum.ALL.name().equals(groupPO.getRanges())) {
            UserQueryParam userQueryParam = new UserQueryParam();
            userQueryParam.setEnabled(true);
            userQueryParam.setPageNumber(1);
            userQueryParam.setPageSize(999);

            Pagination<UserDTO> userDTOPagination = userClient.listUserByTenant(groupPO.getTenant(), userQueryParam);
            List<UserDTO> userDTOList = userDTOPagination.getRecords();
            if (userDTOList != null && !userDTOList.isEmpty()) {
                Long groupId = groupPO.getId();
                Long ruleId = groupPO.getSchedulingRuleId();
                Long clockId = groupPO.getClockRuleId();
                groupStaffPOList = userDTOList.stream().map(u -> AttGroupStaffPO.builder()
                                                                    .groupId(groupId)
                                                                    .schedulingRuleId(ruleId)
                                                                    .clockRuleId(clockId)
                                                                    .staffId(u.getId())
                                                                    .name(u.getName())
                                                                    .deptId(u.getOrganization() == null ? null : u.getOrganization().getId())
                                                                    .deptName(u.getOrganization() == null ? "" : u.getOrganization().getName())
                                                                    .build()
                                                           ).collect(Collectors.toList());
            }
        } else {
            groupStaffPOList = attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                      .eq(AttGroupStaffPO::getGroupId, groupPO.getId())
                                                             );
        }

        return groupStaffPOList;
    }
}
