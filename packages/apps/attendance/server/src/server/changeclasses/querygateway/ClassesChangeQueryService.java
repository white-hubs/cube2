package com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.dto.ClassesDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.query.ArrangeWorkClassesQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.commandgateway.command.ClassesChangeCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.vo.params.ChangeClassesParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.FormFieldVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.ClassesMarkEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator.ClassesValidator;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClassesChangeQueryService {
    private final ArrangeWorkMapper arrangeWorkMapper;
    private final ArrangeWorkClassesMapper arrangeWorkClassesMapper;
    private final DutyWorkMapper dutyWorkMapper;
    private final ClassesValidator classesValidator;

    private static final int dateMaxLength = 13;

    /**
     * 查询换班人
     * 2023-07-21 跨考勤组、部门换班
     * 且排班的考勤组也需要换班
     *
     * @return
     */
    public List<FormFieldVO> getWorkUsers(ChangeClassesParam param) {
        this.initParam(param);

        if (AttendanceGroupTypeEnum.DUTY.name().equals(param.getWorkType())) {
            return dutyWorkMapper.getDutyUsers(Wrappers.<DutyWorkPO>lambdaQuery().ge(DutyWorkPO::getDate, param.getDate()));
        }
        return arrangeWorkMapper.getArrangeUsers(Wrappers.<ArrangeWorkPO>lambdaQuery().ge(ArrangeWorkPO::getDate, param.getDate()));
    }

    /**
     * 查询人员班次 专用于换班申请
     * 支持 1.班休互换，2.自己和自己换
     *
     * @param param
     *
     * @return
     */
    public List<FormFieldVO> getWorkClasses(ChangeClassesParam param) {
        this.initParam(param);
        Assert.isParameterError(param.getStaffId() == null, "请选择换班人员");
        if (AttendanceGroupTypeEnum.DUTY.name().equals(param.getWorkType())) {
            return dutyWorkMapper.getChangeClasses(Wrappers.<DutyWorkPO>lambdaQuery().eq(DutyWorkPO::getStaffId, param.getStaffId())
                                                           .ge(DutyWorkPO::getDate, param.getDate()));
        }
        return arrangeWorkClassesMapper.getChangeClasses(Wrappers.<ArrangeWorkPO>lambdaQuery()
                                                                 .eq(ArrangeWorkPO::getStaffId, param.getStaffId())
                                                                 .ge(ArrangeWorkPO::getDate, param.getDate()));
    }

    /**
     * 检查交换班次是否符合要求
     * 6-29支持 班和休之间互相交换
     *
     * @param command
     *
     * @return
     */
    public boolean checkClassesChange(ClassesChangeCommand command) {
        //申请班次
        ArrangeWorkClassesPO fromClasses = new ArrangeWorkClassesPO();
        String fromClassIdString = String.valueOf(command.getFromClassesId());
        if (fromClassIdString.length() < dateMaxLength) {
            fromClasses.setStartRealTime(DateUtils.beginOfDay(DateUtils.parse(fromClassIdString, "yyyyMMdd")));
            fromClasses.setEndRealTime(DateUtils.endOfDay(DateUtils.parse(fromClassIdString, "yyyyMMdd")));
            fromClasses.setMark(ClassesMarkEnum.All_DAY_REST.name());
        } else {
            if (AttendanceGroupTypeEnum.DUTY.name().equals(command.getWorkType())) {
                fromClasses = dutyWorkMapper.getDutyClassesToArrangeClasses(command.getFromClassesId());
            } else {
                fromClasses = arrangeWorkClassesMapper.selectById(command.getFromClassesId());
            }

            Assert.isNotFound(fromClasses, "未查询到申请班次，请检查是否被变更！");
        }

        //被换的班
        ArrangeWorkClassesPO toClasses = new ArrangeWorkClassesPO();
        String toClassIdString = String.valueOf(command.getToClassesId());
        if (toClassIdString.length() < dateMaxLength) {
            toClasses.setMark(ClassesMarkEnum.All_DAY_REST.name());
            toClasses.setStartRealTime(DateUtils.beginOfDay(DateUtils.parse(toClassIdString, "yyyyMMdd")));
            toClasses.setEndRealTime(DateUtils.endOfDay(DateUtils.parse(toClassIdString, "yyyyMMdd")));
        } else {
            if (AttendanceGroupTypeEnum.DUTY.name().equals(command.getWorkType())) {
                toClasses = dutyWorkMapper.getDutyClassesToArrangeClasses(command.getToClassesId());
            } else {
                toClasses = arrangeWorkClassesMapper.selectById(command.getToClassesId());
            }
            Assert.isNotFound(toClasses, "未查询到被换班次，请检查是否被变更！");
        }
        //当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        if (fromClasses.getStartRealTime() != null && fromClasses.getMark().contains("WORK")) {
            Assert.isConflict(fromClasses.getStartRealTime().isBefore(currentTime), "申请班次的时间已经开始，不能换班");
        }

        if (toClasses.getStartRealTime() != null && toClasses.getMark().contains("WORK")) {//工作班次
            Assert.isConflict(toClasses.getStartRealTime().isBefore(currentTime), "被换班次时间已经开始，不能换班");
        }
        boolean boolRest = ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark()) &&
                           ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark());
        Assert.isConflict(boolRest, "要换班次与被换班次同为休息，无需调换");

        if (String.valueOf(command.getApplicantId()).equals(command.getChangerId().toString())) { // 自己和自己换
            // 同一天
            boolean boolDay = Objects.equals(fromClasses.getStartRealTime().getDayOfMonth(), toClasses.getStartRealTime().getDayOfMonth());
            Assert.isConflict(boolDay, "自己和自己同一天的班次，无需调换");
            // 班次时间相同
            boolean boolTime = Objects.equals(fromClasses.getStartWorkTime(), toClasses.getStartWorkTime()) &&
                               Objects.equals(fromClasses.getEndWorkTime(), toClasses.getEndWorkTime());
            Assert.isConflict(boolTime, "要换班次与被换班次相同，无需调换");
            //班换班时间冲突校验
            if (!ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark()) &&
                !ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark())) {

                ArrangeWorkClassesPO addToClasses = new ArrangeWorkClassesPO();
                BeanUtils.copyProperties(fromClasses, addToClasses);

                String addToStartRealTime =
                        DateUtils.formatDate(toClasses.getStartRealTime()) + " " + fromClasses.getStartWorkTime() + ":59";
                addToClasses.setStartRealTime(DateUtils.parse(addToStartRealTime));
                if (fromClasses.getBeforeStartRealTime() != null) {
                    long befStartTime = DateUtils.toTimestamp(fromClasses.getStartRealTime()) -
                                        DateUtils.toTimestamp(fromClasses.getBeforeStartRealTime());
                    addToClasses.setBeforeStartRealTime(DateUtils.parse(
                            DateUtils.toTimestamp(addToClasses.getStartRealTime()) - befStartTime));
                }
                if (fromClasses.getAfterStartRealTime() != null) {
                    long afterStartTime = DateUtils.toTimestamp(fromClasses.getAfterStartRealTime()) -
                                          DateUtils.toTimestamp(fromClasses.getStartRealTime());
                    addToClasses.setAfterStartRealTime(DateUtils.parse(
                            DateUtils.toTimestamp(addToClasses.getStartRealTime()) + afterStartTime));
                }
                if (fromClasses.getRestStartRealTime() != null) {
                    long restStartTime = DateUtils.toTimestamp(fromClasses.getStartRealTime()) -
                                         DateUtils.toTimestamp(fromClasses.getRestStartRealTime());
                    addToClasses.setAfterStartRealTime(DateUtils.parse(
                            DateUtils.toTimestamp(addToClasses.getStartRealTime()) - restStartTime));
                }

                String addToEndRealTime = DateUtils.format(toClasses.getEndRealTime()) + " " + fromClasses.getEndWorkTime() + ":59";
                addToClasses.setEndRealTime(DateUtils.parse(addToEndRealTime));
                if (fromClasses.getBeforeEndRealTime() != null) {
                    long befEndTime =
                            DateUtils.toTimestamp(fromClasses.getEndRealTime()) - DateUtils.toTimestamp(fromClasses.getBeforeEndRealTime());
                    addToClasses.setBeforeEndRealTime(DateUtils.parse(DateUtils.toTimestamp(addToClasses.getEndRealTime()) - befEndTime));
                }
                if (fromClasses.getAfterEndRealTime() != null) {
                    long afterEndTime =
                            DateUtils.toTimestamp(fromClasses.getAfterEndRealTime()) - DateUtils.toTimestamp(fromClasses.getEndRealTime());
                    addToClasses.setAfterEndRealTime(DateUtils.parse(DateUtils.toTimestamp(addToClasses.getEndRealTime()) + afterEndTime));
                }
                if (fromClasses.getRestEndRealTime() != null) {
                    long restEndTime =
                            DateUtils.toTimestamp(fromClasses.getEndRealTime()) - DateUtils.toTimestamp(fromClasses.getRestEndRealTime());
                    addToClasses.setAfterEndRealTime(DateUtils.parse(DateUtils.toTimestamp(addToClasses.getEndRealTime()) - restEndTime));
                }

                ArrangeWorkClassesPO addFromClasses = new ArrangeWorkClassesPO();
                BeanUtils.copyProperties(toClasses, addFromClasses);

                String addFromStartRealTime =
                        DateUtils.formatDate(fromClasses.getStartRealTime()) + " " + toClasses.getStartWorkTime() + ":00";
                addFromClasses.setStartRealTime(DateUtils.parse(addFromStartRealTime));
                if (toClasses.getBeforeStartRealTime() != null) {
                    long befStartTime =
                            DateUtils.toTimestamp(toClasses.getStartRealTime()) - DateUtils.toTimestamp(toClasses.getBeforeStartRealTime());
                    addFromClasses.setBeforeStartRealTime(DateUtils.parse(
                            DateUtils.toTimestamp(addFromClasses.getStartRealTime()) - befStartTime));
                }
                if (toClasses.getAfterStartRealTime() != null) {
                    long afterStartTime =
                            DateUtils.toTimestamp(toClasses.getAfterStartRealTime()) - DateUtils.toTimestamp(toClasses.getStartRealTime());
                    addFromClasses.setAfterStartRealTime(DateUtils.parse(
                            DateUtils.toTimestamp(addFromClasses.getStartRealTime()) + afterStartTime));
                }
                if (toClasses.getRestStartRealTime() != null) {
                    long restStartTime =
                            DateUtils.toTimestamp(toClasses.getStartRealTime()) - DateUtils.toTimestamp(toClasses.getRestStartRealTime());
                    addFromClasses.setAfterStartRealTime(DateUtils.parse(
                            DateUtils.toTimestamp(addFromClasses.getStartRealTime()) - restStartTime));
                }

                String addFromEndRealTime = DateUtils.formatDate(fromClasses.getEndRealTime()) + " " + toClasses.getEndWorkTime() + ":59";
                addFromClasses.setEndRealTime(DateUtils.parse(addFromEndRealTime));
                if (toClasses.getBeforeEndRealTime() != null) {
                    long befEndTime =
                            DateUtils.toTimestamp(toClasses.getEndRealTime()) - DateUtils.toTimestamp(toClasses.getBeforeEndRealTime());
                    addFromClasses.setBeforeEndRealTime(DateUtils.parse(
                            DateUtils.toTimestamp(addFromClasses.getEndRealTime()) - befEndTime));
                }
                if (toClasses.getAfterEndRealTime() != null) {
                    long afterEndTime =
                            DateUtils.toTimestamp(toClasses.getAfterEndRealTime()) - DateUtils.toTimestamp(toClasses.getEndRealTime());
                    addFromClasses.setAfterEndRealTime(DateUtils.parse(
                            DateUtils.toTimestamp(addFromClasses.getEndRealTime()) + afterEndTime));
                }
                if (toClasses.getRestEndRealTime() != null) {
                    long restEndTime =
                            DateUtils.toTimestamp(toClasses.getEndRealTime()) - DateUtils.toTimestamp(toClasses.getRestEndRealTime());
                    addFromClasses.setAfterEndRealTime(DateUtils.parse(
                            DateUtils.toTimestamp(addFromClasses.getEndRealTime()) - restEndTime));
                }

                // 校验交换后的时间是否已过期
                if (addFromClasses.getStartRealTime() != null && addFromClasses.getMark().contains("WORK")) {
                    Assert.isConflict(addFromClasses.getStartRealTime()
                                              .isBefore(currentTime), "交换后的班次时间已经开始，不能换班")
                    ;
                }
                if (addToClasses.getStartRealTime() != null && addToClasses.getMark().contains("WORK")) {
                    Assert.isConflict(addToClasses.getStartRealTime()
                                              .isBefore(currentTime), "交换后的班次时间已经开始，不能换班")
                    ;
                }
                ClassesDTO addFromClassesDto = new ClassesDTO();
                BeanUtils.copyProperties(addFromClasses, addFromClassesDto);
                ClassesDTO addToClassesDto = new ClassesDTO();
                BeanUtils.copyProperties(addToClasses, addToClassesDto);

                //发起人 在申请班次当天,的所有班次(因为是自己和自己换，所以是自己发起那天的班次信息)
                List<ClassesDTO> appClassesList;
                if (AttendanceGroupTypeEnum.DUTY.name().equals(command.getWorkType())) {
                    appClassesList = dutyWorkMapper.getDutyClassesListToArrange(Wrappers.query().eq("dw.staff_id", command.getChangerId())
                                                                                        .eq("dw.date", DateUtils.format(fromClasses.getStartRealTime(), "yyyy-MM-dd")));
                } else {
                    appClassesList = arrangeWorkClassesMapper.getArrangeWorkClasses(ArrangeWorkClassesQueryParam.builder()
                                                                                            .userId(command.getChangerId())
                                                                                            .date(DateUtils.format(fromClasses.getStartRealTime(), "yyyy-MM-dd"))
                                                                                            .build());
                }
                //移除减少的班次
                List<ClassesDTO> newAppClassesList = appClassesList.stream()
                        .filter(c -> c.getId().longValue() != command.getFromClassesId().longValue()).collect(Collectors.toList());
                // 添加新增的班次
                newAppClassesList.add(addFromClassesDto);

                //被换人 在被交换班次当天,的所有班次(因为是自己和自己换，所以是自己被换那天的班次信息)
                List<ClassesDTO> changeClassesList;
                if (AttendanceGroupTypeEnum.DUTY.name().equals(command.getWorkType())) {
                    changeClassesList = dutyWorkMapper.getDutyClassesListToArrange(Wrappers.query()
                                                                                           .eq("dw.staff_id", command.getChangerId())
                                                                                           .eq("dw.date", DateUtils.format(toClasses.getStartRealTime(), "yyyy-MM-dd")));
                } else {
                    changeClassesList = arrangeWorkClassesMapper.getArrangeWorkClasses(ArrangeWorkClassesQueryParam.builder()
                                                                                               .userId(command.getChangerId())
                                                                                               .date(DateUtils.format(toClasses.getStartRealTime(), "yyyy-MM-dd"))
                                                                                               .build());
                }
                //移除减少的班次
                List<ClassesDTO> newChangeClassesList = changeClassesList.stream()
                        .filter(c -> c.getId().longValue() != command.getToClassesId().longValue()).collect(Collectors.toList());
                // 添加新的班次
                newChangeClassesList.add(addToClassesDto);
                // 校验班次是否冲突
                boolean flagChange = classesValidator.anyClassesNoCross(ObjectUtils.copying(newAppClassesList, Classes.class));
                boolean flagChanged = classesValidator.anyClassesNoCross(ObjectUtils.copying(newChangeClassesList, Classes.class));
                Assert.isConflict(!flagChanged || !flagChange, "班次调换造成时间冲突，不能换班");
            } // 班换休和休换班 可以直接调换不需要校验

        } else { // 自己和别人换
            // 申请的班次信息
            ClassesDTO fromClassesDTO = ObjectUtils.copying(fromClasses, ClassesDTO.class);
            // 被换班次信息
            ClassesDTO toClassesDTO = ObjectUtils.copying(toClasses, ClassesDTO.class);

            //申请人 在被交换班次当天,的所有班次
            List<ClassesDTO> changeClassesList;

            if (AttendanceGroupTypeEnum.DUTY.name().equals(command.getWorkType())) {
                changeClassesList = dutyWorkMapper.getDutyClassesListToArrange(Wrappers.query().eq("dw.staff_id", command.getApplicantId())
                                                                                       .eq("dw.date", DateUtils.format(toClasses.getStartRealTime(), "yyyy-MM-dd")));
            } else {
                changeClassesList = arrangeWorkClassesMapper.getArrangeWorkClasses(ArrangeWorkClassesQueryParam.builder()
                                                                                           .userId(command.getApplicantId())
                                                                                           .date(DateUtils.format(toClasses.getStartRealTime(), "yyyy-MM-dd"))
                                                                                           .build());
            }

            //被换人 在申请班次当天,的所有班次
            List<ClassesDTO> changedClassesList;
            if (AttendanceGroupTypeEnum.DUTY.name().equals(command.getWorkType())) {
                changedClassesList = dutyWorkMapper.getDutyClassesListToArrange(Wrappers.query().eq("dw.staff_id", command.getChangerId())
                                                                                        .eq("dw.date", DateUtils.format(fromClasses.getStartRealTime(), "yyyy-MM-dd")));
            } else {
                changedClassesList = arrangeWorkClassesMapper.getArrangeWorkClasses(ArrangeWorkClassesQueryParam.builder()
                                                                                            .userId(command.getChangerId())
                                                                                            .date(DateUtils.format(fromClasses.getStartRealTime(), "yyyy-MM-dd"))
                                                                                            .build());
            }

            // 移除减少的班次
            changeClassesList = changeClassesList.stream()
                    .filter(c -> c.getId().longValue() != command.getFromClassesId().longValue()).collect(Collectors.toList());
            changedClassesList = changedClassesList.stream()
                    .filter(c -> c.getId().longValue() != command.getToClassesId().longValue()).collect(Collectors.toList());

            //加入增加的班次
            if (!ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark())) {
                changeClassesList.add(toClassesDTO);
            }
            if (!ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark())) {
                changedClassesList.add(fromClassesDTO);
            }
            // 校验班次是否冲突
            boolean flagChange = classesValidator.anyClassesNoCross(ObjectUtils.copying(changeClassesList, Classes.class));
            boolean flagChanged = classesValidator.anyClassesNoCross(ObjectUtils.copying(changedClassesList, Classes.class));
            Assert.isConflict(!flagChanged || !flagChange, "班次调换造成时间冲突，不能换班");
        }

        return true;
    }

    private void initParam(ChangeClassesParam param) {
        Assert.isParameterError((param == null || param.getWorkType() == null), "请选择换班类型");
        param.setStaffId(param.getUserId());
        if (param.getDate() == null) {
            param.setDate(DateUtils.beginOfDay(LocalDateTime.now()));
        }
    }
}
