package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.ArrangeWorkClassesCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.ArrangeWorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.DutyWorkClassesCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.DutyWorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.WorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.command.SubmitWorkCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.command.WorkDateCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.HolidayQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.tool.StatutoryHolidayResponse;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupClassesCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupStaffCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.ClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.AttendanceGroupQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.ClassesQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator.ClassesValidator;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.StaffQuery;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/16
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkCommandServiceImpl implements WorkCommandService {

    private final ArrangeWorkClassesCommandService arrangeWorkClassesCommandService;
    private final DutyWorkClassesCommandService dutyWorkClassesCommandService;
    private final DutyWorkCommandService dutyWorkCommandService;
    private final HolidayQueryService holidayQueryService;
    private final ArrangeWorkCommandService arrangeWorkCommandService;
    private final StaffClient staffClient;
    private final AttendanceGroupQueryService groupQueryService;
    private final AttendanceGroupStaffMapper attendanceGroupStaffMapper;
    private final ClassesQueryService classesQueryService;
    private final ClassesValidator classesValidator;

    @Override
    public void generateWorkData(SubmitWorkCommand command) {
        this.saveData(command);
    }

    @Override
    public void removeWorkData(List<Long> removeStaffIds, String type) {
        if (CollectionUtils.isEmpty(removeStaffIds)) {
            return;
        }
        // 获取今日之后的到月末的时间数据
        List<WorkDateCommand> workDateList = this.getDateToMonthLastDay(null, false);
        workDateList.forEach(date -> removeStaffIds.forEach(staffId -> {
            // 不删除当前班次及之前的班次，后续的班次删除
            // 排班的走排班表；值班的走值班表
            if (AttendanceGroupTypeEnum.ARRANGE.name().equals(type)) {
                arrangeWorkCommandService.removeData(date.getDate(), staffId);
            } else {
                dutyWorkCommandService.removeData(date.getDate(), staffId);
            }
        }));
        // 下月之后的数据直接删除
        LocalDateTime dateTime = DateUtils.endOfMonth(LocalDateTime.now());
        if (AttendanceGroupTypeEnum.ARRANGE.name().equals(type)) {
            arrangeWorkCommandService.remove(Wrappers.<ArrangeWorkPO>lambdaQuery().gt(ArrangeWorkPO::getDate, dateTime)
                                                     .in(ArrangeWorkPO::getStaffId, removeStaffIds));
        } else {
            dutyWorkCommandService.remove(Wrappers.<DutyWorkPO>lambdaQuery().gt(DutyWorkPO::getDate, dateTime)
                                                  .in(DutyWorkPO::getStaffId, removeStaffIds));
        }
    }

    @Override
    public void importTemplate(String month, MultipartFile file, String type) {
        Assert.isParameterError(StringUtils.isEmpty(month), "必须选择月份");
        Assert.isParameterError(file.isEmpty(), "必须上传excel文件");
        Assert.isNotAcceptable(
                DateUtils.compare(DateUtils.parseDate(month + "-01"), DateUtils.beginOfMonth(LocalDateTime.now())) < 0,
                "历史月份不能再进行排班操作");
        List<List<Object>> datas;
        try {
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            datas = new ArrayList<>(reader.read(2));
        } catch (Exception e) {
            log.error("上传排班模板解析失败", e);
            throw new ServiceException("excel文件解析失败");
        }
        try {
            datas.forEach(data -> {
                SubmitAttendanceGroupStaffCommand command = new SubmitAttendanceGroupStaffCommand();
                // 获取员工所在考勤组
                Object groupName = data.get(3);
                AttendanceGroupPO groupPO = groupQueryService.lambdaQuery().eq(AttendanceGroupPO::getName, groupName).one();
                // 获取员工联系电话
                Object mobile = data.get(1);
                AttendanceGroupStaffPO attendanceGroupStaffPO = attendanceGroupStaffMapper.selectOne(Wrappers
                                                                                                             .<AttendanceGroupStaffPO>lambdaQuery()
                                                                                                             .eq(AttendanceGroupStaffPO::getGroupId, groupPO.getId())
                                                                                                             .eq(AttendanceGroupStaffPO::getStaffMobile, mobile));
                if (attendanceGroupStaffPO == null) {
                    StaffDetailDTO byMobile = staffClient.getByMobile((String) mobile);
                    ObjectUtils.copying(byMobile, command);
                } else {
                    ObjectUtils.copying(attendanceGroupStaffPO, command);
                }
                // 解析班次信息并排班
                this.parseClassesToArrange(data, groupPO.getId(), command, month, type);
            });
        } catch (Exception e) {
            // 产品定义：目前不做详细错误提示，只要有错就全部导入失败，2023-01-04
            log.error("上传排班模板校验失败", e);
            throw new ServiceException("导入排班失败，请检查班次是否有交叉或下载最新模板重试");
        }
    }

    private void parseClassesToArrange(List<Object> data, Long groupId, SubmitAttendanceGroupStaffCommand staffCommand, String month,
                                       String type
                                      ) {
        LocalDateTime date = DateUtils.parseDate(month + "-01");
        List<Classes> tomorrowClassesList = new ArrayList<>();
        List<Classes> theDayClassesList = new ArrayList<>();
        int startIndex = 4;
        for (int i = 4; i < data.size(); i++) {
            // 今天之前时间的不排班
            int compare = DateUtils.compare(date, DateUtils.beginOfDay(LocalDateTime.now()));
            if (compare < 0) {
                date = DateUtils.offsetDay(date, 1);
                startIndex++;
                continue;
            }
            // 根据班次名称查询班次信息
            if (i == startIndex) {
                theDayClassesList = this.getClassesBySummaryName((String) data.get(i));
            }
            if (i != data.size() - 1) {
                tomorrowClassesList = this.getClassesBySummaryName((String) data.get(i + 1));
            }
            // 校验当天的班次是否有冲突，目前不会有，因为现在每天是单选班次
            Assert.isNotAcceptable(!classesValidator.anyClassesNoCross(theDayClassesList),
                                   DateUtils.formatDate(date) + "的班次时间有交叉");
            // 取当前日期和后一天的班次进行比较是否有班次时间冲突
            boolean noCross = classesValidator.anyClassesNoCross(theDayClassesList, tomorrowClassesList);
            Assert.isNotAcceptable(!noCross, DateUtils.formatDate(date) + "与" + DateUtils.formatDate(DateUtils.offsetDay(date, 1)) +
                                             "班次时间有交叉");
            WorkDateCommand dateCommand = WorkDateCommand.builder().month(month).date(date).week(date.getDayOfWeek().getValue())
                    .isHolidayAndAutoRest(false).build();
            if (AttendanceGroupTypeEnum.ARRANGE.name().equals(type)) {
                arrangeWorkCommandService.saveOrUpdateOneStaffOneDay(groupId, dateCommand, staffCommand,
                                                                     arrangeWorkClassesCommandService.saveClassesByOneDay(DateUtils.format(date, "yyyy-MM-dd"), theDayClassesList), false);
            } else if (AttendanceGroupTypeEnum.DUTY.name().equals(type)) {
                dutyWorkCommandService.saveOrUpdateOneStaffOneDay(groupId, dateCommand, staffCommand, dutyWorkClassesCommandService.saveClassesByOneDay(theDayClassesList), false);
            }
            date = DateUtils.offsetDay(date, 1);
            theDayClassesList = tomorrowClassesList;
        }
    }

    /**
     * 通过班次聚合名称查询班次
     *
     * @param classesSummaryName 聚合名称 如：默认班次:09:00-18:00
     *
     * @return 班次集合
     */
    private List<Classes> getClassesBySummaryName(String classesSummaryName) {
        if (StringUtils.isEmpty(classesSummaryName) || "休息".equals(classesSummaryName)) {
            return Collections.emptyList();
        }
        String[] split = classesSummaryName.split(":");
        String classesName = split[0];
        List<ClassesPO> classesPOS = classesQueryService.lambdaQuery().eq(ClassesPO::getName, classesName).list();
        return ObjectUtils.copying(classesPOS, Classes.class);
    }

    private void saveData(SubmitWorkCommand command) {
        // 考勤组类型：排班还是值班
        String type = command.getType();
        List<SubmitAttendanceGroupStaffCommand> staffPOS = new ArrayList<>();
        // 2、查询排班用户
        if (AttendanceGroupRangesEnum.All_STAFF.name().equals(command.getRanges())) {
            // 所有员工
            // 查询所有员工的接口
            staffPOS.addAll(ObjectUtils.copying(staffClient.getList(new StaffQuery()), SubmitAttendanceGroupStaffCommand.class));
        } else {
            staffPOS.addAll(command.getStaffPOS());
        }
        // 3、获取今日之后的到月末的时间数据
        List<WorkDateCommand> workDateList = this.getDateToMonthLastDay(command.getBeginDate(), command.getAutoRest());
        workDateList.forEach(date -> {
            List<String> workClassIds = this.saveEveryDayClasses(type, command.getClassesType(), date, command.getClassesPOS());
            staffPOS.forEach(staff -> {
                // 不影响当前班次及之前的班次，后续的班次更改
                // 排班的走排班表；值班的走值班表
                if (AttendanceGroupTypeEnum.ARRANGE.name().equals(type)) {
                    arrangeWorkCommandService.saveOrUpdateOneStaffOneDay(command.getGroupId(), date, staff, workClassIds, false);
                } else {
                    dutyWorkCommandService.saveOrUpdateOneStaffOneDay(command.getGroupId(), date, staff, workClassIds, false);
                }
            });
        });
    }

    /**
     * 保存每天的班次信息
     *
     * @param type            考勤组类型
     * @param classesType     考勤班次类型
     * @param command         时间
     * @param classesCommands 班次
     *
     * @return 保存后的班次关联表IDS
     */
    private List<String> saveEveryDayClasses(String type, String classesType, WorkDateCommand command,
                                             List<SubmitAttendanceGroupClassesCommand> classesCommands
                                            ) {
        List<String> arrangeClassIds = new ArrayList<>();
        if (AttendanceTypeEnum.ARRANGE.name().equals(classesType)) {
            return arrangeClassIds;
        }
        String date = DateUtils.format(command.getDate(), "yyyy-MM-dd");
        // 如果是节假日自动排休，则将当日的班次排为休息，目前不保存休息班次
        if (command.getIsHolidayAndAutoRest() == null || !command.getIsHolidayAndAutoRest()) {
            // 找到对应周几的班次
            SubmitAttendanceGroupClassesCommand classesCommand = classesCommands.stream()
                    .filter(cla -> cla.getWeek().equals(command.getWeek())).findAny().orElse(null);
            if (null == classesCommand) {
                return arrangeClassIds;
            }
            List<Classes> classes = classesCommand.getClasses();
            // 排班
            if (AttendanceGroupTypeEnum.ARRANGE.name().equals(type)) {
                arrangeClassIds.addAll(arrangeWorkClassesCommandService.saveClassesByOneDay(date, classes));
            } else {
                // 值班
                arrangeClassIds.addAll(dutyWorkClassesCommandService.saveClassesByOneDay(classes));
            }

        }
        return arrangeClassIds;
    }

    /**
     * 获取到最后月末最后一天的时间
     *
     * @param currentDate 开始时间，如果为空取当前时间
     * @param autoRest    节假日是否自动排休
     *
     * @return 日期列表
     */
    private List<WorkDateCommand> getDateToMonthLastDay(LocalDateTime currentDate, Boolean autoRest) {
        List<WorkDateCommand> pos = new ArrayList<>();
        if (null == currentDate) {
            currentDate = LocalDateTime.now();
        }
        String yearMonth = DateUtils.format(currentDate, "yyyy-MM");
        LocalDateTime dateTime = DateUtils.endOfMonth(currentDate);
        int intervalByDays = Integer.parseInt(String.valueOf(DateUtils.between(currentDate, dateTime, ChronoUnit.DAYS, true)));
        // 如果指定节假日自动排班，查询当前时间段内的节假日
        List<StatutoryHolidayResponse> holidayResponses = new ArrayList<>();
        if (Boolean.TRUE.equals(autoRest)) {
            Period period = new Period(currentDate, DateUtils.offsetDay(currentDate, intervalByDays));
            holidayResponses.addAll(holidayQueryService.getStatutoryHolidaysByPeriod(period));
        }
        for (int i = 0; i <= intervalByDays; i++) {
            WorkDateCommand po = new WorkDateCommand();
            LocalDateTime date = DateUtils.offsetDay(currentDate, i);
            int week = date.getDayOfWeek().getValue();
            po.setMonth(yearMonth);
            po.setWeek(week);
            po.setDate(date);
            if (Boolean.TRUE.equals(autoRest) && !CollectionUtils.isEmpty(holidayResponses)) {
                boolean isHoliday = holidayResponses.stream().anyMatch(holiday -> DateUtils.formatDate(date).equals(holiday.getDate()) &&
                                                                                  StatutoryHolidayResponse.Status.DAY_OFF.equals(holiday.getStatus()));
                po.setIsHolidayAndAutoRest(isHoliday);
            }
            pos.add(po);
        }
        return pos;
    }
}
