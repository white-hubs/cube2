package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.application.commandgateway;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.domain.repository.AttDutyRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.mapper.AttDutyMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.mapper.AttDutyStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param.AttDutyAddStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param.AttDutyClassParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param.AttDutyDateParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param.AttDutyHandParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param.AttDutyRemoveStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.param.AttDutyStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttPoAttributeSet;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.commandgateway.imp.AttendanceLogServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.po.AttendanceOperationLog;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 值班操作服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttDutyCommandService extends ServiceImpl<AttDutyMapper, AttDutyPO> {
    private final AttDutyRepository attDutyRepository;
    private final AttDutyStaffMapper attDutyStaffMapper;
    private final AttDutyMapper attDutyMapper;
    private final AttClassesMapper attClassesMapper;
    private final AttendanceLogServiceImpl attendanceLogServiceImpl;

    /**
     * 手动值班
     *
     * @param param 值班
     */
    public void handDuty(AttDutyHandParam param) {
        Map<Long, List<AttDutyPO>> dutyPOMap = new HashMap<>();
        List<AttDutyPO> dutyPOList = attDutyMapper.selectList(Wrappers.<AttDutyPO>lambdaQuery()
                                                                      .eq(AttDutyPO::getDutyMonth, param.getYearMonth())
                                                                      .isNotNull(AttDutyPO::getClassesName)
                                                             );
        if (dutyPOList != null) {
            dutyPOMap = dutyPOList.stream().collect(Collectors.groupingBy(AttDutyPO::getStaffId));
        }
        for (int i = 0; i < param.getDutyStaffList().size(); i++) {
            AttDutyStaffParam staffParam = param.getDutyStaffList().get(i);
            List<AttDutyDateParam> dateParamList = staffParam.getStaffDateList();
            this.handStaffDateList(param.getYearMonth(), staffParam.getStaffId(), staffParam.getStaffName(), staffParam.getStaffDeptId(), staffParam.getStaffDeptName(), dateParamList, dutyPOMap.get(staffParam.getStaffId()));
        }
    }

    /**
     * 值班模板导入
     * 导入excel值班
     *
     * @param yearMonth 月份 格式：yyyy-MM
     * @param file      文件
     */
    public void importTemplateDuty(String yearMonth, MultipartFile file) {
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
            if (!"DUTY".equals(hideSource.getFirst().get(2))) {
                throw new ServiceException("1");
            }
            /* 读取正文 */
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            dataSource = reader.read(1);
        } catch (Exception e) {
            log.error("上传排班模板解析失败", e);
            if (e.getMessage().equals("0")) {
                throw new ServiceException("请下载对应月份的excel模板");
            } else if (e.getMessage().equals("1")) {
                throw new ServiceException("模板错误，请下载值班模板");
            } else {
                throw new ServiceException("excel文件解析失败");
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
        AttDutyHandParam param = new AttDutyHandParam();
        try {
            // 起始日期
            LocalDateTime monthDay = DateUtils.parseDate(yearMonth + "-01");
            LocalDateTime startDate = DateUtils.offsetDay(monthDay, -1);
            LocalDateTime endMonthDate = DateUtils.endOfMonth(monthDay);
            LocalDateTime nowDayEnd = DateUtils.endOfDay(LocalDateTime.now());

            /* 班次数据准备 */
            Map<String, AttClassesPO> classesMap = new HashMap<>();
            List<AttClassesPO> classesList = attClassesMapper.selectList(Wrappers.emptyWrapper());
            if (classesList != null) {
                classesMap = classesList.stream()
                        .collect(Collectors.toMap(k -> k.getName() + ":" + k.getStartWorkTime() + "-" + k.getEndWorkTime(), v -> v));
            }
            /* 遍历组装入参 */

            param.setYearMonth(yearMonth);
            List<AttDutyStaffParam> dutyStaffList = new ArrayList<>();
            for (List<Object> staffListObj : dataSource) {
                AttDutyStaffParam staffParam = new AttDutyStaffParam();
                Long staffId = null;
                if (Objects.nonNull(staffListObj.get(0))) {
                    staffId = Long.valueOf((String) staffListObj.getFirst());
                }
                staffParam.setStaffId(staffId);
                staffParam.setStaffName((String) staffListObj.get(1));
                List<AttDutyDateParam> staffDateList = new ArrayList<>();
                LocalDateTime printDate = startDate;
                for (int i = 2; i < staffListObj.size(); i++) {
                    printDate = DateUtils.offsetDay(printDate, 1);
                    // 跳过当天
                    if (printDate.isBefore(nowDayEnd)) {
                        continue;
                    }
                    if (printDate.isAfter(endMonthDate)) {
                        break;
                    }
                    String classStr = (String) staffListObj.get(i);
                    AttDutyDateParam dateParam = new AttDutyDateParam();
                    dateParam.setDutyDate(DateUtils.formatDate(printDate));
                    dateParam.setWeek(printDate.getDayOfWeek().getValue());
                    List<AttDutyClassParam> dayClassList = new ArrayList<>();
                    if (StringUtils.isNotBlank(classStr)) {
                        AttClassesPO classesPO = classesMap.get(classStr);
                        if (classesPO != null) {
                            AttDutyClassParam classParam = new AttDutyClassParam();
                            classParam.setClassId(classesPO.getId());
                            classParam.setClassName(classesPO.getName());
                            classParam.setClassNickName(classesPO.getNickName());
                            classParam.setClassType(classesPO.getClassesType());
                            classParam.setStartWorkTime(classesPO.getStartWorkTime());
                            classParam.setEndWorkTime(classesPO.getEndWorkTime());
                            classParam.setEndTimeIsMorrow(classesPO.getEndTimeIsMorrow());
                            dayClassList.add(classParam);
                        }
                    }
                    dateParam.setDateClassList(dayClassList);
                    staffDateList.add(dateParam);
                }
                staffParam.setStaffDateList(staffDateList);
                dutyStaffList.add(staffParam);
            }
            param.setDutyStaffList(dutyStaffList);
        } catch (Exception e) {
            log.error("值班导入解析数据失败：", e);
            log.error("值班导入解析数据失败：{}", JsonUtils.serialize(dataSource));
            throw new ServiceException("excel数据解析失败,下载模板后请勿更改excel格式。");
        }
        this.handDuty(param);
    }

    /**
     * 手动排某人的值班
     *
     * @param yearMonth     年月
     * @param staffId       人员id
     * @param staffName     人员名称
     * @param staffDeptId   部门id
     * @param staffDeptName 部门名称
     * @param staffDateList 人员排班日期集合
     * @param oldPOList     人员原来的排班集合
     */
    public void handStaffDateList(String yearMonth, Long staffId, String staffName, Long staffDeptId, String staffDeptName,
                                  List<AttDutyDateParam> staffDateList, List<AttDutyPO> oldPOList
                                 ) {
        if (staffDateList == null) {
            return;
        }
        if (oldPOList == null) {
            oldPOList = new ArrayList<>();
        }
        Map<LocalDateTime, List<AttDutyPO>> oldDutyMap = oldPOList.stream().collect(Collectors.groupingBy(AttDutyPO::getDutyDate));
        // 需要删除的人员的排班的日期
        List<LocalDateTime> removeDateList = new ArrayList<>();
        // 需要插入的集合
        List<AttDutyPO> insertPOList = new ArrayList<>();
        StringBuilder errorBuffer = new StringBuilder();
        LocalDateTime newTime = LocalDateTime.now();
        // 操作日志集合
        List<AttendanceOperationLog> operationLogList = new ArrayList<>();
        Principal principal = SecurityContextHolder.getPrincipal();
        // 遍历入参组装插入和删除的数据
        for (int i = 0; i < staffDateList.size(); i++) {
            AttDutyDateParam dateParam = staffDateList.get(i);
            LocalDateTime day = DateUtils.parseDate(dateParam.getDutyDate());
            if (newTime.isAfter(day)) {
                throw new ServiceException("只能修改今日之后的值班");
            }
            removeDateList.add(DateUtils.parseDate(dateParam.getDutyDate()));
            List<AttDutyClassParam> classParamList = dateParam.getDateClassList();

            // 操作日志
            AttendanceOperationLog operationLog = new AttendanceOperationLog();
            operationLog.setId(IdWorker.getId());
            operationLog.setBusinessType("ATTENDANCE_MANAGER"); // 考勤
            operationLog.setOperationObj("BE_ON_DUTY"); // 值班
            operationLog.setDeleted(false);
            operationLog.setCreateTime(LocalDateTime.now());
            if (principal != null) {
                operationLog.setUserId(principal.getId());
                operationLog.setUserName(principal.getName());
                operationLog.setCreateBy(principal.toString());
            }
            String oldClassName = "休息";
            List<AttDutyPO> oldClassNameList = oldDutyMap.get(day);
            if (oldClassNameList != null) {
                StringBuilder sb = new StringBuilder();
                for (AttDutyPO dutyPO : oldClassNameList) {
                    if (StringUtils.isBlank(dutyPO.getClassesName())) {
                        continue;
                    }
                    sb.append(dutyPO.getClassesName()).append(",");
                }
                oldClassName = sb.substring(AttConstants.zeroInt, sb.length() - AttConstants.oneInt);
            }
            String newClassName = "休息";
            if (classParamList != null) {
                StringBuilder stringBuffer = new StringBuilder();
                for (AttDutyClassParam dutyParam : classParamList) {
                    if (dutyParam.getClassId() == null) {
                        newClassName = "休息";
                        break;
                    } else {
                        stringBuffer.append(dutyParam.getClassName()).append(",");
                    }
                }
                if (!stringBuffer.isEmpty()) {
                    newClassName = stringBuffer.substring(AttConstants.zeroInt, stringBuffer.length() - AttConstants.oneInt);
                }
            }
            operationLog.setOperationType("UPDATE");
            operationLog.setOperationContent(
                    "修改:" + staffName + " " + DateUtils.formatDate(day) + "值班,从【" + oldClassName + "】改为【" + newClassName + "】");
            operationLogList.add(operationLog);

            if (classParamList == null || classParamList.isEmpty()) {
                continue;
            }
            for (AttDutyClassParam classParam : classParamList) {
                AttDutyPO dutyPO = AttDutyPO.builder()
                        .id(IdWorker.getId())
                        .staffId(staffId)
                        .staffName(staffName)
                        .staffDeptId(staffDeptId)
                        .staffDeptName(staffDeptName)
                        .dutyMonth(yearMonth)
                        .dutyDate(DateUtils.parseDate(dateParam.getDutyDate()))
                        .classesId(classParam.getClassId())
                        .classesName(classParam.getClassName())
                        .classesNickName(classParam.getClassNickName())
                        .classesType(classParam.getClassType())
                        .classesRealStartTime(DateUtils.parse(
                                dateParam.getDutyDate() + " " + classParam.getStartWorkTime() + ":00", "yyyy-MM-dd HH:mm:ss"))
                        .classesRealEndTime(DateUtils.parse(
                                dateParam.getDutyDate() + " " + classParam.getEndWorkTime() + ":00", "yyyy-MM-dd HH:mm:ss"))
                        .classesEndTimeIsMorrow(classParam.getEndTimeIsMorrow())
                        .build();
                if (Boolean.TRUE.equals(classParam.getEndTimeIsMorrow())) {
                    dutyPO.setClassesRealEndTime(DateUtils.offsetDay(dutyPO.getClassesRealEndTime(), 1));
                }
                AttPoAttributeSet.initAddBasePoAttribute(dutyPO);
                insertPOList.add(dutyPO);
            }
        }
        // 班次交叉校验
        List<AttDutyPO> paramList = insertPOList.stream().filter(dutyPO -> dutyPO.getClassesRealStartTime() != null)
                .sorted(Comparator.comparing(AttDutyPO::getClassesRealStartTime)).toList();
        List<AttDutyPO> checkList = new ArrayList<>();
        if (!paramList.isEmpty()) {
            // 查询前一天
            LocalDateTime befTime = DateUtils.offsetDay(paramList.getFirst().getDutyDate(), -1);
            List<AttDutyPO> befDayList = attDutyMapper.selectList(Wrappers.<AttDutyPO>lambdaQuery()
                                                                          .eq(AttDutyPO::getStaffId, staffId)
                                                                          .eq(AttDutyPO::getDutyDate, befTime)
                                                                 );
            checkList.addAll(befDayList);
            checkList.addAll(paramList);
            // 查询后一天
            LocalDateTime afterTime = DateUtils.offsetDay(paramList.getLast().getDutyDate(), 1);
            List<AttDutyPO> afterDayList = attDutyMapper.selectList(Wrappers.<AttDutyPO>lambdaQuery()
                                                                            .eq(AttDutyPO::getStaffId, staffId)
                                                                            .eq(AttDutyPO::getDutyDate, afterTime)
                                                                   );
            checkList.addAll(afterDayList);
        }
        checkList.sort(Comparator.comparing(AttDutyPO::getClassesRealStartTime));
        for (int i = 0; i < checkList.size(); i++) {
            int nextI = i + 1;
            if (nextI >= checkList.size()) {
                break;
            }
            AttDutyPO poI = checkList.get(i);
            AttDutyPO poNext = checkList.get(nextI);
            if (!poI.getClassesRealEndTime().isBefore(poNext.getClassesRealStartTime())) {
                if (poI.getDutyDate().equals(poNext.getDutyDate())) {
                    errorBuffer.append(staffName).append(DateUtils.formatDate(poI.getDutyDate())).append("班次有交叉");
                } else {
                    errorBuffer.append(staffName).append(DateUtils.formatDate(poI.getDutyDate())).append("与")
                            .append(DateUtils.formatDate(poNext.getDutyDate())).append("的班次有交叉");
                }
            }
        }
        if (!errorBuffer.isEmpty()) {
            throw new ServiceException(errorBuffer.toString());
        }
        // 先删除原来的
        if (!removeDateList.isEmpty()) {
            attDutyRepository.removeStaffDateList(yearMonth, staffId, removeDateList);
        }
        // 插入新增的(性能优化:批量插入）
        if (!insertPOList.isEmpty()) {
            this.saveBatch(insertPOList, AttConstants.bachSize);
        }

        if (!operationLogList.isEmpty()) {
            attendanceLogServiceImpl.saveBatch(operationLogList);
        }
    }

    /**
     * 添加值班人员
     *
     * @param param 值班人员入参
     */
    public void addDutyStaff(AttDutyAddStaffParam param) {
        for (int i = 0; i < param.getStaffList().size(); i++) {
            AttDutyStaffParam dutyStaffParam = param.getStaffList().get(i);
            // 查询该月是否存在这个人
            Long count = attDutyStaffMapper.selectCount(Wrappers.<AttDutyStaffPO>lambdaQuery()
                                                                .eq(AttDutyStaffPO::getDutyMonth, param.getYearMonth())
                                                                .eq(AttDutyStaffPO::getStaffId, dutyStaffParam.getStaffId())
                                                       );
            if (count != null && count > 0) {
                continue;
            }
            AttDutyStaffPO dutyStaffPO = AttDutyStaffPO.builder().build();
            BeanUtils.copyProperties(dutyStaffParam, dutyStaffPO);
            dutyStaffPO.setDutyMonth(param.getYearMonth());
            dutyStaffPO.setStaffId(dutyStaffPO.getStaffId());
            dutyStaffPO.setStaffName(dutyStaffPO.getStaffName());
            AttPoAttributeSet.initAddBasePoAttribute(dutyStaffPO);
            attDutyStaffMapper.insert(dutyStaffPO);
        }
    }

    /**
     * 移除排班人员
     *
     * @param param 值班人员
     */
    public void removeDutyStaff(AttDutyRemoveStaffParam param) {
        attDutyStaffMapper.remove(DateUtils.toTimestamp(LocalDateTime.now()), Wrappers.<AttDutyStaffPO>lambdaQuery()
                                          .eq(AttDutyStaffPO::getDutyMonth, param.getYearMonth())
                                          .in(AttDutyStaffPO::getStaffId, param.getStaffIdList())
                                 );
    }
}
