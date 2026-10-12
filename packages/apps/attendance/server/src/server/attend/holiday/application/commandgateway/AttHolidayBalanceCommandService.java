package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.commandgateway;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.WorkOvertimeResultEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupStaffQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.dto.AttBalanceOperationLogDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto.AttHolidayBalanceMutationDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto.AttHolidayGrantQuotaDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto.AttHolidayGrantValidityDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttHolidayGrantWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttHolidayQuotaTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttHolidayQuotaValidityEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.repository.AttHolidayBalanceRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayBalanceDeadlineMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayBalanceMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayBalanceRecordMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalanceDeadlinePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalancePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalanceRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.param.AttHolidayBalanceParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayBalanceImportResult;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 假期余额
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttHolidayBalanceCommandService extends ServiceImpl<AttHolidayBalanceMapper, AttHolidayBalancePO> {

    private final AttHolidayBalanceMapper attHolidayBalanceMapper;
    private final AttHolidayBalanceRepository attHolidayBalanceRepository;
    private final AttHolidayBalanceRecordCommandService attHolidayBalanceRecordCommandService;
    private final AttHolidayBalanceRecordMapper attHolidayBalanceRecordMapper;
    private final AttHolidayRuleMapper attHolidayRuleMapper;
    private final AttHolidayBalanceDeadlineMapper attHolidayBalanceDeadlineMapper;
    private final AttHolidayBalanceDeadlineCommandService attHolidayBalanceDeadlineCommandService;
    private final AttGroupMapper attGroupMapper;
    private final StaffClient staffClient;
    private final AttGroupRepository attGroupRepository;
    private final UserClient userClient;
    private final AttGroupStaffMapper attGroupStaffMapper;

    /**
     * 编辑假期余额
     * 默认：直接编辑 入参是多少最终假期余额就是多少
     * param.incrementalUpdate 为true时：增量修改，即余额+=value
     *
     * @param param 修改的余额
     */
    public void editHolidayBalance(AttHolidayBalanceParam param) {
        AttHolidayRulePO holidayRulePO = attHolidayRuleMapper.selectById(param.getHolidayRuleId());
        Assert.isNotFound(holidayRulePO, "假期类型不存在，请确认是否被删除");
        // 校验人员是否有该假期
        //        Assert.isError(!this.checkStaffHaveHoliday(param.getStaffId(),param.getHolidayRuleId()),"人员无此假期类型！");
        long available = AttHolidayDomain.unitAndNumToLong(param.getAvailableBalance(), param.getUnit(), holidayRulePO.getDayOfHour());
        AttHolidayBalancePO oldBalance = attHolidayBalanceMapper.selectOne(Wrappers.<AttHolidayBalancePO>lambdaQuery()
                                                                                   .eq(AttHolidayBalancePO::getStaffId, param.getStaffId())
                                                                                   .eq(AttHolidayBalancePO::getHolidayRuleId, param.getHolidayRuleId())
                                                                                   .last("limit 1")
                                                                          );
        if (oldBalance == null) {
            oldBalance = attHolidayBalanceRepository.createIfAbsent(param.getStaffId(), param.getHolidayRuleId(), holidayRulePO);
        }
        LocalDateTime deadline = param.getDeadline() == null ? AttConstants.PERMANENT_DATE : param.getDeadline();
        String operator = SecurityContextHolder.getPrincipal() == null ? "" : SecurityContextHolder.getPrincipal().getName();
        String staffName = StringUtils.isBlank(param.getStaffName()) ? this.getStaffName(param.getStaffId()) : param.getStaffName();
        if (Boolean.TRUE.equals(param.getIncrementalUpdate())) {
            String remark = param.getRemark();
            if (StringUtils.isBlank(param.getRemark())) {
                remark = "添加余额：" + param.getAvailableBalance() + param.getUnit();
            }
            Long deadlineId = null;
            if (available > AttConstants.zeroLong) {
                deadlineId = IdWorker.getId();
                AttHolidayBalanceDeadlinePO deadlinePO = AttHolidayBalanceDeadlinePO.builder()
                        .id(deadlineId)
                        .balance(available)
                        .deadline(deadline)
                        .holidayRuleId(param.getHolidayRuleId())
                        .staffId(param.getStaffId())
                        .build();
                deadlinePO.setDeleted(false);
                attHolidayBalanceDeadlineMapper.insert(deadlinePO);
            }
            attHolidayBalanceRepository.incrementAvailableBalance(AttHolidayBalanceMutationDTO.builder()
                                                                          .staffId(param.getStaffId())
                                                                          .holidayRuleId(param.getHolidayRuleId())
                                                                          .availableDelta(available)
                                                                          .changeNumber(available)
                                                                          .deadlineId(deadlineId)
                                                                          .deadline(deadline)
                                                                          .remark(remark)
                                                                          .operator(operator)
                                                                          .staffName(staffName)
                                                                          .processInstanceId(param.getProcessInstanceId())
                                                                          .businessType(param.getBusinessType())
                                                                          .knownBeforeBalance(oldBalance.getAvailableBalance())
                                                                          .notLimit(holidayRulePO.getNotLimit())
                                                                          .build());
        } else {
            // 业务操作日志对象
            AttHolidayBalancePO newPo = new AttHolidayBalancePO();
            BeanUtils.copyProperties(param, newPo);
            Long oldBalanceValue = null;
            if (oldBalance != null && oldBalance.getAvailableBalance() != null) {
                newPo.setOccupyBalance(oldBalance.getOccupyBalance());
                newPo.setId(oldBalance.getId());
                oldBalanceValue = oldBalance.getAvailableBalance();
            }
            newPo.setUpdateRemark(param.getRemark());
            newPo.setAvailableBalance(available);
            // 记录业务操作日志
            this.operationLogBeforeHandle(newPo, deadline, oldBalanceValue);
            String remark = param.getRemark();
            if (StringUtils.isBlank(param.getRemark())) {
                remark = "直接修改为固定值：" + param.getAvailableBalance() + param.getUnit();
            }
            attHolidayBalanceDeadlineMapper.delete(Wrappers.<AttHolidayBalanceDeadlinePO>lambdaUpdate()
                                                           .eq(AttHolidayBalanceDeadlinePO::getStaffId, param.getStaffId())
                                                           .eq(AttHolidayBalanceDeadlinePO::getHolidayRuleId, param.getHolidayRuleId())
                                                  );
            Long deadlineId = IdWorker.getId();
            AttHolidayBalanceDeadlinePO deadlinePO = AttHolidayBalanceDeadlinePO.builder()
                    .id(deadlineId)
                    .balance(available)
                    .deadline(deadline)
                    .holidayRuleId(param.getHolidayRuleId())
                    .staffId(param.getStaffId())
                    .build();
            deadlinePO.setDeleted(false);
            attHolidayBalanceDeadlineMapper.insert(deadlinePO);
            attHolidayBalanceRepository.replaceAvailableBalance(AttHolidayBalanceMutationDTO.builder()
                                                                        .staffId(param.getStaffId())
                                                                        .holidayRuleId(param.getHolidayRuleId())
                                                                        .targetAvailableBalance(available)
                                                                        .changeNumber(available)
                                                                        .deadlineId(deadlineId)
                                                                        .deadline(deadline)
                                                                        .remark(remark)
                                                                        .operator(operator)
                                                                        .staffName(staffName)
                                                                        .processInstanceId(param.getProcessInstanceId())
                                                                        .businessType(param.getBusinessType())
                                                                        .knownBeforeBalance(oldBalanceValue)
                                                                        .notLimit(holidayRulePO.getNotLimit())
                                                                        .build());
        }
    }

    /**
     * 批量给人员添加假期类型
     * （对应人员无此假期类型）
     *
     * @param staffIdList 人id集合
     * @param holidayId   假期规则id
     * @param holidayRule 假期规则对象,可以为空(为空就此方法自己根据holidayId查询)
     */
    public void addStaffHolidayTypeBatch(List<Long> staffIdList, Long holidayId, AttHolidayRulePO holidayRule) {
        if (staffIdList == null || holidayId == null || staffIdList.isEmpty()) {
            return;
        }
        if (holidayRule == null) {
            holidayRule = attHolidayRuleMapper.selectById(holidayId);
        }
        Assert.isNotFound(holidayRule, "假期类型不存在");
        for (Long staffId : staffIdList) {
            attHolidayBalanceRepository.createIfAbsent(staffId, holidayId, holidayRule);
        }
    }

    /**
     * 发放假期余额(定时任务)
     *
     * @param nowTime 日期
     */
    public void grantBalance(LocalDateTime nowTime) {
        // 根据租户发放
        List<AttHolidayRulePO> holidayRuleList = attHolidayRuleMapper.selectList(Wrappers.emptyWrapper());
        if (holidayRuleList != null && !holidayRuleList.isEmpty()) {
            for (AttHolidayRulePO holidayRulePO : holidayRuleList) {
                // 获取适用规则的考勤组-》获取到使用规则的人员-》遍历人员发放余额
                List<AttGroupPO> groupUseRuleList = attGroupMapper.selectList(Wrappers.<AttGroupPO>lambdaQuery()
                                                                                      .like(AttGroupPO::getHolidayRuleIds, holidayRulePO.getId())
                                                                             );
                if (groupUseRuleList == null || groupUseRuleList.isEmpty()) {
                    // 规则没有使用就跳过
                    break;
                }
                Set<Long> groupIdList = new HashSet<>();
                for (AttGroupPO groupPO : groupUseRuleList) {
                    groupIdList.add(groupPO.getId());
                }
                // 获取人员
                AttGroupStaffQueryParam queryParam = new AttGroupStaffQueryParam();
                queryParam.setGroupIdList(new ArrayList<>(groupIdList));
                List<AttGroupStaffPO> groupStaffPOList = attGroupRepository.getGroupStaffList(queryParam);
                List<AttHolidayBalanceMutationDTO> initMutations = new ArrayList<>();
                List<AttHolidayBalanceMutationDTO> balanceMutations = new ArrayList<>();
                List<AttHolidayBalanceDeadlinePO> deadlineList = new ArrayList<>();
                for (AttGroupStaffPO groupStaffPO : groupStaffPOList) {
                    // 处理发放的假期
                    AttHolidayBalancePO grantBalancePO = new AttHolidayBalancePO();
                    this.grantStaffBalance(groupStaffPO, holidayRulePO, nowTime, grantBalancePO, balanceMutations, deadlineList);
                    initMutations.add(AttHolidayBalanceMutationDTO.builder()
                                              .staffId(groupStaffPO.getStaffId())
                                              .holidayRuleId(holidayRulePO.getId())
                                              .notLimit(grantBalancePO.getNotLimit())
                                              .remark("初始化")
                                              .build());
                }
                // 批量插入
                attHolidayBalanceRepository.createIfAbsentBatch(initMutations);
                attHolidayBalanceRepository.applyBalanceMutations(balanceMutations);
                attHolidayBalanceDeadlineCommandService.saveBatch(deadlineList, AttConstants.bachSize);
            }
        }
    }

    /**
     * 给考勤组人员添加余额
     *
     * @param group   考勤组
     * @param nowTime 当前时间
     */
    public void staffAddBalance(AttGroupPO group, List<AttGroupStaffParam> addStaffList, LocalDateTime nowTime) {
        if (group == null || StringUtils.isBlank(group.getHolidayRuleIds())) {
            return;
        }
        // 获取规则
        String[] ruleIds = group.getHolidayRuleIds().split(",");
        if (ruleIds.length == AttConstants.zeroInt) {
            return;
        }
        List<AttHolidayRulePO> holidayRulePOList = attHolidayRuleMapper.selectList(Wrappers.<AttHolidayRulePO>lambdaQuery()
                                                                                           .in(AttHolidayRulePO::getId, Arrays.asList(ruleIds)));
        if (holidayRulePOList == null || holidayRulePOList.isEmpty()) {
            return;
        }

        // 人员集合
        List<AttGroupStaffPO> staffPOList = new ArrayList<>();
        if (AttGroupRangesEnum.ALL.name().equals(group.getRanges())) {
            UserQueryParam queryParam = new UserQueryParam();
            queryParam.setEnabled(true);
            queryParam.setPageSize(AttConstants.oneInt);
            queryParam.setPageNumber(999);
            Pagination<UserDTO> pagination = userClient.listUserByTenant(SessionContextHolder.getRequester().getTenant(), queryParam);
            if (pagination.getTotal() > AttConstants.zeroLong) {
                for (UserDTO userDTO : pagination.getRecords()) {
                    AttGroupStaffPO staffPO = new AttGroupStaffPO();
                    staffPO.setStaffId(userDTO.getId());
                    staffPO.setMobile(userDTO.getMobile());
                    staffPO.setName(userDTO.getName());
                    staffPO.setGroupId(group.getId());
                    staffPO.setGroupName(group.getName());
                    staffPO.setEntryDate(userDTO.getEntryDate());
                    staffPO.setClockRuleId(group.getClockRuleId());
                    staffPO.setSchedulingRuleId(group.getSchedulingRuleId());
                    UserDTO.Organization organization = userDTO.getOrganization();
                    if (organization != null) {
                        staffPO.setDeptId(organization.getId());
                        staffPO.setDeptName(organization.getName());
                    }

                    staffPOList.add(staffPO);
                }
            }
        } else if (addStaffList != null && !addStaffList.isEmpty()) {
            for (AttGroupStaffParam attGroupStaffParam : addStaffList) {
                AttGroupStaffPO groupStaffPO = new AttGroupStaffPO();
                BeanUtils.copyProperties(attGroupStaffParam, groupStaffPO);
                staffPOList.add(groupStaffPO);
            }
        }
        if (staffPOList.isEmpty()) {
            return;
        }

        List<AttHolidayBalanceMutationDTO> initMutations = new ArrayList<>();
        List<AttHolidayBalanceMutationDTO> balanceMutations = new ArrayList<>();
        List<AttHolidayBalanceDeadlinePO> deadlineList = new ArrayList<>();
        /* 遍历人员生成余额*/
        for (AttGroupStaffPO groupStaffPO : staffPOList) {
            for (AttHolidayRulePO holidayRulePO : holidayRulePOList) {
                // 处理发放的假期
                this.handleInsertBalance(groupStaffPO, holidayRulePO, initMutations, balanceMutations, deadlineList, nowTime);
            }
        }

        // 批量插入
        attHolidayBalanceRepository.createIfAbsentBatch(initMutations);
        attHolidayBalanceRepository.applyBalanceMutations(balanceMutations);
        attHolidayBalanceDeadlineCommandService.saveBatch(deadlineList, AttConstants.bachSize);
    }

    /**
     * 清理过期余额(定时任务)
     *
     * @param nowTime 日期
     */
    public void cleanExpireBalance(LocalDateTime nowTime) {
        if (nowTime == null) {
            nowTime = LocalDateTime.now();
        }
        List<AttHolidayBalanceDeadlinePO> deadlineList = attHolidayBalanceDeadlineMapper.selectList(Wrappers
                                                                                                            .<AttHolidayBalanceDeadlinePO>lambdaQuery()
                                                                                                            .le(AttHolidayBalanceDeadlinePO::getDeadline, nowTime)
                                                                                                   );
        if (deadlineList != null && !deadlineList.isEmpty()) {
            List<Long> deadlineIdList = new ArrayList<>();
            List<Long> delList = new ArrayList<>();
            List<AttHolidayBalanceMutationDTO> mutations = new ArrayList<>();
            for (AttHolidayBalanceDeadlinePO deadlinePO : deadlineList) {
                delList.add(deadlinePO.getId());
                deadlineIdList.add(deadlinePO.getId());
                mutations.add(AttHolidayBalanceMutationDTO.builder()
                                      .staffId(deadlinePO.getStaffId())
                                      .holidayRuleId(deadlinePO.getHolidayRuleId())
                                      .availableDelta(-deadlinePO.getBalance())
                                      .changeNumber(-deadlinePO.getBalance())
                                      .deadlineId(deadlinePO.getId())
                                      .operator("system")
                                      .effective(false)
                                      .remark("余额过期扣减")
                                      .deleted(true)
                                      .createBy("system")
                                      .build());
            }
            attHolidayBalanceRepository.applyBalanceMutations(mutations);
            // 删除过期的有效余额
            attHolidayBalanceDeadlineMapper.deleteByIds(delList);
            // 已过期的余额记录不能再作废
            attHolidayBalanceRecordCommandService.update(Wrappers.<AttHolidayBalanceRecordPO>lambdaUpdate()
                                                                 .set(AttHolidayBalanceRecordPO::getEffective, false)
                                                                 .set(AttHolidayBalanceRecordPO::getEffectiveTime, nowTime)
                                                                 .set(AttHolidayBalanceRecordPO::getEffectiveRemark, "过期作废")
                                                                 .set(AttHolidayBalanceRecordPO::getUpdateName, "system")
                                                                 .in(AttHolidayBalanceRecordPO::getDeadlineId, deadlineIdList)
                                                                 .ge(AttHolidayBalanceRecordPO::getChangeNumber, AttConstants.zeroInt)
                                                        );

        }
    }

    /**
     * 加班计入余额
     *
     * @param staffId        人员id
     * @param actualDuration 加班时间长（小时）
     */
    public String workOverIntoHolidayBalance(Long staffId, String actualDuration, String processInstanceId, String businessType) {
        if (staffId == null || StringUtils.isBlank(actualDuration)) {
            return WorkOvertimeResultEnum.LEAVE_USER_CHECK.getRemarks();
        }
        AttHolidayRulePO holidayRulePO = attHolidayRuleMapper.selectOne(Wrappers.<AttHolidayRulePO>lambdaQuery()
                                                                                .eq(AttHolidayRulePO::getGrantWay, AttHolidayGrantWayEnum.OVERTIME_APPEND)
                                                                                .last(" limit 1")
                                                                       );
        if (holidayRulePO == null) {
            log.error("没有计入余额的假期");
            return WorkOvertimeResultEnum.LEAVE_TYPE_CHECK.getRemarks();
        }

        // 修改数量
        float num = Float.parseFloat(actualDuration);
        // 有效期
        LocalDateTime dateTime = AttConstants.PERMANENT_DATE;
        if (StringUtils.isNotBlank(holidayRulePO.getValidity())) {
            AttHolidayGrantValidityDTO validityDTO = JsonUtils.deserialize(holidayRulePO.getValidity(), AttHolidayGrantValidityDTO.class);
            dateTime = AttHolidayDomain.calcDeadline(validityDTO, LocalDateTime.now());
        }
        // 修改入参
        AttHolidayBalanceParam param = new AttHolidayBalanceParam();
        param.setAvailableBalance(num);
        param.setHolidayRuleId(holidayRulePO.getId());
        param.setStaffId(staffId);
        param.setUnit(AttConstants.HOUR);
        param.setDeadline(dateTime);
        param.setIncrementalUpdate(true);
        param.setRemark("加班计入余额：" + num);
        param.setProcessInstanceId(processInstanceId);
        param.setBusinessType(businessType);
        this.editHolidayBalance(param);
        return WorkOvertimeResultEnum.LEAVE_OK.getRemarks();
    }

    /**
     * 处理人员假期
     *
     * @param groupStaffPO     人员信息
     * @param holidayRulePO    假期规则信息
     * @param initMutations    需要初始化的余额集合
     * @param balanceMutations 需要写入的余额变更集合
     * @param deadlineList     需要插入有效期的集合
     * @param nowTime          当前时间
     */
    private void handleInsertBalance(AttGroupStaffPO groupStaffPO, AttHolidayRulePO holidayRulePO,
                                     List<AttHolidayBalanceMutationDTO> initMutations,
                                     List<AttHolidayBalanceMutationDTO> balanceMutations,
                                     List<AttHolidayBalanceDeadlinePO> deadlineList, LocalDateTime nowTime
                                    ) {
        // 处理发放的假期
        AttHolidayBalancePO grantBalancePO = new AttHolidayBalancePO();
        this.grantStaffBalance(groupStaffPO, holidayRulePO, nowTime, grantBalancePO, balanceMutations, deadlineList);
        initMutations.add(AttHolidayBalanceMutationDTO.builder()
                                  .staffId(groupStaffPO.getStaffId())
                                  .holidayRuleId(holidayRulePO.getId())
                                  .notLimit(grantBalancePO.getNotLimit())
                                  .remark("初始化")
                                  .build());
    }

    /**
     * 发放人员假期
     *
     * @param groupStaffPO 人员信息
     * @param rule         规则信息
     */
    private void grantStaffBalance(AttGroupStaffPO groupStaffPO, AttHolidayRulePO rule, LocalDateTime nowTime,
                                   AttHolidayBalancePO grantBalancePO, List<AttHolidayBalanceMutationDTO> balanceMutations,
                                   List<AttHolidayBalanceDeadlinePO> deadlineList
                                  ) {
        grantBalancePO.setStaffId(groupStaffPO.getStaffId());
        grantBalancePO.setHolidayRuleId(rule.getId());
        grantBalancePO.setNotLimit(rule.getNotLimit());
        grantBalancePO.setOccupyBalance(AttConstants.zeroLong);
        grantBalancePO.setAvailableBalance(AttConstants.zeroLong);
        grantBalancePO.setUpdateRemark("系统发放");
        grantBalancePO.setNotLimit(true);
        Long deadlineId = IdWorker.getId();
        AttHolidayBalanceDeadlinePO deadlinePO = AttHolidayBalanceDeadlinePO.builder()
                .id(deadlineId)
                .staffId(groupStaffPO.getStaffId())
                .holidayRuleId(rule.getId())
                .build();
        if (Boolean.TRUE.equals(rule.getNotLimit())) {
            // 不限额
            return;
        }
        grantBalancePO.setNotLimit(false);
        // 假期额度对象
        AttHolidayGrantQuotaDTO grantQuotaDTO = JsonUtils.deserialize(rule.getQuota(), AttHolidayGrantQuotaDTO.class);
        // 额度有效期对象
        AttHolidayGrantValidityDTO grantValidityDTO = JsonUtils.deserialize(rule.getValidity(), AttHolidayGrantValidityDTO.class);
        // 额度有效期计算
        LocalDateTime deadline = AttHolidayDomain.calcDeadline(grantValidityDTO, nowTime);
        deadlinePO.setDeadline(deadline);
        long availableBalance;

        // 自动发放且是发放日期
        boolean grantWayBool = rule.getGrantDate() != null && AttHolidayGrantWayEnum.EVERY_YEAR.name().equals(rule.getGrantWay()) &&
                               DateUtils.isSameDay(nowTime, DateUtils.parseDate(nowTime.getYear() + "-" + rule.getGrantDate()));

        if (grantWayBool) {
            if (AttHolidayQuotaTypeEnum.FIXED.name().equals(grantQuotaDTO.getQuotaType())) {
                // 按固定额度发放
                availableBalance = AttHolidayDomain.unitAndNumToLong(grantQuotaDTO.getFixedNumber(), AttHolidayQuotaValidityEnum.getValueByName(grantQuotaDTO.getFixedUnit()), rule.getDayOfHour());
                grantBalancePO.setAvailableBalance(availableBalance);
                if (availableBalance > AttConstants.zeroLong) {
                    balanceMutations.add(this.buildGrantMutation(groupStaffPO, rule, availableBalance, deadlineId, deadline));
                    deadlinePO.setBalance(availableBalance);
                    deadlineList.add(deadlinePO);
                }

            } else if (AttHolidayQuotaTypeEnum.SOCIETY_AGE.name().equals(grantQuotaDTO.getQuotaType())) {
                // 按社会工龄发放（社会工龄在人事管理里面
                StaffDetailDTO staffDetailDTO = staffClient.getById(groupStaffPO.getStaffId());
                LocalDateTime seniorityDate = staffDetailDTO.getPaySocialInsuranceFirstDate();
                if (seniorityDate == null) {
                    // 如果没有首次缴纳社保时间，开始工作时间就是入职时间
                    seniorityDate = groupStaffPO.getEntryDate();
                }
                availableBalance = AttHolidayDomain.getUserSocietyFloat(seniorityDate, grantQuotaDTO.getQuotaLevelList(), nowTime, rule.getDayOfHour());
                grantBalancePO.setAvailableBalance(availableBalance);
                if (availableBalance > AttConstants.zeroLong) {
                    balanceMutations.add(this.buildGrantMutation(groupStaffPO, rule, availableBalance, deadlineId, deadline));
                    deadlinePO.setBalance(availableBalance);
                    deadlineList.add(deadlinePO);
                }
            } else if (AttHolidayQuotaTypeEnum.COMPANY_AGE.name().equals(grantQuotaDTO.getQuotaType())) {
                // 按公司工龄发放
                availableBalance = AttHolidayDomain.getUserSocietyFloat(groupStaffPO.getEntryDate(), grantQuotaDTO.getQuotaLevelList(), nowTime, rule.getDayOfHour());
                grantBalancePO.setAvailableBalance(availableBalance);
                if (availableBalance > AttConstants.zeroLong) {
                    balanceMutations.add(this.buildGrantMutation(groupStaffPO, rule, availableBalance, deadlineId, deadline));
                    deadlinePO.setBalance(availableBalance);
                    deadlineList.add(deadlinePO);
                }
            } else {
                grantBalancePO.setAvailableBalance(AttConstants.zeroLong);
            }
        }

    }

    private AttHolidayBalanceMutationDTO buildGrantMutation(AttGroupStaffPO groupStaffPO, AttHolidayRulePO rule, Long availableBalance,
                                                            Long deadlineId, LocalDateTime deadline
                                                           ) {
        return AttHolidayBalanceMutationDTO.builder()
                .staffId(groupStaffPO.getStaffId())
                .holidayRuleId(rule.getId())
                .availableDelta(availableBalance)
                .changeNumber(availableBalance)
                .deadlineId(deadlineId)
                .deadline(AttConstants.PERMANENT_DATE.equals(deadline) ? null : deadline)
                .remark("系统发放")
                .operator("system")
                .staffName(groupStaffPO.getName())
                .notLimit(rule.getNotLimit())
                .build();
    }

    private void applyHolidayBalanceParams(List<AttHolidayBalanceParam> paramList, AttHolidayRulePO holidayRule) {
        if (paramList == null || paramList.isEmpty()) {
            return;
        }
        String operator = SecurityContextHolder.getPrincipal() == null ? "" : SecurityContextHolder.getPrincipal().getName();
        List<AttHolidayBalanceMutationDTO> mutations = new ArrayList<>();
        for (AttHolidayBalanceParam param : paramList) {
            long available = AttHolidayDomain.unitAndNumToLong(param.getAvailableBalance(), param.getUnit(), holidayRule.getDayOfHour());
            LocalDateTime deadline = param.getDeadline() == null ? AttConstants.PERMANENT_DATE : param.getDeadline();
            Long deadlineId = null;
            if (Boolean.TRUE.equals(param.getIncrementalUpdate())) {
                if (available > AttConstants.zeroLong) {
                    deadlineId = IdWorker.getId();
                    AttHolidayBalanceDeadlinePO deadlinePO = AttHolidayBalanceDeadlinePO.builder()
                            .id(deadlineId)
                            .balance(available)
                            .deadline(deadline)
                            .holidayRuleId(param.getHolidayRuleId())
                            .staffId(param.getStaffId())
                            .build();
                    deadlinePO.setDeleted(false);
                    attHolidayBalanceDeadlineMapper.insert(deadlinePO);
                }
                mutations.add(AttHolidayBalanceMutationDTO.builder()
                                      .staffId(param.getStaffId())
                                      .holidayRuleId(param.getHolidayRuleId())
                                      .availableDelta(available)
                                      .changeNumber(available)
                                      .deadlineId(deadlineId)
                                      .deadline(deadline)
                                      .remark(param.getRemark())
                                      .operator(operator)
                                      .staffName(param.getStaffName())
                                      .notLimit(holidayRule.getNotLimit())
                                      .build());
            } else {
                attHolidayBalanceDeadlineMapper.delete(Wrappers.<AttHolidayBalanceDeadlinePO>lambdaUpdate()
                                                               .eq(AttHolidayBalanceDeadlinePO::getStaffId, param.getStaffId())
                                                               .eq(AttHolidayBalanceDeadlinePO::getHolidayRuleId, param.getHolidayRuleId())
                                                      );
                deadlineId = IdWorker.getId();
                AttHolidayBalanceDeadlinePO deadlinePO = AttHolidayBalanceDeadlinePO.builder()
                        .id(deadlineId)
                        .balance(available)
                        .deadline(deadline)
                        .holidayRuleId(param.getHolidayRuleId())
                        .staffId(param.getStaffId())
                        .build();
                deadlinePO.setDeleted(false);
                attHolidayBalanceDeadlineMapper.insert(deadlinePO);
                mutations.add(AttHolidayBalanceMutationDTO.builder()
                                      .staffId(param.getStaffId())
                                      .holidayRuleId(param.getHolidayRuleId())
                                      .targetAvailableBalance(available)
                                      .changeNumber(available)
                                      .deadlineId(deadlineId)
                                      .deadline(deadline)
                                      .remark(param.getRemark())
                                      .operator(operator)
                                      .staffName(param.getStaffName())
                                      .notLimit(holidayRule.getNotLimit())
                                      .build());
            }
        }
        attHolidayBalanceRepository.applyBalanceMutations(mutations);
    }

    private String getStaffName(Long staffId) {
        AttGroupStaffPO groupStaff = attGroupStaffMapper.selectOne(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                           .eq(AttGroupStaffPO::getStaffId, staffId)
                                                                           .last(" limit 1 ")
                                                                  );
        return groupStaff == null ? null : groupStaff.getName();
    }

    public AttHolidayBalanceImportResult readInitBalance(MultipartFile file, String ruleNames, Boolean init) {
        Assert.isParameterError(StringUtils.isBlank(ruleNames), "请选择要导入的假期！");
        List<String> ruleNameList = Arrays.asList(ruleNames.split(","));
        Map<String, Boolean> paramRuleMap = ruleNameList.stream().collect(Collectors.toMap(s -> s, s -> true));
        List<AttHolidayRulePO> holidayRulePOList = attHolidayRuleMapper.selectList(Wrappers.emptyWrapper());
        if (holidayRulePOList == null || holidayRulePOList.isEmpty()) {
            throw new ServiceException("系统暂无假期");
        }
        // 获取假期信息
        Map<String, AttHolidayRulePO> holidayMap = new HashMap<>();
        for (AttHolidayRulePO rulePO : holidayRulePOList) {
            holidayMap.put(rulePO.getName(), rulePO);
        }
        // 获取人员信息
        Map<Long, String> staffMap = new HashMap<>();
        Pagination<UserDTO> pagination = userClient.listUsers(UserQueryParam.builder().pageNumber(1).pageSize(999).enabled(true).build());
        if (pagination.getTotal() > AttConstants.zeroInt) {
            for (UserDTO userDto : pagination.getRecords()) {
                staffMap.put(userDto.getId(), userDto.getName());
            }
        }

        // 读取excel
        int sheetCount = AttConstants.zeroInt;
        List<String> sheetNames = null;
        try {
            ExcelReader excelReaderStart = ExcelUtil.getReader(file.getInputStream(), 0);
            sheetNames = excelReaderStart.getSheetNames();
            if (sheetNames != null) {
                sheetCount = sheetNames.size();
            }
        } catch (Exception e) {
            log.error("未获取到excel文件sheet！", e);
            throw new ServiceException("excel为空！");
        }
        if (sheetNames != null && !sheetNames.isEmpty()) {
            // 校验选择的假期
            Assert.isParameterError(
                    sheetNames.size() - AttConstants.oneInt != paramRuleMap.size(), "模板假期数量与勾选数量不匹配，请检查后再导入！");
            for (int i = AttConstants.oneInt; i < sheetNames.size(); i++) {
                String name = sheetNames.get(i);
                Assert.isParameterError(paramRuleMap.get(name) == null, "模板假期名称与勾选假期名称不匹配，请检查后再导入！");
                Assert.isNotFound(holidayMap.get(name), "未找系统假期！(" + name + ")");
            }
        }
        AttHolidayBalanceImportResult result = new AttHolidayBalanceImportResult();
        result.setFail(AttConstants.zeroInt);
        result.setSuccess(AttConstants.zeroInt);
        result.setErrorMsg(new ArrayList<>());
        for (int i = AttConstants.oneInt; i < sheetCount; i++) {
            ExcelReader excelReader = null;
            try {
                excelReader = ExcelUtil.getReader(file.getInputStream(), i);
            } catch (Exception e) {
                log.error("第{}页文件解析失败！", i, e);
                throw new ServiceException("第" + i + "页文件解析失败！");
            }
            List<List<Object>> dataSource = excelReader.read();
            this.setHolidayBalance(excelReader, dataSource, holidayMap, i, init, staffMap, result);
        }
        //        result.setErrorMsg(result.getErrorMsg().stream().sorted(Comparator.comparing(AttStingKeyValueVO::getKey)).collect(Collectors.toList()));
        result.setErrorMsg(result.getErrorMsg().stream().sorted().collect(Collectors.toList()));
        return result;
    }

    /**
     * 操作记录作废
     *
     * @param id              记录id
     * @param effectiveRemark 作废理由
     */
    public void balanceRecordCancel(Long id, String effectiveRemark) {
        AttHolidayBalanceRecordPO balanceRecordPO = attHolidayBalanceRecordMapper.selectById(id);
        Assert.isNotFound(balanceRecordPO, "记录不存在请确认是否已被删除。");

        if (Boolean.FALSE.equals(balanceRecordPO.getEffective())) {
            throw new ServiceException("记录已作废，无需重复操作。");
        }
        if (balanceRecordPO.getDeadlineId() == null || balanceRecordPO.getDeadlineId() == AttConstants.zeroLong) {
            throw new ServiceException("无具体余额数据无法作废。");
        }
        // 如果是新增余额需要校验是否已经被使用，已被使用的
        boolean addBool =
                Boolean.TRUE.equals(balanceRecordPO.getAddOrDeduct()) && balanceRecordPO.getChangeNumber() > AttConstants.zeroLong;
        if (addBool) {
            AttHolidayBalanceDeadlinePO balanceDeadlinePO = attHolidayBalanceDeadlineMapper.selectById(balanceRecordPO.getDeadlineId());
            Assert.isNotFound(balanceDeadlinePO, "余额不存在请确认是否已被删除。");
            // 如果已被使用则不能作废
            if (!Boolean.TRUE.equals(balanceDeadlinePO.getNormal()) ||
                !Objects.equals(balanceRecordPO.getChangeNumber(), balanceDeadlinePO.getBalance())) {
                throw new ServiceException("已使用的余额，不能作废！");
            }
        }
        LocalDateTime newDate = LocalDateTime.now();
        // 处理有效余额数据
        if (addBool) {
            attHolidayBalanceDeadlineMapper.deleteById(balanceRecordPO.getDeadlineId());
        } else {
            AttHolidayBalanceDeadlinePO addDeadlinePO = new AttHolidayBalanceDeadlinePO();
            addDeadlinePO.setId(IdWorker.getId());
            addDeadlinePO.setNormal(true);
            addDeadlinePO.setBalance(balanceRecordPO.getChangeNumber());
            addDeadlinePO.setDeadline(balanceRecordPO.getDeadline());
            addDeadlinePO.setDeleted(false);
            addDeadlinePO.setHolidayRuleId(balanceRecordPO.getHolidayRuleId());
            addDeadlinePO.setStaffId(balanceRecordPO.getStaffId());
            addDeadlinePO.setTenant(balanceRecordPO.getTenant());
            addDeadlinePO.setCreateTime(newDate);
            attHolidayBalanceDeadlineCommandService.save(addDeadlinePO);
        }
        // 更新余额
        attHolidayBalanceRepository.incrementAvailableBalance(AttHolidayBalanceMutationDTO.builder()
                                                                      .staffId(balanceRecordPO.getStaffId())
                                                                      .holidayRuleId(balanceRecordPO.getHolidayRuleId())
                                                                      .availableDelta(-balanceRecordPO.getChangeNumber())
                                                                      .changeNumber(-balanceRecordPO.getChangeNumber())
                                                                      .deadline(balanceRecordPO.getDeadline())
                                                                      .remark("作废记录")
                                                                      .operator(SecurityContextHolder.getPrincipal().getName())
                                                                      .staffName(balanceRecordPO.getStaffName())
                                                                      .deleted(true)
                                                                      .build());
        //更新记录作废信息
        balanceRecordPO.setEffective(false);
        balanceRecordPO.setEffectiveRemark(effectiveRemark);
        balanceRecordPO.setUpdateName(SecurityContextHolder.getPrincipal().getName());
        balanceRecordPO.setUpdateTime(newDate);
        balanceRecordPO.setEffectiveTime(newDate);
        attHolidayBalanceRecordCommandService.updateById(balanceRecordPO);
    }

    private void setHolidayBalance(ExcelReader excelReader, List<List<Object>> dataSource, Map<String, AttHolidayRulePO> holidayMap, int i,
                                   Boolean init, Map<Long, String> staffMap, AttHolidayBalanceImportResult result
                                  ) {
        if (CollectionUtils.isEmpty(dataSource) || dataSource.size() < 2) {
            return;
        }
        List<Object> row = dataSource.getFirst();
        if (!CollectionUtils.isEmpty(row) && !org.springframework.util.ObjectUtils.isEmpty(row.get(0))) {
            String cont = (String) row.getFirst();
            Assert.isNotAcceptable(Boolean.TRUE.equals(init) && cont.contains("累加"), "增量模板不能做替换导入！");
            Assert.isNotAcceptable(!Boolean.TRUE.equals(init) && cont.contains("替换"), "覆盖模板不能做累加导入！");

        }
        String sheetName = excelReader.getSheet().getSheetName();
        AttHolidayRulePO holidayRule = holidayMap.get(sheetName);
        /* 处理假期数据 */
        List<AttHolidayBalanceParam> paramList = new ArrayList<>();
        for (int i1 = 2; i1 < dataSource.size(); i1++) {
            List<Object> staffListObject = dataSource.get(i1);
            Object staffIdObj = staffListObject.getFirst();
            long staffId = AttConstants.zeroLong;
            if (!Objects.isNull(staffIdObj)) {
                staffId = Long.parseLong(String.valueOf(staffIdObj));
            }
            String staffName = (String) staffListObject.get(1);
            String unit = AttConstants.DAYS;
            float balance = AttConstants.zeroFloat;
            LocalDateTime time = AttConstants.PERMANENT_DATE;
            // 单位
            if (AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(holidayRule.getGrantWay())) {
                unit = AttConstants.HOUR;
            }
            // 余额
            Object bananceObj = staffListObject.get(3);
            if (bananceObj != null && !bananceObj.equals("") && !bananceObj.equals("null")) {
                balance = Float.parseFloat(String.valueOf(bananceObj));
            }
            // 过期时间
            Object timeObj = staffListObject.get(4);
            if (timeObj != null && !timeObj.equals("") && !timeObj.equals("null")) {
                try {
                    time = (LocalDateTime) timeObj;
                } catch (Exception e) {
                    time = DateUtils.parse(String.valueOf(timeObj));
                }
            }
            time = DateUtils.parse(DateUtils.toTimestamp(DateUtils.endOfDay(time)) - 500);
            AttHolidayBalanceParam editParam = new AttHolidayBalanceParam();
            editParam.setIncrementalUpdate(!Boolean.TRUE.equals(init));
            editParam.setUnit(unit);
            editParam.setAvailableBalance(balance);
            editParam.setDeadline(time);
            editParam.setStaffId(staffId);
            editParam.setStaffName(staffName);
            editParam.setHolidayRuleId(holidayRule.getId());
            editParam.setHolidayRuleName(holidayRule.getName());
            if (!Boolean.TRUE.equals(init)) {
                editParam.setRemark("批量导入");
            } else {
                editParam.setRemark("初始化导入");
            }
            paramList.add(editParam);
        }
        if (paramList.isEmpty()) {
            return;
        }
        /* 这里单次修改为固定值，如果一个人有多条会变成后面的覆盖前面的，所以要做处理。*/
        Map<Long, List<AttHolidayBalanceParam>> paramMap = paramList.stream()
                .collect(Collectors.groupingBy(AttHolidayBalanceParam::getStaffId));
        List<AttHolidayBalanceParam> validParams = new ArrayList<>();
        paramMap.forEach((staffId, list) -> {
            for (int i1 = 0; i1 < list.size(); i1++) {
                AttHolidayBalanceParam param = list.get(i1);
                StringBuilder stringBuilder = new StringBuilder();
                if (Boolean.TRUE.equals(param.getIncrementalUpdate())) {
                    stringBuilder.append("(增量导入)");
                } else {
                    stringBuilder.append("(覆盖导入)");
                }
                stringBuilder.append(param.getStaffName()).append("(").append(param.getStaffId()).append(")").append("[")
                        .append(param.getHolidayRuleName()).append("]").append(param.getAvailableBalance()).append(param.getUnit());
                if (param.getDeadline() != null) {
                    stringBuilder.append("(").append(DateUtils.formatDate(param.getDeadline())).append(")");
                }
                stringBuilder.append(":");
                if (param.getAvailableBalance() == null || param.getAvailableBalance() < AttConstants.zeroFloat) {
                    result.setFail(result.getFail() + AttConstants.oneInt);
                    stringBuilder.append("余额小于零！");
                    result.getErrorMsg().add(stringBuilder.toString());
                    continue;
                }
                if (param.getDeadline() != null && param.getDeadline().isBefore(LocalDateTime.now())) {
                    result.setFail(result.getFail() + AttConstants.oneInt);
                    stringBuilder.append("余额已过期！");
                    result.getErrorMsg().add(stringBuilder.toString());
                    continue;
                }
                if (StringUtils.isBlank(staffMap.get(staffId))) {
                    result.setFail(result.getFail() + AttConstants.oneInt);
                    stringBuilder.append("未找到人员！");
                    result.getErrorMsg().add(stringBuilder.toString());
                    continue;
                }

                /* 这里单次修改为固定值，如果一个人有多条会变成后面的覆盖前面的，所以要做处理。*/
                if (i1 != 0) {
                    param.setIncrementalUpdate(true);
                }
                validParams.add(param);
                result.setSuccess(result.getSuccess() + AttConstants.oneInt);
                stringBuilder.append("完成！");
            }
        });
        this.applyHolidayBalanceParams(validParams, holidayRule);
    }

    /**
     * 操作日志前置处理
     *
     * @param oldHolidayBalance 旧数据
     * @param newPo             新对象
     */
    private void operationLogBeforeHandle(AttHolidayBalancePO newPo, LocalDateTime balanceDeadline, Long oldHolidayBalance) {
        Long id = AttConstants.zeroLong;
        String staffName = "";
        String holidayName = "";
        String oldBalance = "";
        String newBalance = "";
        String unit = "";
        String remark = "";
        String deadline = "";
        AttHolidayRulePO rulePO = null;
        // 人员名称+规则
        if (newPo != null) {
            id = newPo.getId();
            remark = newPo.getUpdateRemark();
            if (newPo.getHolidayRuleId() != null) {
                rulePO = attHolidayRuleMapper.selectById(newPo.getHolidayRuleId());
            }
            if (newPo.getStaffId() != null) {
                AttGroupStaffPO groupStaff = attGroupStaffMapper.selectOne(Wrappers.<AttGroupStaffPO>lambdaQuery()
                                                                                   .eq(AttGroupStaffPO::getStaffId, newPo.getStaffId())
                                                                                   .last(" limit 1 ")
                                                                          );
                if (groupStaff != null) {
                    staffName = groupStaff.getName();
                }
            }
        }
        // 规则名称+余额+单位
        if (rulePO != null) {
            holidayName = rulePO.getName();
            unit = AttConstants.DAYS;
            if (AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(rulePO.getGrantWay())) {
                unit = AttConstants.HOUR;
            }
            newBalance = AttHolidayDomain.longToUnit(newPo.getAvailableBalance(), unit, rulePO.getDayOfHour(), false);
            if (oldHolidayBalance != null) {
                oldBalance = AttHolidayDomain.longToUnit(oldHolidayBalance, unit, rulePO.getDayOfHour(), false);
            }
        }

        if (balanceDeadline != null) {
            deadline = DateUtils.formatDate(balanceDeadline);
        }

        AttBalanceOperationLogDTO newLog = AttBalanceOperationLogDTO.builder()
                .id(id)
                .staffName(staffName)
                .holidayName(holidayName)
                .balance(newBalance)
                .unit(unit)
                .deadline(deadline)
                .remark(remark)
                .build();
        String content =
                "修改:" + newLog.getStaffName() + " " + newLog.getHolidayName() + " 从" + oldBalance + newLog.getUnit() + "修改为" +
                newLog.getBalance() + newLog.getUnit() + " <br/>有效期【" + newLog.getDeadline() + "】";
        if (StringUtils.isNotBlank(newLog.getRemark())) {
            content += " <br/>备注【" + newLog.getRemark() + "】";
        }
        attHolidayBalanceRepository.addOperationLog(newLog.getId(), content);
    }
}
