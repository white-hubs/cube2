package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.infrastructure.mapper.AttAwayBusinessRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.infrastructure.po.AttAwayBusinessRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.vo.AttGroupFunctionVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.event.AttGroupStaffChangeEvent;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupStaffQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupAwayBusinessRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupCardRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupHolidayRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupLeavePostRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupOvertimeRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.infrastructure.mapper.AttLeavePostRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.infrastructure.po.AttLeavePostRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttPoAttributeSet;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.SupplementCardRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.WorkOvertimeRuleDetailMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.WorkOvertimeRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeCountDurationEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.SupplementCardRuleVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.SupplementCardRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.WorkOvertimeRuleDetailPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.WorkOvertimeRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.SupplementCardRuleQueryService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttDefaultGroupRepository implements AttGroupRepository {

    private final UserClient userClient;
    private final AttGroupMapper attGroupMapper;
    private final AttGroupStaffMapper attGroupStaffMapper;
    private final AttSchedulingMapper attSchedulingMapper;
    private final AttHolidayRuleMapper attHolidayRuleMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SupplementCardRuleMapper supplementCardRuleMapper;
    private final WorkOvertimeRuleMapper workOvertimeRuleMapper;
    private final WorkOvertimeRuleDetailMapper workOvertimeRuleDetailMapper;
    private final AttLeavePostRuleMapper attLeavePostRuleMapper;
    private final AttAwayBusinessRuleMapper attAwayBusinessRuleMapper;
    private final SupplementCardRuleQueryService supplementCardRuleQueryService;

    /**
     * 获取考勤所有的租户
     */
    @Override
    public List<Integer> getAttTenantAll() {
        List<Integer> resulstList = new ArrayList<>();
        List<AttGroupPO> groupPOList = attGroupMapper.selectList(Wrappers.<AttGroupPO>lambdaQuery()
                                                                         .groupBy(AttGroupPO::getTenant)
                                                                );
        if (groupPOList != null && !groupPOList.isEmpty()) {
            resulstList = groupPOList.stream().map(AttGroupPO::getTenant).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        }
        return resulstList;
    }

    /**
     * 新增考勤组
     *
     * @param param 考勤组
     *
     * @return id
     */
    @Override
    @LogRecord(success = "新增:{{#newPO.name}}", subType = "ATTENDANCE_SECTION", subTypeName = "考勤组", operationType = "ADD",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#newPO.id}}"
    )
    public Long insertAttGroup(AttGroupParam param, AttGroupFunctionVO newPO) {

        AttGroupPO groupPO = AttGroupPO.builder().build();
        BeanUtils.copyProperties(param, groupPO);

        AttPoAttributeSet.initAddBasePoAttribute(groupPO);
        attGroupMapper.insert(groupPO);

        applicationEventPublisher.publishEvent(new AttGroupStaffChangeEvent(this, groupPO, DateUtils.format(LocalDateTime.now(), "yyyy-MM"), param.getGroupStaffList(), param.getGroupStaffList(), null));
        return param.getId();
    }

    /**
     * 修改考勤组
     *
     * @param param 考勤组对象
     *
     * @return id
     */
    @Override
    @LogRecord(success = "更新:{_DIFF{#oldPO,#newPO}}", subType = "ATTENDANCE_SECTION", operationType = "UPDATE",
               subTypeName = "考勤组", type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#newPO.id}}"
    )
    public Long updateAttGroup(AttGroupFunctionVO oldPO, AttGroupFunctionVO newPO, AttGroupParam param) {
        // 获取当前登录用户
        Principal principal = SecurityContextHolder.getPrincipal();
        String loginUserId = principal == null ? "" : String.valueOf(principal.getId());
        // 修改考勤组
        attGroupMapper.update(null, Wrappers.<AttGroupPO>lambdaUpdate().eq(AttGroupPO::getId, param.getId())
                                      .set(StringUtils.isNotBlank(param.getName()), AttGroupPO::getName, param.getName())
                                      .set(StringUtils.isNotBlank(param.getRanges()), AttGroupPO::getRanges, param.getRanges())
                                      .set(AttGroupPO::getSchedulingRuleId, param.getSchedulingRuleId())
                                      .set(AttGroupPO::getCardRuleIds, param.getCardRuleIds())
                                      .set(AttGroupPO::getOvertimeRuleId, param.getOvertimeRuleId())
                                      .set(param.getClockRuleId() != null, AttGroupPO::getClockRuleId, param.getClockRuleId())
                                      .set(AttGroupPO::getUpdateTime, LocalDateTime.now())
                                      .set(AttGroupPO::getUpdateBy, loginUserId)
                                      .set(AttGroupPO::getHolidayRuleIds, param.getHolidayRuleIds())
                                      .set(AttGroupPO::getLeavePostRuleId, param.getLeavePostRuleId())
                                      .set(AttGroupPO::getAwayBusinessRuleId, param.getAwayBusinessRuleId())
                                      .set(AttGroupPO::getGroupClassIds, param.getGroupClassIds())
                             );
        if (AttGroupRangesEnum.ALL.name().equals(param.getRanges())) {
            return param.getId();
        }

        // 需要移除的人员id集合（如果原来的人员id在新的入参中没找到，就表示是需要移除的
        List<Long> removeStaffIdList = new ArrayList<>();
        // 需要添加的人员id集合
        List<AttGroupStaffParam> addStaffList = new ArrayList<>();
        List<AttGroupStaffPO> oldGroupStaffList = attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                                         .eq(AttGroupStaffPO::getGroupId, param.getId())
                                                                                );
        Map<Long, AttGroupStaffPO> oldStaffMap = oldGroupStaffList.stream().collect(Collectors.toMap(AttGroupStaffPO::getStaffId, v -> v));
        Map<Long, AttGroupStaffParam> paramStaffMap = param.getGroupStaffList().stream()
                .collect(Collectors.toMap(AttGroupStaffParam::getId, v -> v));
        for (AttGroupStaffPO groupStaffPO : oldGroupStaffList) {
            if (paramStaffMap.get(groupStaffPO.getStaffId()) == null) {
                removeStaffIdList.add(groupStaffPO.getStaffId());
            }
        }
        for (AttGroupStaffParam attGroupStaffParam : param.getGroupStaffList()) {
            if (oldStaffMap.get(attGroupStaffParam.getId()) == null) {
                addStaffList.add(attGroupStaffParam);
            }
        }
        AttGroupPO groupPO = AttGroupPO.builder().build();
        BeanUtils.copyProperties(param, groupPO);
        applicationEventPublisher.publishEvent(new AttGroupStaffChangeEvent(this, groupPO, DateUtils.format(LocalDateTime.now(), "yyyy-MM"), param.getGroupStaffList(), addStaffList, removeStaffIdList));

        return param.getId();
    }

    /**
     * 删除考勤组
     *
     * @param groupPO 考勤组
     *
     * @return id
     */
    @Override
    @LogRecord(success = "删除考勤组:{{#groupPO.name}}", subType = "ATTENDANCE_SECTION", subTypeName = "考勤组", operationType = "DELETE",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#groupPO.id}}"
    )
    public Long deleteAttGroup(AttGroupPO groupPO) {
        // 清除考勤组（未来时间）的手动排班
        attSchedulingMapper.remove(DateUtils.toTimestamp(LocalDateTime.now()), Wrappers.<AttSchedulingPO>lambdaQuery()
                                           .eq(AttSchedulingPO::getStaffGroupId, groupPO.getId())
                                           .gt(AttSchedulingPO::getSchedulingDate, LocalDateTime.now())
                                  );
        // 清除考勤组人员关联关系
        attGroupStaffMapper.removeByGroupId(DateUtils.toTimestamp(LocalDateTime.now()), groupPO.getId());
        // 清除考勤组
        attGroupMapper.removeById(DateUtils.toTimestamp(LocalDateTime.now()), groupPO.getId());
        return groupPO.getId();
    }

    /**
     * 获取人员考勤组id
     *
     * @param staffId 员工id
     *
     * @return id
     */
    @Override
    public Long getGroupIdByStaffId(Long staffId) {
        AttGroupPO allGroupPo = attGroupMapper.selectOne(Wrappers.<AttGroupPO>lambdaQuery()
                                                                 .eq(AttGroupPO::getRanges, AttGroupRangesEnum.ALL.name()));
        if (allGroupPo != null) {
            return allGroupPo.getId();
        }
        List<AttGroupStaffPO> groupPOList = attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                                   .eq(AttGroupStaffPO::getStaffId, staffId));
        if (groupPOList != null && !groupPOList.isEmpty()) {
            return groupPOList.getFirst().getGroupId();
        }
        return null;

    }

    /**
     * 获取人员考勤组名称
     *
     * @param staffId 员工id
     *
     * @return 名称
     */
    @Override
    public String getGroupNameByStaffId(Long staffId) {
        AttGroupPO groupPo = this.getGroupByStaffId(staffId);
        return groupPo == null ? "" : groupPo.getName();
    }

    /**
     * 获取人员考勤组
     *
     * @param staffId 员工id
     *
     * @return 考勤组
     */
    @Override
    public AttGroupPO getGroupByStaffId(Long staffId) {
        AttGroupPO groupPO = null;
        List<AttGroupStaffPO> groupPOList = attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                                   .eq(AttGroupStaffPO::getStaffId, staffId));
        if (groupPOList != null && !groupPOList.isEmpty()) {
            groupPO = attGroupMapper.selectById(groupPOList.getFirst().getGroupId());
        }
        if (groupPO != null) {
            return groupPO;
        }
        List<AttGroupPO> allGroupList = attGroupMapper.selectList(Wrappers.<AttGroupPO>lambdaQuery()
                                                                          .eq(AttGroupPO::getRanges, AttGroupRangesEnum.ALL.name()));
        if (allGroupList != null && !allGroupList.isEmpty()) {
            return allGroupList.getFirst();
        }
        return null;
    }

    /**
     * 添加考勤组人员
     *
     * @param groupId          考勤组id
     * @param schedulingRuleId 排班规则id
     * @param clockRuleId      打卡规则id
     * @param addStaffList     需要添加的集合
     */
    private void addGroupStaff(Long groupId, String groupName, Long schedulingRuleId, Long clockRuleId,
                               List<AttGroupStaffParam> addStaffList
                              ) {
        addStaffList.forEach(staffParam -> {
            // 添加考勤组人员
            AttGroupStaffPO groupStaffPO = AttGroupStaffPO.builder().build();
            BeanUtils.copyProperties(staffParam, groupStaffPO);
            AttPoAttributeSet.initAddBasePoAttribute(groupStaffPO);
            groupStaffPO.setGroupName(groupName);
            groupStaffPO.setId(IdWorker.getId());
            groupStaffPO.setGroupId(groupId);
            groupStaffPO.setSchedulingRuleId(schedulingRuleId);
            groupStaffPO.setClockRuleId(clockRuleId);
            attGroupStaffMapper.insert(groupStaffPO);
        });
    }

    /**
     * 获取补卡规则
     *
     * @param cardRuleIdStr 规则ids
     *
     * @return 规则集合
     */
    @Override
    public List<AttGroupCardRuleResult> getGroupCardRuleList(String cardRuleIdStr) {
        List<AttGroupCardRuleResult> resultList = new ArrayList<>();
        if (StringUtils.isNotBlank(cardRuleIdStr)) {
            String[] cardRuleIds = cardRuleIdStr.split(",");
            List<Long> cardRuleIdList = Arrays.stream(cardRuleIds).map(Long::valueOf).collect(Collectors.toList());
            List<SupplementCardRulePO> cardRuleList = supplementCardRuleMapper.selectByIds(cardRuleIdList);
            if (cardRuleList != null && !cardRuleList.isEmpty()) {
                resultList = cardRuleList.stream().map(po -> {
                    SupplementCardRuleVO supplementCardRuleVO = supplementCardRuleQueryService.extend(po);
                    return ObjectUtils.copying(supplementCardRuleVO, AttGroupCardRuleResult.class);
                }).collect(Collectors.toList());
            }
        }

        return resultList;
    }

    /**
     * 获取假期规则
     *
     * @param holidayRuleIdStr 假期规则ids
     *
     * @return 规则集合
     */
    @Override
    public List<AttGroupHolidayRuleResult> getGroupHolidayRuleList(String holidayRuleIdStr) {
        List<AttGroupHolidayRuleResult> resultList = new ArrayList<>();
        if (StringUtils.isNotBlank(holidayRuleIdStr)) {
            String[] holidayRuleIds = holidayRuleIdStr.split(",");
            List<Long> holidayRuleIdList = Arrays.stream(holidayRuleIds).map(Long::valueOf).collect(Collectors.toList());
            List<AttHolidayRulePO> holidayRuleList = attHolidayRuleMapper.selectByIds(holidayRuleIdList);
            if (holidayRuleList != null && !holidayRuleList.isEmpty()) {
                resultList = holidayRuleList.stream().map(po -> {
                    AttGroupHolidayRuleResult rs = new AttGroupHolidayRuleResult();
                    BeanUtils.copyProperties(po, rs);
                    return rs;
                }).collect(Collectors.toList());
            }
        }

        return resultList;
    }

    /**
     * 获取加班规则
     *
     * @param overtimeRuleId 规则id
     *
     * @return 加班规则
     */
    @Override
    public AttGroupOvertimeRuleResult getGroupOvretimeRuleList(Long overtimeRuleId) {
        AttGroupOvertimeRuleResult rs = new AttGroupOvertimeRuleResult();
        if (overtimeRuleId != null) {
            WorkOvertimeRulePO workOvertimeRulePO = workOvertimeRuleMapper.selectById(overtimeRuleId);
            if (workOvertimeRulePO != null) {
                BeanUtils.copyProperties(workOvertimeRulePO, rs);
                // 规则内容
                List<String> content = new ArrayList<>();
                List<WorkOvertimeRuleDetailPO> ruleDetailPOS = workOvertimeRuleDetailMapper.selectList(Wrappers
                                                                                                               .<WorkOvertimeRuleDetailPO>lambdaQuery()
                                                                                                               .eq(WorkOvertimeRuleDetailPO::getRuleId, workOvertimeRulePO.getId()));
                if (ruleDetailPOS != null && !ruleDetailPOS.isEmpty()) {
                    WorkOvertimeRuleDetailPO workDetailPO = ruleDetailPOS.parallelStream()
                            .filter(r -> WorkOvertimeTypeEnum.WORK.name().equals(r.getType())).findAny()
                            .orElse(new WorkOvertimeRuleDetailPO());
                    if (com.guhecloud.platform.base.util.StringUtils.isNotEmpty(workDetailPO.getMethod())) {
                        String work = "工作日加班：" + WorkOvertimeCountDurationEnum.valueOf(workDetailPO.getMethod()).getValue();
                        content.add(work);
                    }
                    WorkOvertimeRuleDetailPO restDetailPO = ruleDetailPOS.parallelStream()
                            .filter(r -> WorkOvertimeTypeEnum.REST.name().equals(r.getType())).findAny()
                            .orElse(new WorkOvertimeRuleDetailPO());
                    if (com.guhecloud.platform.base.util.StringUtils.isNotEmpty(restDetailPO.getMethod())) {
                        String work = "休息日加班：" + WorkOvertimeCountDurationEnum.valueOf(restDetailPO.getMethod()).getValue();
                        content.add(work);
                    }
                    WorkOvertimeRuleDetailPO festivalDetailPO = ruleDetailPOS.parallelStream()
                            .filter(r -> WorkOvertimeTypeEnum.FESTIVAL.name().equals(r.getType())).findAny()
                            .orElse(new WorkOvertimeRuleDetailPO());
                    if (com.guhecloud.platform.base.util.StringUtils.isNotEmpty(festivalDetailPO.getMethod())) {
                        String work = "节假日加班：" + WorkOvertimeCountDurationEnum.valueOf(festivalDetailPO.getMethod()).getValue();
                        content.add(work);
                    }
                }
                rs.setContent(content);
            }
        }
        return rs;
    }

    /**
     * 离岗规则
     *
     * @param leavePostRuleId 规则id
     *
     * @return 离岗规则
     */
    @Override
    public AttGroupLeavePostRuleResult getGroupLeavePostRule(Long leavePostRuleId) {
        AttGroupLeavePostRuleResult result = new AttGroupLeavePostRuleResult();
        if (leavePostRuleId != null) {
            AttLeavePostRulePO postRulePO = attLeavePostRuleMapper.selectById(leavePostRuleId);
            if (postRulePO != null) {
                BeanUtils.copyProperties(postRulePO, result);
            }
        }
        return result;
    }

    /**
     * 获取出差规则返回对象
     *
     * @param awayBusinessRuleId 出差规则id
     *
     * @return 出差规则
     */
    @Override
    public AttGroupAwayBusinessRuleResult getGroupAwayBusinessRule(Long awayBusinessRuleId) {
        AttGroupAwayBusinessRuleResult result = new AttGroupAwayBusinessRuleResult();
        if (awayBusinessRuleId != null) {
            AttAwayBusinessRulePO awayRulePO = attAwayBusinessRuleMapper.selectById(awayBusinessRuleId);
            if (awayRulePO != null) {
                BeanUtils.copyProperties(awayRulePO, result);
            }
        }

        return result;
    }

    /**
     * 获取用户考勤组关系
     *
     * @return 用户考勤组关系map
     */
    @Override
    public Map<Long, AttGroupPO> getStaffGroupMap() {
        Map<Long, AttGroupPO> staffGroupMap = new HashMap<>();
        AttGroupPO allGroup = attGroupMapper.selectOne(Wrappers.<AttGroupPO>lambdaQuery()
                                                               .eq(AttGroupPO::getRanges, AttGroupRangesEnum.ALL.name())
                                                               .last("limit 1")
                                                      );
        if (allGroup != null) {
            UserQueryParam userQueryParam = new UserQueryParam();
            userQueryParam.setEnabled(true);
            userQueryParam.setPageSize(999);
            userQueryParam.setPageNumber(1);
            Pagination<UserDTO> pagination = userClient.listUserByTenant(SessionContextHolder.getRequester().getTenant(), userQueryParam);
            if (pagination.getRecords() != null) {
                pagination.getRecords().forEach(userDTO -> staffGroupMap.put(userDTO.getId(), allGroup));
            }

        } else {
            Map<Long, AttGroupPO> groupPOMap = new HashMap<>();
            List<AttGroupPO> attGroupPOList = attGroupMapper.selectList(Wrappers.emptyWrapper());
            if (attGroupPOList != null && !attGroupPOList.isEmpty()) {
                groupPOMap = attGroupPOList.stream().collect(Collectors.toMap(AttGroupPO::getId, v -> v));
                List<AttGroupStaffPO> groupStaffPOList = attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery());
                if (groupStaffPOList != null && !groupStaffPOList.isEmpty()) {
                    for (AttGroupStaffPO groupStaffPO : groupStaffPOList) {
                        staffGroupMap.put(groupStaffPO.getStaffId(), groupPOMap.get(groupStaffPO.getGroupId()));
                    }
                }
            }
        }
        return staffGroupMap;
    }

    /**
     * 获取考勤组人员分页
     *
     * @param param 条件
     *
     * @return 分页
     */
    @Override
    public Pagination<AttGroupStaffPO> getGroupStaffPage(AttGroupStaffQueryParam param) {
        Page<AttGroupStaffPO> page = attGroupStaffMapper.selectPage(PagingUtils.convert(param), Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                            .eq(Objects.nonNull(param.getDeptId()), AttGroupStaffPO::getDeptId, param.getDeptId())
                                                                            .eq(Objects.nonNull(param.getGroupId()), AttGroupStaffPO::getGroupId, param.getGroupId())
                                                                            .like(StringUtils.isNotBlank(param.getStaffName()), AttGroupStaffPO::getName, param.getStaffName())
                                                                            .in(CollectionUtils.isNotEmpty(param.getGroupIdList()), AttGroupStaffPO::getGroupId, param.getGroupIdList())
                                                                            .orderByAsc(AttGroupStaffPO::getName)
                                                                   );
        List<AttGroupStaffPO> records = new ArrayList<>();
        if (page.getTotal() > AttConstants.zeroInt) {
            records = page.getRecords();
        }
        return new Pagination<>(page.getTotal(), page.getPages(), records);
    }

    /**
     * 获取考勤组人员列表
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<AttGroupStaffPO> getGroupStaffList(AttGroupStaffQueryParam param) {
        List<AttGroupStaffPO> list = attGroupStaffMapper.selectList(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                            .eq(Objects.nonNull(param.getDeptId()), AttGroupStaffPO::getDeptId, param.getDeptId())
                                                                            .eq(Objects.nonNull(param.getGroupId()), AttGroupStaffPO::getGroupId, param.getGroupId())
                                                                            .like(StringUtils.isNotBlank(param.getStaffName()), AttGroupStaffPO::getName, param.getStaffName())
                                                                            .in(CollectionUtils.isNotEmpty(param.getGroupIdList()), AttGroupStaffPO::getGroupId, param.getGroupIdList())
                                                                            .orderByAsc(AttGroupStaffPO::getName)
                                                                   );
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

}
