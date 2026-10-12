package com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.dto.ClassesDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.commandgateway.command.ClassesChangeCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.dao.ClassesChangeSituationMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.po.ClassesChangeSituationPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.querygateway.ClassesChangeQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.ClassesMarkEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.AttendanceGroupQueryService;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author appet
 * @date 2022/11/21
 * @description 加班明细查询
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClassesChangeSituationCommandService extends ServiceImpl<ClassesChangeSituationMapper, ClassesChangeSituationPO> {
    private final ArrangeWorkClassesMapper arrangeWorkClassesMapper;
    private final ArrangeWorkMapper arrangeWorkMapper;
    private final AttendanceGroupQueryService attendanceGroupQueryService;
    private final StaffClient staffClient;
    private final ClassesChangeQueryService classesChangeQueryService;
    private final DutyWorkMapper dutyWorkMapper;
    private final DutyWorkClassesMapper dutyWorkClassesMapper;

    private static final int dateMaxLength = 13;

    /**
     * 换班
     */
    @Transactional(rollbackFor = Exception.class)
    public void addClassesChangeSituationAndChangeClasses(ClassesChangeCommand command) {
        // 检查班次是否符合要求
        classesChangeQueryService.checkClassesChange(command);
        // 调换班次
        if (AttendanceGroupTypeEnum.DUTY.name().equals(command.getWorkType())) {
            this.dutyClassesChange(command);
        } else {
            this.arrangeClassesChange(command);
            //添加换班记录
            this.addClassesChangeSituation(command);
        }
    }

    /**
     * 排班换班
     * 2323-06-29支持班休互相转换
     */
    @Transactional(rollbackFor = Exception.class)
    public void arrangeClassesChange(ClassesChangeCommand command) {
        String fromClassIdString = String.valueOf(command.getFromClassesId());
        String toClassIdString = String.valueOf(command.getToClassesId());

        //申请班次
        ArrangeWorkClassesPO fromClasses = new ArrangeWorkClassesPO();
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

        // 查询当天的排班
        ArrangeWorkPO fromWork = arrangeWorkMapper.selectOne(Wrappers.<ArrangeWorkPO>lambdaQuery()
                                                                     .eq(ArrangeWorkPO::getDate, DateUtils.format(fromClasses.getStartRealTime(), "yyyy-MM-dd"))
                                                                     .eq(ArrangeWorkPO::getStaffId, command.getApplicantId()));
        Assert.isNotAcceptable(fromWork == null, "班次信息发生变化，无法换班，请确认排班信息");

        ArrangeWorkPO toWork = arrangeWorkMapper.selectOne(Wrappers.<ArrangeWorkPO>lambdaQuery()
                                                                   .eq(ArrangeWorkPO::getDate, DateUtils.format(toClasses.getStartRealTime(), "yyyy-MM-dd"))
                                                                   .eq(ArrangeWorkPO::getStaffId, command.getChangerId())
                                                          );
        Assert.isNotAcceptable(toWork == null, "班次信息发生变化，无法换班，请确认排班信息");

        // 双方都去掉自己 被交换的班次
        if (StringUtils.isNotBlank(fromWork.getArrangeWorkClassesIds())) {
            fromWork.setArrangeWorkClassesIds(Arrays.stream(fromWork.getArrangeWorkClassesIds().split(","))
                                                      .filter(id -> !id.equals(command.getFromClassesId().toString()))
                                                      .collect(Collectors.joining(",")));
            if (!command.getApplicantId().equals(command.getChangerId())) {
                //更新保存 去掉了要交换的班次
                arrangeWorkMapper.updateById(fromWork);
            }
        }

        if (StringUtils.isNotBlank(toWork.getArrangeWorkClassesIds())) {
            toWork.setArrangeWorkClassesIds(Arrays.stream(toWork.getArrangeWorkClassesIds().split(","))
                                                    .filter(id -> !id.equals(command.getToClassesId().toString()))
                                                    .collect(Collectors.joining(",")));
            if (!command.getApplicantId().equals(command.getChangerId())) {
                //更新保存 去掉了要交换的班次
                arrangeWorkMapper.updateById(toWork);
            }
        }

        if (command.getApplicantId().equals(command.getChangerId())) { //自己和自己换
            if (!ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark())) {

                ArrangeWorkClassesPO addToClasses = new ArrangeWorkClassesPO();
                BeanUtils.copyProperties(fromClasses, addToClasses);

                String addToStartRealTime =
                        DateUtils.formatDate(toClasses.getStartRealTime()) + " " + fromClasses.getStartWorkTime() + ":00";
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

                String addToEndRealTime = DateUtils.formatDate(toClasses.getEndRealTime()) + " " + fromClasses.getEndWorkTime() + ":59";
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
                arrangeWorkClassesMapper.updateById(addToClasses);
            }

            if (!ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark())) {

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
                arrangeWorkClassesMapper.updateById(addFromClasses);
            }

            if (ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark()) &&
                !ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark())) {
                // 休换班
                fromWork.setArrangeWorkClassesIds(toClassIdString);
            } else if (!ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark()) &&
                       !ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark())) {
                // 班换班
                toWork.setArrangeWorkClassesIds(StringUtils.isNotBlank(toWork.getArrangeWorkClassesIds())
                                                ? toWork.getArrangeWorkClassesIds() + "," + command.getFromClassesId()
                                                : command.getFromClassesId().toString());

                fromWork.setArrangeWorkClassesIds(StringUtils.isNotBlank(fromWork.getArrangeWorkClassesIds())
                                                  ? fromWork.getArrangeWorkClassesIds() + "," + command.getToClassesId()
                                                  : command.getToClassesId().toString());
            } else if (!ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark()) &&
                       ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark())) {
                // 班换休
                toWork.setArrangeWorkClassesIds(fromClassIdString);
            }
            //更新班次
            arrangeWorkMapper.updateById(toWork);
            arrangeWorkMapper.updateById(fromWork);

        } else {
            // 都将对方的班次 添加到自己对应日期中的排班中。例如我用1号的班 换他2号的班。就将他2号的班添加到我2号的排班中，并且将我一号的班添加到他1号的排班中
            ArrangeWorkPO appWorkPO = arrangeWorkMapper.selectOne(Wrappers.<ArrangeWorkPO>lambdaQuery()
                                                                          .eq(ArrangeWorkPO::getDate, DateUtils.format(toClasses.getStartRealTime(), "yyyy-MM-dd"))
                                                                          .eq(ArrangeWorkPO::getStaffId, command.getApplicantId()));

            ArrangeWorkPO changeWorkPO = arrangeWorkMapper.selectOne(Wrappers.<ArrangeWorkPO>lambdaQuery()
                                                                             .eq(ArrangeWorkPO::getDate, DateUtils.format(fromClasses.getStartRealTime(), "yyyy-MM-dd"))
                                                                             .eq(ArrangeWorkPO::getStaffId, command.getChangerId()));

            // 换班
            changeWorkPO.setArrangeWorkClassesIds(StringUtils.isNotBlank(changeWorkPO.getArrangeWorkClassesIds())
                                                  ? changeWorkPO.getArrangeWorkClassesIds() +
                                                    (ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark()) ? "" : "," +
                                                                                                                              command.getFromClassesId())
                                                  : (ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark()) ? "" : command
                                                          .getFromClassesId().toString()));

            appWorkPO.setArrangeWorkClassesIds(StringUtils.isNotBlank(appWorkPO.getArrangeWorkClassesIds())
                                               ? appWorkPO.getArrangeWorkClassesIds()
                                                 + (ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark()) ? "" : "," +
                                                                                                                           command.getToClassesId())
                                               : (ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark()) ? ""
                                                                                                                  : command.getToClassesId()
                                                          .toString()));

            //更新班次
            arrangeWorkMapper.updateById(appWorkPO);
            arrangeWorkMapper.updateById(changeWorkPO);
        }
    }

    /**
     * 值班换班
     * 2323-07-26新增值班换班逻辑
     */
    @Transactional(rollbackFor = Exception.class)
    public void dutyClassesChange(ClassesChangeCommand command) {
        String fromClassIdString = String.valueOf(command.getFromClassesId());
        String toClassIdString = String.valueOf(command.getToClassesId());

        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;

        //申请班次
        DutyWorkClassesPO fromClasses = new DutyWorkClassesPO();
        if (fromClassIdString.length() < dateMaxLength) {
            fromClasses.setMark(ClassesMarkEnum.All_DAY_REST.name());
            fromClasses.setStartWorkTime("00:00");
            fromClasses.setEndWorkTime("23:59");
            fromDate = DateUtils.parse(String.valueOf(command.getFromClassesId()), "yyyyMMdd");
        } else {
            fromClasses = dutyWorkClassesMapper.selectById(command.getFromClassesId());
            List<ClassesDTO> fromDateDTOlist = dutyWorkMapper.getDutyClassesListToArrange(Wrappers.query()
                                                                                                  .eq("dwc.id", command.getFromClassesId()));
            if (fromDateDTOlist != null) {
                fromDate = DateUtils.parseDate(fromDateDTOlist.getFirst().getDate());
            }
            Assert.isNotFound(fromClasses, "未查询到申请班次，请检查是否被变更！");
        }

        //被换的班
        DutyWorkClassesPO toClasses = new DutyWorkClassesPO();
        if (toClassIdString.length() < dateMaxLength) {
            toClasses.setMark(ClassesMarkEnum.All_DAY_REST.name());
            toDate = DateUtils.parse(String.valueOf(command.getToClassesId()), "yyyyMMdd");
            toClasses.setStartWorkTime("00:00");
            toClasses.setEndWorkTime("23:59");
        } else {
            toClasses = dutyWorkClassesMapper.selectById(command.getToClassesId());
            List<ClassesDTO> toDateDTOlist = dutyWorkMapper.getDutyClassesListToArrange(Wrappers.query()
                                                                                                .eq("dwc.id", command.getToClassesId()));
            if (toDateDTOlist != null) {
                toDate = DateUtils.parseDate(toDateDTOlist.getFirst().getDate());
            }
            Assert.isNotFound(toClasses, "未查询到被换班次，请检查是否被变更！");
        }

        // 查询当天的排班
        DutyWorkPO fromWork = dutyWorkMapper.selectOne(Wrappers.<DutyWorkPO>lambdaQuery()
                                                               .eq(DutyWorkPO::getDate, fromDate)
                                                               .eq(DutyWorkPO::getStaffId, command.getApplicantId()));
        Assert.isNotAcceptable(fromWork == null, "班次信息发生变化，无法换班，请确认排班信息");

        DutyWorkPO toWork = dutyWorkMapper.selectOne(Wrappers.<DutyWorkPO>lambdaQuery()
                                                             .eq(DutyWorkPO::getDate, toDate)
                                                             .eq(DutyWorkPO::getStaffId, command.getChangerId())
                                                    );
        Assert.isNotAcceptable(toWork == null, "班次信息发生变化，无法换班，请确认排班信息");

        // 双方都去掉自己 被交换的班次
        if (StringUtils.isNotBlank(fromWork.getDutyWorkClassesIds())) {
            fromWork.setDutyWorkClassesIds(Arrays.stream(fromWork.getDutyWorkClassesIds().split(","))
                                                   .filter(id -> !id.equals(command.getFromClassesId().toString()))
                                                   .collect(Collectors.joining(",")));
            if (!command.getApplicantId().equals(command.getChangerId())) {
                //更新保存 去掉了要交换的班次
                dutyWorkMapper.updateById(fromWork);
            }
        }

        if (StringUtils.isNotBlank(toWork.getDutyWorkClassesIds())) {
            toWork.setDutyWorkClassesIds(Arrays.stream(toWork.getDutyWorkClassesIds().split(","))
                                                 .filter(id -> !id.equals(command.getToClassesId().toString()))
                                                 .collect(Collectors.joining(",")));
            if (!command.getApplicantId().equals(command.getChangerId())) {
                //更新保存 去掉了要交换的班次
                dutyWorkMapper.updateById(toWork);
            }
        }

        if (command.getApplicantId().equals(command.getChangerId())) { //自己和自己换

            if (ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark()) &&
                !ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark())) {
                // 休换班
                fromWork.setDutyWorkClassesIds(toClassIdString);
            } else if (!ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark()) &&
                       !ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark())) {
                // 班换班
                toWork.setDutyWorkClassesIds(StringUtils.isNotBlank(toWork.getDutyWorkClassesIds())
                                             ? toWork.getDutyWorkClassesIds() + "," + command.getFromClassesId()
                                             : command.getFromClassesId().toString());

                fromWork.setDutyWorkClassesIds(StringUtils.isNotBlank(fromWork.getDutyWorkClassesIds())
                                               ? fromWork.getDutyWorkClassesIds() + "," + command.getToClassesId()
                                               : command.getToClassesId().toString());
            } else if (!ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark()) &&
                       ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark())) {
                // 班换休
                toWork.setDutyWorkClassesIds(fromClassIdString);
            }
            //更新班次
            dutyWorkMapper.updateById(toWork);
            dutyWorkMapper.updateById(fromWork);

        } else {
            // 获取各自原本的班次(上一步已经)
            DutyWorkPO appWorkPO = dutyWorkMapper.selectOne(Wrappers.<DutyWorkPO>lambdaQuery()
                                                                    .eq(DutyWorkPO::getDate, toDate)
                                                                    .eq(DutyWorkPO::getStaffId, command.getApplicantId()));

            DutyWorkPO changeWorkPO = dutyWorkMapper.selectOne(Wrappers.<DutyWorkPO>lambdaQuery()
                                                                       .eq(DutyWorkPO::getDate, fromDate)
                                                                       .eq(DutyWorkPO::getStaffId, command.getChangerId()));

            // 换班， 都将对方的班次 添加到自己对应日期中的排班中。例如我用1号的班 换他2号的班。就将他2号的班添加到我2号的排班中，并且将我一号的班添加到他1号的排班中
            changeWorkPO.setDutyWorkClassesIds(StringUtils.isNotBlank(changeWorkPO.getDutyWorkClassesIds())
                                               ? changeWorkPO.getDutyWorkClassesIds() +
                                                 (ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark()) ? "" : "," +
                                                                                                                           command.getFromClassesId())
                                               : (ClassesMarkEnum.All_DAY_REST.name().equals(fromClasses.getMark()) ? "" : command
                                                       .getFromClassesId().toString()));

            appWorkPO.setDutyWorkClassesIds(StringUtils.isNotBlank(appWorkPO.getDutyWorkClassesIds())
                                            ? appWorkPO.getDutyWorkClassesIds()
                                              + (ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark()) ? "" : "," +
                                                                                                                        command.getToClassesId())
                                            : (ClassesMarkEnum.All_DAY_REST.name().equals(toClasses.getMark()) ? ""
                                                                                                               : command.getToClassesId()
                                                       .toString()));

            //更新班次
            dutyWorkMapper.updateById(appWorkPO);
            dutyWorkMapper.updateById(changeWorkPO);
        }
    }

    /**
     * 添加换班记录
     *
     * @param command
     */
    @Transactional(rollbackFor = Exception.class)
    public void addClassesChangeSituation(ClassesChangeCommand command) {
        ClassesChangeSituationPO cc = ObjectUtils.copying(command, ClassesChangeSituationPO.class);
        //填充申请人数据
        StaffDetailDTO applicant = staffClient.getById(cc.getApplicantId());
        if (applicant != null) {
            cc.setApplicantName(applicant.getName());
            cc.setApplicantDeptId(applicant.getOrganizationId());
        }
        //填充被换人数据
        StaffDetailDTO changer = staffClient.getById(cc.getChangerId());
        if (changer != null) {
            cc.setChangerName(changer.getName());
        }
        //获取考情组
        AttendanceGroupPO myAttendanceGroup = attendanceGroupQueryService.getMyAttendanceGroup(cc.getApplicantId());
        cc.setAttendanceGroupId(myAttendanceGroup != null ? myAttendanceGroup.getId() : null);
        //插入数据
        this.baseMapper.insert(cc);
    }

}
