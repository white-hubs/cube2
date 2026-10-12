package com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupPunchCardWayMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupSupplementCardRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupWorkOvertimeRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.ClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupPunchCardWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.ClassesMarkEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WeekEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.AttendanceGroupDefaultWeekClassesVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.AttendanceGroupPunchCardWayVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.AttendanceGroupRepairClockRuleVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.AttendanceGroupStaffVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.AttendanceGroupVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.AttendanceGroupWorkOvertimeRuleVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPunchCardWayPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupSupplementCardRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupWorkOvertimeRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.ClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.query.AttendanceGroupQueryParam;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.StaffQuery;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤组查询
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendanceGroupQueryService extends ServiceImpl<AttendanceGroupMapper, AttendanceGroupPO> {

    private static final String ATTENDANCE_GROUP_TYPE = "attendanceGroupType";
    private static final String ATTENDANCE_TYPE = "attendanceType";

    private final AttendanceGroupMapper attendanceGroupMapper;
    private final AttendanceGroupStaffMapper attendanceGroupStaffMapper;
    private final AttendanceGroupClassesMapper attendanceGroupClassesMapper;
    private final AttendanceGroupPunchCardWayMapper attendanceGroupPunchCardWayMapper;
    private final AttendanceGroupWorkOvertimeRuleMapper attendanceGroupWorkOvertimeRuleMapper;
    private final AttendanceGroupSupplementCardRuleMapper attendanceGroupSupplementCardRuleMapper;
    private final ClassesMapper classesMapper;
    private final StaffClient staffClient;
    private final ArrangeWorkMapper arrangeWorkMapper;
    private final DutyWorkMapper dutyWorkMapper;

    /**
     * 根据ID查询打卡方式
     *
     * @param id 考勤组id
     *
     * @return 考勤组
     */
    public AttendanceGroupVO getPunchCardWayById(Long id) {
        AttendanceGroupPO groupPO = attendanceGroupMapper.selectById(id);
        AttendanceGroupVO groupVO = ObjectUtils.copying(groupPO, AttendanceGroupVO.class);
        this.extendPunchCardWay(groupVO);
        return groupVO;
    }

    /**
     * 列表查询
     *
     * @param param 列表条件入参
     *
     * @return 考勤组列表
     */
    public List<AttendanceGroupVO> getList(AttendanceGroupQueryParam param) {
        List<AttendanceGroupPO> groupPOS = this.getQueryChainWrapper(param).list();
        List<AttendanceGroupVO> groupVOS = ObjectUtils.copying(groupPOS, AttendanceGroupVO.class);
        groupVOS.forEach(this::extendList);
        return groupVOS;
    }

    /**
     * 分页查询
     *
     * @param param 分页条件入参
     *
     * @return 考勤组分页结果
     */
    public Pagination<AttendanceGroupVO> getPage(AttendanceGroupQueryParam param) {
        Page<AttendanceGroupPO> page = this.getQueryChainWrapper(param)
                .page(PagingUtils.convert(param));
        Pagination<AttendanceGroupVO> pagination = PagingUtils.convert(page, AttendanceGroupVO.class);
        pagination.getRecords().forEach(this::extendList);
        return pagination;
    }

    /**
     * 查询人员所在的排班制考勤组
     *
     * @param staffIds 员工IDS
     *
     * @return ？
     */
    public List<AttendanceGroupStaffVO> getArrangeGroupIdByStaffIds(List<Long> staffIds) {
        return this.getGroupIdByStaffIds(staffIds, AttendanceGroupTypeEnum.ARRANGE.name());
    }

    /**
     * 查询人员所在的考勤组
     *
     * @param staffIds  员工IDS
     * @param groupType 考勤组类型
     *
     * @return 考勤组人员集合
     */
    public List<AttendanceGroupStaffVO> getGroupIdByStaffIds(List<Long> staffIds, String groupType) {
        AttendanceGroupPO groupPO = attendanceGroupMapper.selectOne(Wrappers.<AttendanceGroupPO>lambdaQuery()
                                                                            .eq(AttendanceGroupPO::getType, groupType)
                                                                            .eq(AttendanceGroupPO::getRanges, AttendanceGroupRangesEnum.All_STAFF.name()));
        if (Objects.isNull(groupPO)) {
            return attendanceGroupStaffMapper.getGroupIdByStaffIds(staffIds, groupType);
        } else {
            // 如果存在全员考勤组，则直接返回值，因为全员考勤组没有实例化每个人到表中
            return staffIds.stream().map(staffId -> {
                AttendanceGroupStaffVO vo = new AttendanceGroupStaffVO();
                vo.setGroupId(groupPO.getId());
                vo.setStaffId(staffId);
                return vo;
            }).collect(Collectors.toList());
        }
    }

    /**
     * 查询考勤组下的所有人员
     *
     * @param groupId   考勤组ID
     * @param groupType 考勤组类型，如果考勤组ID为null，则查询该考勤类型下的所有人员
     *
     * @return 考勤组人员集合
     */
    public List<AttendanceGroupStaffVO> getStaffByGroupId(Long groupId, @NonNull String groupType) {
        if (null != groupId) {
            AttendanceGroupPO groupPO = attendanceGroupMapper.selectById(groupId);
            Assert.isNotFound(Objects.isNull(groupPO), "该考勤组不存在");
            // 代码扫描判断不出ErrorUtils.availableResource() 已经验证过空，所以才加断言。实际上不会触发这个断言
            org.springframework.util.Assert.notNull(groupPO, "该考勤组不存在");
            if (AttendanceGroupRangesEnum.All_STAFF.name().equals(groupPO.getRanges())) {
                List<StaffDetailDTO> staffClientList = staffClient.getList(new StaffQuery());
                List<AttendanceGroupStaffVO> staffVOS = ObjectUtils.copying(staffClientList, AttendanceGroupStaffVO.class);
                staffVOS.parallelStream().forEach(s -> {
                    s.setStaffId(s.getId());
                    s.setGroupId(groupId);
                });
                return staffVOS;
            } else {
                List<AttendanceGroupStaffPO> attendanceGroupStaffPOS = attendanceGroupStaffMapper.selectList(Wrappers
                                                                                                                     .<AttendanceGroupStaffPO>lambdaQuery()
                                                                                                                     .eq(AttendanceGroupStaffPO::getGroupId, groupId));
                return ObjectUtils.copying(attendanceGroupStaffPOS, AttendanceGroupStaffVO.class);
            }
        } else {
            List<AttendanceGroupPO> groupPOS = attendanceGroupMapper.selectList(Wrappers.<AttendanceGroupPO>lambdaQuery()
                                                                                        .eq(AttendanceGroupPO::getType, groupType));
            AttendanceGroupPO allStaffGroup = groupPOS.parallelStream()
                    .filter(g -> AttendanceGroupRangesEnum.All_STAFF.name().equals(g.getRanges())).findAny().orElse(null);
            if (null != allStaffGroup) {
                List<StaffDetailDTO> staffClientList = staffClient.getList(new StaffQuery());
                List<AttendanceGroupStaffVO> staffVOS = ObjectUtils.copying(staffClientList, AttendanceGroupStaffVO.class);
                staffVOS.parallelStream().forEach(s -> {
                    s.setStaffId(s.getId());
                    s.setGroupId(allStaffGroup.getId());
                });
                return staffVOS;
            } else {
                if (CollectionUtils.isEmpty(groupPOS)) {
                    return Collections.emptyList();
                }
                List<AttendanceGroupStaffPO> attendanceGroupStaffPOS = attendanceGroupStaffMapper.selectList(Wrappers
                                                                                                                     .<AttendanceGroupStaffPO>lambdaQuery()
                                                                                                                     .in(AttendanceGroupStaffPO::getGroupId, groupPOS
                                                                                                                             .parallelStream()
                                                                                                                             .map(AttendanceGroupPO::getId)
                                                                                                                             .collect(Collectors.toList())));
                return ObjectUtils.copying(attendanceGroupStaffPOS, AttendanceGroupStaffVO.class);
            }
        }
    }

    public List<Map<String, String>> getEnumerateList(String type) {
        if (ATTENDANCE_GROUP_TYPE.equals(type)) {
            return AttendanceGroupTypeEnum.getList();
        } else if (ATTENDANCE_TYPE.equals(type)) {
            return AttendanceTypeEnum.getList();
        }
        return new ArrayList<>();
    }

    private LambdaQueryChainWrapper<AttendanceGroupPO> getQueryChainWrapper(AttendanceGroupQueryParam param) {
        return this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getType()), AttendanceGroupPO::getType, param.getType())
                .like(StringUtils.isNotEmpty(param.getKeyword()), AttendanceGroupPO::getName, param.getKeyword())
                .orderByDesc(BasicEntity::getUpdateTime)
                ;
    }

    private void extendList(AttendanceGroupVO attendanceGroupVO) {
        long num = 0;
        if (AttendanceGroupRangesEnum.All_STAFF.name().equals(attendanceGroupVO.getRanges())) {
            // 如果是全员考勤组，动态计算当前人数
            //num = staffClient.getNum();
            // 如果是全员考勤组，获取当天的排班人数（排班人数才是真实的考勤组人数）
            if (AttendanceGroupTypeEnum.ARRANGE.name().equals(attendanceGroupVO.getType())) {
                num = arrangeWorkMapper.getArrangeWorkUserCount(Wrappers.<ArrangeWorkPO>lambdaQuery()
                                                                        .eq(ArrangeWorkPO::getGroupId, attendanceGroupVO.getId())
                                                                        .eq(ArrangeWorkPO::getDate, DateUtils.formatDate(LocalDateTime.now(), "yyyy-MM-dd"))
                                                               );
            } else {
                num = dutyWorkMapper.getDutyWorkUserCount(Wrappers.<DutyWorkPO>lambdaQuery()
                                                                  .eq(DutyWorkPO::getGroupId, attendanceGroupVO.getId())
                                                                  .eq(DutyWorkPO::getDate, DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd"))
                                                         );
            }

        } else {
            num = attendanceGroupStaffMapper.selectCount(Wrappers.<AttendanceGroupStaffPO>lambdaQuery()
                                                                 .eq(AttendanceGroupStaffPO::getGroupId, attendanceGroupVO.getId()));
        }
        attendanceGroupVO.setNum(num);
        List<AttendanceGroupClassesPO> classesPOS = attendanceGroupClassesMapper.selectList(Wrappers.<AttendanceGroupClassesPO>lambdaQuery()
                                                                                                    .eq(AttendanceGroupClassesPO::getGroupId, attendanceGroupVO.getId()));
        if (CollectionUtils.isNotEmpty(classesPOS)) {
            StringBuffer sb = new StringBuffer();
            classesPOS.forEach(c -> {
                if (StringUtils.isNotEmpty(c.getStartWorkTime())) {
                    sb.append(c.getStartWorkTime()).append("-").append(c.getEndWorkTime()).append(",");
                }
            });
            attendanceGroupVO.setDateStr(sb.isEmpty() ? "" : sb.deleteCharAt(sb.length() - 1).toString());
        }
    }

    private void extendPunchCardWay(AttendanceGroupVO groupVO) {
        if (groupVO.getIsPunchCard()) {
            List<String> punchCardWays = groupVO.getPunchCardWay();
            if (CollectionUtils.isNotEmpty(punchCardWays)) {
                List<AttendanceGroupPunchCardWayPO> wayPOS = attendanceGroupPunchCardWayMapper.selectList(Wrappers
                                                                                                                  .<AttendanceGroupPunchCardWayPO>lambdaQuery()
                                                                                                                  .eq(AttendanceGroupPunchCardWayPO::getGroupId, groupVO.getId()));
                List<AttendanceGroupPunchCardWayVO> wayVOS = ObjectUtils.copying(wayPOS, AttendanceGroupPunchCardWayVO.class);
                groupVO.setPunchCardWayWifi(wayVOS.stream().filter(w -> AttendanceGroupPunchCardWayEnum.WIFI.name().equals(w.getWay()))
                                                    .collect(Collectors.toList()));
                groupVO.setPunchCardWayGps(wayVOS.stream().filter(w -> AttendanceGroupPunchCardWayEnum.GPS.name().equals(w.getWay()))
                                                   .collect(Collectors.toList()));
                groupVO.setPunchCardWayMachine(wayVOS.stream()
                                                       .filter(w -> AttendanceGroupPunchCardWayEnum.MACHINE.name().equals(w.getWay()))
                                                       .collect(Collectors.toList()));
            }
        }
    }

    /**
     * 获取我的加班规则
     *
     * @return 加班规则
     */
    public AttendanceGroupWorkOvertimeRuleVO getMyWorkOvertimeRule(@NotNull Long userId) {

        //根据考情组 找到加班规则
        AttendanceGroupPO myAttendanceGroup = this.getMyAttendanceGroup(userId);
        Assert.isNotFound(Objects.isNull(myAttendanceGroup), "未找到对应的考勤组");
        // 代码扫描判断不出ErrorUtils.availableResource() 已经验证过空，所以才加断言。实际上不会触发这个断言
        Assert.isNotFound(myAttendanceGroup, "未找到对应的考勤组");
        List<AttendanceGroupWorkOvertimeRulePO> overtimeRuleList = attendanceGroupWorkOvertimeRuleMapper.selectList(Wrappers
                                                                                                                            .<AttendanceGroupWorkOvertimeRulePO>lambdaQuery()
                                                                                                                            .eq(AttendanceGroupWorkOvertimeRulePO::getGroupId, myAttendanceGroup.getId()));

        Assert.isNotFound(CollectionUtils.isEmpty(overtimeRuleList), "未找到对应的加班规则");
        Assert.isNotAcceptable(overtimeRuleList.size() != 1, "找到多条加班规则，请联系管理员");
        AttendanceGroupWorkOvertimeRulePO workOvertimeRulePO = overtimeRuleList.getFirst();
        return ObjectUtils.copying(workOvertimeRulePO, AttendanceGroupWorkOvertimeRuleVO.class);
    }

    /**
     * 获取我的考情组
     *
     * @param userId 人员id
     *
     * @return 考勤
     */
    public AttendanceGroupPO getMyAttendanceGroup(Long userId) {
        List<AttendanceGroupStaffPO> attendanceGroupList = attendanceGroupStaffMapper.selectList(Wrappers
                                                                                                         .<AttendanceGroupStaffPO>lambdaQuery()
                                                                                                         .eq(AttendanceGroupStaffPO::getGroupType, AttendanceGroupTypeEnum.ARRANGE.name())
                                                                                                         .eq(AttendanceGroupStaffPO::getStaffId, userId));
        if (CollectionUtils.isEmpty(attendanceGroupList)) {
            //查询是否有 所有人员 考勤组
            List<AttendanceGroupPO> allGroups = attendanceGroupMapper.selectList(Wrappers.emptyWrapper());
            AttendanceGroupPO allStaffGroup = allGroups.stream()
                    .filter(g -> AttendanceGroupRangesEnum.All_STAFF.name().equals(g.getRanges())).findFirst().orElse(null);
            if (Objects.isNull(allStaffGroup)) {
                return null;
            }
            return new AttendanceGroupPO(allStaffGroup.getId(), allStaffGroup.getName());
        } else {
            if (attendanceGroupList.size() > 1) {
                log.error("考勤统计找到员工[" + userId + "]的多个考勤组");
            }
            AttendanceGroupStaffPO attendanceGroupStaffPO = attendanceGroupList.get(0);
            AttendanceGroupPO groupPO = attendanceGroupMapper.selectById(attendanceGroupStaffPO.getGroupId());
            return new AttendanceGroupPO(attendanceGroupStaffPO.getGroupId(), groupPO != null ? groupPO.getName() : "");
        }
    }

    /**
     * 获取用户所在考勤组下的所有人员
     *
     * @param userId 人员id
     *
     * @return 考勤组人员集合
     */
    public List<AttendanceGroupStaffPO> getAllUsersInMyAttendanceGroup(@NotNull Long userId) {
        List<AttendanceGroupStaffPO> attendanceGroupList = attendanceGroupStaffMapper.selectList(Wrappers
                                                                                                         .<AttendanceGroupStaffPO>lambdaQuery()
                                                                                                         .eq(AttendanceGroupStaffPO::getStaffId, userId)
                                                                                                         .eq(AttendanceGroupStaffPO::getGroupType, AttendanceGroupTypeEnum.ARRANGE));
        if (CollectionUtils.isNotEmpty(attendanceGroupList) && attendanceGroupList.size() == 1) {
            return attendanceGroupStaffMapper.selectList(Wrappers.<AttendanceGroupStaffPO>lambdaQuery()
                                                                 .eq(AttendanceGroupStaffPO::getGroupId, attendanceGroupList.get(0)
                                                                         .getGroupId()));
        }
        return Collections.emptyList();
    }

    /**
     * 我的补卡规则
     *
     * @param userId 人员id
     *
     * @return 补卡规则集
     */
    public List<AttendanceGroupRepairClockRuleVO> getMyRepairClockRule(@NotNull Long userId) {
        AttendanceGroupPO myAttendanceGroup = this.getMyAttendanceGroup(userId);
        Assert.isNotFound(Objects.isNull(myAttendanceGroup), "未找到对应的考勤组");
        // 代码扫描判断不出ErrorUtils.availableResource() 已经验证过空，所以才加断言。实际上不会触发这个断言
        Assert.isParameterError(Objects.isNull(myAttendanceGroup), "未找到对应的考勤组");
        List<AttendanceGroupSupplementCardRulePO> supplementCardRuleList = attendanceGroupSupplementCardRuleMapper.selectList(Wrappers
                                                                                                                                      .<AttendanceGroupSupplementCardRulePO>lambdaQuery()
                                                                                                                                      .eq(AttendanceGroupSupplementCardRulePO::getGroupId, myAttendanceGroup.getId()));

        Assert.isNotAcceptable(CollectionUtils.isEmpty(supplementCardRuleList), "未找到对应的补卡规则");

        return ObjectUtils.copying(supplementCardRuleList, AttendanceGroupRepairClockRuleVO.class);
    }

    /**
     * 获取考勤组内一周的默认班次
     *
     * @return 一周的班次集合
     */
    public List<AttendanceGroupDefaultWeekClassesVO> getDefaultClasses() {
        List<AttendanceGroupDefaultWeekClassesVO> list = new ArrayList<>();
        Classes defaultClasses = ObjectUtils.copying(classesMapper.selectOne(Wrappers.<ClassesPO>lambdaQuery()
                                                                                     .eq(ClassesPO::getMark, ClassesMarkEnum.DEFAULT_WORK.name())), Classes.class);
        Classes restClasses = ObjectUtils.copying(classesMapper.selectOne(Wrappers.<ClassesPO>lambdaQuery()
                                                                                  .eq(ClassesPO::getMark, ClassesMarkEnum.All_DAY_REST.name())), Classes.class);
        for (int i = 2; i <= 8; i++) {
            List<Classes> classes = new ArrayList<>();
            int week = i;
            AttendanceGroupDefaultWeekClassesVO vo = new AttendanceGroupDefaultWeekClassesVO();
            if (i == 8) {
                week = 1;
            }
            vo.setWeek(week);
            vo.setName(WeekEnum.getWeekName(week).substring(1));
            if (i < 7) {
                if (null != defaultClasses) {
                    classes.add(defaultClasses);
                    vo.setClasses(classes);
                }
            } else {
                if (null != restClasses) {
                    classes.add(restClasses);
                    vo.setClasses(classes);
                }
            }
            list.add(vo);
        }
        return list;
    }

}
